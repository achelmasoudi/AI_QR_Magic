package com.aimagic.aiqrmagicpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import com.aimagic.aiqrmagicpro.Controller.DataBaseHelper_Generated;
import com.aimagic.aiqrmagicpro.Fragments.LanguageManager;
import com.aimagic.aiqrmagicpro.Model.GeneratedQrCode;
import de.hdodenhof.circleimageview.CircleImageView;


public class GenerateEmail extends AppCompatActivity implements View.OnClickListener {

    private CardView generateEmailBtn;
    private TextInputLayout emailEditTxt , subjectEditTxt , bodyEditText;
    private String myEmailEditText , mySubjectEditText , myBodyEditText;
    private ImageView qrCodeImageView;

    //To Save the QrCode in Gallery
    private static int REQUEST_CODE = 100;
    private OutputStream outputStream;
    private CircleImageView downloadEmailBtn;

    //To Share the QrCode
    private CircleImageView shareEmailBtn;

    //For The Bottom Sheet
    private BottomSheetBehavior bottomSheetBehavior;
    private FrameLayout frameLayout;

    private CircleImageView saveEmailBtn , savedSuccessEmailBtn;
    private TextView saveEmailTxtView , savedSuccessEmailTxtView;
    private View shadowFromView1 , shadowFromView2;
    private DataBaseHelper_Generated dataBaseHelperGenerated;

    //Custom The Qrcode
    private CircleImageView customQrcodeBtn;
    private int qrcode_background_color , qrcode_code_color;
    private int currentNightMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the locale language and The Mode when the app starts
        LanguageManager.loadLocale(this);

        setContentView(R.layout.activity_generate_email);

        //For the BottomSheet
        frameLayout = (FrameLayout) findViewById(R.id.bottomSheetIdFrameLayoutGenerateEmail);
        bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
        bottomSheetBehavior.setPeekHeight(0);

        //When the bottomSheet is Expanded The Shadow will appear
        shadowFromView1 = (View) findViewById(R.id.shadowFromViewId_GenerateEmail);
        shadowFromView1.setVisibility(View.GONE);

        generateEmailBtn = (CardView) findViewById(R.id.generate_Email_Btn);
        generateEmailBtn.setOnClickListener(this);

        //If the bottom sheet is expanded and a touch event is detected on the Shadow, collapse the bottom sheet
        shadowFromView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    // If the bottom sheet is expanded and a touch event is detected on the Shadow,
                    // collapse the bottom sheet
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    return true; // Consume the touch event
                }
                return false; // Allow other touch events to be handled as usual
            }
        });

        //The order is very important
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    // The BottomSheet is expanded.
                    shadowFromView1.setVisibility(View.VISIBLE);
                }
                else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // The BottomSheet is hidden.
                    shadowFromView1.setVisibility(View.GONE);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
        });

        //Initialize the variables
        emailEditTxt = (TextInputLayout) findViewById(R.id.generateFragment_EmailGenerate_EmailId);
        subjectEditTxt = (TextInputLayout) findViewById(R.id.generateFragment_EmailGenerate_SubjectId);
        bodyEditText = (TextInputLayout) findViewById(R.id.generateFragment_EmailGenerate_BodyId);
        qrCodeImageView = (ImageView) findViewById(R.id.imageOfQrCode_EmailGenerateId);

        //Arrow Btn From GenerateEmail to GenerateFragment
        arrowBack();

        //To Download The QRCode in Save it in Gallery
        downloadButtonSaveToGallery();

        //To Share The QrCode
        shareQrCodeButton();

        saveEmailBtn = (CircleImageView) findViewById(R.id.saveButtonIdGenerateEmail);
        savedSuccessEmailBtn = (CircleImageView) findViewById(R.id.savedSuccessButtonIdGenerateEmail);
        saveEmailTxtView = (TextView) findViewById(R.id.saveTxtViewIdGenerateEmail);
        savedSuccessEmailTxtView = (TextView) findViewById(R.id.savedSuccessTxtViewIdGenerateEmail);
        saveQrCodeToGeneratedFragmentButton();

        customQrcodeButton();

        qrcode_background_color = Color.parseColor("#FFFFFF");
        qrcode_code_color = Color.parseColor("#000000");

        //For Dark - Light Mode ( BottomSheet and Icon Of Btns )
        currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //Light - Dark Mode
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode is active
            frameLayout.setBackgroundResource(R.drawable.bottom_sheet_background_darkmode);
            saveEmailBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            savedSuccessEmailBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            shareEmailBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            downloadEmailBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            customQrcodeBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
        }
        else {
            // Light mode is active
            frameLayout.setBackgroundResource(R.drawable.bottom_sheet_background);
            saveEmailBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            savedSuccessEmailBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            shareEmailBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            downloadEmailBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            customQrcodeBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
        }

    }

    private void customQrcodeButton() {
        //For the BottomSheet
        FrameLayout frameLayout_CustomQrCode = (FrameLayout) findViewById(R.id.bottomSheetIdFrameLayoutGenerateEmail_CustomQrcode);
        BottomSheetBehavior bottomSheetBehavior_CustomQrCode = BottomSheetBehavior.from(frameLayout_CustomQrCode);
        bottomSheetBehavior_CustomQrCode.setPeekHeight(0);

        //For Dark - Light Mode ( BottomSheet and Icon Of Btns )
        currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //Light - Dark Mode
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode is active
            frameLayout_CustomQrCode.setBackgroundResource(R.drawable.bottom_sheet_background_darkmode);
        }
        else {
            frameLayout_CustomQrCode.setBackgroundResource(R.drawable.bottom_sheet_background);
        }

        //When the bottomSheet is Expanded The Shadow will appear
        shadowFromView2 = (View) findViewById(R.id.shadowFromViewId_GenerateEmail2);

        //If the bottom sheet is expanded and a touch event is detected on the Shadow, collapse the bottom sheet
        shadowFromView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (bottomSheetBehavior_CustomQrCode.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    // If the bottom sheet is expanded and a touch event is detected on the Shadow,
                    // collapse the bottom sheet
                    bottomSheetBehavior_CustomQrCode.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    return true; // Consume the touch event
                }
                return false; // Allow other touch events to be handled as usual
            }
        });

        //The order is very important
        bottomSheetBehavior_CustomQrCode.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    // The BottomSheet is expanded.
                    shadowFromView2.setVisibility(View.VISIBLE);
                }
                else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // The BottomSheet is hidden.
                    shadowFromView2.setVisibility(View.GONE);
                    shadowFromView1.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
        });

        customQrcodeBtn = (CircleImageView) findViewById(R.id.customButtonIdGenerateEmail);
        customQrcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior_CustomQrCode.setState(BottomSheetBehavior.STATE_EXPANDED);
                shadowFromView1.setVisibility(View.GONE);
            }
        });
    }

    private void updateQRCodeColor() {
        // Regenerate and update the QR code with the selected colors
        Bitmap qrCodeBitmapEmail = generateEmailQRCode(myEmailEditText , mySubjectEditText , myBodyEditText);
        if (qrCodeBitmapEmail != null) {
            qrCodeImageView.setImageBitmap(qrCodeBitmapEmail);
        } else {
            Toast.makeText(getApplicationContext(), "Failed to update QR code", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        myEmailEditText = emailEditTxt.getEditText().getText().toString();
        mySubjectEditText = subjectEditTxt.getEditText().getText().toString();
        myBodyEditText = bodyEditText.getEditText().getText().toString();

        //About the Visibility Of Save and SavedSuccess
        saveEmailBtn.setVisibility(View.VISIBLE);
        saveEmailTxtView.setVisibility(View.VISIBLE);
        savedSuccessEmailBtn.setVisibility(View.GONE);
        savedSuccessEmailTxtView.setVisibility(View.GONE);

        emailEditTxt.setErrorEnabled(true);

        if(myEmailEditText.isEmpty()) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.PleaseenteryourEmail), Toast.LENGTH_SHORT).show();
            emailEditTxt.setError(getResources().getString(R.string.EnterYourEmail));
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(myEmailEditText).matches()) {
            emailEditTxt.setError(getResources().getString(R.string.PleaseenteravalidEmail));
        }
        else {
            emailEditTxt.setErrorEnabled(false);

            try{
                // Clear the focus from the EditText to hide the selection
                emailEditTxt.getEditText().clearFocus();
                subjectEditTxt.getEditText().clearFocus();
                bodyEditText.getEditText().clearFocus();

                // Hide the virtual keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                Bitmap qrCodeBitmapEmail = generateEmailQRCode(myEmailEditText , mySubjectEditText , myBodyEditText);

                if(qrCodeBitmapEmail != null) {
                    qrCodeImageView.setImageBitmap(qrCodeBitmapEmail);
                }
                else {
                    Toast.makeText(getApplicationContext() , "Failed" , Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext() , "Failed" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    // I will save it in the GeneratedFragment using SQlite
    private void saveQrCodeToGeneratedFragmentButton() {
        dataBaseHelperGenerated = new DataBaseHelper_Generated(this);

        saveEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Convert The ImageView To Bitmap
                qrCodeImageView.setDrawingCacheEnabled(true);
                Bitmap qrCodeBitmap = Bitmap.createBitmap(qrCodeImageView.getDrawingCache());
                qrCodeImageView.setDrawingCacheEnabled(false);

                // Convert the Bitmap to a byte[]
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] qrCodeImageBytes = byteArrayOutputStream.toByteArray();

                String generated_QrCode_Type = getResources().getString(R.string.Email);
                dataBaseHelperGenerated.addData_Generated(new GeneratedQrCode(generated_QrCode_Type , getResources().getString(R.string.Email)+": " + myEmailEditText , qrCodeImageBytes , R.drawable.email_icon));
                Toast.makeText(getApplicationContext() , getResources().getString(R.string.Savedsuccessfully) , Toast.LENGTH_SHORT).show();
                saveEmailBtn.setVisibility(View.GONE);
                saveEmailTxtView.setVisibility(View.GONE);
                savedSuccessEmailBtn.setVisibility(View.VISIBLE);
                savedSuccessEmailTxtView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void shareQrCodeButton() {
        shareEmailBtn = (CircleImageView) findViewById(R.id.shareButtonIdGenerateEmail);
        shareEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareQrCode();
            }
        });
    }

    private void shareQrCode() {
        BitmapDrawable drawable = (BitmapDrawable) qrCodeImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        try {
            File cachePath = new File(getApplicationContext().getExternalCacheDir(), "images");
            cachePath.mkdirs();
            File imageFile = new File(cachePath, "qr_code.png");

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            Uri imageUri = FileProvider.getUriForFile(getApplicationContext(), "com.aimagic.aiqrmagic.fileprovider", imageFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.ShareQrCodevia)));
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //download Btn and onReqPer.. and downloadQrcodeImage functions about to save it in Gallery
    private void downloadButtonSaveToGallery() {
        downloadEmailBtn = (CircleImageView) findViewById(R.id.downloadButtonIdGenerateEmail);
        downloadEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadQrcodeImage();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission granted, you can now download the image
            downloadQrcodeImage();
        } else {
            // Permission denied, handle it as needed (e.g., show a message to the user)
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Pleaseprovidetherequiredpermissions), Toast.LENGTH_SHORT).show();
        }
    }

    private void downloadQrcodeImage() {

//        //We should add this android:requestLegacyExternalStorage="true" to the manifests

        if (ContextCompat.checkSelfPermission(GenerateEmail.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) GenerateEmail.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return; // Return to avoid attempting to save the image without the required permission.
        }

        FileOutputStream outputStream = null;
        BitmapDrawable drawable = (BitmapDrawable) qrCodeImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        // Use MediaStore to save the image to the gallery
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, "QRCode_" + System.currentTimeMillis());
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        // Specify the relative path where the image will be saved (e.g., Pictures/my app name)
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + getResources().getString(R.string.app_name));

        Uri uri = getApplicationContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            outputStream = (FileOutputStream) getApplicationContext().getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.Downloadedsuccessfully), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private Bitmap generateEmailQRCode(String email, String subject, String body) {
        String emailContent = "mailto:" + email + "?subject=" + subject + "&body=" + body;

        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(emailContent, BarcodeFormat.QR_CODE, 500, 500, hints);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? qrcode_code_color : qrcode_background_color);
                }
            }
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void arrowBack() {
        CardView arrowBack = (CardView) findViewById(R.id.arrowback_from_generateEmailId);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //For Back from Activity to Fragment !!!!!
                GenerateEmail.super.onBackPressed();
            }
        });
    }

}