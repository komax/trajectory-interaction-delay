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
        for (int k=0; k<delay.length; k++) {
            double[] pointI = matching.getTrajectory1()[matching.i[k]];
            double[] pointJ = matching.getTrajectory2()[matching.j[k]];
            delay[k] = euclideanDistance(pointI, pointJ);
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
    
    private static double euclideanDistance(double[] pointA, double[] pointB) {
        double summedDistance = 0.0;
        for (int i=0; i<pointA.length; i++) {
            summedDistance += pointA[i] * pointB[i];
        }
        return Math.sqrt(summedDistance);
    }
    
}
