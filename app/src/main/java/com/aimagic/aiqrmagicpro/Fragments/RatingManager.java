package com.aimagic.aiqrmagicpro.Fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.Toast;

import com.aimagic.aiqrmagicpro.R;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

public class RatingManager {
    //I used this Method cuz The in-app review API is designed to show the review dialog to the user ONLY ONCE

    public static final String PREF_HAS_REVIEWED = "hasReviewed";

    // Check if the user has already reviewed the app
    public static boolean hasUserReviewed(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(PREF_HAS_REVIEWED, false);
    }

    // Save that the user has reviewed the app
    public static void setUserReviewed(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_HAS_REVIEWED, true);
        editor.apply();
    }
    // Show in-app review flow
    public static void showInAppReview(Context context , Activity activity) {
        ReviewManager reviewManager = ReviewManagerFactory.create(activity);
        Task<ReviewInfo> request = reviewManager.requestReviewFlow();

        request.addOnCompleteListener(task -> {
            try {
                if (task.isSuccessful()) {
                    // We can get the ReviewInfo object
                    ReviewInfo reviewInfo = task.getResult();
                    Task<Void> reviewFlow = reviewManager.launchReviewFlow(activity, reviewInfo);
                    reviewFlow.addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
//                            Toast.makeText(context, context.getResources().getString(R.string.Ratingiscompleted), Toast.LENGTH_SHORT).show();
                            // Save that the user has reviewed the app
                            setUserReviewed(context);
                        } else {
                            // There was some problem, log or handle the error code.
                            Toast.makeText(context, context.getResources().getString(R.string.Reviewfailedtostart), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // There was some problem, log or handle the error code.
                    Toast.makeText(context, context.getResources().getString(R.string.Reviewfailedtostart), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Open Play Store page for your app
    public static void openPlayStorePage(Context context) {
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, uri);
            playStoreIntent.setPackage("com.android.vending");
            context.startActivity(playStoreIntent);
        } catch (ActivityNotFoundException e) {
            // If Play Store is not available, open the app in a browser
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=" + context.getPackageName());
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(browserIntent);
        }
    }
}
