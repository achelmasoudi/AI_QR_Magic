package com.aimagic.aiqrmagicpro.Fragments;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraControl;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.TorchState;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.aimagic.aiqrmagicpro.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.hdodenhof.circleimageview.CircleImageView;
import com.aimagic.aiqrmagicpro.scannerTypes.ScannedContact;
import com.aimagic.aiqrmagicpro.scannerTypes.ScannedEmail;
import com.aimagic.aiqrmagicpro.scannerTypes.ScannedInstagram;
import com.aimagic.aiqrmagicpro.scannerTypes.ScannedLinkedin;
import com.aimagic.aiqrmagicpro.scannerTypes.ScannedLocation;
import com.aimagic.aiqrmagicpro.scannerTypes.ScannedPlayStoreApp;
import com.aimagic.aiqrmagicpro.scannerTypes.ScannedWebsite;
import com.aimagic.aiqrmagicpro.scannerTypes.ScannedWifi;
import com.aimagic.aiqrmagicpro.scannerTypes.ScannedX;
import com.aimagic.aiqrmagicpro.scannerTypes.ScannedYoutube;


public class ScanFragment extends Fragment {

    private View view;

    //About Zoom
    private SeekBar zoomSeekBar;
    private Camera camera;
    private CameraControl cameraControl;

    //About Gallery
    private CardView galleryBtn;

    //About FlashLight
    private CardView flashBtn;
    private Bitmap bitmapPictureFromGallery;
    //For The Qr Scan
    private ListenableFuture cameraProviderFuture;
    private ExecutorService cameraExecutor;
    private PreviewView previewView;
    private MyImageAnalyzer analyzer;
    private ImageCapture imageCapture;

    private boolean qrCodeRead = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scan , container , false);


        //About Zoom
        zoomCameraSeekBar();


        //About The gallery
        galleryBtn = (CardView) view.findViewById(R.id.fragmentScan_GalerryBtn);
        galleryButton();

        //About The FlashLight
        flashBtn = (CardView) view.findViewById(R.id.fragmentScan_FlashBtn);
        flashButton();

        previewView = view.findViewById(R.id.previewViewIdCamera);

        //Background JOB
        cameraExecutor = Executors.newSingleThreadExecutor();
        cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());

        analyzer = new MyImageAnalyzer(getActivity().getSupportFragmentManager());

        //Camera Provider Future
        cameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                //In background JOB
                try {
                    ProcessCameraProvider processCameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
                    if(ActivityCompat.checkSelfPermission(getContext() , Manifest.permission.CAMERA) != (PackageManager.PERMISSION_GRANTED) ) {
                        ActivityCompat.requestPermissions(getActivity() , new String[] {Manifest.permission.CAMERA} , 101);
                        bindPreview(processCameraProvider);
                    }
                    else {
                        bindPreview(processCameraProvider);
                    }
                }
                catch(ExecutionException e) {
                    e.printStackTrace();
                }
                catch(InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } , ContextCompat.getMainExecutor(getContext()));

        return view;
    }

    //About the zoom Of Camera ---------------------------------------------------------------------
    private void zoomCameraSeekBar() {
        zoomSeekBar = view.findViewById(R.id.seekBar_ZoomId);
        zoomSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float maxZoomRatio = camera.getCameraInfo().getZoomState().getValue().getMaxZoomRatio();
                float minZoomRatio = 1.0f;
                float zoomRatio = minZoomRatio + (maxZoomRatio - minZoomRatio) * progress / 100f;

                cameraControl.setZoomRatio(zoomRatio);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //About the FlashLight of the Phone ------------------------------------------------------------
    private void flashButton() {
        CircleImageView flashImage = (CircleImageView) view.findViewById(R.id.circleImageOfFlashId);
        flashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (camera != null && cameraControl != null) {
                    boolean isFlashlightOn = camera.getCameraInfo().getTorchState().getValue() == TorchState.ON;
                    cameraControl.enableTorch(!isFlashlightOn);
                    if(isFlashlightOn) {
                        flashImage.setImageResource(R.drawable.flash_off);
                    }
                    else {
                        flashImage.setImageResource(R.drawable.flash_on);
                    }
                }
            }
        });
    }
    @Override
    public void onPause() {
        super.onPause();
        // Turn off the flash and update the image
        if (camera != null && cameraControl != null) {
            if (camera.getCameraInfo().getTorchState().getValue() == TorchState.ON) {
                cameraControl.enableTorch(false);
                CircleImageView flashImage = view.findViewById(R.id.circleImageOfFlashId);
                flashImage.setImageResource(R.drawable.flash_off);
            }
        }
    }

    //About take qrcode from your gallery -------------------------------------
    private void galleryButton() {
        galleryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, 2);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            try {
                bitmapPictureFromGallery = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), selectedImage);
                // Decode the QR code from the selected image and extract the URL.
                scanQRCodeFromImage(bitmapPictureFromGallery);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void scanQRCodeFromImage(Bitmap imageBitmap) {
        // Decode the QR code from the image and extract the URL.
        BarcodeScannerOptions scannerOptions = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build();

        BarcodeScanner scanner = BarcodeScanning.getClient(scannerOptions);
        InputImage inputImage = InputImage.fromBitmap(imageBitmap, 0);

        scanner.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        typesOfBarcode(barcodes);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Failed to read the QR code from the image.
                        Toast.makeText(getContext(), getResources().getString(R.string.FailedtoreadtheQRcodefromthegallery), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //About take qrcode from your camera -------------------------------------

    //Requesting Permissions from the user
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if( requestCode == 101 && grantResults.length > 0 ) {
            ProcessCameraProvider processCameraProvider = null;
            try {
                processCameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
            }
            catch(ExecutionException e) {
                e.printStackTrace();
            }
            catch(InterruptedException e) {
                e.printStackTrace();
            }
            bindPreview(processCameraProvider);
        }
    }

    private void bindPreview(ProcessCameraProvider processCameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        //For zoom
        camera = processCameraProvider.bindToLifecycle(this, cameraSelector, preview);
        cameraControl = camera.getCameraControl();

        imageCapture = new ImageCapture.Builder().build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(cameraExecutor , analyzer);
        processCameraProvider.unbindAll();
        processCameraProvider.bindToLifecycle(this , cameraSelector , preview ,
                imageCapture , imageAnalysis);
    }

    //Image analyzer
    public class MyImageAnalyzer implements ImageAnalysis.Analyzer {
        private FragmentManager fragmentManager;
        public MyImageAnalyzer(FragmentManager supportFragmentManager) {
            this.fragmentManager = supportFragmentManager;
        }

        @Override
        public void analyze(@NonNull ImageProxy image) {
            scanBarCode(image);
        }

        private void scanBarCode(ImageProxy image) {
            @SuppressLint("UnsafeOptInUsageError")
            Image image1 = image.getImage();

            assert image1 != null;
            InputImage inputImage = InputImage.fromMediaImage(image1 , image.getImageInfo().getRotationDegrees());

            BarcodeScannerOptions scannerOptions = new BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(Barcode.FORMAT_QR_CODE , Barcode.FORMAT_AZTEC ).build();


            BarcodeScanner scanner = BarcodeScanning.getClient(scannerOptions);
            Task<List<Barcode>> result = scanner.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            readerBarCodeData(barcodes);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Failed to read the QR code
                            Toast.makeText(getContext() , "Failed to read the Qr code" , Toast.LENGTH_SHORT).show();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
                        @Override
                        public void onComplete(@NonNull Task<List<Barcode>> task) {
                            image.close();
                        }
                    });
        }

        private void readerBarCodeData(List<Barcode> barcodes) {
            typesOfBarcode(barcodes);
        }
    }

    public void typesOfBarcode(List<Barcode> barcodes) {
        if (qrCodeRead) {
            // If a QR code has already been read, do nothing
            return;
        }

        for(Barcode barcode : barcodes) {
            Rect bounds = barcode.getBoundingBox();

            Point[] corners = barcode.getCornerPoints();
            String rawValue = barcode.getRawValue();
            int valueType = barcode.getValueType();

            switch(valueType) {
                case Barcode.TYPE_EMAIL: {
                    qrCodeRead = true;
                    String email = barcode.getEmail().getAddress();
                    String subject = barcode.getEmail().getSubject();
                    String body = barcode.getEmail().getBody();
                    Intent intentEmail = new Intent(getActivity() , ScannedEmail.class);
                    intentEmail.putExtra("email", email);
                    intentEmail.putExtra("subject", subject);
                    intentEmail.putExtra("body", body);
                    startActivity(intentEmail);

                    // Play the sound when a QR code is successfully scanned
                    // Check the state of the scannerSoundCheckBox
                    playScannerSound();
                    playVibration();
                }
                break;
                case Barcode.TYPE_GEO: {
                    qrCodeRead = true;
                    String latitude = String.valueOf(barcode.getGeoPoint().getLat());
                    String longitude = String.valueOf(barcode.getGeoPoint().getLng());
                    Intent intentLocation = new Intent(getActivity() , ScannedLocation.class);
                    intentLocation.putExtra("latitude", latitude);
                    intentLocation.putExtra("longitude", longitude);
                    startActivity(intentLocation);
                    // Play the sound when a QR code is successfully scanned
                    playScannerSound();
                    playVibration();
                }
                break;
                case Barcode.TYPE_URL: {
                    qrCodeRead = true;
                    String url = barcode.getUrl().getUrl();
                    String instagramUrl = "https://www.instagram.com/";
                    String youtubeUrl = "https://www.youtube.com/";
                    String twitterUrl = "https://twitter.com/";
                    String linkedinUrl = "https://www.linkedin.com/in/";
                    String playStoreUrl = "https://play.google.com/store/apps/details?id=";

                    try {
                        if( !url.substring(0,26).equals(instagramUrl) && !url.substring(0,24).equals(youtubeUrl) && !url.substring(0,20).equals(twitterUrl)
                                && !url.substring(0,28).equals(linkedinUrl) && !url.substring(0,46).equals(playStoreUrl) ) {
                            Intent intentUrl = new Intent(getActivity() , ScannedWebsite.class);
                            intentUrl.putExtra("url", url);
                            startActivity(intentUrl);
                            // Play the sound when a QR code is successfully scanned
                            playScannerSound();
                            playVibration();
                        }
                        else if(url.substring(0,26).equals(instagramUrl)) {
                            Intent intentInstagram = new Intent(getActivity() , ScannedInstagram.class);
                            intentInstagram.putExtra("url", url);
                            startActivity(intentInstagram);
                            // Play the sound when a QR code is successfully scanned
                            playScannerSound();
                            playVibration();
                        }
                        else if(url.substring(0,24).equals(youtubeUrl)) {
                            Intent intentYoutube = new Intent(getActivity() , ScannedYoutube.class);
                            intentYoutube.putExtra("url", url);
                            startActivity(intentYoutube);
                            // Play the sound when a QR code is successfully scanned
                            playScannerSound();
                            playVibration();
                        }
                        else if(url.substring(0,20).equals(twitterUrl)) {
                            Intent intentLinkedin = new Intent(getActivity() , ScannedX.class);
                            intentLinkedin.putExtra("url", url);
                            startActivity(intentLinkedin);
                            // Play the sound when a QR code is successfully scanned
                            playScannerSound();
                            playVibration();
                        }
                        else if(url.substring(0,28).equals(linkedinUrl)) {
                            Intent intentLinkedin = new Intent(getActivity() , ScannedLinkedin.class);
                            intentLinkedin.putExtra("url", url);
                            startActivity(intentLinkedin);
                            // Play the sound when a QR code is successfully scanned
                            playScannerSound();
                            playVibration();
                        }
                        else if(url.substring(0,46).equals(playStoreUrl)) {
                            Intent intentPlayStoreApp = new Intent(getActivity() , ScannedPlayStoreApp.class);
                            intentPlayStoreApp.putExtra("url", url);
                            startActivity(intentPlayStoreApp);
                            // Play the sound when a QR code is successfully scanned
                            playScannerSound();
                            playVibration();
                        }
                    }
                    catch(Exception e) {
                        Intent intentUrl = new Intent(getActivity() , ScannedWebsite.class);
                        intentUrl.putExtra("url", url);
                        startActivity(intentUrl);
                        // Play the sound when a QR code is successfully scanned
                        playScannerSound();
                        playVibration();
                    }
                }
                break;
                case Barcode.TYPE_WIFI: {
                    qrCodeRead = true;
                    //SSID : the name of the password
                    String ssid = barcode.getWifi().getSsid();
                    String password = barcode.getWifi().getPassword();
                    int encryptionType = barcode.getWifi().getEncryptionType();
                    String securityType = getSecurityTypeString(encryptionType);
                    Intent intentWifi = new Intent(getActivity() , ScannedWifi.class);
                    intentWifi.putExtra("ssid", ssid);
                    intentWifi.putExtra("password", password);
                    intentWifi.putExtra("securitytype" , securityType);
                    startActivity(intentWifi);
                    // Play the sound when a QR code is successfully scanned
                    playScannerSound();
                    playVibration();
                }
                break;
                case Barcode.TYPE_CONTACT_INFO: {
                    qrCodeRead = true;
                    String fullName = barcode.getContactInfo().getName().getFormattedName();
                    String phoneNumber = barcode.getContactInfo().getPhones().get(0).getNumber();
                    Intent intentContact = new Intent(getActivity() , ScannedContact.class);
                    intentContact.putExtra("fullname", fullName);
                    intentContact.putExtra("phonenumber", phoneNumber);
                    startActivity(intentContact);
                    // Play the sound when a QR code is successfully scanned
                    playScannerSound();
                    playVibration();
                }
                break;
                default:
                    // Display a Toast message for unsupported barcode types
                    Toast.makeText(getContext(), getResources().getString(R.string.notsupportingthisbarcode), Toast.LENGTH_LONG).show();
                break;
            }
        }
    }

    private String getSecurityTypeString(int encryptionType) {
        switch (encryptionType) {
            case 1:
                return "None";
            case 2:
                return "WEP";
            case 3:
                return "WPA";
            case 4:
                return "WPA2";
            default:
                return "Unknown";
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reset the qrCodeRead flag when the fragment is resumed.
        qrCodeRead = false;
    }

    private void playScannerSound() {
        //For The Scanner Sound
        //This code for if the checkBox on The SettingsFragment Checked Or Unchecked
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefsFile_ScanSound", Context.MODE_PRIVATE);
        boolean scannerSoundChecked = sharedPreferences.getBoolean("scannerSoundChecked", false);
        if (scannerSoundChecked) {
            MediaPlayer mediaPlayer = MediaPlayer.create(getContext(), R.raw.scanner_sound);
            mediaPlayer.start();
        }
    }

    private void playVibration() {
        //For The Scanner Vibrate
        //This code for if the checkBox on The SettingsFragment Checked Or Unchecked
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefsFile_ScanVibrate", Context.MODE_PRIVATE);
        boolean scannerVibrateChecked = sharedPreferences.getBoolean("scannerVibrateChecked", false);
        if (scannerVibrateChecked) {
            Vibrator vibrator = (Vibrator) getContext().getSystemService(getContext().VIBRATOR_SERVICE);
            long vibrationDuration = 800; // ( 1000 = 1 second )
            vibrator.vibrate(vibrationDuration);
        }
    }

}