package com.aimagic.aiqrmagicpro.Fragments;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.aimagic.aiqrmagicpro.R;

public class ModeManager {

    public static void saveAppMode(Context context , int mode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefsFile_AppMode", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("appMode", mode);
        editor.apply();
        AppCompatDelegate.setDefaultNightMode(mode); // Set the mode immediately
    }

    public static void setAppModeFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefsFile_AppMode", Context.MODE_PRIVATE);
        int savedMode = sharedPreferences.getInt("appMode", AppCompatDelegate.getDefaultNightMode()); // This is the default Mode
        AppCompatDelegate.setDefaultNightMode(savedMode); // Set the mode at app startup
    }

    // Helper method to save the app mode image resource
    public static void saveAppModeImageResource(Context context,int imageResource) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt("appModeImageResource", imageResource).apply();
    }

    // Helper method to retrieve the app mode image resource
    public static int getAppModeImageResource(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("appModeImageResource", R.drawable.light_mode_icon); // This is the default imageview
    }

}
