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

import com.aimagic.aiqrmagicpro.Model.GeneratedQrCode;
import com.aimagic.aiqrmagicpro.constants.Const_Generated;

public class DataBaseHelper_Generated extends SQLiteOpenHelper {

    public DataBaseHelper_Generated(Context context) {
        super(context , Const_Generated.DATABASE_NAME , null , Const_Generated.DATABASE_VERSION );
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String myTable = "CREATE TABLE " + Const_Generated.TABLE_NAME + " (" +
                Const_Generated.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Const_Generated.COLUMN_QrCode_Type + " TEXT, " +
                Const_Generated.COLUMN_QrCode_Link + " TEXT, " +
                Const_Generated.COLUMN_QrCode_Image + " BLOB, " +
                Const_Generated.COLUMN_QrCode_ImageType + " INTEGER, " +
                Const_Generated.COLUMN_TIME_STAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP );" ;
        sqLiteDatabase.execSQL(myTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Const_Generated.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    //Insert (long function) DATA ( Add DATA Function )
    public long addData_Generated(GeneratedQrCode data) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put( Const_Generated.COLUMN_QrCode_Type , data.getGenerated_QrCode_Type() );
        cv.put( Const_Generated.COLUMN_QrCode_Link , data.getGenerated_QrCode_Link() );
        cv.put( Const_Generated.COLUMN_QrCode_Image , data.getGenerated_QrCode_Image());
        cv.put( Const_Generated.COLUMN_QrCode_ImageType , data.getGenerated_QrCode_ImageType() );

        // Get the current date and time in your local timezone
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        dateFormat.setTimeZone(TimeZone.getDefault());
        timeFormat.setTimeZone(TimeZone.getDefault());

        String formattedDate = dateFormat.format(new Date());
        String formattedTime = timeFormat.format(new Date());

        String combinedDateAndTime = formattedDate + "    " + formattedTime;

        cv.put(Const_Generated.COLUMN_TIME_STAMP, combinedDateAndTime);

        long insData = database.insert(Const_Generated.TABLE_NAME , null , cv);
        database.close();
        return insData;
    }

    // Delete DATA FUNCTION / ( ONLY VOID WITHOUT RETURN )
    public void deleteData_Generated(GeneratedQrCode data) {
        SQLiteDatabase database = this.getWritableDatabase();
        String arr2[] = { String.valueOf(data.getId()) } ;
        database.delete(Const_Generated.TABLE_NAME , Const_Generated.COLUMN_ID + "=?" , arr2 );
        database.close();
    }

    //Get ALL Data ( USING Cursor !!! / RETURN DATA )
    @SuppressLint("Range")
    public List<GeneratedQrCode> getAllData_Generated() {
        SQLiteDatabase database = this.getReadableDatabase();
        List<GeneratedQrCode> dataList = new ArrayList<>();

        try {
            String query = "SELECT * FROM " + Const_Generated.TABLE_NAME +
                    " ORDER BY " + Const_Generated.COLUMN_TIME_STAMP + " DESC ";

            Cursor cursor = database.rawQuery(query , null);

            if( cursor.moveToFirst() ) {
                do {
                    GeneratedQrCode data = new GeneratedQrCode();
                    data.setId( cursor.getInt( cursor.getColumnIndex(Const_Generated.COLUMN_ID) ) );
                    data.setGenerated_QrCode_Type( cursor.getString( cursor.getColumnIndex(Const_Generated.COLUMN_QrCode_Type) ) );
                    data.setGenerated_QrCode_Link( cursor.getString( cursor.getColumnIndex(Const_Generated.COLUMN_QrCode_Link) ) );
                    data.setGenerated_QrCode_Image( cursor.getBlob( cursor.getColumnIndex(Const_Generated.COLUMN_QrCode_Image) ) );
                    data.setGenerated_QrCode_ImageType( cursor.getInt( cursor.getColumnIndex(Const_Generated.COLUMN_QrCode_ImageType) ) );
                    data.setGenerated_QrCode_DateTime( cursor.getString( cursor.getColumnIndex(Const_Generated.COLUMN_TIME_STAMP) ) );
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


    //this function for the version
//    @Override
//    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.setVersion(oldVersion);
//    }
}
