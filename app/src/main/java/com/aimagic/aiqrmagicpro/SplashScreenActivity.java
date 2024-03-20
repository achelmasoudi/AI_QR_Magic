package com.aimagic.aiqrmagicpro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.aimagic.aiqrmagicpro.Fragments.LanguageManager;
import com.aimagic.aiqrmagicpro.Fragments.ModeManager;

public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_SPEED = 1500;
    private TextView AIQRMagic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set The Mode when the app is opening
        ModeManager.setAppModeFromSharedPreferences(this);

        setContentView(R.layout.activity_splash_screen);

        AIQRMagic = (TextView) findViewById(R.id.titleOfAppinSplashScreenId);

//        //Animation For Title Of Application (AIQRScanner)
//        YoYo.with(Techniques.FadeIn).duration(1400).repeat(0).playOn(AIQRMagic);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.title_animation_splashscreen);
        AIQRMagic.setAnimation(anim);


        toTheWelcomeActivity();

    }

    private void toTheWelcomeActivity() {

        SharedPreferences preferences = getSharedPreferences("my_preferences", MODE_PRIVATE);
        boolean firstLaunch = preferences.getBoolean("welcome_activity", true);

        if (firstLaunch) {
            // Navigate to the welcome activity
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_SPEED);
            // Mark that the welcome activity has been shown
            preferences.edit().putBoolean("welcome_activity", false).apply();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_SPEED);
            // Load the locale language and The Mode when the app starts
            LanguageManager.loadLocale(this);
        }
    }
}