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
public class EditDistanceOnRealSequence {
    private final int n;
    private final int m;
    private final double[][] edrMatrix;
    private final IntPair[][] predecessors;
    private final Trajectory trajectory1;
    private final Trajectory trajectory2;
    private final DelaySpace delayspace;
    private final double epsilon;
    
    public EditDistanceOnRealSequence(Trajectory trajectory1, Trajectory trajectory2, DelaySpace delayspace, double epsilon) {
        this.trajectory1 = trajectory1;
        this.trajectory2 = trajectory2;
        this.delayspace = delayspace;
        this.epsilon = epsilon;
        
        this.n = trajectory1.length();
        this.m = trajectory2.length();
        
        this.edrMatrix = new double[n + 1][m + 1];
        this.predecessors = new IntPair[n + 1][m + 1];
    }
    
    public Matching computeMatching() {
        computeEditDistance();
        List<IntPair> indices = getMatchingIndices();
        return transformIntoMatching(indices);
    }
    
    private boolean match(int i, int j) {
        double[] p = trajectory1.getPoint(i);
        double[] q = trajectory2.getPoint(j);
        double absX = Math.abs(p[0] - q[0]);
        if (absX <= epsilon) {
            double absY = Math.abs(p[1] - q[1]);
            return absY <= epsilon;
        } else {
            return false;
        }
    }
    
    private void computeEditDistance() {
        // Initialization.
        for (int i = 1; i <= n; i++) {
            edrMatrix[i][0] = n;
        }
        for (int j = 1; j <= m; j++) {
            edrMatrix[0][j] = m;
        }
        edrMatrix[0][0] = 0;
        
        // Dynamic Program to compute EDR.
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                // FIXME still DTW. This needs to be updated.
                double cost = delayspace.get(i - 1 , j - 1);
                edrMatrix[i][j] = cost;
                double leftValue = edrMatrix[i - 1][j]; // Insertion.
                double downValue = edrMatrix[i][j - 1]; // Deletion.
                double diagonalValue = edrMatrix[i - 1][j - 1]; // Match.
                if (leftValue < downValue && leftValue < diagonalValue) {
                    edrMatrix[i][j] = cost + leftValue;
                    predecessors[i][j] = IntPair.createIntTuple(i - 1, j);
                } else if (downValue < diagonalValue) {
                    edrMatrix[i][j] = cost + downValue;
                    predecessors[i][j] = IntPair.createIntTuple(i, j - 1);
                } else {
                    edrMatrix[i][j] = cost + diagonalValue;
                    predecessors[i][j] = IntPair.createIntTuple(i - 1, j - 1);
                }
            }
        }
    }
    
    private List<IntPair> getMatchingIndices() {
        IntPair predecessor  = predecessors[n][m];
        List<IntPair> matchingIndices = new ArrayList<>();
        matchingIndices.add(IntPair.createIntTuple(n, m));
        while (predecessor != null) {
            matchingIndices.add(predecessor);
            predecessor = predecessors[predecessor.i][predecessor.j];
        }
        Collections.reverse(matchingIndices);
        return matchingIndices;
    }
    
    private Matching transformIntoMatching(List<IntPair> matchingIndices) {
        int lengthMatching = matchingIndices.size();
        int[] i = new int[lengthMatching];
        int[] j = new int[lengthMatching];
        for (int k = 1; k < lengthMatching; k++) {
            IntPair edge = matchingIndices.get(k);
            i[k] = edge.i - 1;
            j[k] = edge.j - 1;
        }
        return Matching.createMatching(i, j, lengthMatching);
    }
    
}