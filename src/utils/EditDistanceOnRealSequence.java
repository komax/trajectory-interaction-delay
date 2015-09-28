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
    private final int[][] edrMatrix;
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
        
        this.edrMatrix = new int[n + 1][m + 1];
        this.predecessors = new IntPair[n + 1][m + 1];
    }
    
    public Matching computeMatching() {
        computeEditDistance();
        List<IntPair> indices = getMatchingIndices();
        return transformIntoMatching(indices);
    }
    
    private boolean match(int i, int j) {
        return delayspace.get(i, j) <= epsilon;
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
                int leftValue = edrMatrix[i - 1][j] + 1; // Insertion.
                int downValue = edrMatrix[i][j - 1] + 1; // Deletion.
                int diagonalValue = edrMatrix[i - 1][j - 1]; // Match.
                // Add subcost if the points do not match based on epsilon.
                if (!match(i - 1 , j - 1)) {
                    diagonalValue += 1;
                }
                if (leftValue < downValue && leftValue < diagonalValue) {
                    edrMatrix[i][j] = leftValue;
                    predecessors[i][j] = IntPair.createIntTuple(i - 1, j);
                } else if (downValue < diagonalValue) {
                    edrMatrix[i][j] = downValue;
                    predecessors[i][j] = IntPair.createIntTuple(i, j - 1);
                } else {
                    edrMatrix[i][j] = diagonalValue;
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
