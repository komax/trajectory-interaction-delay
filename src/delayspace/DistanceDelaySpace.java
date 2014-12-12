/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package delayspace;

import utils.distance.DistanceNormType;

/**
 *
 * @author max
 */
public final class DistanceDelaySpace extends DelaySpace {
    
    public DistanceDelaySpace(double[][] trajectory1, double[][] trajectory2, DistanceNormType normType) {
        super(trajectory1, trajectory2, DelaySpaceType.USUAL, normType);
        computeDelaySpace();
    }

    @Override
    protected void computeDelaySpace() {
        for (int i = 0; i < trajectory1.length; i++) {
            for (int j = 0; j < trajectory2.length; j++) {
                double[] pointP = trajectory1[i];
                double[] pointQ = trajectory2[j];
                double distanceValue = distance.distance(pointP, pointQ);
                delaySpace[i][j] = distanceValue;
            }
        }
    }
    
}
