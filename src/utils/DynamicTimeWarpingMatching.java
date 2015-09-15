/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import delayspace.DelaySpace;
import frechet.Matching;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author max
 */
public class DynamicTimeWarpingMatching {
    static public class IntTuple {
        public final int i;
        public final int j;
        
        public IntTuple(int i, int j) {
            this.i = i;
            this.j = j;
        }
        
        public static IntTuple createIntTuple(int i, int j) {
            return new  IntTuple(i, j);
        }
    }
    
    public static List<IntTuple> computeDTWMatching(Trajectory trajectory1, Trajectory trajectory2, DelaySpace delayspace) {
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
        
        IntTuple[][] predecessors = new IntTuple[n][m];
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
                    predecessors[i][j] = IntTuple.createIntTuple(i - 1, j);
                } else if (downValue < diagonalValue) {
                    dtwMatrix[i][j] = cost + downValue;
                    predecessors[i][j] = IntTuple.createIntTuple(i, j - 1);
                } else {
                    dtwMatrix[i][j] = cost + diagonalValue;
                    predecessors[i][j] = IntTuple.createIntTuple(i - 1, j - 1);
                }
            }
        }
        IntTuple predecessor  = predecessors[n][m];
        List<IntTuple> matchingIndices = new ArrayList<>();
        matchingIndices.add(IntTuple.createIntTuple(n, m));
        while (predecessor != null) {
            matchingIndices.add(predecessor);
            predecessor = predecessors[predecessor.i][predecessor.j];
        }
        Collections.reverse(matchingIndices);
        return matchingIndices;
    }
    
}
