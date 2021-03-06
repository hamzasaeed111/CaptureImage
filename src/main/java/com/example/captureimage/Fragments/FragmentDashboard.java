package com.example.captureimage.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.captureimage.Activities.Estimation;
import com.example.captureimage.R;

public class FragmentDashboard extends Fragment implements View.OnClickListener {
    CardView predictPanel,materialAdPanel,visualizationPanel,receiptPanel, uploadAd, feedback;
    ImageView predictPanelpic,materialAdPanelpic,visualizationPanelpic,receiptPanelpic, uploadAdpic, feedbackpic;

    String TAG = "Fragment";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        predictPanel = view.findViewById(R.id.predictionPanel);
        materialAdPanel = view.findViewById(R.id.AdPanel);
        visualizationPanel = view.findViewById(R.id.ComparisonPanel);
        receiptPanel = view.findViewById(R.id.ExpenseLogPanel);
        uploadAd = view.findViewById(R.id.upload);
        feedback = view.findViewById(R.id.feedback);

        predictPanelpic = view.findViewById(R.id.imgCostPrediction);
        materialAdPanelpic = view.findViewById(R.id.imgMaterialAds);
        visualizationPanelpic = view.findViewById(R.id.imgVisualAnalysis);
        receiptPanelpic = view.findViewById(R.id.imgExpenseLog);
        uploadAdpic = view.findViewById(R.id.imgUploadAds);
        feedbackpic = view.findViewById(R.id.imgFeedback);

        predictPanelpic.setOnClickListener(this);
        materialAdPanelpic.setOnClickListener(this);
        visualizationPanelpic.setOnClickListener(this);
        receiptPanelpic.setOnClickListener(this);
        uploadAdpic.setOnClickListener(this);
        feedbackpic.setOnClickListener(this);

        predictPanel.setOnClickListener(this);
        materialAdPanel.setOnClickListener(this);
        visualizationPanel.setOnClickListener(this);
        receiptPanel.setOnClickListener(this);
        uploadAd.setOnClickListener(this);
        feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        //Load CostPredictionFragmanet
        if(view.getId() == predictPanel.getId() ||view.getId() == predictPanelpic.getId())
        {
            Log.d(TAG, "In Fragment"+ Estimation.class.toString().trim());
            try {
                Intent in = new Intent(getActivity(), Estimation.class);
                startActivity(in);
            }catch (Exception e)
            {

            }
        }

        if(view.getId() == feedback.getId()||view.getId() == feedbackpic.getId()){
            Fragment ad = new FeedbackFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment,ad);
            fragmentTransaction.commit();
        }

        if (view.getId() == uploadAd.getId()||view.getId() == uploadAdpic.getId()){
            Fragment ad = new UploadAdFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment,ad);
            fragmentTransaction.commit();
        }

        //Load CostPredictionFragmanet
        if(view.getId() == materialAdPanel.getId()||view.getId() == materialAdPanelpic.getId())
        {
            Fragment ad = new KnowledgeBaseFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, ad);
            fragmentTransaction.commit();
        }

        //Load CostPredictionFragmanet
        if(view.getId() == visualizationPanel.getId()||view.getId() == visualizationPanelpic.getId())
        {
//            Fragment charts = new PieChartGraph();
//            FragmentManager fm = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fm.beginTransaction();
//            fragmentTransaction.replace(R.id.nav_host_fragment, charts);
//            fragmentTransaction.commit();
            try {
                Intent in = new Intent(getActivity(), Estimation.class);
                startActivity(in);
            }catch (Exception e)
            {


            }
        }

        //Load CostPredictionFragmanet
        if(view.getId() == receiptPanel.getId()||view.getId() == receiptPanelpic.getId())
        {
            Fragment expense_logs = new ExpenseLogFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, expense_logs);
            fragmentTransaction.commit();
        }
    }
}