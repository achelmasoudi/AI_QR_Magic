package com.aimagic.aiqrmagicpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.aimagic.aiqrmagicpro.Fragments.GenerateFragment;
import com.aimagic.aiqrmagicpro.Fragments.LanguageManager;
import com.aimagic.aiqrmagicpro.Fragments.ModeManager;
import com.aimagic.aiqrmagicpro.Fragments.SavedFragment;
import com.aimagic.aiqrmagicpro.Fragments.ScanFragment;
import com.aimagic.aiqrmagicpro.Fragments.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    public static BottomNavigationView bottomNavigationView;
    private long backButtonPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set The Mode when the app is opening
        ModeManager.setAppModeFromSharedPreferences(this);

        // Load the locale language and The Mode when the app starts
        LanguageManager.loadLocale(this);

        setContentView(R.layout.activity_main);

        //The calling of the fragment func
        funcAboutTheFragment();
    }

    //Initialize The Fragment
    private void funcAboutTheFragment() {
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationId);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerId , new GenerateFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectedFragment = null;
                switch(item.getItemId()) {
                    case R.id.bottomNavigation_Items_GenerateId:
                        selectedFragment = new GenerateFragment();
                        break;
                    case R.id.bottomNavigation_Items_ScanId:
                        selectedFragment = new ScanFragment();
                        break;
                    case R.id.bottomNavigation_Items_SavedId:
                        selectedFragment = new SavedFragment();
                        break;
                    case R.id.bottomNavigation_Items_SettingsId:
                        selectedFragment = new SettingsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerId , selectedFragment).commit();
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();

        if (backButtonPressedTime + 2000 > currentTime) {
            // If the back button is pressed again within 2 seconds, exit the app
            super.onBackPressed();
        } else {
            // Show a toast or a message to inform the user to press again to exit
            Toast.makeText(this, getResources().getString(R.string.Pressbackagaintoclosetheapp), Toast.LENGTH_SHORT).show();
        }

        backButtonPressedTime = currentTime;
    }
}