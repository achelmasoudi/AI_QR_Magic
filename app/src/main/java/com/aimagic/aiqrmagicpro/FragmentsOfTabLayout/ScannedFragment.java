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

import com.aimagic.aiqrmagicpro.Controller.DataBaseHelper_Scanned;
import com.aimagic.aiqrmagicpro.Controller.ScannedFragmentAdapter;

public class ScannedFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView_scannedFragment;
    private static ScannedFragmentAdapter scannedFragmentAdapter;
    private static LottieAnimationView nothingSavedYetAnimation;
    private static TextView nothingSavedYetTxtView;

    private DataBaseHelper_Scanned dataBaseHelperScanned;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tablayout_fragment_scanned , container , false);

        nothingSavedYetAnimation = view.findViewById(R.id.lottieAnimationId_nothingSavedYetScanned);
        nothingSavedYetTxtView = view.findViewById(R.id.textViewId_nothingSavedYetScanned);

        //About The RecyclerView
        myRecyclerViewDetails();

        return view;
    }

    private void myRecyclerViewDetails() {

        recyclerView_scannedFragment = (RecyclerView) view.findViewById(R.id.recyclerView_scannedFragment);
        dataBaseHelperScanned = new DataBaseHelper_Scanned(getContext());

        scannedFragmentAdapter = new ScannedFragmentAdapter( getContext() , dataBaseHelperScanned.getAllData_Scanned() , dataBaseHelperScanned);

        recyclerView_scannedFragment.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView_scannedFragment.setAdapter(scannedFragmentAdapter);

        // Check if the RecyclerView is empty and update the visibility of the Animation
        updateAnimationVisibility();
    }

    // Update the visibility of the animation and text view
    public static void updateAnimationVisibility() {
        if (scannedFragmentAdapter.getItemCount() == 0) {
            nothingSavedYetAnimation.setVisibility(View.VISIBLE);
            nothingSavedYetTxtView.setVisibility(View.VISIBLE);
        } else {
            nothingSavedYetAnimation.setVisibility(View.GONE);
            nothingSavedYetTxtView.setVisibility(View.GONE);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public static void notifyAdapter() {
        scannedFragmentAdapter.notifyDataSetChanged();
    }
}
