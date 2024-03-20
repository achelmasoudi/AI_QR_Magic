package com.aimagic.aiqrmagicpro.scannerTypes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.aimagic.aiqrmagicpro.R;

import com.aimagic.aiqrmagicpro.Fragments.LanguageManager;

public class InAppBrowser extends AppCompatActivity {

    private WebView inAppBrowser;
    private String searchQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the locale language when the app starts
        LanguageManager.loadLocale(this);

        setContentView(R.layout.activity_in_app_browser);

        //initiliaze the variables
        inAppBrowser = (WebView) findViewById(R.id.webViewInAppBrowser);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            searchQuery = bundle.getString("searchquery");
        }

        inAppBrowser.setWebViewClient(new WebViewClient());
        inAppBrowser.loadUrl("https://www.google.com/search?q=" + searchQuery);

        //For back Pressed in The App Browser
        inAppBrowser.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK && inAppBrowser.canGoBack()) {
                inAppBrowser.goBack();
                return true;
            }
            return false;
        });

        arrowBack();
    }
    private void arrowBack() {
        CardView arrowBack = (CardView) findViewById(R.id.arrowback_from_InAppBrowserId);
        arrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //For Back from Activity to Fragment !!!!!
                InAppBrowser.super.onBackPressed();
            }
        });
    }
    //When i clicked the phone's back Button
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}