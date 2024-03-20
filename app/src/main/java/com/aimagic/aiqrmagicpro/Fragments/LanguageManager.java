package com.aimagic.aiqrmagicpro.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

public class LanguageManager {

    public static void setLocaleLanguage(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        // Save the selected language
        saveSelectedLanguage(context, language);
    }

    public static void setLocale(Context context) {
        String selectedLanguage = loadSelectedLanguage(context);
        setLocaleLanguage(context, selectedLanguage);
    }

    public static void loadLocale(Context context) {
        setLocale(context);
    }

    public static void saveSelectedLanguage(Context context, String language) {
        SharedPreferences preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Selected_Language", language);
        editor.apply();
    }

    public static String loadSelectedLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        return preferences.getString("Selected_Language", "");
    }
}
