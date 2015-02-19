/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package delayspace;

import utils.Utils;
import utils.distance.DistanceNorm;
import utils.distance.DistanceNormFactory;
import utils.distance.DistanceNormType;

/**
 *
 * @author max
 */
public abstract class DelaySpace {
    // TODO Debug for all subclasses and its results.
    
    protected DistanceNorm distance;
    protected final double[][] delaySpace;
    protected final double[][] trajectory1;
    protected final double[][] trajectory2;
    protected double alpha;
    
    public static DelaySpace createDelaySpace(double[][] trajectory1, double[][] trajectory2, DelaySpaceType delayType, DistanceNorm distanceNorm) {
        DelaySpace delaySpace = null;
        double alpha = 1.0;
        switch(delayType) {
            case USUAL:
                delaySpace = new DistanceDelaySpace(trajectory1, trajectory2);
                break;
            case HEADING:
                delaySpace = new Heading(trajectory1, trajectory2);
                break;
            case DIRECTIONAL_DISTANCE:
                delaySpace = new DirectionalDistance(trajectory1, trajectory2);
                break;
            case DYNAMIC_INTERACTION:
                delaySpace = new DynamicInteraction(trajectory1, trajectory2, alpha);
                break;
            case DISPLACEMENT:
                delaySpace = new Displacement(trajectory1, trajectory2, alpha);
                break;
            default:
                throw new RuntimeException("This delayspace type is not supported: " + delayType);
        }
        delaySpace.setDistanceNorm(distanceNorm);
        delaySpace.computeDelaySpace();
        return delaySpace;
    }
    
    public DelaySpace(double[][] trajectory1, double[][] trajectory2) {
        this.distance = DistanceNormFactory.EuclideanDistance;
        this.alpha = Double.NaN;
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
    
    public void setDistanceNorm(DistanceNorm distanceNorm) {
        this.distance = distanceNorm;
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
    
    public abstract boolean isDirectional();
}
