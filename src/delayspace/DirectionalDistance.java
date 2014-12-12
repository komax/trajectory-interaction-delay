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
public final class DirectionalDistance extends DelaySpace {
    private final double[][] distanceDelaySpace;
    private final double[][] headingDelaySpace;

    public DirectionalDistance(double[][] trajectory1, double[][] trajectory2, DistanceNormType normType) {
        super(trajectory1, trajectory2, DelaySpaceType.DIRECTIONAL_DISTANCE, normType);
        
        this.distanceDelaySpace = (new DistanceDelaySpace(trajectory1, trajectory2, normType)).delaySpace;
        this.headingDelaySpace = (new Heading(trajectory1, trajectory2)).delaySpace;
        computeDelaySpace();
    }

    @Override
    protected void computeDelaySpace() {
        for (int i = 0; i < trajectory1.length; i++) {
            for (int j = 0; j < trajectory2.length; j++) {
                double directionalDistanceVal = distanceDelaySpace[i][j] * (headingDelaySpace[i][j] + 1.);
                delaySpace[i][j] = directionalDistanceVal;
            }
        }
    }
    
}
