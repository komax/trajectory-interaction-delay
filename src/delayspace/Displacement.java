/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package delayspace;

import utils.Trajectory;
import static utils.Utils.computeDisplacement;
import static utils.Utils.computeDistanceOnMovementVector;

/**
 *
 * @author max
 */
public final class Displacement extends DelaySpace {

    public Displacement(Trajectory trajectory1, Trajectory trajectory2, double alpha) {
        super(trajectory1, trajectory2);
        this.alpha = alpha;
    }

    @Override
    protected void computeDelaySpace() {
        for (int i = 0; i < trajectory1.length(); i++) {
            for (int j = 0; j < trajectory2.length(); j++) {
                double[] pointI = trajectory1.getPoint(i);
                double[] pointJ = trajectory2.getPoint(j);
                double[] followPointI;
                double[] followPointJ;
                
                if (i < (trajectory1.length() - 1)) {
                    followPointI = trajectory1.getPoint(i + 1);
                } else {
                    followPointI = trajectory1.getPoint(trajectory1.length() - 1);
                }
                if (j < (trajectory2.length() - 1)) {
                    followPointJ = trajectory2.getPoint(j + 1);
                } else {
                    followPointJ = trajectory2.getPoint(trajectory2.length() - 1);
                }
                double distanceI = computeDistanceOnMovementVector(pointI, followPointI, distance);
                double distanceJ = computeDistanceOnMovementVector(pointJ, followPointJ, distance);
                double displacementValue = computeDisplacement(distanceI, distanceJ, alpha);
                delaySpace[i][j] = displacementValue;
            }
        }
    }

    @Override
    public boolean isDirectional() {
        return false;
    }
    
}
