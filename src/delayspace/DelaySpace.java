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
public abstract class DelaySpace {
    
    protected final DistanceNorm distance;
    protected final DelaySpaceType delaySpaceType;
    protected final double[][] delaySpace;
    protected final double[][] trajectory1;
    protected final double[][] trajectory2;
    
    public static DelaySpace createDelaySpace(double[][] trajectory1, double[][] trajectory2, DelaySpaceType delayType, DistanceNormType normType) {
        return null;
    }
    
    public DelaySpace(double[][] trajectory1, double[][] trajectory2, DelaySpaceType delayType, DistanceNormType normType) {
        this.distance = DistanceNormFactory.getDistanceNorm(normType);
        this.delaySpaceType = delayType;
        
        this.trajectory1 = trajectory1;
        this.trajectory2 = trajectory2;
        
        int lengthTraject1 = trajectory1.length;
        int lengthTraject2 = trajectory2.length;
        
        this.delaySpace = new double[lengthTraject1][lengthTraject2];
    }
    
    protected abstract void computeDelaySpace();
    
    public double[][] getDelaySpace() {
        return delaySpace;
    }
    
}
