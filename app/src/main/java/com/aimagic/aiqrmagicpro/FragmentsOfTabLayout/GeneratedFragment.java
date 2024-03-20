package com.aimagic.aiqrmagicpro.FragmentsOfTabLayout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.aimagic.aiqrmagicpro.R;

import com.aimagic.aiqrmagicpro.Controller.DataBaseHelper_Generated;
import com.aimagic.aiqrmagicpro.Controller.GeneratedFragmentAdapter;

public class GeneratedFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView_generatedFragment;
    private static GeneratedFragmentAdapter generatedFragmentAdapter;
    private static LottieAnimationView nothingSavedYetAnimation;
    private static TextView nothingSavedYetTxtView;

    private DataBaseHelper_Generated dataBaseHelperGenerated;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.tablayout_fragment_generated , container , false);

        nothingSavedYetAnimation = view.findViewById(R.id.lottieAnimationId_nothingSavedYetGenerated);
        nothingSavedYetTxtView = view.findViewById(R.id.textViewId_nothingSavedYetGenerated);

        //About The RecyclerView
        myRecyclerViewDetails();

        return view;
    }

    private void myRecyclerViewDetails() {

        recyclerView_generatedFragment = (RecyclerView) view.findViewById(R.id.recyclerView_generatedFragment);
        dataBaseHelperGenerated = new DataBaseHelper_Generated(getContext());

        generatedFragmentAdapter = new GeneratedFragmentAdapter( getContext() , dataBaseHelperGenerated.getAllData_Generated() , dataBaseHelperGenerated);

        recyclerView_generatedFragment.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView_generatedFragment.setAdapter(generatedFragmentAdapter);

        // Check if the RecyclerView is empty and update the visibility of the Animation
        updateAnimationVisibility();
    }

    // Update the visibility of the animation and text view
    public static void updateAnimationVisibility() {
        if (generatedFragmentAdapter.getItemCount() == 0) {
            nothingSavedYetAnimation.setVisibility(View.VISIBLE);
            nothingSavedYetTxtView.setVisibility(View.VISIBLE);
        } else {
            nothingSavedYetAnimation.setVisibility(View.GONE);
            nothingSavedYetTxtView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void notifyAdapter() {
        generatedFragmentAdapter.notifyDataSetChanged();
    }
}
