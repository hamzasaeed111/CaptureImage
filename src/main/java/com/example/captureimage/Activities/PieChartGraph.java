package com.example.captureimage.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.captureimage.Classes.LinearRegressionLogic;
import com.example.captureimage.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import static com.example.captureimage.Activities.Estimation.Training_Data;

public class PieChartGraph extends AppCompatActivity {
    private static String TAG = "pieChartActivity";
    private double[] CostMisc;
    private String Total_Sum_ret;
    private double Total_Sum;
    private ArrayList<Float>CostMiscfinal= new ArrayList<>();
    private ArrayList<String>xEntry = new ArrayList<>();
    private String[] xData={"Cement","Sand","Aggregate","Brick","Size Stone","Steel","Wood","Miscellaneous Cost"};
    private String[] xData2={"Cement","Sand","Aggregate","Brick","Size Stone","Steel","Wood"};
    private TextView tv_lineEstimator,tv_resultestimator ;
    private Button btn_lineEstimator;
    private EditText et_lineEstimator;




    PieChart pieChartGraph;
    Description desc = new Description();

    private BarChart brchart;
    private LineChart lnchart;
    LinearRegressionLogic lr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart_graph);
        Log.d(TAG, "On Create: starting to create chart");

        Intent pie= getIntent();
        Total_Sum_ret= pie.getStringExtra("MiscValue");
        CostMisc = pie.getDoubleArrayExtra("costarray");


        Total_Sum =Double.valueOf(Total_Sum_ret);

        Log.d(TAG, "Total sum "+ String.valueOf(Total_Sum));

        //CostMisc[7]=Total_Sum;

        try {
            for (int i = 0; i <CostMisc.length; i++)
            {


                CostMiscfinal.add((float)CostMisc[i]);

                Log.d(TAG,"The value of COSTMISC"+ String.valueOf(CostMisc[i]));
            }
            for (int i =0; i<xData.length;i++)
            {
                xEntry.add(xData[i]);
            }

        }catch (Exception e){

        }
        CostMiscfinal.add((float)Total_Sum);
       // CostMiscfinal.add((float)5);

        desc.setText("");
        desc.setTextSize(18);
        Log.d(TAG, "CostMiscFinal 7 "+String.valueOf(CostMiscfinal.get(7)));

        tv_lineEstimator=(TextView)findViewById(R.id.tv_estimateline) ;
        btn_lineEstimator=(Button)findViewById(R.id.btn_estimateline) ;
        et_lineEstimator=(EditText)findViewById(R.id.et_estimateline) ;
        tv_resultestimator=(TextView)findViewById(R.id.tv_resultestimateline);

        btn_lineEstimator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value=et_lineEstimator.getText().toString().trim();
                double[][]xArray = new double[Estimation.Training_Data.length][Estimation.Training_Data[0][0].length];
                double[][]yArray = new double[Estimation.Training_Data.length][1];

                for(int i=0;i<Estimation.Training_Data.length;i++)
                {
                    for (int j=0;j<Estimation.Training_Data[0][0].length;j++)
                    {
                        xArray[i][j]=Estimation.Training_Data[i][0][j];
                    }
                    yArray[i][0]= Estimation.Training_Data[i][1][0];
                }

                try {
                    LinearRegressionLogic lr = new LinearRegressionLogic(xArray,yArray);
                    lr.setYear(value);
                    tv_resultestimator.setText(String.valueOf((int)Math.round(lr.estimateCost())));
                }catch (Exception e )
                {

                }




            }
        });








        pieChartGraph = (PieChart)findViewById(R.id.piechartcost);

        pieChartGraph.setRotationEnabled(true);
        // pieChartGraph.setHoleRadius(25f);
        pieChartGraph.setTransparentCircleAlpha(0);
        pieChartGraph.setDescription(desc);
        pieChartGraph.setCenterText("Construction Cost");
        pieChartGraph.setCenterTextSize(20f);
        pieChartGraph.setUsePercentValues(false);

        pieChartGraph.setDragDecelerationFrictionCoef(0.95f);
        pieChartGraph.setDrawHoleEnabled(true);
//        pieChartGraph.setDrawSliceText(true);
//        pieChartGraph.setMaxAngle(180f);
        pieChartGraph.setTransparentCircleRadius(61f);
        pieChartGraph.animateY(1000, Easing.EaseInOutCubic);
        pieChartGraph.setEntryLabelTextSize(12f);

        //creating barchart

        brchart = (BarChart)findViewById(R.id.bcgraph);
        brchart.getDescription().setText("");



        setData(CostMisc.length);
        brchart.setFitBars(true);
        brchart.setDrawGridBackground(false);

        //creating line chart
        lnchart = (LineChart)findViewById(R.id.linechartid);
        lnchart.setDragEnabled(true);
        lnchart.setScaleEnabled(false);
        DataLine();






        addDataSet();

        pieChartGraph.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {

            }

            @Override
            public void onNothingSelected() {

            }
        });






    }

    private void addDataSet() {

        ArrayList<PieEntry> yEntry = new ArrayList<>();

        Log.d(TAG, "costmiscfinal size "+String.valueOf(CostMiscfinal.size()));
        Log.d(TAG, "xdata size "+String.valueOf(xData.length));

        for (int i =0; i<CostMiscfinal.size();i++) {
            yEntry.add(new PieEntry(CostMiscfinal.get(i),xEntry.get(i)));
        }
//        for(int i =1;i<xData.length;i++)
//        {
//            xEntry.add(xData[i]);
//        }
        Log.d(TAG, "yEntry size "+String.valueOf(yEntry.size()));
        Log.d(TAG, "yEntry value "+String.valueOf(yEntry.get(6)));
        PieDataSet pieDataset = new PieDataSet(yEntry,"Cost of Construction");
        pieDataset.setSliceSpace(3f);
        pieDataset.setSelectionShift(5f);
        pieDataset.setValueTextSize(12);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.GRAY);
        colors.add(Color.BLUE);
        colors.add(Color.CYAN);
        colors.add(Color.MAGENTA);
        colors.add(Color.LTGRAY);
        colors.add(Color.YELLOW);
        colors.add(Color.RED);
        colors.add(Color.GREEN);

        pieDataset.setColors(ColorTemplate.MATERIAL_COLORS);

//        Legend legend =pieChartGraph.getLegend();
//        legend.setForm(Legend.LegendForm.CIRCLE);
//
//        legend.setXEntrySpace(7);
//        legend.setYEntrySpace(5);


        PieData pieData = new PieData(pieDataset);
        pieData.setValueTextSize(20f);
        pieData.setValueTextColor(Color.BLACK);
        pieChartGraph.setData(pieData);
        pieChartGraph.invalidate();





    }

    //creating barchart values
    private void setData(int count)
    {
        ArrayList<BarEntry> yValues = new ArrayList<>();

        for(int i =0;i<count; i++)
        {

            yValues.add(new BarEntry(i,(float)CostMisc[i]));

        }
        brchart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xData2));
        brchart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        BarDataSet set = new BarDataSet(yValues,"Data Set");
        set.setColors(ColorTemplate.MATERIAL_COLORS);
        set.setDrawValues(true);

        BarData data = new BarData(set);

        brchart.setData(data);
        brchart.invalidate();
        brchart.animateY(500, Easing.EaseInOutBounce);
    }

    private void DataLine()
    {
        double[][][] data= Training_Data;

        double[][]xArray = new double[Training_Data.length][Training_Data[0][0].length];
        double[][]yArray = new double[Training_Data.length][1];

        for(int i=0;i<Training_Data.length;i++)
        {
            for (int j=0;j<Training_Data[0][0].length;j++)
            {
                xArray[i][j]=Training_Data[i][0][j];
            }
            yArray[i][0]= Training_Data[i][1][0];
        }


        try {
            lr= new LinearRegressionLogic(xArray,yArray);
        } catch (Exception e) {
            e.printStackTrace();
        }


        ArrayList<Entry> yValuesline = new ArrayList<>();
        for(int i =0;i<Training_Data.length;i++)
        {
            yValuesline.add(new Entry(((float)Training_Data[i][0][1]),(float)Training_Data[i][1][0]));
        }
        ArrayList<Entry> bestfitline = new ArrayList<>();
        for(int i =0;i<Training_Data.length;i++)
        {
            bestfitline.add(new Entry(((float)Training_Data[i][0][1]),(float)lr.getEstimate().getEntry(i,0)));
        }

        LineDataSet linedatavalues,bestfitdatavalues;
        linedatavalues= new LineDataSet(yValuesline,"Average Cost of Square/metersquare");
        linedatavalues.setFillAlpha(110);
        linedatavalues.setColor(Color.BLACK);
        linedatavalues.setLineWidth(1f);
        linedatavalues.setValueTextSize(12);
        linedatavalues.setValueTextColor(Color.BLACK);

        bestfitdatavalues= new LineDataSet(bestfitline,"Best Fit Line");
        bestfitdatavalues.setFillAlpha(110);
        bestfitdatavalues.setColor(Color.RED);
        bestfitdatavalues.setLineWidth(5f);
        bestfitdatavalues.setValueTextSize(1);
        bestfitdatavalues.setValueTextColor(Color.RED);

        ArrayList<ILineDataSet> linedataset = new ArrayList<>();
        linedataset.add(bestfitdatavalues);
//        linedataset.add(linedatavalues);
////
//        ArrayList<ILineDataSet> linedataset2 = new ArrayList<>();
//        linedataset2.add(bestfitdatavalues);

        LineData lndata = new LineData(linedatavalues,bestfitdatavalues);
        lnchart.setData(lndata);
    }









}
