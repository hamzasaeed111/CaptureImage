package com.example.captureimage.Classes;

import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

public class LinearRegressionLogic {

    private RealMatrix w, estimate;
    private String Year="2019";

    public void setYear(String year) {
        Year = year;
    }



    public LinearRegressionLogic(double [][] xArray, double[][] yArray) throws Exception
    {
        applyNormalEquation(MatrixUtils.createRealMatrix(xArray), MatrixUtils.createRealMatrix(yArray));

    }
    private void applyNormalEquation(RealMatrix x , RealMatrix y) throws Exception
    {
        LUDecomposition luDecomposition = new LUDecomposition(x.transpose().multiply(x));
        if (luDecomposition.getDeterminant()==0)throw new Exception("Singular matrix no inverse");
        else {
            w = luDecomposition.getSolver().getInverse().multiply((x.transpose().multiply(y)));
        }
        estimate = x.multiply(w);

    }

    public double estimateCost()
    {
        return MatrixUtils.createColumnRealMatrix(new double[]{1, Double.valueOf(Year)}).transpose().multiply(w).getData()[0][0];


    }

    public RealMatrix getW() {
        return w;
    }

    public RealMatrix getEstimate() {
        return estimate;
    }
}
