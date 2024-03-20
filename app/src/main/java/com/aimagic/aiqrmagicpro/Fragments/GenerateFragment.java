package com.aimagic.aiqrmagicpro.Fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.aimagic.aiqrmagicpro.GenerateContact;
import com.aimagic.aiqrmagicpro.GenerateEmail;
import com.aimagic.aiqrmagicpro.GenerateInstagram;
import com.aimagic.aiqrmagicpro.GenerateLinkedin;
import com.aimagic.aiqrmagicpro.GenerateLocation;
import com.aimagic.aiqrmagicpro.GeneratePlayStoreApp;
import com.aimagic.aiqrmagicpro.GenerateWebsite;
import com.aimagic.aiqrmagicpro.GenerateWifi;
import com.aimagic.aiqrmagicpro.GenerateX;
import com.aimagic.aiqrmagicpro.GenerateYoutube;
import com.aimagic.aiqrmagicpro.R;


public class GenerateFragment extends Fragment {

    private View view;
    private CardView webSiteBtn , wifiBtn , contactBtn , locationBtn , emailBtn ,
                    instagramBtn , youtubeBtn , playStoreBtn , linkedinBtn , xBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_generate , container , false);

        //For the icons (White and Black) ---- LightDarkMode
        ImageView twitter_icon = view.findViewById(R.id.fragmentGenerate_TwitterIcon);
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode is active
            twitter_icon.setImageResource(R.drawable.xtwitter_icon_white);
        } else {
            // Light mode is active
            twitter_icon.setImageResource(R.drawable.xtwitter_icon);
        }


        //Initialize the variables
        webSiteBtn = (CardView) view.findViewById(R.id.cardViewWebSiteId);
        wifiBtn = (CardView) view.findViewById(R.id.cardViewWifiId);
        contactBtn = (CardView) view.findViewById(R.id.cardViewContactId);
        locationBtn = (CardView) view.findViewById(R.id.cardViewLocationId);
        emailBtn = (CardView) view.findViewById(R.id.cardViewEmailId);
        instagramBtn = (CardView) view.findViewById(R.id.cardViewInstagramId);
        youtubeBtn = (CardView) view.findViewById(R.id.cardViewYoutubeId);
        playStoreBtn = (CardView) view.findViewById(R.id.cardViewPlayStoreId);
        linkedinBtn = (CardView) view.findViewById(R.id.cardViewLinkedinId);
        xBtn = (CardView) view.findViewById(R.id.cardViewXId);

        //The Functions Of the Btns
        webSiteButton();
        wifiButton();
        contactButton();
        locationButton();
        emailButton();
        instagramButton();
        youtubeButton();
        playStoreButton();
        linkedinButton();
        xButton();

        return view;
    }

    private void webSiteButton() {
        webSiteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , GenerateWebsite.class);
                startActivity(intent);
                Toast.makeText(getContext() , getResources().getString(R.string.GenerateYourWebSite) , Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void wifiButton() {
        wifiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , GenerateWifi.class);
                startActivity(intent);
                Toast.makeText(getContext() , getResources().getString(R.string.GenerateYourWiFi) , Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void contactButton() {
        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , GenerateContact.class);
                startActivity(intent);
                Toast.makeText(getContext() , getResources().getString(R.string.GenerateYourContact) , Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void locationButton() {
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , GenerateLocation.class);
                startActivity(intent);
                Toast.makeText(getContext() , getResources().getString(R.string.GenerateYourLocation) , Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void emailButton() {
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity() , GenerateEmail.class);
                startActivity(intent);
                Toast.makeText(getContext() , getResources().getString(R.string.GenerateYourEmail) , Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void instagramButton() {
        instagramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , GenerateInstagram.class);
                startActivity(intent);
                Toast.makeText(getContext() , getResources().getString(R.string.GenerateYourInstagramaccount) , Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void youtubeButton() {
        youtubeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , GenerateYoutube.class);
                startActivity(intent);
                Toast.makeText(getContext() , getResources().getString(R.string.GenerateYourYoutubeaccount) , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void playStoreButton() {
        playStoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , GeneratePlayStoreApp.class);
                startActivity(intent);
                Toast.makeText(getContext() , getResources().getString(R.string.GenerateYourPlayStoreApplication) , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void linkedinButton() {
        linkedinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , GenerateLinkedin.class);
                startActivity(intent);
                Toast.makeText(getContext() , getResources().getString(R.string.GenerateYourLinkedinaccount) , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void xButton() {
        xBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , GenerateX.class);
                startActivity(intent);
                Toast.makeText(getContext() , getResources().getString(R.string.GenerateYourXaccount) , Toast.LENGTH_SHORT).show();
            }
        });
    }

}
