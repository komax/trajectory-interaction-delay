/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package delayspace;

import utils.Trajectory;

/**
 *
 * @author max
 */
public final class DirectionalDistance extends DelaySpace {
    private final DelaySpace distanceDelaySpace;
    private final DelaySpace headingDelaySpace;

    public DirectionalDistance(Trajectory trajectory1, Trajectory trajectory2) {
        super(trajectory1, trajectory2);
        
        this.distanceDelaySpace = new DistanceDelaySpace(trajectory1, trajectory2);
        this.headingDelaySpace = new Heading(trajectory1, trajectory2);
    }

    @Override
    protected void computeDelaySpace() {
        distanceDelaySpace.computeDelaySpace();
        headingDelaySpace.computeDelaySpace();
        for (int i = 0; i < trajectory1.length(); i++) {
            for (int j = 0; j < trajectory2.length(); j++) {
                double directionalDistanceVal = distanceDelaySpace.get(i, j) * (headingDelaySpace.get(i, j) + 1.);
                delaySpace[i][j] = directionalDistanceVal;
            }
        }
    }

    @Override
    public boolean isDirectional() {
        return true;
    }
    
}
