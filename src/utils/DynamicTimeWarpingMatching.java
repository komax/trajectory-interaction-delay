/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import delayspace.DelaySpace;
import frechet.Matching;

/**
 *
 * @author max
 */
public class DynamicTimeWarpingMatching {
    static class IntTuple {
        public final int i;
        public final int j;
        
        public IntTuple(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }
    
    public static Matching computeDTWMatching(Trajectory trajectory1, Trajectory trajectory2, DelaySpace delayspace) {
        int n = trajectory1.length();
        int m = trajectory2.length();
        double[][] dtwMatrix = new double[n][m];
        
        // Initialization.
        for (int i = 0; i < n; i++) {
            dtwMatrix[i][0] = Double.MAX_VALUE;
        }
        for (int j = 0; j < m; j++) {
            dtwMatrix[0][j] = Double.MAX_VALUE;
        }
        dtwMatrix[0][0] = 0;
        
        // Dynamic Program to compute DTW.
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                double cost = delayspace.get(i - 1 , j - 1);
                dtwMatrix[i][j] = cost;
                double leftValue = dtwMatrix[i - 1][j]; // Insertion.
                double downValue = dtwMatrix[i][j - 1]; // Deletion.
                double diagonalValue = dtwMatrix[i - 1][j - 1]; // Match.
                if (leftValue < downValue && leftValue < diagonalValue) {
                    dtwMatrix[i][j] = cost + leftValue;
                } else if (downValue < diagonalValue) {
                    dtwMatrix[i][j] = cost + downValue;
                } else {
                    dtwMatrix[i][j] = cost + diagonalValue;
                }
            }
        }
        return null;
    }
    
}
