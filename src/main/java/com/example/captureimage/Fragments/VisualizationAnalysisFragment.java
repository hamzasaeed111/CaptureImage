package com.example.captureimage.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.captureimage.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class VisualizationAnalysisFragment extends Fragment
{
    private BarChart brchart;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_visualization_analysis, container, false);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        brchart = (BarChart)view.findViewById(R.id.bcgraph);
        setData(8);
        brchart.setFitBars(true);
    }

    private void setData(int count)
    {
        ArrayList<BarEntry> yValues = new ArrayList<>();

        for(int i =0;i<count; i++)
        {
            float values = (float)(Math.random()*100);
            yValues.add(new BarEntry(i,(int)values));
        }
        BarDataSet set = new BarDataSet(yValues,"Data Set");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);

        BarData data = new BarData(set);

        brchart.setData(data);
        brchart.invalidate();
        brchart.animateY(500, Easing.EaseInOutBounce);
    }
}