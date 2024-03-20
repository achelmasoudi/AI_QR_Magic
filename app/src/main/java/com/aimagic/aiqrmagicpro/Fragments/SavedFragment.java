package com.aimagic.aiqrmagicpro.Fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.aimagic.aiqrmagicpro.R;
import com.google.android.material.tabs.TabLayout;

import com.aimagic.aiqrmagicpro.ControllerOfTabLayout.ViewPagerAdapter;


public class SavedFragment extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private ViewPagerAdapter viewPagerAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_saved, container , false);
        //Initialize The variables
        tabLayout = view.findViewById(R.id.tablayoutId);
        viewPager2 = view.findViewById(R.id.viewPagerId);

        //TabLayout ( Dark Light mode )
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // Dark mode is active
            tabLayout.setBackgroundResource(R.drawable.background_tablayout_custom_darkmode);
        } else {
            // Light mode is active
            tabLayout.setBackgroundResource(R.drawable.background_tablayout_custom);
        }


        setTabLayoutWithViewPager();

        return view;
    }

    private void setTabLayoutWithViewPager(){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        viewPagerAdapter = new ViewPagerAdapter(fragmentManager , getActivity().getLifecycle());

        viewPager2.setAdapter(viewPagerAdapter);

        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.Generated)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.Scanned)));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }



}