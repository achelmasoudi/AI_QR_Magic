package com.aimagic.aiqrmagicpro.ControllerOfTabLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.aimagic.aiqrmagicpro.FragmentsOfTabLayout.GeneratedFragment;
import com.aimagic.aiqrmagicpro.FragmentsOfTabLayout.ScannedFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if(position==1) {
            //the second TabItem
            return new ScannedFragment();
        }
        //the first TabItem
        return new GeneratedFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
