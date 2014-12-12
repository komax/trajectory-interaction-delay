/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package delayspace;

import static utils.Utils.computeDisplacement;
import static utils.Utils.computeDistanceOnMovementVector;
import utils.distance.DistanceNormType;

/**
 *
 * @author max
 */
public final class Displacement extends DelaySpace {
    private final double alpha;

    public Displacement(double[][] trajectory1, double[][] trajectory2, DistanceNormType normType, double alpha) {
        super(trajectory1, trajectory2, DelaySpaceType.DISPLACEMENT, normType);
        this.alpha = alpha;
        computeDelaySpace();
    }

    @Override
    protected void computeDelaySpace() {
        for (int i = 0; i < trajectory1.length; i++) {
            for (int j = 0; j < trajectory2.length; j++) {
                double[] pointI = trajectory1[i];
                double[] pointJ = trajectory2[j];
                double[] followPointI;
                double[] followPointJ;
                
                if (i < (trajectory1.length - 1)) {
                    followPointI = trajectory1[i + 1];
                } else {
                    followPointI = trajectory1[trajectory1.length -1];
                }
                if (j < (trajectory2.length - 1)) {
                    followPointJ = trajectory2[j + 1];
                } else {
                    followPointJ = trajectory2[trajectory2.length -1];
                }
                double distanceI = computeDistanceOnMovementVector(pointI, followPointI, distance);
                double distanceJ = computeDistanceOnMovementVector(pointJ, followPointJ, distance);
                double displacementValue = computeDisplacement(distanceI, distanceJ, alpha);
            }
        }
    }
    
}
