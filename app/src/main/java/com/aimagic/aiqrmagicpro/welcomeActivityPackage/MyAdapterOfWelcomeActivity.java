package com.aimagic.aiqrmagicpro.welcomeActivityPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.aimagic.aiqrmagicpro.R;

import java.util.List;

public class MyAdapterOfWelcomeActivity extends PagerAdapter {

    private Context context;
    private List<DataOfWelcomeActivity> list;


    public MyAdapterOfWelcomeActivity(Context pContext , List<DataOfWelcomeActivity> pList) {
        this.context = pContext;
        this.list = pList;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(context).inflate(R.layout.welcome_activity_card_item , container , false);

        ImageView image = (ImageView) view.findViewById(R.id.imageOfItems_WelcomeActivity);
        TextView description = (TextView) view.findViewById(R.id.descriptionOfItems_WelcomeActivity);

        DataOfWelcomeActivity dataOfWelcomeActivity = list.get(position);
        int imageOfWelcome = dataOfWelcomeActivity.getImageOfWelcome();
        String descriptionOfWelcome = dataOfWelcomeActivity.getDescriptionOfWelcome();

        image.setImageResource(imageOfWelcome);
        description.setText(descriptionOfWelcome);

        container.addView(view);

        return view;
    }



    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView( (View) object );
    }
}
