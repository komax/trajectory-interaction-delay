/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frechet;

import delayspace.DelaySpace;
import delayspace.DelaySpaceType;
import java.util.ArrayList;
import java.util.List;
import utils.IntTriple;
import utils.PairInTriple;
import utils.Trajectory;
import utils.distance.DistanceNormFactory;

/**
 *
 * @author max
 */
public class FrechetDistance2DTriplet {
    
    private final Trajectory traject1;
    private final Trajectory traject2;
    private final Trajectory traject3;
    private final IntTriple leftEnd;
    private final IntTriple rightEnd;
    private final PairInTriple pairType;
    
    public FrechetDistance2DTriplet(IntTriple leftEnd, IntTriple rightEnd, Trajectory traject1, Trajectory traject2, Trajectory traject3) {        
        this.traject1 = traject1;
        this.traject2 = traject2;
        this.traject3 = traject3;
        
        this.leftEnd = leftEnd;
        this.rightEnd = rightEnd;
        
        int lengthX = rightEnd.i - leftEnd.i  + 1;
        int lengthY = rightEnd.j - leftEnd.j + 1;
        int lengthZ = rightEnd.k - leftEnd.k + 1;
        
        if (lengthX == 1) {
            this.pairType = PairInTriple.TRAJ_23;
        } else if (lengthY == 1) {
            this.pairType = PairInTriple.TRAJ_13;
        } else if (lengthZ == 1) {
            this.pairType = PairInTriple.TRAJ_12;
        } else {
            throw new RuntimeException("The range between " +leftEnd + " and " + rightEnd + " is a 3D grid");
        }
    }
    
    public List<IntTriple> computeMatching() {
        Matching matching2D = compute2DMatching();
        List<IntTriple> tripletMatching = transform2DMatching(matching2D);
        return tripletMatching;
    }
    
    
    private Matching compute2DMatching() {
        Trajectory trajectA;
        Trajectory trajectB;
        switch(pairType) {
            case TRAJ_12:
                trajectA = traject1.subtrajectory(leftEnd.i, rightEnd.i);
                trajectB = traject2.subtrajectory(leftEnd.j, rightEnd.j);
                break;
            case TRAJ_13:
                trajectA = traject1.subtrajectory(leftEnd.i, rightEnd.i);
                trajectB = traject3.subtrajectory(leftEnd.k, rightEnd.k);
                break;
            case TRAJ_23:
                trajectA = traject2.subtrajectory(leftEnd.j, rightEnd.j);
                trajectB = traject3.subtrajectory(leftEnd.k, rightEnd.k);
                break;
            default:
                throw new RuntimeException("Invalid pairtype = " + pairType);
        }
        DelaySpace delaySpace = DelaySpace.createDelaySpace(trajectA, trajectB, DelaySpaceType.USUAL, DistanceNormFactory.EuclideanDistance);
        return LocallyCorrectFrechet.compute(delaySpace, trajectA, trajectB);
    }
    
    private List<IntTriple> transform2DMatching(Matching matching2D) {
        List<IntTriple> transformedTriples = new ArrayList<>();
        int lengthMatching = matching2D.getLength();
        switch(pairType) {
            case TRAJ_12:
                for (int l = 0; l < lengthMatching; l++) {
                    IntTriple triple = IntTriple.createIntTriple(matching2D.i[l] + leftEnd.i, matching2D.j[l] + leftEnd.j, leftEnd.k);
                    transformedTriples.add(triple);
                }
                break;
            case TRAJ_13:
                for (int l = 0; l < lengthMatching; l++) {
                    IntTriple triple = IntTriple.createIntTriple(matching2D.i[l] + leftEnd.i, leftEnd.j, matching2D.j[l] + leftEnd.k);
                    transformedTriples.add(triple);
                }
                break;
            case TRAJ_23:
                for (int l = 0; l < lengthMatching; l++) {
                    IntTriple triple = IntTriple.createIntTriple(leftEnd.i, matching2D.i[l] + leftEnd.j, matching2D.j[l] + leftEnd.k);
                    transformedTriples.add(triple);
                }
                break;
            default:
                throw new RuntimeException("Invalid pairtype = " + pairType);
        }
        return transformedTriples;
    }
    
}
