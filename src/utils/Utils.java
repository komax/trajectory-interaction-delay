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
        
        @Override
        public String toString() {
            return "NormInf";
        }
    };

    public static DistanceNorm selectDistanceNorm(int order) {
        switch (order) {
            case 1:
                return L1Distance;
            case 2:
                return EuclideanDistance;
            case Integer.MAX_VALUE:
                return LInfDistance;
            default:
                throw new RuntimeException("order=" + order + " is not a valid norm");
        }
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
        for (double delay : values) {
            if (delay > maxValue) {
                maxValue = delay;
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

    public static double[] distancesOnMatching(Matching matching, DistanceNorm distance) {
        double[] delay = new double[matching.i.length];
        double[][] traject1 = matching.getTrajectory1();
        double[][] traject2 = matching.getTrajectory2();
        for (int k = 0; k < delay.length; k++) {
            double[] pointI = traject1[matching.i[k]];
            double[] pointJ = traject2[matching.j[k]];
            delay[k] = distance.distance(pointI, pointJ);
        }
        return delay;
    }

    public static int[] delayInTimestamps(Matching matching) {
        int[] delays = new int[matching.i.length];
        for (int k = 0; k < delays.length; k++) {
            int diff = Math.abs(matching.i[k] - matching.j[k]);
            delays[k] = diff;
        }
        return delays;
    }
    
    public static double[] headingOnMatching(Matching matching) {
        double[] headings = new double[matching.i.length];
        double[][] traject1 = matching.getTrajectory1();
        double[][] traject2 = matching.getTrajectory2();  
        for (int k = 0; k < headings.length; k++) {
            int indexI = matching.i[k];
            int indexJ = matching.j[k];
            double[] pointI = traject1[indexI];
            double[] pointJ = traject2[indexJ];
            double[] followPointI;
            double[] followPointJ;
            if (indexI < (traject1.length - 1)) {
                followPointI = traject1[indexI + 1];
            } else {
                followPointI = traject1[traject1.length -1];
            }
            if (indexJ < (traject2.length - 1)) {
                followPointJ = traject2[indexJ + 1];
            } else {
                followPointJ = traject2[traject2.length -1];
            }
            double angleI = computeHeadingAngle(pointI, followPointI);
            double angleJ = computeHeadingAngle(pointJ, followPointJ);
            double headingValue = Math.cos(angleI - angleJ);
            headings[k] = headingValue; 
        }
        return headings;
    }
    
    public static double[] headingDistancesOnMatching(Matching matching) {
        double[] headingDistances = new double[matching.i.length];
        double[] headings = headingOnMatching(matching);
        for (int k = 0; k < headings.length; k++) {
            headingDistances[k] = 1.0 - headings[k];
        }
        return headingDistances;
    }
    
    public static double[] dynamicInteractionOnMatching(Matching matching, DistanceNorm distance, double alpha) {
        double[] displacements = displacementsOnMatching(matching, distance, alpha);
        double[] headings = headingOnMatching(matching);
        double[] dynamicInteractions = new double[displacements.length];
        for (int i = 0; i < dynamicInteractions.length; i++) {
            dynamicInteractions[i] = 1.0 - (displacements[i] * headings[i]);
        }
        return dynamicInteractions;
    }
    
    private static double[] displacementsOnMatching(Matching matching, DistanceNorm distance, double alpha) {
        double[] displacements = new double[matching.i.length];
        double[][] traject1 = matching.getTrajectory1();
        double[][] traject2 = matching.getTrajectory2();  
        for (int k = 0; k < displacements.length; k++) {
            double[] pointI = traject1[matching.i[k]];
            double[] pointJ = traject2[matching.j[k]];
            double[] followPointI;
            double[] followPointJ;
            if (k < (displacements.length - 1)) {
                followPointI = traject1[matching.i[k+1]];
                followPointJ = traject2[matching.j[k+1]];
            } else {
                followPointI = traject1[traject1.length - 1];
                followPointJ = traject2[traject2.length - 1];
            }
            double distanceI = computeLineSegmentDistance(pointI, followPointI, distance);
            double distanceJ = computeLineSegmentDistance(pointJ, followPointJ, distance);
            double displacementValue = computeDisplacement(distanceI, distanceJ, alpha);
            displacements[k] = displacementValue; 
        }
        return displacements;
    }
    
    private static double computeLineSegmentDistance(double[] pointI, double[] successorPointI, DistanceNorm distance) {
        return vectorNorm(diff(pointI, successorPointI), distance);
    }
    
    private static double computeDisplacement(double distanceI, double distanceJ, double alpha) {
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
    
    public static double[] directionalDistancesOnMatching(Matching matching, DistanceNorm distance) {
        double[] distances = distancesOnMatching(matching, distance);
        double[] headings = headingOnMatching(matching);
        double[] directionalDistances = new double[distances.length];
        for (int i = 0; i < directionalDistances.length; i++) {
            directionalDistances[i] = distances[i] * (2 - headings[i]);
        }
        return directionalDistances;
    }
    
    public static double[] crossProduct(double[] p, double[] q) {
        double[] product = new double[p.length];
        product[0] = p[1] * q[2] - p[2] * q[1];
        product[1] = p[2] * q[0] - p[0] * q[2];
        product[2] = p[0] * q[1] - p[1] * q[0];
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
        double angle = Math.atan2(vectorNorm(crossProduct(vectorA, vectorB), EuclideanDistance),
                dotProduct(vectorA, vectorB));
        return angle;
    }
    
    private static double computeHeadingAngle(double[] p, double[] successorP) {
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
