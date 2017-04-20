package frechet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import utils.IntTriple;
import utils.Trajectory;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author max
 */
public class TripleFrechetMatching {
    
    private final Trajectory trajectory1;
    private final Trajectory trajectory2;
    private final Trajectory trajectory3;
    private final int n;
    
    public List<IntTriple> compute() {
        IntTriple startMatching = IntTriple.createIntTriple(0, 0, 0);
        IntTriple endMatching = IntTriple.createIntTriple(n - 1, n - 1, n - 1);
        return computeMatchingRecursively(startMatching, endMatching);
    }
    
    public TripleFrechetMatching(Trajectory trajectory1, Trajectory trajectory2,
            Trajectory trajectory3) {
        this.trajectory1 = trajectory1;
        this.trajectory2 = trajectory2;
        this.trajectory3 = trajectory3;
        this.n = trajectory1.length();
    }
    
    private List<IntTriple> computeMatchingRecursively(IntTriple leftEnd,
            IntTriple rightEnd) {
        IntTriple diff = IntTriple.minus(rightEnd, leftEnd);
        // Stop recursion if left and right end are the same.
        if (diff.isEqual(0, 0, 0)) {
            return Collections.EMPTY_LIST;
        }
        // If two coordinates of diff are 0, just walk the line for the remaining.
        if (diff.i == 0 && diff.j == 0 && diff.k != 0) {
            List<IntTriple> resultingEdges = new ArrayList<>();
            // Walk the line only k coordinate.
            for (int k = leftEnd.k; k <= rightEnd.k; k++) {
                resultingEdges.add(IntTriple.createIntTriple(leftEnd.i, leftEnd.j, k));
            }
            return resultingEdges;
        } else if (diff.i != 0 && diff.j == 0 && diff.k == 0) {
            List<IntTriple> resultingEdges = new ArrayList<>();
            // Walk the line only i coordinate.
            for (int i = leftEnd.i; i <= rightEnd.i; i++) {
                resultingEdges.add(IntTriple.createIntTriple(i, leftEnd.j, leftEnd.k));
            }
            return resultingEdges;
        } else if (diff.i == 0 && diff.j != 0 && diff.k == 0) {
            List<IntTriple> resultingEdges = new ArrayList<>();
            // Walk the line only j coordinate.
            for (int j = leftEnd.j; j <= rightEnd.j; j++) {
                resultingEdges.add(IntTriple.createIntTriple(leftEnd.i, j, leftEnd.k));
            }
            return resultingEdges;
        }
        // Recursion end on 2D trajectory computation if one dimension is fixed.
        if (diff.i == 0 || diff.j == 0 || diff.k == 0) {
            FrechetDistance2DTriplet frechet2D = new FrechetDistance2DTriplet(leftEnd, rightEnd, trajectory1, trajectory2, trajectory3);
            List<IntTriple> resultingEdges = frechet2D.computeMatching();
            return resultingEdges;
        }
        FrechetDistanceDPTriplet frechetDP = new FrechetDistanceDPTriplet(
                leftEnd, rightEnd, trajectory1, trajectory2, trajectory3);
        frechetDP.computeFrechetDistance();
        IntTriple bottleneck = frechetDP.getBottleneck();
        IntTriple leftBottleneck = frechetDP.getLeftBottleneck();
        //System.out.println("leftEnd = "+leftEnd+" bottleneck = "+bottleneck+ " rightEnd="+rightEnd+ " d_bottleneck = " + frechetDP.getFrechetDistance());
  
        // Recurse if the boundries are distinct from the bottleneck.
        List<IntTriple> leftEdges;
        if (leftEnd.equals(bottleneck) || leftBottleneck == IntTriple.NULL_TRIPLE) {
            leftEdges = Collections.EMPTY_LIST;
        } else {
            leftEdges = computeMatchingRecursively(leftEnd, leftBottleneck);
        }
        List<IntTriple> rightEdges;
        if (rightEnd.equals(bottleneck)) {
            rightEdges = Collections.EMPTY_LIST;
        } else {
            rightEdges = computeMatchingRecursively(bottleneck, rightEnd);
        }
        List<IntTriple> resultingEdges = new ArrayList<>();
        resultingEdges.addAll(leftEdges);
        resultingEdges.addAll(rightEdges);
        return resultingEdges;
    }

}
