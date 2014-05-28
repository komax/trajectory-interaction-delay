/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import frechet.Matching;

/**
 *
 * @author max
 */
public class Utils {
    public static final DistanceNorm EuclideanDistance = new AbstractPNorm() {

        @Override
        public int getOrder() {
            return 2;
        }
    };
    
    public static final DistanceNorm L1Distance = new AbstractPNorm() {
        
        @Override
        public int getOrder() {
            return 1;
        }
    };
    
    public static final DistanceNorm LInfDistance = new DistanceNorm() {

        @Override
        public double distance(double[] pointP, double[] pointQ) {
            double maxDistance = Double.MIN_VALUE;
            for (int i = 0; i < pointP.length; i++) {
                double diff = Math.abs(pointP[i] - pointQ[i]);
                maxDistance = Math.max(maxDistance, diff);
            }
            return maxDistance;
        }
    };
    
    public static int findMatchingIndex(Matching matching, int i, int j) {
        for (int k=0; k<matching.i.length; k++) {
            if (matching.i[k] == i && matching.j[k] == j) {
                return k;
            }
        }
        return -1;
    }
    
    public static double[] normalizedDelay(double[] delays) {
        double maxDelay = Double.MIN_VALUE;
        double[] normalizedDelays = new double[delays.length];
        for (double delay : delays) {
            if (delay > maxDelay) {
                maxDelay = delay;
            }
        }
        
        for (int i=0; i<delays.length; i++) {
            normalizedDelays[i] = delays[i] / maxDelay;
        }
        return normalizedDelays;
    }
    
    public static boolean[] trajectroy1IsAhead(Matching matching) {
        boolean[] traject1IsAhead = new boolean[matching.i.length];
        for (int k=0; k<traject1IsAhead.length; k++) {
            traject1IsAhead[k] = matching.i[k] > matching.j[k];
        }
        return traject1IsAhead;
    }
    
    public static boolean[] trajectroy2IsAhead(Matching matching) {
        boolean[] traject2IsAhead = new boolean[matching.i.length];
        for (int k=0; k<traject2IsAhead.length; k++) {
            traject2IsAhead[k] = matching.i[k] < matching.j[k];
        }
        return traject2IsAhead;
    }
    
    public static double[] delayWithEuclideanNorm(Matching matching) {
        double[] delay = new double[matching.i.length];
        double[][] traject1 = matching.getTrajectory1();
        double[][] traject2 = matching.getTrajectory2();
        for (int k=0; k<delay.length; k++) {
            double[] pointI = traject1[matching.i[k]];
            double[] pointJ = traject2[matching.j[k]];
            delay[k] = EuclideanDistance.distance(pointI, pointJ);
        }
        return delay;
    }
    
    public static int[] delayInTimestamps(Matching matching) {
        int[] delays = new int[matching.i.length];
        for (int k=0; k<delays.length; k++) {
            int diff = Math.abs(matching.i[k] - matching.j[k]);
            delays[k] = diff;
        }
        return delays;
    }
    
}
