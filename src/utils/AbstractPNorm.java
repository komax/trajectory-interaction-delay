/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author max
 */
public abstract class AbstractPNorm implements DistanceNorm {

    public abstract int getOrder();

    @Override
    public double distance(double[] pointP, double[] pointQ) {
        double summedDistance = 0.0;
        int order = getOrder();
        for (int i = 0; i < pointP.length; i++) {
            double diff = pointP[i] - pointQ[i];
            double summand = 1;
            for (int j=0; j<order; j++) {
                summand = summand * diff;
            }
            summedDistance += summand;
        }
        return Math.pow(summedDistance, 1.0/order);
    }

}
