package com.aimagic.aiqrmagicpro.Fragments;

import static com.aimagic.aiqrmagicpro.Fragments.RatingManager.hasUserReviewed;
import static com.aimagic.aiqrmagicpro.Fragments.RatingManager.openPlayStorePage;
import static com.aimagic.aiqrmagicpro.Fragments.RatingManager.showInAppReview;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.aimagic.aiqrmagicpro.MainActivity;
import com.aimagic.aiqrmagicpro.R;
import com.aimagic.aiqrmagicpro.SplashScreenActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

public class SettingsFragment extends Fragment {

    private View view;

    //About Light and Dark mode
    private CardView applicationLanguage, applicationMode, scannerSound, scannerVibrate;

    //Feedback
    private CardView rateApplicationBtn, recommendApplicationBtn, suggestionsAndCommentsBtn, reportIssueBtn;

    //For The Bottom Sheet
    private BottomSheetBehavior bottomSheetBehavior_Language, bottomSheetBehavior_Mode;
    private FrameLayout frameLayout_Language, frameLayout_Mode;
    private View shadowFromView1, shadowFromView2;
    private int currentNightMode;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //this code is for when the user enter the first time to the app will be the imageview of the mode light or dark ( Depending on the phone's mode )
        int initialNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        // Set the initial app mode image resource based on the initialNightMode
        int initialAppModeImageResource = (initialNightMode == Configuration.UI_MODE_NIGHT_YES)
                ? R.drawable.dark_mode_icon
                : R.drawable.light_mode_icon;
        // Save the initial app mode image resource
        ModeManager.saveAppModeImageResource(getContext(), initialAppModeImageResource);

        view = inflater.inflate(R.layout.fragment_settings, container, false);

        //For the icons (White and Black) ---- LightDarkMode
        ImageView languageImageView = view.findViewById(R.id.fragmentSettings_imageViewLanguageId);
        ImageView modeImageView = view.findViewById(R.id.fragmentSettings_imageViewModeId);
        ImageView ScannerSoundImageView = view.findViewById(R.id.fragmentSettings_imageViewScannerSoundId);
        ImageView ScannerVivrateImageView = view.findViewById(R.id.fragmentSettings_imageViewScannerVivrateId);
        ImageView RateImageView = view.findViewById(R.id.fragmentSettings_imageViewRateId);
        ImageView RecommendImageView = view.findViewById(R.id.fragmentSettings_imageViewRecommendId);
        ImageView SuggestionsImageView = view.findViewById(R.id.fragmentSettings_imageViewSuggestionsId);
        ImageView ReportIssueImageView = view.findViewById(R.id.fragmentSettings_imageViewReportIssueId);

        currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode is active
            languageImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
            modeImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
            ScannerSoundImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
            ScannerVivrateImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
            RateImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
            RecommendImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
            SuggestionsImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
            ReportIssueImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.white));
        } else {
            // Light mode is active
            languageImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
            modeImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
            ScannerSoundImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
            ScannerVivrateImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
            RateImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
            RecommendImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
            SuggestionsImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
            ReportIssueImageView.setColorFilter(ContextCompat.getColor(getContext(), R.color.black));
        }

        //Initialize the variables
        applicationLanguage = (CardView) view.findViewById(R.id.fragmentSettings_cardViewLanguage);
        applicationMode = (CardView) view.findViewById(R.id.fragmentSettings_cardViewMode);
        scannerSound = (CardView) view.findViewById(R.id.fragmentSettings_cardViewScannerSound);
        scannerVibrate = (CardView) view.findViewById(R.id.fragmentSettings_cardViewScannerVibrate);

        //Initialize the feedback buttons
        rateApplicationBtn = (CardView) view.findViewById(R.id.fragmentSettings_cardViewRateApp);
        recommendApplicationBtn = (CardView) view.findViewById(R.id.fragmentSettings_cardViewRecommendApp);
        suggestionsAndCommentsBtn = (CardView) view.findViewById(R.id.fragmentSettings_cardViewSuggestionsAndComments);
        reportIssueBtn = (CardView) view.findViewById(R.id.fragmentSettings_cardViewReportIssue);

        //BottomSheetBehavior_For_ApplicationLanguage
        bottomSheetBehavior_Language();

        //BottomSheetBehavior_For_ApplicationLanguage
        bottomSheetBehavior_Mode();

        //Function of Scanner's sound
        scannerSoundFunction();

        //Function of Scanner's sound
        scannerVibrateFunction();

        //Rating The Application
        rateOurAppFunc();

        //Recommend The Application
        recommendOurAppFunc();

        //Suggestions For The Application
        suggestionsForOurAppFunc();

        //Report An Issue With The Application
        reportIssueWithOurAppFunc();

        return view;
    }

    //About the Language of The Application
    private void bottomSheetBehavior_Language() {
        //For the BottomSheet
        frameLayout_Language = (FrameLayout) view.findViewById(R.id.bottomSheetIdFrameLayoutFragmentSettings_ApplicatioLanguage);
        bottomSheetBehavior_Language = BottomSheetBehavior.from(frameLayout_Language);
        bottomSheetBehavior_Language.setPeekHeight(0);

        //Light - Dark Mode
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode is active
            frameLayout_Language.setBackgroundResource(R.drawable.bottom_sheet_background_darkmode);
        } else {
            // Light mode is active
            frameLayout_Language.setBackgroundResource(R.drawable.bottom_sheet_background);
        }

        //When the bottomSheet is Expanded The Shadow will appear
        shadowFromView1 = (View) view.findViewById(R.id.shadowFromViewId_FragmentSettings_Language);
        shadowFromView1.setVisibility(View.GONE);

        //If the bottom sheet is expanded and a touch event is detected on the Shadow, collapse the bottom sheet
        shadowFromView1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (bottomSheetBehavior_Language.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    // If the bottom sheet is expanded and a touch event is detected on the Shadow,
                    // collapse the bottom sheet
                    bottomSheetBehavior_Language.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    return true; // Consume the touch event
                }
                return false; // Allow other touch events to be handled as usual
            }
        });

        //The order is very important
        bottomSheetBehavior_Language.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    // The BottomSheet is expanded.
                    shadowFromView1.setVisibility(View.VISIBLE);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // The BottomSheet is hidden.
                    shadowFromView1.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });


        applicationLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior_Language.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        //Initialize the variables
        CardView englishLanguageBtn = (CardView) view.findViewById(R.id.englishLanguageButtonId_Settings);
        CardView frenchLanguageBtn = (CardView) view.findViewById(R.id.frenchLanguageButtonId_Settings);
        CardView turkishLanguageBtn = (CardView) view.findViewById(R.id.turkishLanguageButtonId_Settings);
        TextView appLanguageTextView = (TextView) view.findViewById(R.id.fragmentSettings_cardViewLanguage_English_Turkish_TextView);

        // Set the initial text for the language TextView to "English"
        appLanguageTextView.setText("English");

        // Load the selected language and set it in the TextView
        String selectedLanguage = LanguageManager.loadSelectedLanguage(getContext());
        if (selectedLanguage.isEmpty()) {
            // If no language preference is saved, set the default language to English
            LanguageManager.setLocaleLanguage(getContext(), "en");
            appLanguageTextView.setText("English");
        } else {
            if (selectedLanguage.equals("en")) {
                appLanguageTextView.setText("English");
            } else if (selectedLanguage.equals("fr")) {
                appLanguageTextView.setText("Français");
            } else if (selectedLanguage.equals("tr")) {
                appLanguageTextView.setText("Türkçe");
            }
        }

        //For English Language
        englishLanguageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageManager.setLocaleLanguage(getContext(), "en");

                // Restart the app to apply the new language
                Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //For French Language
        frenchLanguageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageManager.setLocaleLanguage(getContext(), "fr");

                // Restart the app to apply the new language
                Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //For Turkish Language
        turkishLanguageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageManager.setLocaleLanguage(getContext(), "tr");

                // Restart the app to apply the new language
                Intent intent = new Intent(getActivity(), SplashScreenActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    //About the Mode of The Application
    private void bottomSheetBehavior_Mode() {

        //For the BottomSheet
        frameLayout_Mode = (FrameLayout) view.findViewById(R.id.bottomSheetIdFrameLayoutFragmentSettings_ApplicationMode);
        bottomSheetBehavior_Mode = BottomSheetBehavior.from(frameLayout_Mode);
        bottomSheetBehavior_Mode.setPeekHeight(0);

        //Light - Dark Mode
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode is active
            frameLayout_Mode.setBackgroundResource(R.drawable.bottom_sheet_background_darkmode);
        } else {
            // Light mode is active
            frameLayout_Mode.setBackgroundResource(R.drawable.bottom_sheet_background);
        }

        //When the bottomSheet is Expanded The Shadow will appear
        shadowFromView2 = (View) view.findViewById(R.id.shadowFromViewId_FragmentSettings_Mode);
        shadowFromView2.setVisibility(View.GONE);

        //If the bottom sheet is expanded and a touch event is detected on the Shadow, collapse the bottom sheet
        shadowFromView2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (bottomSheetBehavior_Mode.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    // If the bottom sheet is expanded and a touch event is detected on the Shadow,
                    // collapse the bottom sheet
                    bottomSheetBehavior_Mode.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    return true; // Consume the touch event
                }
                return false; // Allow other touch events to be handled as usual
            }
        });

        //The order is very important
        bottomSheetBehavior_Mode.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_SETTLING) {
                    // The BottomSheet is expanded.
                    shadowFromView2.setVisibility(View.VISIBLE);
                } else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    // The BottomSheet is hidden.
                    shadowFromView2.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });

        applicationMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior_Mode.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });

        //Initialize the variables
        CardView lightModeBtn = (CardView) view.findViewById(R.id.lightModeButtonId_Settings);
        CardView darkModeBtn = (CardView) view.findViewById(R.id.darkModeButtonId_Settings);
        ImageView appModeImageView = (ImageView) view.findViewById(R.id.fragmentSettings_cardViewMode_Light_Mode_ImageView);

        //To Put the ImageView Of the Mode that i saved
        int appModeImageResource = ModeManager.getAppModeImageResource(getContext());
        appModeImageView.setImageResource(appModeImageResource);


        //For Light Mode
        lightModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the mode to light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                // Save the selected mode
                ModeManager.saveAppMode(getContext(), AppCompatDelegate.MODE_NIGHT_NO);

                // Save the selected image resource
                ModeManager.saveAppModeImageResource(getContext(), R.drawable.light_mode_icon);

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerId, new GenerateFragment());
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.bottomNavigation_Items_GenerateId);
            }
        });

        //For Dark Mode
        darkModeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the mode to light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                // Save the selected mode
                ModeManager.saveAppMode(getContext(), AppCompatDelegate.MODE_NIGHT_YES);

                // Save the selected image resource
                ModeManager.saveAppModeImageResource(getContext(), R.drawable.dark_mode_icon);

                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.fragmentContainerId, new GenerateFragment());
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.bottomNavigation_Items_GenerateId);
            }
        });
    }


    private void scannerSoundFunction() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefsFile_ScanSound", Context.MODE_PRIVATE);

        // Load the state of scannerSoundCheckBox from SharedPreferences
        boolean scannerSoundChecked = sharedPreferences.getBoolean("scannerSoundChecked", false);
        CheckBox scannerSoundCheckBox = (CheckBox) view.findViewById(R.id.fragmentSettings_cardViewScannerSound_checkBox);
        scannerSoundCheckBox.setChecked(scannerSoundChecked);

        //Click On The CardView
        scannerSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the checkbox
                boolean isChecked = scannerSoundCheckBox.isChecked();
                scannerSoundCheckBox.setChecked(!isChecked);

                // Save the state in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("scannerSoundChecked", !isChecked);
                editor.apply();
            }
        });

        //Click On The Box
        scannerSoundCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the state in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("scannerSoundChecked", isChecked);
                editor.apply();
            }
        });
    }

    private void scannerVibrateFunction() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefsFile_ScanVibrate", Context.MODE_PRIVATE);

        // Load the state of scannerVibrateCheckBox from SharedPreferences
        boolean scannerVibrateChecked = sharedPreferences.getBoolean("scannerVibrateChecked", false);
        CheckBox scannerVibrateCheckBox = (CheckBox) view.findViewById(R.id.fragmentSettings_cardViewScannerVibrate_checkBox);
        scannerVibrateCheckBox.setChecked(scannerVibrateChecked);

        //Click On The CardView
        scannerVibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle the checkbox
                boolean isChecked = scannerVibrateCheckBox.isChecked();
                scannerVibrateCheckBox.setChecked(!isChecked);

                // Save the state in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("scannerVibrateChecked", !isChecked);
                editor.apply();
            }
        });

        //Click On The Box
        scannerVibrateCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Save the state in SharedPreferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("scannerVibrateChecked", isChecked);
                editor.apply();
            }
        });
    }

    // In App Reviews ( Rating !! )
    private void rateOurAppFunc() {
        rateApplicationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the user has already reviewed the app
                if (hasUserReviewed(getContext())) {
                    // Open the Play Store page for your app
                    openPlayStorePage(getContext());
                } else {
                    // User has not reviewed, show in-app review
                    showInAppReview(getContext(), getActivity());
                }
            }
        });
    }

    // Recommend Our Application Function
    private void recommendOurAppFunc() {
        recommendApplicationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String appLink = "https://play.google.com/store/apps/details?id=" + getContext().getPackageName();
                String recommendationMessage = getContext().getResources().getString(R.string.recommendationMessage) + "\n\n" + appLink;

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, recommendationMessage);
                startActivity(Intent.createChooser(shareIntent, getContext().getString(R.string.ShareQrCodevia)));
            }
        });
    }

    // Suggestions For Our Application Function
    private void suggestionsForOurAppFunc() {
        suggestionsAndCommentsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String recipientEmail = "achelmasoudi@gmail.com";
                String emailSubject = getContext().getResources().getString(R.string.Suggestions);
                String emailMessage = getContext().getResources().getString(R.string.emailMessageSuggestions);

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.setPackage("com.google.android.gm");
                // Add the email
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});
                // Add the email subject
                intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                // Add the email body text
                intent.putExtra(Intent.EXTRA_TEXT, emailMessage);

                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Handle the case where no email app is installed on the device.
                }
            }
        });
    }

    //Report An Issue With The Application
    private void reportIssueWithOurAppFunc() {
        reportIssueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String recipientEmail = "achelmasoudi@gmail.com";
                String emailSubject = getContext().getResources().getString(R.string.ReportingIssue);
                String emailMessage = getContext().getResources().getString(R.string.emailMessageReportingIssue);

                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.setPackage("com.google.android.gm");
                // Add the email
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{recipientEmail});
                // Add the email subject
                intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                // Add the email body text
                intent.putExtra(Intent.EXTRA_TEXT, emailMessage);

                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Handle the case where no email app is installed on the device.
                }
            }
        });
    }
}
