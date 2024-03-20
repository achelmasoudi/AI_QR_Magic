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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.textfield.TextInputLayout;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
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

public class GenerateLocation extends AppCompatActivity implements OnMapReadyCallback , View.OnClickListener {

    private CardView generateLocationBtn;
    private TextInputLayout latitudeEditTxt , longitudeEditTxt;
    private String myLatitudeEditTxt , myLongitudeEditTxt;
    private ImageView qrCodeImageView;

    private CircleImageView downloadLocationBtn;

    //To Share the QrCode
    private CircleImageView shareLocationBtn;

    //For The Bottom Sheet
    private BottomSheetBehavior bottomSheetBehavior;
    private FrameLayout frameLayout;

    private CircleImageView saveLocationBtn , savedSuccessLocationBtn;
    private TextView saveLocationTxtView , savedSuccessLocationTxtView;
    private View shadowFromView1 , shadowFromView2;
    private DataBaseHelper_Generated dataBaseHelperGenerated;
    private GoogleMap mMap;
    private String mapLatitude , mapLongitude;

    //Custom The Qrcode
    private CircleImageView customQrcodeBtn;
    private int qrcode_background_color , qrcode_code_color;
   private int currentNightMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the locale language and The Mode when the app starts
        LanguageManager.loadLocale(this);

        setContentView(R.layout.activity_generate_location);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.generate_Location_Map_Fragment);
        mapFragment.getMapAsync(this);


        //For the BottomSheet
        frameLayout = (FrameLayout) findViewById(R.id.bottomSheetIdFrameLayoutGenerateLocation);
        bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
        bottomSheetBehavior.setPeekHeight(0);

        //When the bottomSheet is Expanded The Shadow will appear
        shadowFromView1 = (View) findViewById(R.id.shadowFromViewId_GenerateLocation);
        shadowFromView1.setVisibility(View.GONE);

        generateLocationBtn = (CardView) findViewById(R.id.generate_Location_Btn);
        generateLocationBtn.setOnClickListener(this);

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
        latitudeEditTxt = (TextInputLayout) findViewById(R.id.generateFragment_LocationGenerate_LatitudeId);
        longitudeEditTxt = (TextInputLayout) findViewById(R.id.generateFragment_LocationGenerate_LongitudeId);
        qrCodeImageView = (ImageView) findViewById(R.id.imageOfQrCode_LocationGenerateId);

        //Arrow Btn From GenerateContact to GenerateFragment
        arrowBack();

        //To Download The QRCode in Save it in Gallery
        downloadButtonSaveToGallery();

        //To Share The QrCode
        shareQrCodeButton();

        saveLocationBtn = (CircleImageView) findViewById(R.id.saveButtonIdGenerateLocation);
        savedSuccessLocationBtn = (CircleImageView) findViewById(R.id.savedSuccessButtonIdGenerateLocation);
        saveLocationTxtView = (TextView) findViewById(R.id.saveTxtViewIdGenerateLocation);
        savedSuccessLocationTxtView = (TextView) findViewById(R.id.savedSuccessTxtViewIdGenerateLocation);
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
            saveLocationBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            savedSuccessLocationBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            shareLocationBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            downloadLocationBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            customQrcodeBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
        }
        else {
            // Light mode is active
            frameLayout.setBackgroundResource(R.drawable.bottom_sheet_background);
            saveLocationBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            savedSuccessLocationBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            shareLocationBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            downloadLocationBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            customQrcodeBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
        }
    }

    private void customQrcodeButton() {
        //For the BottomSheet
        FrameLayout frameLayout_CustomQrCode = (FrameLayout) findViewById(R.id.bottomSheetIdFrameLayoutGenerateLocation_CustomQrcode);
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
        shadowFromView2 = (View) findViewById(R.id.shadowFromViewId_GenerateLocation2);

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

        customQrcodeBtn = (CircleImageView) findViewById(R.id.customButtonIdGenerateLocation);
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
        Bitmap qrCodeBitmapLocation = generateLocationQRCode(myLatitudeEditTxt , myLongitudeEditTxt);
        if (qrCodeBitmapLocation != null) {
            qrCodeImageView.setImageBitmap(qrCodeBitmapLocation);
        } else {
            Toast.makeText(getApplicationContext(), "Failed to update QR code", Toast.LENGTH_SHORT).show();
        }
    }


    //For GOOGLE MAPS
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                // Zoom in or out on the clicked location.
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                mMap.animateCamera(CameraUpdateFactory.zoomOut());

                aboutMarker(latLng);

                mapLatitude = String.valueOf(latLng.latitude);
                mapLongitude = String.valueOf(latLng.longitude);

                latitudeEditTxt.getEditText().setText(mapLatitude);
                longitudeEditTxt.getEditText().setText(mapLongitude);

            }
        });

    }

    private void aboutMarker(LatLng latLng) {
        //Clear the map to remove the previous marker
        mMap.clear();

        // Create a marker and add it to the map.
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(getResources().getString(R.string.Youselectedthislocation)));

        // Animate the camera to the marker.
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
    }
    @Override
    public void onClick(View view) {

        myLatitudeEditTxt = latitudeEditTxt.getEditText().getText().toString();
        myLongitudeEditTxt = longitudeEditTxt.getEditText().getText().toString();


        //About the Visibility Of Save and SavedSuccess
        saveLocationBtn.setVisibility(View.VISIBLE);
        saveLocationTxtView.setVisibility(View.VISIBLE);
        savedSuccessLocationBtn.setVisibility(View.GONE);
        savedSuccessLocationTxtView.setVisibility(View.GONE);

        latitudeEditTxt.setErrorEnabled(true);
        longitudeEditTxt.setErrorEnabled(true);

        if(myLatitudeEditTxt.isEmpty() || myLongitudeEditTxt.isEmpty()) {
            if(myLatitudeEditTxt.isEmpty() && myLongitudeEditTxt.isEmpty()) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.PleaseentertheLatitudeandLongitude), Toast.LENGTH_SHORT).show();
                latitudeEditTxt.setError(getResources().getString(R.string.EntertheLatitude));
                longitudeEditTxt.setError(getResources().getString(R.string.EntertheLongitude));
            }
            else if (myLatitudeEditTxt.isEmpty() && !myLongitudeEditTxt.isEmpty()) {
                Toast.makeText(getApplicationContext() , getResources().getString(R.string.PleaseentertheLatitude) , Toast.LENGTH_SHORT).show();
                latitudeEditTxt.setError(getResources().getString(R.string.EntertheLatitude));
                longitudeEditTxt.setErrorEnabled(false);
            }
            else if(myLongitudeEditTxt.isEmpty() && !myLatitudeEditTxt.isEmpty()) {
                Toast.makeText(getApplicationContext() , getResources().getString(R.string.PleaseentertheLongitude) , Toast.LENGTH_SHORT).show();
                longitudeEditTxt.setError(getResources().getString(R.string.EntertheLongitude));
                latitudeEditTxt.setErrorEnabled(false);
            }
        }

        else {

            //put the Marker on the map after entering Latitude & Longitude in EditText
            //Clear the map to remove the previous marker
            mMap.clear();
            LatLng latLng = new LatLng(Double.parseDouble(myLatitudeEditTxt) , Double.parseDouble(myLongitudeEditTxt));
            mMap.addMarker(new MarkerOptions().position(latLng).title(getResources().getString(R.string.Youselectedthislocation)));
            // Animate the camera to the marker.
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            latitudeEditTxt.setErrorEnabled(false);
            longitudeEditTxt.setErrorEnabled(false);

            try {
                // Clear the focus from the EditText to hide the selection
                latitudeEditTxt.getEditText().clearFocus();
                longitudeEditTxt.getEditText().clearFocus();

                // Hide the virtual keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                Bitmap qrCodeBitmapLocation = generateLocationQRCode(myLatitudeEditTxt ,myLongitudeEditTxt );

                if(qrCodeBitmapLocation != null) {
                    qrCodeImageView.setImageBitmap(qrCodeBitmapLocation);
                }
                else {
                    Toast.makeText(getApplicationContext() , "Failed" , Toast.LENGTH_SHORT).show();
                }
            }
            catch(Exception e) {
                Toast.makeText(getApplicationContext() , "Failed" , Toast.LENGTH_SHORT).show();
            }
        }
    }

    // I will save it in the GeneratedFragment using SQlite
    private void saveQrCodeToGeneratedFragmentButton() {
        dataBaseHelperGenerated = new DataBaseHelper_Generated(this);

        saveLocationBtn.setOnClickListener(new View.OnClickListener() {
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

                String generated_QrCode_Type = getResources().getString(R.string.Location);
                dataBaseHelperGenerated.addData_Generated(new GeneratedQrCode(generated_QrCode_Type , getResources().getString(R.string.Geo) + ": " + myLatitudeEditTxt+" , "+myLongitudeEditTxt , qrCodeImageBytes , R.drawable.location_icon));
                Toast.makeText(getApplicationContext() , getResources().getString(R.string.Savedsuccessfully) , Toast.LENGTH_SHORT).show();
                saveLocationBtn.setVisibility(View.GONE);
                saveLocationTxtView.setVisibility(View.GONE);
                savedSuccessLocationBtn.setVisibility(View.VISIBLE);
                savedSuccessLocationTxtView.setVisibility(View.VISIBLE);
            }
        });
    }
    private void shareQrCodeButton() {
        shareLocationBtn = (CircleImageView) findViewById(R.id.shareButtonIdGenerateLocation);
        shareLocationBtn.setOnClickListener(new View.OnClickListener() {
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
        downloadLocationBtn = (CircleImageView) findViewById(R.id.downloadButtonIdGenerateLocation);
        downloadLocationBtn.setOnClickListener(new View.OnClickListener() {
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

        if (ContextCompat.checkSelfPermission(GenerateLocation.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) GenerateLocation.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
    public Bitmap generateLocationQRCode(String latitude, String longitude) {

        String geo = "geo:" + latitude + "," + longitude;
        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);


        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(geo , BarcodeFormat.QR_CODE, 500, 500, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? qrcode_code_color : qrcode_background_color);
                }
            }
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void arrowBack() {
        CardView arrowBack = (CardView) findViewById(R.id.arrowback_from_generateLocationId);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //For Back from Activity to Fragment !!!!!
                GenerateLocation.super.onBackPressed();
            }
        });
    }
}