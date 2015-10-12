/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frechet;

import utils.Trajectory;
import utils.distance.DistanceNorm;
import utils.distance.DistanceNormFactory;

/**
 *
 * @author max
 */
public class FrechetDistanceDPTriplet {
    private final Trajectory traject1;
    private final Trajectory traject2;
    private final Trajectory traject3;
    private final int n;
    private final double[][][] frechetDistances;
    
    private static final DistanceNorm EuclideanDistance = DistanceNormFactory.EuclideanDistance;
    
    private static double maxTriplet(double val1, double val2, double val3) {
        double maxVal = Math.max(val1, val2);
        maxVal = Math.max(maxVal, val3);
        return maxVal;
    }
    
    public FrechetDistanceDPTriplet(Trajectory traject1, Trajectory traject2, Trajectory traject3) {
        this.traject1 = traject1;
        this.traject2 = traject2;
        this.traject3 = traject3;
        
        this.n = traject1.length();
        
        this.frechetDistances = new double[n][n][n];
    }
    
    private double euclideanDistanceTraject12(int i, int j) {
        double[] pointI = traject1.getPoint(i);
        double[] pointJ = traject2.getPoint(j);
        return EuclideanDistance.distance(pointI, pointJ);
    }

    private double euclideanDistanceTraject23(int j, int k) {
        double[] pointJ = traject2.getPoint(j);
        double[] pointK = traject3.getPoint(k);
        return EuclideanDistance.distance(pointJ, pointK);
    }
    
    private double euclideanDistanceTraject13(int i, int k) {
        double[] pointI = traject1.getPoint(i);
        double[] pointK = traject3.getPoint(k);
        return EuclideanDistance.distance(pointI, pointK);
    }
    
    private double longestLeashOnTriplet(int i, int j, int k) {
        double distance12 = euclideanDistanceTraject12(i, j);
        double distance23 = euclideanDistanceTraject23(j, k);
        double distance13 = euclideanDistanceTraject13(i, k);
        return maxTriplet(distance12, distance23, distance13);
    }
    
    private double minPreviousValues(int i, int j, int k) {
        double bestMinValue = Math.min(frechetDistances[i][j][k-1], frechetDistances[i][j-1][k]);
        bestMinValue = Math.min(bestMinValue, frechetDistances[i][j-1][k-1]);
        bestMinValue = Math.min(bestMinValue, frechetDistances[i-1][j][k]);
        bestMinValue = Math.min(bestMinValue, frechetDistances[i-1][j-1][k]);
        bestMinValue = Math.min(bestMinValue, frechetDistances[i-1][j][k-1]);
        bestMinValue = Math.min(bestMinValue, frechetDistances[i-1][j-1][k-1]);
        return bestMinValue;
    }
    
    public double computeFrechetDistance() {
        // Initialization
        // Omit longest leash value for (0,0,0).
        frechetDistances[0][0][0] = Double.MIN_VALUE;
        
        for (int j = 0; j <= n - 1; j++) {
            for (int k = 0; k <= n - 1; k++) {
                frechetDistances[0][j][k] = longestLeashOnTriplet(0, j, k);
            }
        }
        
        for (int i = 0; i <= n - 1; i++) {
            for (int k = 0; k <= n - 1; k++) {
                frechetDistances[i][0][k] = longestLeashOnTriplet(i, 0, k);
            }
        }
        
        for (int i = 0; i <= n - 1; i++) {
            for (int j = 0; j <= n - 1; j++) {
                frechetDistances[i][j][0] = longestLeashOnTriplet(i, j, 0);
            }
        }
        
        // Dynamic Program to compute the Frechet bottleneck.
        for (int i = 1; i <= n - 1; i++) {
            for (int j = 1; j <= n - 1; j++) {
                for (int k = 1; k <= n - 1; k++) {
                    double frechetEntry = Double.MAX_VALUE;
                    if (i != n-1 && j != n-1 && k != n-1) {
                        // Skip longest leash value from (n-1, n-1, n-1)
                        frechetEntry = longestLeashOnTriplet(i, j, k);
                    }
                    double minPreviousVal = minPreviousValues(i, j, k);
                    frechetEntry = Math.max(frechetEntry, minPreviousVal);
                    frechetDistances[i][j][k] = frechetEntry;
                }
            }
        }
        
        // Return the Frechet distance in the last cell.
        return frechetDistances[n-1][n-1][n-1];
    }
    
}
