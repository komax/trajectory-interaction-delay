/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package triplet;

import frechet.TripleFrechetMatching;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.IntTriple;
import utils.Trajectory;
import utils.TripletTrajectoryReader;

/**
 *
 * @author max
 */
public class TripletMatchingMain {
    
    public static final String PATH_TO_TRAJ_DATA = "/home/max/Documents/phd/flock_pigeon_data/homing_pigeons_2-4_sample.txt";
    private Trajectory trajectory1;
    private Trajectory trajectory2;
    private Trajectory trajectory3;

    public TripletMatchingMain() throws Exception {
        readTrajectories(PATH_TO_TRAJ_DATA);
    }
    
    private void readTrajectories(String filename) throws Exception {
        TripletTrajectoryReader reader = TripletTrajectoryReader.createTrajectoryReader(filename, true);
        this.trajectory1 = reader.getTrajectory1();
        this.trajectory2 = reader.getTrajectory2();
        this.trajectory3 = reader.getTrajectory3();
    }
    
    private List<IntTriple> computeMatching() {
        TripleFrechetMatching matching = new TripleFrechetMatching(trajectory1, trajectory2, trajectory3);
        return matching.compute();
    }
    
    private void printMatching(List<IntTriple> matching) {
        StringBuilder builder = new StringBuilder("Matching( (i,j,k)\n");
        for (IntTriple indices : matching) {
            builder.append("(");
            builder.append(indices.i);
            builder.append(", ");
            builder.append(indices.j);
            builder.append(", ");
            builder.append(indices.k);
            builder.append(")\n"); 
        }
        builder.append("\n)");
        System.out.println(builder.toString());
    }
    
    public static void main(String[] args) {
        TripletMatchingMain tmm = null;
        try {
            tmm = new TripletMatchingMain();
        } catch (Exception ex) {
            Logger.getLogger(TripletMatchingMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        List<IntTriple> matching = tmm.computeMatching();
        tmm.printMatching(matching);
    }
    
}
