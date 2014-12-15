/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import delayspace.DelaySpace;
import delayspace.DelaySpaceType;
import frechet.LocallyCorrectFrechet;
import frechet.Matching;
import utils.distance.DistanceNormType;

/**
 *
 * @author max
 */
public class Experiment {
    private DelaySpace delaySpace;
    private double[][] trajectory1;
    private double[][] trajectory2;
    
    public Experiment(String fileName, DelaySpaceType delaySpaceType, DistanceNormType dNormType) {
        try {
            TrajectoryReader reader = TrajectoryReader.createTrajectoryReader(fileName, true);
            this.trajectory1 = reader.getTrajectory1();
            this.trajectory2 = reader.getTrajectory2();
            this.delaySpace = DelaySpace.createDelaySpace(trajectory1, trajectory2, delaySpaceType, dNormType);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public Experiment(DelaySpace delaySpace) {
        this.delaySpace = delaySpace;
        this.trajectory1 = delaySpace.getTrajectory1();
        this.trajectory2 = delaySpace.getTrajectory2();
    }
    
    public Matching run() {
        int lengthTraject1 = trajectory1.length;
        int lengthTraject2 = trajectory2.length;
        Matching matching = LocallyCorrectFrechet.compute(delaySpace.getDelaySpace(), trajectory1, trajectory2, lengthTraject1, lengthTraject2);
        return matching;
    }
    
}
