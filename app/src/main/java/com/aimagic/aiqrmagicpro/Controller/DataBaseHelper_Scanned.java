package com.aimagic.aiqrmagicpro.Controller;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import com.aimagic.aiqrmagicpro.Model.ScannedQrCode;
import com.aimagic.aiqrmagicpro.constants.Const_Generated;
import com.aimagic.aiqrmagicpro.constants.Const_Scanned;

public class DataBaseHelper_Scanned extends SQLiteOpenHelper {

    public DataBaseHelper_Scanned(Context context) {
        super(context , Const_Scanned.DATABASE_NAME , null , Const_Scanned.DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String myTable = "CREATE TABLE " + Const_Scanned.TABLE_NAME + " (" +
                Const_Scanned.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Const_Scanned.COLUMN_QrCode_Type + " TEXT, " +
                Const_Scanned.COLUMN_QrCode_Link + " TEXT, " +
                Const_Scanned.COLUMN_QrCode_Image + " BLOB, " +
                Const_Scanned.COLUMN_QrCode_ImageType + " INTEGER, " +
                Const_Scanned.COLUMN_TIME_STAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP );" ;
        db.execSQL(myTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Const_Scanned.TABLE_NAME);
        onCreate(db);
    }

    public long addData_Scanned(ScannedQrCode data) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put( Const_Scanned.COLUMN_QrCode_Type , data.getScanned_QrCode_Type() );
        cv.put( Const_Scanned.COLUMN_QrCode_Link , data.getScanned_QrCode_Link() );
        cv.put( Const_Scanned.COLUMN_QrCode_Image , data.getScanned_QrCode_Image());
        cv.put( Const_Scanned.COLUMN_QrCode_ImageType , data.getScanned_QrCode_ImageType() );

        // Get the current date and time in your local timezone
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        dateFormat.setTimeZone(TimeZone.getDefault());
        timeFormat.setTimeZone(TimeZone.getDefault());

        String formattedDate = dateFormat.format(new Date());
        String formattedTime = timeFormat.format(new Date());

        String combinedDateAndTime = formattedDate + "    " + formattedTime;

        cv.put(Const_Generated.COLUMN_TIME_STAMP, combinedDateAndTime);

        long insData = database.insert(Const_Scanned.TABLE_NAME , null , cv);
        database.close();
        return insData;
    }
    public void deleteData_Scanned(ScannedQrCode data) {
        SQLiteDatabase database = this.getWritableDatabase();
        String arr2[] = { String.valueOf(data.getId()) } ;
        database.delete(Const_Scanned.TABLE_NAME , Const_Scanned.COLUMN_ID + "=?" , arr2 );
        database.close();
    }

    @SuppressLint("Range")
    public List<ScannedQrCode> getAllData_Scanned() {
        SQLiteDatabase database = this.getReadableDatabase();
        List<ScannedQrCode> dataList = new ArrayList<>();

        try {
            String query = "SELECT * FROM " + Const_Scanned.TABLE_NAME +
                    " ORDER BY " + Const_Scanned.COLUMN_TIME_STAMP + " DESC ";

            Cursor cursor = database.rawQuery(query , null);

            if( cursor.moveToFirst() ) {
                do {
                    ScannedQrCode data = new ScannedQrCode();
                    data.setId( cursor.getInt( cursor.getColumnIndex(Const_Scanned.COLUMN_ID) ) );
                    data.setScanned_QrCode_Type( cursor.getString( cursor.getColumnIndex(Const_Scanned.COLUMN_QrCode_Type) ) );
                    data.setScanned_QrCode_Link( cursor.getString( cursor.getColumnIndex(Const_Scanned.COLUMN_QrCode_Link) ) );
                    data.setScanned_QrCode_Image( cursor.getBlob( cursor.getColumnIndex(Const_Scanned.COLUMN_QrCode_Image) ) );
                    data.setScanned_QrCode_ImageType( cursor.getInt( cursor.getColumnIndex(Const_Scanned.COLUMN_QrCode_ImageType) ) );
                    data.setScanned_QrCode_DateTime( cursor.getString( cursor.getColumnIndex(Const_Scanned.COLUMN_TIME_STAMP) ) );
                    dataList.add(data);
                }
                while ( cursor.moveToNext() ) ;
            }

            database.close();
            cursor.close();
        }
        catch(Exception e) {
            Log.e("Error" , "Error : " + e.getMessage());
        }
        return dataList;
    }


}
