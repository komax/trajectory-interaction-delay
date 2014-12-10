/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package delayspace;

import utils.distance.DistanceNorm;
import utils.distance.DistanceNormFactory;
import utils.distance.DistanceNormType;

/**
 *
 * @author max
 */
public class DelaySpace {
    
    private final DistanceNorm distance;
    private final DelaySpaceType delaySpaceType;
    private final double[][] delaySpace;
    
    public DelaySpace(double[][] trajectory1, double[][] trajectory2, DelaySpaceType delayType, DistanceNormType normType) {
        this.distance = DistanceNormFactory.getDistanceNorm(normType);
        this.delaySpaceType = delayType;
        
        int lengthTraject1 = trajectory1.length;
        int lengthTraject2 = trajectory2.length;
        
        this.delaySpace = new double[lengthTraject1][lengthTraject2];
        // TODO Compute the values for the delay space.
    }
    
    public double[][] getDelaySpace() {
        return delaySpace;
    }
    
}
