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
    private double[][] trajectory1;
    private double[][] trajectory2;
    private final BufferedReader inputReader;
    
    public TrajectoryReader(String fileName) throws Exception {
        this.inputReader = new BufferedReader(new FileReader(fileName));
        
        // Read content of the file into the fields.
        readTrajectories();
    }
    
    private void readTrajectories() throws IOException {
        List<double[]> traject1 = new ArrayList<>();
        List<double[]> traject2 = new ArrayList<>();
        
        // Skip the first line.
        inputReader.readLine();
        
        String currentLine = "";
        
        // Read the lines and their data points.
        while ((currentLine = inputReader.readLine()) != null) {
            String[] lineElements = currentLine.split("\\s+");
            double[] pointsTraject1 = new double[2];
            double[] pointsTraject2 = new double[2];
            pointsTraject1[0] = Double.parseDouble(lineElements[1]);
            pointsTraject1[1] = Double.parseDouble(lineElements[2]);
            traject1.add(pointsTraject1);
            pointsTraject2[0] = Double.parseDouble(lineElements[3]);
            pointsTraject2[1] = Double.parseDouble(lineElements[4]);
            traject2.add(pointsTraject2);
        }
        
        int dimensions = 2;
        this.trajectory1 = convertTrajectoryRecursively(traject1, dimensions);
        this.trajectory2 = convertTrajectoryRecursively(traject2, dimensions);
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
    
    public double[][] getTrajectory1() {
        return trajectory1;
    }
    
    public double[][] getTrajectory2() {
        return trajectory2;
    }
    
}
