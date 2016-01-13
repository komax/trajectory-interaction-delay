/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import utils.distance.DistanceNorm;
import utils.distance.DistanceNormFactory;

/**
 *
 * @author max
 */
public class TripleDTWMatching {
    
    private static final DistanceNorm EuclideanDistance = DistanceNormFactory.EuclideanDistance;
    
    private final Trajectory trajectory1;
    private final Trajectory trajectory2;
    private final Trajectory trajectory3;
    private final int n;
    private final double[][][] dtwDistances;
    private final IntTriple[][][] predecessors;
    
    public List<IntTriple> compute() {
        computeValues();
        return createMatching();
    }
    
    public TripleDTWMatching(Trajectory trajectory1, Trajectory trajectory2,
            Trajectory trajectory3) {
        this.trajectory1 = trajectory1;
        this.trajectory2 = trajectory2;
        this.trajectory3 = trajectory3;
        this.n = trajectory1.length();
        
        this.dtwDistances = new double[n + 1][n + 1][n + 1];
        this.predecessors = new IntTriple[n + 1][n + 1][n + 1];
    }
    
    private static double maxTriplet(double val1, double val2, double val3) {
        double maxVal = Math.max(val1, val2);
        maxVal = Math.max(maxVal, val3);
        return maxVal;
    }
    
    private double euclideanDistanceTraject12(int i, int j) {
        double[] pointI = trajectory1.getPoint(i);
        double[] pointJ = trajectory2.getPoint(j);
        return EuclideanDistance.distance(pointI, pointJ);
    }

    private double euclideanDistanceTraject23(int j, int k) {
        double[] pointJ = trajectory2.getPoint(j);
        double[] pointK = trajectory3.getPoint(k);
        return EuclideanDistance.distance(pointJ, pointK);
    }
    
    private double euclideanDistanceTraject13(int i, int k) {
        double[] pointI = trajectory1.getPoint(i);
        double[] pointK = trajectory3.getPoint(k);
        return EuclideanDistance.distance(pointI, pointK);
    }
    
    private double leashValueOnTriplet(int i, int j, int k) {
        double distance12 = euclideanDistanceTraject12(i, j);
        double distance23 = euclideanDistanceTraject23(j, k);
        double distance13 = euclideanDistanceTraject13(i, k);
     //   return distance12 + distance13 + distance23;
        return maxTriplet(distance12, distance23, distance13);
        //return distance12 * distance12 + distance13 * distance13 + distance23 * distance23;
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
    
    public void computeValues() {
        // Initialization
        
        for (int j = 0; j <= n; j++) {
            for (int k = 0; k <= n; k++) {
                dtwDistances[0][j][k] = Double.MAX_VALUE;
                predecessors[0][j][k] = IntTriple.createIntTriple(0, j, k);
            }
        }
        
        for (int i = 0; i <= n; i++) {
            for (int k = 0; k <= n; k++) {
                dtwDistances[i][0][k] = Double.MAX_VALUE;
                predecessors[i][0][k] = IntTriple.createIntTriple(i, 0, k);
            }
        }
        
        for (int i = 0; i <= n; i++) {
            for (int j = 0; j <= n; j++) {
                dtwDistances[i][j][0] = Double.MAX_VALUE;
                predecessors[i][j][0] = IntTriple.createIntTriple(i, j, 0);
            }
        }
        // small value for (0,0,0).
        dtwDistances[0][0][0] = 0.0;
        
        // Dynamic Program to compute the DTW bottleneck.
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                for (int k = 1; k <= n; k++) {
                    double leashValue = leashValueOnTriplet(i - 1, j - 1, k - 1);
                    IntTriple bestParentIndices = minPreviousIndices(i, j, k);
                    double bestParentVal = dtwDistances[bestParentIndices.i][bestParentIndices.j][bestParentIndices.k];
                    predecessors[i][j][k] = bestParentIndices;
                    dtwDistances[i][j][k] = leashValue + bestParentVal;
                }
            }
        }
    }
    
    private List<IntTriple> createMatching() {
        IntTriple predecessor  = predecessors[n][n][n];
        List<IntTriple> matchingIndices = new ArrayList<>();
        matchingIndices.add(IntTriple.createIntTriple(n - 1, n - 1, n - 1));
        while (!predecessor.isEqual(0, 0, 0)) {
            matchingIndices.add(IntTriple.createIntTriple(predecessor.i - 1, predecessor.j  - 1, predecessor.k - 1));
            predecessor = predecessors[predecessor.i][predecessor.j][predecessor.k];
        }
        matchingIndices.add(IntTriple.createIntTriple(0, 0, 0));
        Collections.reverse(matchingIndices);
        return matchingIndices;
    }
    
}
