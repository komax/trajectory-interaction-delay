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
    // TODO Debug for all subclasses and its results.
    
    protected final DistanceNorm distance;
    protected final DelaySpaceType delaySpaceType; // FIXME Drop this ugly field.
    protected final double[][] delaySpace;
    protected final double[][] trajectory1;
    protected final double[][] trajectory2;
    
    public static final double ALPHA = 1.0;
    
    public static DelaySpace createDelaySpace(double[][] trajectory1, double[][] trajectory2, DelaySpaceType delayType, DistanceNormType normType) {
        switch(delayType) {
            case USUAL:
                return new DistanceDelaySpace(trajectory1, trajectory2, normType);
            case HEADING:
                return new Heading(trajectory1, trajectory2);
            case DIRECTIONAL_DISTANCE:
                return new DirectionalDistance(trajectory1, trajectory2, normType);
            case DYNAMIC_INTERACTION:
                return new DynamicInteraction(trajectory1, trajectory2, normType, ALPHA);
            case DISPLACEMENT:
                return new Displacement(trajectory1, trajectory2, normType, ALPHA);
            default:
                throw new RuntimeException("This delayspace type is not supported: " + delayType);
        }
    }
    
    public DelaySpace(double[][] trajectory1, double[][] trajectory2, DelaySpaceType delayType, DistanceNormType normType) {
        this.distance = DistanceNormFactory.getDistanceNorm(normType);
        this.delaySpaceType = delayType;
        
        this.trajectory1 = trajectory1;
        this.trajectory2 = trajectory2;
        
        int lengthTraject1 = trajectory1.length;
        int lengthTraject2 = trajectory2.length;
        
        this.delaySpace = new double[lengthTraject1][lengthTraject2];
        clear();
    }
    
    public final void clear() {
        for (int i = 0; i < numberRows(); i++) {
            for (int j = 0; j < numberColumns(); j++) {
                delaySpace[i][j] = Double.NaN;
            }
        }
    }
    
    protected abstract void computeDelaySpace();
    
    public double get(int i, int j) {
        return delaySpace[i][j];
    }
    
    public int numberRows() {
        return delaySpace.length;
    }
    
    public int numberColumns() {
        return delaySpace[0].length;
    }
    
    public double getMinValue() {
        double minValue = Double.MAX_VALUE;
        
        for (int i = 0; i < numberRows(); i++) {
            for (int j = 0; j < numberColumns(); j++) {
                minValue = Math.min(minValue, delaySpace[i][j]);
            }
        }
        return minValue;
    }
    
    public double getMaxValue() {
        double maxValue = Double.MIN_VALUE;
        
        for (int i = 0; i < numberRows(); i++) {
            for (int j = 0; j < numberColumns(); j++) {
                maxValue = Math.max(maxValue, delaySpace[i][j]);
            }
        }
        return maxValue;
    }
    
    public double[][] getTrajectory1() {
        return trajectory1;
    }
    
    public double[][] getTrajectory2() {
        return trajectory2;
    }
    
    public DelaySpaceType getType() {
        return delaySpaceType;
    }
    
    public boolean isDirectional() {
        return delaySpaceType.isDirectional();
    }
}
