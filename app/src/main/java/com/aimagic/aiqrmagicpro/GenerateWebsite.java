package com.aimagic.aiqrmagicpro;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

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
import java.util.HashMap;
import java.util.Map;

import com.aimagic.aiqrmagicpro.Controller.DataBaseHelper_Generated;
import com.aimagic.aiqrmagicpro.Fragments.LanguageManager;
import com.aimagic.aiqrmagicpro.Model.GeneratedQrCode;
import de.hdodenhof.circleimageview.CircleImageView;

public class GenerateWebsite extends AppCompatActivity implements View.OnClickListener{

    private CardView generateWebsiteBtn;
    private TextInputLayout linkEditText;
    private String myLinkEditText ;
    private ImageView qrCodeImageView;

    private CircleImageView downloadWebsiteBtn;

    //To Share the QrCode
    private CircleImageView shareWebsiteBtn;

    //For The Bottom Sheet
    private BottomSheetBehavior bottomSheetBehavior;
    private FrameLayout frameLayout;

    private CircleImageView saveWebsiteBtn , savedSuccessWebsiteBtn;

    private TextView saveWebsiteTxtView , savedSuccessWebsiteTxtView;
    private View shadowFromView1 , shadowFromView2;
    private DataBaseHelper_Generated dataBaseHelperGenerated;
    private TextView txtOfhttps , txtOfhttp , txtOfwww , txtOfcom;

    //Custom The Qrcode
    private CircleImageView customQrcodeBtn;
    private int qrcode_background_color , qrcode_code_color;
    private int currentNightMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the locale language and The Mode when the app starts
        LanguageManager.loadLocale(this);

        setContentView(R.layout.activity_generate_website);

        //For the BottomSheet
        frameLayout = (FrameLayout) findViewById(R.id.bottomSheetIdFrameLayoutGenerateWebsite);
        bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
        bottomSheetBehavior.setPeekHeight(0);

        //When the bottomSheet is Expanded The Shadow will appear
        shadowFromView1 = (View) findViewById(R.id.shadowFromViewId_GenerateWebsite);
        shadowFromView1.setVisibility(View.GONE);

        generateWebsiteBtn = (CardView) findViewById(R.id.generate_Website_Btn);
        generateWebsiteBtn.setOnClickListener(this);

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
        linkEditText = (TextInputLayout) findViewById(R.id.generateFragment_WebsiteGenerate_linkId);
        qrCodeImageView = (ImageView) findViewById(R.id.imageOfQrCode_WebsiteGenerateId);

        //Arrow Btn From GenerateContact to GenerateFragment
        arrowBack();

        //To Download The QRCode in Save it in Gallery
        downloadButtonSaveToGallery();

        //To Share The QrCode
        shareQrCodeButton();

        saveWebsiteBtn = (CircleImageView) findViewById(R.id.saveButtonIdGenerateWebsite);
        savedSuccessWebsiteBtn = (CircleImageView) findViewById(R.id.savedSuccessButtonIdGenerateWebsite);
        saveWebsiteTxtView = (TextView) findViewById(R.id.saveTxtViewIdGenerateWebsite);
        savedSuccessWebsiteTxtView = (TextView) findViewById(R.id.savedSuccessTxtViewIdGenerateWebsite);
        saveQrCodeToGeneratedFragmentButton();

        helpingWords();

        customQrcodeButton();

        qrcode_background_color = Color.parseColor("#FFFFFF");
        qrcode_code_color = Color.parseColor("#000000");

        //For Dark - Light Mode ( BottomSheet and Icon Of Btns )
        currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //Light - Dark Mode
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode is active
            frameLayout.setBackgroundResource(R.drawable.bottom_sheet_background_darkmode);
            saveWebsiteBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            savedSuccessWebsiteBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            shareWebsiteBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            downloadWebsiteBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            customQrcodeBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
        }
        else {
            // Light mode is active
            frameLayout.setBackgroundResource(R.drawable.bottom_sheet_background);
            saveWebsiteBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            savedSuccessWebsiteBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            shareWebsiteBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            downloadWebsiteBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            customQrcodeBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
        }

    }

    private void customQrcodeButton() {
        //For the BottomSheet
        FrameLayout frameLayout_CustomQrCode = (FrameLayout) findViewById(R.id.bottomSheetIdFrameLayoutGenerateWebsite_CustomQrcode);
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
        shadowFromView2 = (View) findViewById(R.id.shadowFromViewId_GenerateWebsite2);

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

        customQrcodeBtn = (CircleImageView) findViewById(R.id.customButtonIdGenerateWebsite);
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
        Bitmap qrCodeBitmapWebsite = generateWebsiteQRCode(myLinkEditText);
        if (qrCodeBitmapWebsite != null) {
            qrCodeImageView.setImageBitmap(qrCodeBitmapWebsite);
        } else {
            Toast.makeText(getApplicationContext(), "Failed to update QR code", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View view) {
        myLinkEditText = linkEditText.getEditText().getText().toString();

        //About the Visibility Of Save and SavedSuccess
        saveWebsiteBtn.setVisibility(View.VISIBLE);
        saveWebsiteTxtView.setVisibility(View.VISIBLE);
        savedSuccessWebsiteBtn.setVisibility(View.GONE);
        savedSuccessWebsiteTxtView.setVisibility(View.GONE);

        linkEditText.setErrorEnabled(true);

        try {
            if(myLinkEditText.isEmpty()) {
                Toast.makeText(getApplicationContext() , getResources().getString(R.string.PleaseenteryourLink) , Toast.LENGTH_SHORT).show();
                linkEditText.setError(getResources().getString(R.string.EnterYourLink));
            }
            else if(!myLinkEditText.substring(0,8).equals("https://") && !myLinkEditText.substring(0,7).equals("http://")) {
                Toast.makeText(getApplicationContext() , getResources().getString(R.string.YoumustputhttpsorhttpatthefirstofyourURL) , Toast.LENGTH_SHORT).show();
                linkEditText.setError(getResources().getString(R.string.PuthttpsorhttpatthefirstofyourURL));
            }
            else if(!Patterns.WEB_URL.matcher(myLinkEditText).matches()) {
                linkEditText.setError(getResources().getString(R.string.PleaseenteravalidLink));
            }
            else {
                linkEditText.setErrorEnabled(false);

                try {
                    // Clear the focus from the EditText to hide the selection
                    linkEditText.getEditText().clearFocus();

                    // Hide the virtual keyboard
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                    Bitmap qrCodeBitmapWebsite = generateWebsiteQRCode(myLinkEditText);

                    if(qrCodeBitmapWebsite != null) {
                        qrCodeImageView.setImageBitmap(qrCodeBitmapWebsite);
                    }
                    else {
                        Toast.makeText(getApplicationContext() , "Failed" , Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e) {
                    Toast.makeText(getApplicationContext() , "Error: " + e.getMessage() , Toast.LENGTH_SHORT).show();
                }
            }

        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext() , getResources().getString(R.string.YoumustputhttpsorhttpatthefirstofyourURL) , Toast.LENGTH_SHORT).show();
            linkEditText.setError(getResources().getString(R.string.PuthttpsorhttpatthefirstofyourURL));
        }
    }

    // I will save it in the GeneratedFragment using SQlite
    private void saveQrCodeToGeneratedFragmentButton() {
        dataBaseHelperGenerated = new DataBaseHelper_Generated(this);

        saveWebsiteBtn.setOnClickListener(new View.OnClickListener() {
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

                String generated_QrCode_Type = getResources().getString(R.string.Website);
                dataBaseHelperGenerated.addData_Generated(new GeneratedQrCode(generated_QrCode_Type , getResources().getString(R.string.Website)+": " + myLinkEditText , qrCodeImageBytes , R.drawable.website_icon));
                Toast.makeText(getApplicationContext() , getResources().getString(R.string.Savedsuccessfully) , Toast.LENGTH_SHORT).show();
                saveWebsiteBtn.setVisibility(View.GONE);
                saveWebsiteTxtView.setVisibility(View.GONE);
                savedSuccessWebsiteBtn.setVisibility(View.VISIBLE);
                savedSuccessWebsiteTxtView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void shareQrCodeButton() {
        shareWebsiteBtn = (CircleImageView) findViewById(R.id.shareButtonIdGenerateWebsite);
        shareWebsiteBtn.setOnClickListener(new View.OnClickListener() {
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
        downloadWebsiteBtn = (CircleImageView) findViewById(R.id.downloadButtonIdGenerateWebsite);
        downloadWebsiteBtn.setOnClickListener(new View.OnClickListener() {
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

        if (ContextCompat.checkSelfPermission(GenerateWebsite.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) GenerateWebsite.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
    private Bitmap generateWebsiteQRCode(String url) {

        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 500, 500, hints);

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
        CardView arrowBack = (CardView) findViewById(R.id.arrowback_from_generateWebsiteId);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //For Back from Activity to Fragment !!!!!
                GenerateWebsite.super.onBackPressed();
            }
        });
    }

    private void helpingWords() {

        txtOfhttps = (TextView) findViewById(R.id.generateFragment_WebsiteGenerate_httpsId);
        txtOfhttp = (TextView) findViewById(R.id.generateFragment_WebsiteGenerate_httpId);
        txtOfwww = (TextView) findViewById(R.id.generateFragment_WebsiteGenerate_wwwId);
        txtOfcom = (TextView) findViewById(R.id.generateFragment_WebsiteGenerate_comId);

        txtOfhttps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkEditText.getEditText().append("https://");
                linkEditText.getEditText().setSelection(linkEditText.getEditText().getText().length());
            }
        });

        txtOfhttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkEditText.getEditText().append("http://");
                linkEditText.getEditText().setSelection(linkEditText.getEditText().getText().length());
            }
        });

        txtOfwww.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkEditText.getEditText().append("www.");
                linkEditText.getEditText().setSelection(linkEditText.getEditText().getText().length());
            }
        });

        txtOfcom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkEditText.getEditText().append(".com");
                linkEditText.getEditText().setSelection(linkEditText.getEditText().getText().length());
            }
        });

    }

}