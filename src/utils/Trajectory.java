/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author max
 */
public class Trajectory implements Iterable<double[]> {
    public static final Trajectory EMPTY_TRAJECTORY = new Trajectory();
    
    private final List<double[]> entries;
    
    public Trajectory() {
        this.entries = new ArrayList<>();
    }
    
    public void addPoint(double[] point) {
        this.entries.add(point);
    }
    
    public double[] getPoint(int index) {
        return entries.get(index);
    }
    
    public DoublePoint2D getPointObject(int index) {
        double[] point = getPoint(index);
        return new DoublePoint2D(point[0], point[1]);
    }

    @Override
    public Iterator<double[]> iterator() {
        return entries.iterator();
    }
    
    public int length() {
        return entries.size();
    }
    
    public Trajectory subtrajectory(int startIndex, int endIndex) {
        Trajectory subtraj = new Trajectory();
        subtraj.entries.addAll(entries.subList(startIndex, endIndex + 1));
        return subtraj;
    }
}
