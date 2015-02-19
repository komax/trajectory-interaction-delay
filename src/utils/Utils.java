/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import delayspace.DelaySpace;
import utils.distance.DistanceNorm;
import frechet.Matching;
import static utils.distance.DistanceNormFactory.EuclideanDistance;

/**
 *
 * @author max
 */
public class Utils {
    public static final double EPSILON = 0.000001;
    
    public static int roundDouble(double number) {
        return (int) Math.round(number);
    }

    public static int findMatchingIndex(Matching matching, int i, int j) {
        for (int k = 0; k < matching.i.length; k++) {
            if (matching.i[k] == i && matching.j[k] == j) {
                return k;
            }
        }
        return -1;
    }

    public static double[] normalizeValues(double[] values) {
        double maxValue = Double.MIN_VALUE;
        double[] normalizedDelays = new double[values.length];
        for (double val : values) {
            if (val > maxValue) {
                maxValue = val;
            }
        }

        for (int i = 0; i < values.length; i++) {
            normalizedDelays[i] = values[i] / maxValue;
        }
        return normalizedDelays;
    }

    public static boolean[] trajectroy1IsAhead(Matching matching, int threshold) {
        boolean[] traject1IsAhead = new boolean[matching.i.length];
        for (int k = 0; k < traject1IsAhead.length; k++) {
            if (matching.i[k] > matching.j[k]) {
                int diff = Math.abs(matching.i[k] - matching.j[k]);
                traject1IsAhead[k] = diff >= threshold;
            } else {
                traject1IsAhead[k] = false;
            }
        }
        return traject1IsAhead;
    }

    public static boolean[] trajectroy2IsAhead(Matching matching, int threshold) {
        boolean[] traject2IsAhead = new boolean[matching.i.length];
        for (int k = 0; k < traject2IsAhead.length; k++) {
            if (matching.i[k] < matching.j[k]) {
                int diff = Math.abs(matching.i[k] - matching.j[k]);
                traject2IsAhead[k] = diff >= threshold;
            } else {
                traject2IsAhead[k] = false;
            }
        }
        return traject2IsAhead;
    }

    public static double[] distancesOnMatching(Matching matching, DelaySpace delaySpace) {
        double[] distances = new double[matching.getLength()];
        for (int k = 0; k < distances.length; k++) {
            double cellValue  = delaySpace.get(matching.i[k], matching.j[k]);
            distances[k] = cellValue;
        }
        return distances;
    }

    public static int[] delayInTimestamps(Matching matching) {
        int[] delays = new int[matching.i.length];
        for (int k = 0; k < delays.length; k++) {
            int diff = Math.abs(matching.i[k] - matching.j[k]);
            delays[k] = diff;
        }
        return delays;
    }
    
    public static double computeDistanceOnMovementVector(double[] pointI, double[] successorPointI, DistanceNorm distance) {
        return vectorNorm(diff(pointI, successorPointI), distance);
    }
    
    public static double computeDisplacement(double distanceI, double distanceJ, double alpha) {
        double eps = 0.000001;
        double summedDistance = distanceI + distanceJ;
        if (summedDistance < eps) {
            return 1.0;
        } else {
            double numerator = Math.abs(distanceI - distanceJ);
            double fraction = numerator / summedDistance;
            return 1.0 - Math.pow(fraction, alpha);
        }
    }
    
    public static double[] crossProduct3D(double[] p, double[] q) {
        double[] product = new double[p.length];
        product[0] = p[1] * q[2] - p[2] * q[1];
        product[1] = p[2] * q[0] - p[0] * q[2];
        product[2] = p[0] * q[1] - p[1] * q[0];
        return product;
    }
    
    public static double crossProduct2D(double[] p, double[] q) {
        double product = p[0] * q[1] - p[1] * q[0];
        return product;
    }
    
    public static double dotProduct(double[] p, double[] q) {
        double product = 0.0;
        for (int i = 0; i < p.length; i++) {
            product += p[i] * q[i];
        }
        return product;
    }
    
    public static double[] diff(double[] p, double[] q) {
        double[] diffResult = new double[p.length];
        for (int i = 0; i < p.length; i++) {
            diffResult[i] = p[i] - q[i];
        }
        return diffResult;
    }
    
    private static double vectorNorm(double[] vector, DistanceNorm distance) {
        double[] nullVector = new double[vector.length];
        for (int i = 0; i < vector.length; i++) {
            nullVector[i] = 0.0;
        }
        return distance.distance(vector, nullVector);
    }
    
    private static double computeAngle(double[] vectorA, double[] vectorB) {
        double angle = Math.atan2(crossProduct2D(vectorA, vectorB),
                dotProduct(vectorA, vectorB));
        return angle;
    }
    
    public static double computeHeadingAngle(double[] p, double[] successorP) {
        double[] projectedPoint = new double[p.length];
        projectedPoint[0] = successorP[0];
        for (int i = 1; i < p.length; i++) {
            projectedPoint[i] = p[i];
        }
        double[] vectorA = diff(projectedPoint, p);
        double[] vectorB = diff(successorP, p);
        double angle = computeAngle(vectorA, vectorB);
        if ((successorP[0] - p[0]) > 0) {
            if ((successorP[1] - p[1]) > 0) {
                return angle;
            } else {
                return -angle;
            }
        } else {
            if ((successorP[1] - p[1]) > 0) {
                return Math.PI - angle;
            } else {
                return Math.PI + angle;
            }
        }
    }
}
