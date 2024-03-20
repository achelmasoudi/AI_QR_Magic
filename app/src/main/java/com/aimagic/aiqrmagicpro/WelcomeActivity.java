package com.aimagic.aiqrmagicpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import com.aimagic.aiqrmagicpro.welcomeActivityPackage.DataOfWelcomeActivity;
import com.aimagic.aiqrmagicpro.welcomeActivityPackage.MyAdapterOfWelcomeActivity;

public class WelcomeActivity extends AppCompatActivity {

    private List<DataOfWelcomeActivity> list;
    private MyAdapterOfWelcomeActivity myAdapterOfWelcomeActivity;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private LottieAnimationView getStartedBtn , welcomeAnimation;
    private TextView getStartedText;
    private int currentPage;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_welcome);

        viewPager = findViewById(R.id.viewPagerId);

        listFunc();

        circleIndicator = (CircleIndicator) findViewById(R.id.circleIndicatorId);
        circleIndicator.setViewPager(viewPager);

    }

    private void listFunc() {
        list = new ArrayList<>();

        int pictureOfWelcomePage[] = {0 , R.drawable.multiple_qr_code_types , R.drawable.customize_welcomeactivity,
                R.drawable.scancode_welcomeactivity , R.drawable.saveqrcode_welcomeactivity , R.drawable.dark_light_welcomeactivity ,R.drawable.ready_icon_welcomeactivity };

        String descriptionOfWelcomePage[] = {
                "Welcome to AI QR Magic\nWhere Every Code Tells a Unique Story"+ "\n\nGenerate, Customize, Scan, Save, and Download QR Codes",
                "Generate Multiple QR Code Types",
                "Customize QR Codes with Different Colors",
                "Effortlessly Scan QR Codes from Camera or Gallery",
                "Save Your QR Codes for Easy Sharing",
                "Switch between light and dark modes to set the mood for your QR code adventure!",
                "Ready to explore the QR Universe?\n\nTap [ Get Started ]"
        };

        for(int i = 0 ; i <pictureOfWelcomePage.length ; i++) {
            DataOfWelcomeActivity dataOfWelcomeActivity = new DataOfWelcomeActivity(pictureOfWelcomePage[i] , descriptionOfWelcomePage[i]);
            list.add(dataOfWelcomeActivity);
        }

        myAdapterOfWelcomeActivity = new MyAdapterOfWelcomeActivity(this , list);
        viewPager.setAdapter(myAdapterOfWelcomeActivity);
        viewPager.setPadding(0 , 0 , 0 , 0);


        getStartedBtn = (LottieAnimationView) findViewById(R.id.getStarted_btnId);
        getStartedText = (TextView) findViewById(R.id.textView_getStarted);
        welcomeAnimation = (LottieAnimationView) findViewById(R.id.welcome_animation);


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                //For the Get Started Buuton
                if (currentPage == list.size() - 1) {
                    // Show the "Get Started" button on the last page
                    circleIndicator.setVisibility(View.GONE);
                    getStartedBtn.setVisibility(View.VISIBLE);
                    getStartedText.setVisibility(View.VISIBLE);
                }
                else {
                    // Hide the button on other pages
                    circleIndicator.setVisibility(View.VISIBLE);
                    getStartedBtn.setVisibility(View.GONE);
                    getStartedText.setVisibility(View.GONE);
                }
                //For the Welcome Animation
                if (currentPage == 0) {
                    welcomeAnimation.setVisibility(View.VISIBLE);
                }
                else {
                    welcomeAnimation.setVisibility(View.GONE);
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        getStartedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelcomeActivity.this , SplashScreenActivity.class));
                finish();
            }
        });

    }

}