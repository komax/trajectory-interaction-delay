/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author max
 */
public class TrajectoryReader {
    private Trajectory trajectory1;
    private Trajectory trajectory2;
    private final BufferedReader inputReader;
    
    public static TrajectoryReader createTrajectoryReader(String fileName, boolean skipHeader) throws Exception {
        return new TrajectoryReader(fileName, skipHeader);
    }
    
    public TrajectoryReader(String fileName, boolean skipHeader) throws Exception {
        this.inputReader = new BufferedReader(new FileReader(fileName));
        
        // Read content of the file into the fields.
        readTrajectories(skipHeader);
    }
    
    private void readTrajectories(boolean skipHeader) throws IOException {
        this.trajectory1 = new Trajectory();
        this.trajectory2 = new Trajectory();
        
        if (skipHeader) {
            // Skip the first line.
            inputReader.readLine();
        }
        
        String currentLine = "";
        
        // Read the lines and their data points.
        while ((currentLine = inputReader.readLine()) != null) {
            String[] lineElements = currentLine.split("\\s+");
            double[] pointsTraject1 = new double[2];
            double[] pointsTraject2 = new double[2];
            if (lineElements[0].equals("")) {
//                System.err.println(lineElements[1] + "|" + lineElements[2] + "|" + lineElements[3] + "|" + lineElements[4] + "|" + lineElements[5]);
                pointsTraject1[0] = Double.parseDouble(lineElements[2]);
                pointsTraject1[1] = Double.parseDouble(lineElements[3]);
                pointsTraject2[0] = Double.parseDouble(lineElements[4]);
                pointsTraject2[1] = Double.parseDouble(lineElements[5]);
            } else {
//                System.err.println(lineElements[0] + "|" + lineElements[1] + "|" + lineElements[2] + "|" + lineElements[3] + "|" + lineElements[4]);
                pointsTraject1[0] = Double.parseDouble(lineElements[1]);
                pointsTraject1[1] = Double.parseDouble(lineElements[2]);
                pointsTraject2[0] = Double.parseDouble(lineElements[3]);
                pointsTraject2[1] = Double.parseDouble(lineElements[4]);
            }
            trajectory1.addPoint(pointsTraject1);
            trajectory2.addPoint(pointsTraject2);
        }
    }
    
    private double[][] convertTrajectoryRecursively(List<double[]> traject, int dimensions) {
        int length = traject.size();
        double[][] result = new double[length][dimensions];
        
        for (int i = 0; i < length; i++) {
           double[] pointTraject = traject.get(i);
           result[i] = pointTraject;
        }
        
        return result;
    }
    
    public Trajectory getTrajectory1() {
        return trajectory1;
    }
    
    public Trajectory getTrajectory2() {
        return trajectory2;
    }
    
}
