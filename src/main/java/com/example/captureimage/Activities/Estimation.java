package com.example.captureimage.Activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.captureimage.Classes.LinearRegressionLogic;
import com.example.captureimage.R;

public class Estimation extends AppCompatActivity {


    LinearRegressionLogic lr;

    private boolean table_flg = true;
    private EditText area;
    private EditText cost;
    private Button switch_btn,enter_btn,pie_btn;
    private TextView tb1tv1quantity,tb1tv2quantity,tb1tv3quantity,tb1tv4quantity,tb1tv5quantity,tb1tv6quantity,tb1tv7quantity;
    private TextView tb1tv1cost,tb1tv2cost,tb1tv3cost,tb1tv4cost,tb1tv5cost,tb1tv6cost,tb1tv7cost;
    private TextView tb2tv1cost,tb2tv2cost,tb2tv3cost,tb2tv4cost,tb2tv5cost,tb2tv6cost,tb2tv7cost,tb2tv8cost,tb2tv9cost;
    private TextView showresult;
    private double cemval =0.4;
    private double sandval=1.81;
    private double aggval=1.14;
    private double steelval=2.1;
    private double woodval=0.077;
    private double excval =0.03;
    private double foundationval=0.05;
    private double constplinthval=0.05;
    private double roofingval=0.17;
    private double flooringval=0.06;
    private double woodworkval =0.13;
    private double internalfinishval=0.06;
    private double externalfinsihval=0.03;
    private double watersupplyval=0.04;
    private int brickval = 34;
    private int sizestoneval= 11;
    private double r1,r2,r3,r4,r5,r6,r7;
    private double c1,c2,c3,c4,c5,c6,c7,c8,c9;
    private double x1,x2,x3,x4,x5,x6,x7;
    int areaval;
    int costval;
    double total_item_cost;
    double total_material_cost;
    double Total_sum;
    private String result;
    private  int counter=0;
    private double[] costarray;
    private double[] misc_costarray;
    private static String TAG = "MainActivity";





    static double [][][] Training_Data = {{{1.0, 2008},{750}},
            {{1.0, 2009},{830}},
            {{1.0, 2010},{810}},
            {{1.0, 2011},{900}},
            {{1.0, 2012},{920}},
            {{1.0, 2013},{890}},
            {{1.0, 2014},{1050}},
            {{1.0, 2015},{1050}},
            {{1.0, 2016},{1030}},
            {{1.0, 2017},{1110}},
            {{1.0, 2018},{1230}},
            {{1.0, 2019},{1375}}};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimation);

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



    }
    public void calculateValue(){




        String value1, value2;
        value1 = area.getText().toString().trim();
        value2 = cost.getText().toString().trim();

        if (value1.isEmpty()){

            cost.setText("");

        }



        if(value1.isEmpty()==false && value2.isEmpty()==false)
        {

            try {
                areaval = Integer.parseInt(value1);
                costval = Integer.parseInt(value2);
            }catch (NumberFormatException ex){
                tb1tv1quantity.setText("-");
                tb1tv2quantity.setText("-");
                tb1tv3quantity.setText("-");
                tb1tv4quantity.setText("-");
                tb1tv5quantity.setText("-");
                tb1tv6quantity.setText("-");
                tb1tv7quantity.setText("-");

                tb2tv1cost.setText("-");
                tb2tv2cost.setText("-");
                tb2tv3cost.setText("-");
                tb2tv4cost.setText("-");
                tb2tv5cost.setText("-");
                tb2tv6cost.setText("-");
                tb2tv7cost.setText("-");
                tb2tv8cost.setText("-");
                tb2tv9cost.setText("-");
                Toast.makeText(this, "Please Enter a numeric number", Toast.LENGTH_LONG).show();
            }
            r1= (cemval*1000*areaval)/1000;
            x1=r1*530;
            tb1tv1cost.setText(""+x1+" Rs");
            tb1tv1quantity.setText(""+r1+" Bags/sqft");
            r2= (sandval*1000*areaval)/1000;
            x2=r2*23;
            tb1tv2cost.setText(""+x2+" Rs");
            tb1tv2quantity.setText(""+r2+" Cut/sqft");
            r3= (aggval*1000*areaval)/1000;
            x3=r3*2345;
            tb1tv3cost.setText(""+x3+" Rs");
            tb1tv3quantity.setText(""+r3+" Cut/sqft");
            r4= (brickval*1000*areaval)/1000;
            x4=r4*8.5;
            tb1tv4cost.setText(""+x4+" Rs");
            tb1tv4quantity.setText(""+r4+" Nos/sqft");
            r5= (sizestoneval*1000*areaval)/1000;
            x5=r5*3.2;
            tb1tv5cost.setText(""+x5+" Rs");
            tb1tv5quantity.setText(""+r5+" Nos/sqft");
            r6= (steelval*1000*areaval)/1000;
            x6=r6*105;
            tb1tv6cost.setText(""+x6+" Rs");
            tb1tv6quantity.setText(""+r6+" Kg/sqft");
            r7= (woodval*1000*areaval)/1000;
            x7=r7*3000;
            tb1tv7cost.setText(""+x7+" Rs");
            tb1tv7quantity.setText(""+r7+" CuF/sqft");

            costarray = new double[]{x1, x2, x3, x4, x5, x6, x7};

            //For the cost of the miscellaneous cost
            c1= (excval*1000*costval)*areaval/1000;
            tb2tv1cost.setText(""+c1+" Rs");
            c2= (foundationval*1000*costval)*areaval/1000;
            tb2tv2cost.setText(""+c2+" Rs");
            c3= (constplinthval*1000*costval)*areaval/1000;
            tb2tv3cost.setText(""+c3+" Rs");
            c4= (roofingval*1000*costval)*areaval/1000;
            tb2tv4cost.setText(""+c4+" Rs");
            c5= (flooringval*1000*costval)*areaval/1000;
            tb2tv5cost.setText(""+c5+" Rs");
            c6= (woodworkval*1000*costval)*areaval/1000;
            tb2tv6cost.setText(""+c6+" Rs");
            c7= (internalfinishval*1000*costval)*areaval/1000;
            tb2tv7cost.setText(""+c7+" Rs");
            c8= (externalfinsihval*1000*costval)*areaval/1000;
            tb2tv8cost.setText(""+c8+" Rs");
            c9= (watersupplyval*1000*costval)*areaval/1000;
            tb2tv9cost.setText(""+c9+" Rs");

            misc_costarray = new double[]{c1,c2,c3,c4,c5,c6,c7,c8,c9};

            total_item_cost= c1+c2+c3+c4+c5+c6+c7+c8+c9;


//            total_item_cost=c1+c2+c3+c4+c5+c6+c7+c8+c9;
//            total_material_cost = x1+x2+x3+x4+x5+x6+x7;
//
//            Total_sum=total_item_cost+total_material_cost;
//
//            showresult.setText("The total cost for the construction is "+Total_sum);





            enter_btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // for quantity of the materials
//                    r1= (cemval*1000*areaval)/1000;
//                    tb1tv1quantity.setText(""+r1+" Bags/sqft");
//                    r2= (sandval*1000*areaval)/1000;
//                    tb1tv2quantity.setText(""+r2+" Cut/sqft");
//                    r3= (aggval*1000*areaval)/1000;
//                    tb1tv3quantity.setText(""+r3+" Cut/sqft");
//                    r4= (brickval*1000*areaval)/1000;
//                    tb1tv4quantity.setText(""+r4+" Nos/sqft");
//                    r5= (sizestoneval*1000*areaval)/1000;
//                    tb1tv5quantity.setText(""+r5+" Nos/sqft");
//                    r6= (steelval*1000*areaval)/1000;
//                    tb1tv6quantity.setText(""+r6+" Kg/sqft");
//                    r7= (woodval*1000*areaval)/1000;
//                    tb1tv7quantity.setText(""+r7+" CuF/sqft");




                    r1= (cemval*1000*areaval)/1000;
                    x1=r1*530;
                    tb1tv1quantity.setText(""+r1+" Bags/sqft");
                    r2= (sandval*1000*areaval)/1000;
                    x2=r2*23;
                    tb1tv2quantity.setText(""+r2+" Cut/sqft");
                    r3= (aggval*1000*areaval)/1000;
                    x3=r3*2345;
                    tb1tv3quantity.setText(""+r3+" Cut/sqft");
                    r4= (brickval*1000*areaval)/1000;
                    x4=r4*8.5;
                    tb1tv4quantity.setText(""+r4+" Nos/sqft");
                    r5= (sizestoneval*1000*areaval)/1000;
                    x5=r5*3.2;
                    tb1tv5quantity.setText(""+r5+" Nos/sqft");
                    r6= (steelval*1000*areaval)/1000;
                    x6=r6*105;
                    tb1tv6quantity.setText(""+r6+" Kg/sqft");
                    r7= (woodval*1000*areaval)/1000;
                    x7=r7*3000;
                    tb1tv7quantity.setText(""+r7+" CuF/sqft");

                    total_material_cost = x1+x2+x3+x4+x5+x6+x7;
                    Log.d(TAG,"THe Vlaue of material Cost"+String.valueOf(total_material_cost));
//
                    //For the cost of the miscellaneous cost
                    c1= (excval*1000*costval)*areaval/1000;
                    tb2tv1cost.setText(""+c1+" Rs");
                    c2= (foundationval*1000*costval)*areaval/1000;
                    tb2tv2cost.setText(""+c2+" Rs");
                    c3= (constplinthval*1000*costval)*areaval/1000;
                    tb2tv3cost.setText(""+c3+" Rs");
                    c4= (roofingval*1000*costval)*areaval/1000;
                    tb2tv4cost.setText(""+c4+" Rs");
                    c5= (flooringval*1000*costval)*areaval/1000;
                    tb2tv5cost.setText(""+c5+" Rs");
                    c6= (woodworkval*1000*costval)*areaval/1000;
                    tb2tv6cost.setText(""+c6+" Rs");
                    c7= (internalfinishval*1000*costval)*areaval/1000;
                    tb2tv7cost.setText(""+c7+" Rs");
                    c8= (externalfinsihval*1000*costval)*areaval/1000;
                    tb2tv8cost.setText(""+c8+" Rs");
                    c9= (watersupplyval*1000*costval)*areaval/1000;
                    tb2tv9cost.setText(""+c9+" Rs");

                    showresult=(TextView)findViewById(R.id.tvshow_result);





                    total_item_cost=c1+c2+c3+c4+c5+c6+c7+c8+c9;

                    Log.d(TAG,"THe Value of Total Item Cost"+String.valueOf(total_item_cost));


                    Total_sum=total_item_cost+total_material_cost;

                    showresult.setText("The total cost for the construction is "+Total_sum);
                    showresult.setEnabled(true);


                    //   cost.setText(result);


                }
            });
        }
        else
        {
            Toast.makeText(this, "Please fill the values", Toast.LENGTH_SHORT).show();
        }
        pie_btn.setVisibility(View.VISIBLE);
        pie_btn.setEnabled(true);

        pie_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pie = new Intent(Estimation.this, PieChartGraph.class);

                Log.d(TAG," in intent THe Vlaue of Total Item Cost"+String.valueOf(total_item_cost));

                pie.putExtra("MiscValue", String.valueOf(total_item_cost));
                pie.putExtra("costarray", costarray);
                startActivity(pie);
            }
        });
    }

    public void collapseTable(View view) {
        TableLayout table = findViewById(R.id.tableRM);
        TableLayout table2 = findViewById(R.id.tableRM2);
        switch_btn= (Button)findViewById(R.id.switchBtn);
        pie_btn = (Button)findViewById(R.id.pieBtn);

        TextView heading = (TextView)findViewById(R.id.tvRwMaterials);
        TextView Area = (TextView)findViewById(R.id.tvarea);
        TextView Cost =(TextView)findViewById(R.id.tvcostsqft);
        area=(EditText)findViewById(R.id.etarea);
        cost = (EditText)findViewById(R.id.etcostSqft);
        enter_btn = (Button)findViewById(R.id.enterBtn);
        tb1tv1quantity = (TextView)findViewById(R.id.tb_tvcementquantity);
        tb1tv2quantity = (TextView)findViewById(R.id.tb_tvsandquantity);
        tb1tv3quantity = (TextView)findViewById(R.id.tb_tvaggregatequantity);
        tb1tv4quantity = (TextView)findViewById(R.id.tb_tvbricksquantity);
        tb1tv5quantity = (TextView)findViewById(R.id.tb_tvsizestonequantity);
        tb1tv6quantity = (TextView)findViewById(R.id.tb_tvsteelquantity);
        tb1tv7quantity = (TextView)findViewById(R.id.tb_tvwoodquantity);

        tb2tv1cost = (TextView)findViewById(R.id.tb2_tvsoilandconcretecost);
        tb2tv2cost = (TextView)findViewById(R.id.tb2_tvfoundationcost);
        tb2tv3cost = (TextView)findViewById(R.id.tb2_tvconstructionplinthcost);
        tb2tv4cost = (TextView)findViewById(R.id.tb2_tvroofingincludingwaterproofingcost);
        tb2tv5cost = (TextView)findViewById(R.id.tb2_tvflooringcost);
        tb2tv6cost = (TextView)findViewById(R.id.tb2_tvWoodworkcost);
        tb2tv7cost = (TextView)findViewById(R.id.tb2_tvinternalfinishescost);
        tb2tv8cost = (TextView)findViewById(R.id.tb2_tvexternalfinishescost);
        tb2tv9cost = (TextView)findViewById(R.id.tb2_tvwatersupplycost);


        tb1tv1cost = (TextView)findViewById(R.id.tb_tvcementmanufacturer);
        tb1tv2cost = (TextView)findViewById(R.id.tb_tvsandmanufacturer);
        tb1tv3cost = (TextView)findViewById(R.id.tb_tvaggregatemanufacturer);
        tb1tv4cost = (TextView)findViewById(R.id.tb_tvbricksmanufacturer);
        tb1tv5cost = (TextView)findViewById(R.id.tb_tvsizestonemanufacturer);
        tb1tv6cost = (TextView)findViewById(R.id.tb_tvsteelmanufacturer);
        tb1tv7cost = (TextView)findViewById(R.id.tb_tvwoodmanufacturer);

//        showresult=(TextView)findViewById(R.id.tvshow_result);



//        area.addTextChangedListener(check);
//        cost.addTextChangedListener(check);
        result = String.valueOf((int)Math.round(lr.estimateCost()));
        cost.setText(result);

        calculateValue();

       table.setColumnCollapsed(1,table_flg);
       table.setColumnCollapsed(2,table_flg);
       table2.setColumnCollapsed(1,table_flg);






        if(table_flg){
            table_flg = false;
            switch_btn.setText("Show Details");
        } else {
            table_flg = true;
            switch_btn.setText("Hide Details");
        }




    }
    private TextWatcher check = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {




        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            result = String.valueOf((int)Math.round(lr.estimateCost()));
            cost.setText(result);


            // calculateValue();

        }

        @Override
        public void afterTextChanged(Editable s) {



        }
    };




}
