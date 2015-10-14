/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frechet;

import java.util.List;
import utils.IntTriple;
import utils.Trajectory;

/**
 *
 * @author max
 */
public class FrechetDistance2DTriplet {

    public static enum PairInTriple {
        TRAJ_12,
        TRAJ_23,
        TRAJ_13
    }
    
    private final Trajectory traject1;
    private final Trajectory traject2;
    private final Trajectory traject3;
    private final IntTriple leftEnd;
    private final int lengthX;
    private final int lengthY;
    private final int lengthZ;
    private final PairInTriple pairType;
    
    public FrechetDistance2DTriplet(IntTriple leftEnd, IntTriple rightEnd, Trajectory traject1, Trajectory traject2, Trajectory traject3) {        
        this.traject1 = traject1;
        this.traject2 = traject2;
        this.traject3 = traject3;
        
        this.leftEnd = leftEnd;
        
        this.lengthX = rightEnd.i - leftEnd.i  + 1;
        this.lengthY = rightEnd.j - leftEnd.j + 1;
        this.lengthZ = rightEnd.k - leftEnd.k + 1;
        
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private List<IntTriple> transform2DMatching(Matching matching2D) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
