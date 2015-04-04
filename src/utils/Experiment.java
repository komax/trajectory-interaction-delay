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
import utils.distance.DistanceNorm;

/**
 *
 * @author max
 */
public class Experiment {
    public static enum MatchingType {
        FRECHET, ONE_TO_ONE
    }
    
    private DelaySpace delaySpace;
    private Trajectory trajectory1;
    private Trajectory trajectory2;
    
    public Experiment(String fileName, DelaySpaceType delaySpaceType, DistanceNorm distanceNorm) {
        try {
            TrajectoryReader reader = TrajectoryReader.createTrajectoryReader(fileName, true);
            this.trajectory1 = reader.getTrajectory1();
            this.trajectory2 = reader.getTrajectory2();
            this.delaySpace = DelaySpace.createDelaySpace(trajectory1, trajectory2, delaySpaceType, distanceNorm);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public Experiment(DelaySpace delaySpace) {
        this.delaySpace = delaySpace;
        this.trajectory1 = delaySpace.getTrajectory1();
        this.trajectory2 = delaySpace.getTrajectory2();
    }
    
    public Matching run(MatchingType matchingType) {
        Matching matching;
        switch (matchingType) {
            case FRECHET:
                matching = LocallyCorrectFrechet.compute(delaySpace, trajectory1, trajectory2);
                break;
            case ONE_TO_ONE:
                matching = Matching.createOneToOneMatching(trajectory1, trajectory2);
                break;
            default:
                throw new RuntimeException("This matching type is not allowed yet");
        }
        return matching;
    }
    
}
