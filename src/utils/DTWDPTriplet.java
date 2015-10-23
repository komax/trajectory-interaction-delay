/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import utils.distance.DistanceNorm;
import utils.distance.DistanceNormFactory;

/**
 *
 * @author max
 */
public class DTWDPTriplet {
    private final Trajectory traject1;
    private final Trajectory traject2;
    private final Trajectory traject3;
    private final int lengthX;
    private final int lengthY;
    private final int lengthZ;
    private final IntTriple leftEnd;
    private final double[][][] dtwDistances;
    private final IntTriple[][][] bottleneckIndices;
    
    private static final DistanceNorm EuclideanDistance = DistanceNormFactory.EuclideanDistance;
    
    private static double maxTriplet(double val1, double val2, double val3) {
        double maxVal = Math.max(val1, val2);
        maxVal = Math.max(maxVal, val3);
        return maxVal;
    }
    
    public DTWDPTriplet(IntTriple leftEnd, IntTriple rightEnd, Trajectory traject1, Trajectory traject2, Trajectory traject3) {
        this.traject1 = traject1;
        this.traject2 = traject2;
        this.traject3 = traject3;
        
        this.leftEnd = leftEnd;
        
        this.lengthX = rightEnd.i - leftEnd.i  + 1;
        this.lengthY = rightEnd.j - leftEnd.j + 1;
        this.lengthZ = rightEnd.k - leftEnd.k + 1;
        
        this.dtwDistances = new double[lengthX][lengthY][lengthZ];
        this.bottleneckIndices = new IntTriple[lengthX][lengthY][lengthZ];
    }
    
    private double euclideanDistanceTraject12(int i, int j) {
        double[] pointI = traject1.getPoint(i + leftEnd.i);
        double[] pointJ = traject2.getPoint(j + leftEnd.j);
        return EuclideanDistance.distance(pointI, pointJ);
    }

    private double euclideanDistanceTraject23(int j, int k) {
        double[] pointJ = traject2.getPoint(j + leftEnd.j);
        double[] pointK = traject3.getPoint(k + leftEnd.k);
        return EuclideanDistance.distance(pointJ, pointK);
    }
    
    private double euclideanDistanceTraject13(int i, int k) {
        double[] pointI = traject1.getPoint(i + leftEnd.i);
        double[] pointK = traject3.getPoint(k + leftEnd.k);
        return EuclideanDistance.distance(pointI, pointK);
    }
    
    private double leashValueOnTriplet(int i, int j, int k) {
        double distance12 = euclideanDistanceTraject12(i, j);
        double distance23 = euclideanDistanceTraject23(j, k);
        double distance13 = euclideanDistanceTraject13(i, k);
       // return maxTriplet(distance12, distance23, distance13);
        return distance12 * distance12 + distance13 * distance13 + distance23 * distance23;
    }
    
    @Deprecated
    private double minPreviousValues(int i, int j, int k) {
        double bestMinValue = Math.min(dtwDistances[i][j][k-1], dtwDistances[i][j-1][k]);
        bestMinValue = Math.min(bestMinValue, dtwDistances[i][j-1][k-1]);
        bestMinValue = Math.min(bestMinValue, dtwDistances[i-1][j][k]);
        bestMinValue = Math.min(bestMinValue, dtwDistances[i-1][j-1][k]);
        bestMinValue = Math.min(bestMinValue, dtwDistances[i-1][j][k-1]);
        bestMinValue = Math.min(bestMinValue, dtwDistances[i-1][j-1][k-1]);
        return bestMinValue;
    }
    
    private IntTriple minPreviousIndices(int i, int j, int k) {
        IntTriple bestParentIndices = IntTriple.createIntTriple(i, j, k-1);
        double bestMinValue = dtwDistances[i][j][k-1];
        if (dtwDistances[i][j-1][k] < bestMinValue) {
            bestMinValue = dtwDistances[i][j-1][k];
            bestParentIndices = IntTriple.createIntTriple(i, j-1, k);
        }
        if (dtwDistances[i][j-1][k-1]  < bestMinValue) {
            bestMinValue = dtwDistances[i][j-1][k-1];
            bestParentIndices = IntTriple.createIntTriple(i, j-1, k-1);
        }
        if (dtwDistances[i-1][j][k]  < bestMinValue) {
            bestMinValue = dtwDistances[i-1][j][k];
            bestParentIndices = IntTriple.createIntTriple(i-1, j, k);
        }
        if (dtwDistances[i-1][j-1][k]  < bestMinValue) {
            bestMinValue = dtwDistances[i-1][j-1][k];
            bestParentIndices = IntTriple.createIntTriple(i-1, j-1, k);
        }
        if (dtwDistances[i-1][j][k-1]  < bestMinValue) {
            bestMinValue = dtwDistances[i-1][j][k-1];
            bestParentIndices = IntTriple.createIntTriple(i-1, j, k-1);
        }
        if (dtwDistances[i-1][j-1][k-1]  < bestMinValue) {
            bestMinValue = dtwDistances[i-1][j-1][k-1];
            bestParentIndices = IntTriple.createIntTriple(i-1, j-1, k-1);
        }
        return bestParentIndices;
    }
    
    public void computeFrechetDistance() {
        // Initialization
        // small value for (0,0,0).
        dtwDistances[0][0][0] = 0.0;
        
        for (int j = 0; j <= lengthY - 1; j++) {
            for (int k = 0; k <= lengthZ - 1; k++) {
                dtwDistances[0][j][k] = Double.MAX_VALUE;
                bottleneckIndices[0][j][k] = IntTriple.createIntTriple(0, j, k);
            }
        }
        
        for (int i = 0; i <= lengthX - 1; i++) {
            for (int k = 0; k <= lengthZ - 1; k++) {
                dtwDistances[i][0][k] = Double.MAX_VALUE;
                bottleneckIndices[i][0][k] = IntTriple.createIntTriple(i, 0, k);
            }
        }
        
        for (int i = 0; i <= lengthX - 1; i++) {
            for (int j = 0; j <= lengthY - 1; j++) {
                dtwDistances[i][j][0] = Double.MAX_VALUE;
                bottleneckIndices[i][j][0] = IntTriple.createIntTriple(i, j, 0);
            }
        }
        
        // Dynamic Program to compute the DTW bottleneck.
        for (int i = 1; i <= lengthX - 1; i++) {
            for (int j = 1; j <= lengthY - 1; j++) {
                for (int k = 1; k <= lengthZ - 1; k++) {
                    double leashValue = leashValueOnTriplet(i, j, k);
                    IntTriple bestParentIndices = minPreviousIndices(i, j, k);
                    double bestParentVal = dtwDistances[bestParentIndices.i][bestParentIndices.j][bestParentIndices.k];
                    bottleneckIndices[i][j][k] = bestParentIndices;
                    dtwDistances[i][j][k] = leashValue + bestParentVal;
                }
            }
        }
    }
    
    double getFrechetDistance() {
        // Return the Frechet distance in the last cell.
        return dtwDistances[lengthX - 1][lengthY - 1][lengthZ - 1];
    }
    
    IntTriple getBottleneck() {
        IntTriple zerobasedBottleneck = bottleneckIndices[lengthX - 1][lengthY - 1][lengthZ-1];
        return IntTriple.createIntTriple(leftEnd.i + zerobasedBottleneck.i, leftEnd.j + zerobasedBottleneck.j, leftEnd.k + zerobasedBottleneck.k);
    }
}
