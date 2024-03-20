package com.aimagic.aiqrmagicpro.scannerTypes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aimagic.aiqrmagicpro.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.aimagic.aiqrmagicpro.Controller.DataBaseHelper_Scanned;
import com.aimagic.aiqrmagicpro.Fragments.LanguageManager;
import com.aimagic.aiqrmagicpro.Model.ScannedQrCode;
import de.hdodenhof.circleimageview.CircleImageView;

public class ScannedYoutube extends AppCompatActivity {

    private TextView username;
    private String youtubeLink, myUsername;
    private CircleImageView saveBtn, savedSuccessBtn, moreBtn;
    private TextView saveTxtView, savedSuccessTxtView;
    private CircleImageView openYoutubeBtn, copyBtn, copiedSuccessBtn, searchOnWebBtn;
    private TextView copyTxtView, copiedSuccessTxtView;

    //For The Bottom Sheet
    private BottomSheetBehavior bottomSheetBehavior;
    private FrameLayout frameLayout;
    private View shadowFromView;
    private ImageView qrCodeImage;
    private DataBaseHelper_Scanned dataBaseHelperScanned;
    private int currentNightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the locale language when the app starts
        LanguageManager.loadLocale(this);

        setContentView(R.layout.activity_scanned_youtube);

        qrCodeImage = (ImageView) findViewById(R.id.qrCodeImageIdScannedYoutube);

        //For the BottomSheet
        frameLayout = (FrameLayout) findViewById(R.id.bottomSheetIdFrameLayoutScannedYoutube);
        bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
        bottomSheetBehavior.setPeekHeight(0);

        //When the bottomSheet is Expanded The Shadow will appear
        shadowFromView = (View) findViewById(R.id.shadowFromViewId_ScannedYoutube);
        shadowFromView.setVisibility(View.GONE);

        //If the bottom sheet is expanded and a touch event is detected on the Shadow, collapse the bottom sheet
        shadowFromView.setOnTouchListener(new View.OnTouchListener() {
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
                    shadowFromView.setVisibility(View.VISIBLE);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // The BottomSheet is hidden.
                    shadowFromView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        //initiliaze the variables
        username = (TextView) findViewById(R.id.ScannedYoutube_UsernameId);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            youtubeLink = bundle.getString("url");
            //take the username from the url
            String[] parts = youtubeLink.split("/");
            // Get the last part of the string, which is the username
            myUsername = parts[parts.length - 1];

        }

        username.setText("@" + myUsername);

        moreBtn = (CircleImageView) findViewById(R.id.moreButtonIdScannedYoutube);
        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        openYoutubeButton();
        copyButton();
        searchOnWebButton();

        //Arrow Btn From ScannedEmail to ScanFragment
        arrowBack();
        
        saveBtn = (CircleImageView) findViewById(R.id.saveButtonIdScannedYoutube);
        savedSuccessBtn = (CircleImageView) findViewById(R.id.savedSuccessButtonIdScannedYoutube);
        saveTxtView = (TextView) findViewById(R.id.saveTxtViewIdScannedYoutube);
        savedSuccessTxtView = (TextView) findViewById(R.id.savedSuccessTxtViewIdScannedYoutube);
        saveQrCodeToScannedFragmentButton();

        generateYoutubeQRCode(myUsername);

        //For Dark - Light Mode ( BottomSheet and Icon Of Btns )
        currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        //Light - Dark Mode
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode is active
            frameLayout.setBackgroundResource(R.drawable.bottom_sheet_background_darkmode);
            saveBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            savedSuccessBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            moreBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            openYoutubeBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            searchOnWebBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            copyBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
            copiedSuccessBtn.setColorFilter(ContextCompat.getColor(this, R.color.white));
        } else {
            // Light mode is active
            frameLayout.setBackgroundResource(R.drawable.bottom_sheet_background);
            saveBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            savedSuccessBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            moreBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            openYoutubeBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            searchOnWebBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            copyBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
            copiedSuccessBtn.setColorFilter(ContextCompat.getColor(this, R.color.black));
        }
    }

    //Generate the qr code that the camera read it in put it in image view
    private void generateYoutubeQRCode(String username) {
        String instagramAccount = "https://www.youtube.com/" + username;

        Map<EncodeHintType, ErrorCorrectionLevel> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.Q);

        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(instagramAccount, BarcodeFormat.QR_CODE, 500, 500, hints);

            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            qrCodeImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // I will save it in the ScannedFragment using SQlite
    private void saveQrCodeToScannedFragmentButton() {
        dataBaseHelperScanned = new DataBaseHelper_Scanned(this);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Convert The ImageView To Bitmap
                qrCodeImage.setDrawingCacheEnabled(true);
                Bitmap qrCodeBitmap = Bitmap.createBitmap(qrCodeImage.getDrawingCache());
                qrCodeImage.setDrawingCacheEnabled(false);

                // Convert the Bitmap to a byte[]
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                qrCodeBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] qrCodeImageBytes = byteArrayOutputStream.toByteArray();

                String scanned_QrCode_Type = getResources().getString(R.string.Youtube);
                dataBaseHelperScanned.addData_Scanned(new ScannedQrCode(scanned_QrCode_Type, getResources().getString(R.string.Username_hint) + ": " + myUsername, qrCodeImageBytes, R.drawable.youtube_icon));
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Savedsuccessfully), Toast.LENGTH_SHORT).show();
                saveBtn.setVisibility(View.GONE);
                saveTxtView.setVisibility(View.GONE);
                savedSuccessBtn.setVisibility(View.VISIBLE);
                savedSuccessTxtView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void openYoutubeButton() {
        openYoutubeBtn = (CircleImageView) findViewById(R.id.openYoutubeButtonIdScannedYoutube);
        openYoutubeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if the YouTube app is installed
                boolean appInstalled = isAppInstalled("com.google.android.youtube");
                if (appInstalled) {
                    // YouTube app is installed, open it
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setPackage("com.google.android.youtube");
                    intent.setData(Uri.parse("https://www.youtube.com/" + myUsername));
                    startActivity(intent);
                } else {
                    // YouTube app is not installed, open the YouTube website
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/" + myUsername));
                    startActivity(intent);
                }
            }
        });
    }

    private boolean isAppInstalled(String packageName) {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void copyButton() {
        copyBtn = (CircleImageView) findViewById(R.id.copyButtonIdScannedYoutube);
        copiedSuccessBtn = (CircleImageView) findViewById(R.id.copiedSuccessButtonIdScannedYoutube);
        copyTxtView = (TextView) findViewById(R.id.copyTxtViewIdScannedYoutube);
        copiedSuccessTxtView = (TextView) findViewById(R.id.copiedSuccessTxtViewIdScannedYoutube);

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    clipboard.setText(myUsername);
                } else {
                    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                    android.content.ClipData clip = android.content.ClipData.newPlainText(getResources().getString(R.string.CopiedText), myUsername);
                    clipboard.setPrimaryClip(clip);
                }

                copyBtn.setVisibility(View.GONE);
                copyTxtView.setVisibility(View.GONE);

                copiedSuccessBtn.setVisibility(View.VISIBLE);
                copiedSuccessTxtView.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.Copiedsuccessfully), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchOnWebButton() {
        searchOnWebBtn = (CircleImageView) findViewById(R.id.searchOnWebButtonIdScannedYoutube);
        searchOnWebBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScannedYoutube.this, InAppBrowser.class);
                intent.putExtra("searchquery", myUsername);
                startActivity(intent);
            }
        });
    }

    private void arrowBack() {
        CardView arrowBack = (CardView) findViewById(R.id.arrowback_from_ScannedYoutubeId);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //For Back from Activity to Fragment !!!!!
                ScannedYoutube.super.onBackPressed();
            }
        });
    }
    //When i clicked the phone's back Button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}