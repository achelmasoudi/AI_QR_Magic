package com.aimagic.aiqrmagicpro.Controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.aimagic.aiqrmagicpro.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.aimagic.aiqrmagicpro.FragmentsOfTabLayout.ScannedFragment;
import com.aimagic.aiqrmagicpro.Model.ScannedQrCode;

public class ScannedFragmentAdapter extends RecyclerView.Adapter<ScannedFragmentAdapter.MyViewHolder> {

    private Context context;
    private List<ScannedQrCode> scannedQrCodeList;
    private DataBaseHelper_Scanned dataBaseHelperScanned;
    private AlertDialog.Builder alertDialog;
    public ScannedFragmentAdapter(Context context , List<ScannedQrCode> scannedQrCodeList) {
        this.context = context;
        this.scannedQrCodeList = scannedQrCodeList;
    }

    public ScannedFragmentAdapter(Context pContext , List<ScannedQrCode> pDataList , DataBaseHelper_Scanned dataBaseHelperScanned) {
        this.context = pContext;
        this.scannedQrCodeList = pDataList;
        this.dataBaseHelperScanned = dataBaseHelperScanned;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.scanned_fragment_list_row , parent , false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ScannedQrCode scannedQrCode = scannedQrCodeList.get(position);

        //Convert From byte[] to Bitmap
        byte[] imageByteArray = scannedQrCode.getScanned_QrCode_Image();
        Bitmap qrCodeBitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        holder.scanned_QrCode_ImageView.setImageBitmap(qrCodeBitmap);

        holder.scanned_QrCode_ImageType_ImageView.setImageResource(scannedQrCode.getScanned_QrCode_ImageType());

        holder.scanned_QrCode_Type_TextView.setText(scannedQrCode.getScanned_QrCode_Type());
        holder.scanned_QrCode_Link_TextView.setText(scannedQrCode.getScanned_QrCode_Link());
        holder.scanned_QrCode_dateTime_TextView.setText(scannedQrCode.getScanned_QrCode_DateTime());

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext() , android.R.anim.slide_in_left);
        holder.scanned_QrCode_cardView.startAnimation(animation);


        //The Three Dots Menuuu
        holder.threeDotsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Display Option Menu
                PopupMenu popupMenu = new PopupMenu(context , holder.threeDotsMenu);
                popupMenu.inflate(R.menu.saved_fragment_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch(item.getItemId()) {
                            case R.id.generated_QrCode_deleteId: {
                                alertDialog = new AlertDialog.Builder(context);
                                alertDialog.setCancelable(false);
                                alertDialog.setTitle(context.getResources().getString(R.string.DELETE));
                                alertDialog.setMessage(context.getResources().getString(R.string.Areyousureyouwanttodelete));
                                alertDialog.setPositiveButton(context.getResources().getString(R.string.DELETE), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        deleteOfData(position);
                                    }
                                });
                                alertDialog.setNegativeButton(context.getResources().getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                                AlertDialog dialog = alertDialog.create();

                                //Change The Color Delete Btn
                                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface dialog) {
                                        Button positiveButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                                        positiveButton.setTextColor(Color.parseColor("#FF1B00"));
                                        Button negativeButton = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);
                                        negativeButton.setTextColor(Color.parseColor("#878787"));
                                    }
                                });

                                dialog.show();
                            }
                            break;
                            case R.id.generated_QrCode_shareId:
                                ImageView qrCodeImageViewShare = holder.scanned_QrCode_ImageView;
                                shareFunc(qrCodeImageViewShare);
                                break;
                            case R.id.generated_QrCode_downloadId:
                                ImageView qrCodeImageViewDownload = holder.scanned_QrCode_ImageView;
                                downloadQrcodeImage(qrCodeImageViewDownload);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return scannedQrCodeList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private CardView scanned_QrCode_cardView;
        private ImageView scanned_QrCode_ImageView , scanned_QrCode_ImageType_ImageView;
        private TextView scanned_QrCode_Type_TextView , scanned_QrCode_Link_TextView;
        private TextView scanned_QrCode_dateTime_TextView;

        private CardView threeDotsMenu;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            scanned_QrCode_cardView = (CardView) itemView.findViewById(R.id.scanned_QrCode_cardViewId);
            scanned_QrCode_ImageView = (ImageView) itemView.findViewById(R.id.scanned_QrCode_ImageId);

            scanned_QrCode_ImageType_ImageView = (ImageView) itemView.findViewById(R.id.scanned_QrCode_ImageTypeId);

            scanned_QrCode_Type_TextView = (TextView) itemView.findViewById(R.id.scanned_QrCode_TypeId);
            scanned_QrCode_Link_TextView = (TextView) itemView.findViewById(R.id.scanned_QrCode_LinkId);

            scanned_QrCode_dateTime_TextView = (TextView) itemView.findViewById(R.id.scanned_QrCode_dateTimeId);

            threeDotsMenu = (CardView) itemView.findViewById(R.id.threeDotsOfMenu);
        }
    }

    public void deleteOfData(int position) {
        dataBaseHelperScanned.deleteData_Scanned(scannedQrCodeList.get(position));
        scannedQrCodeList.remove(position);
        ScannedFragment.notifyAdapter();
        Toast.makeText(context , context.getResources().getString(R.string.Successfullydeleted) , Toast.LENGTH_SHORT).show();
        //For The Visibility Of The Animation
        ScannedFragment.updateAnimationVisibility();
    }
    private void shareFunc(ImageView qrCodeImageView) {
        BitmapDrawable drawable = (BitmapDrawable) qrCodeImageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        try {
            File cachePath = new File(context.getExternalCacheDir(), "images");
            cachePath.mkdirs();
            File imageFile = new File(cachePath, "qr_code.png");

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            Uri imageUri = FileProvider.getUriForFile(context, "com.aimagic.aiqrmagic.fileprovider", imageFile);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/*");
            shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(shareIntent, context.getResources().getString(R.string.ShareQrCodevia)));
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //downloadQrcodeImage functions about to save it in Gallery
    private void downloadQrcodeImage(ImageView qrCodeImageView) {

        //We should add this android:requestLegacyExternalStorage="true" to the manifests
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
        values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + File.separator + context.getResources().getString(R.string.app_name));

        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        try {
            outputStream = (FileOutputStream) context.getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            Toast.makeText(context, context.getResources().getString(R.string.Downloadedsuccessfully), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
}
