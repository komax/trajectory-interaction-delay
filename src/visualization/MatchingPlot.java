package visualization;

import frechet.Matching;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by max on 25-4-14.
 */
public class MatchingPlot extends JPanel {
    
    private static class Point2D {
        private final double x;
        private final double y;
        
        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }
        
        public double getX() {
            return x;
        }
        
        public double getY() {
            return y;
        }
    }
    
    private Point2D cartesianToPanelPoint(Point2D cartesianPoint, int width, int height) {
        double panelX = width - cartesianPoint.x / maxX * width;
        double panelY = height - cartesianPoint.y / maxY * height;
        return new Point2D(panelX, panelY);
    }
    
    private Point2D panelToCartesianPoint(Point2D panelPoint, int width, int height) {
        double cartesianX = panelPoint.x / width * maxX;
        double cartesianY = panelPoint.y / height * maxY;
        return new Point2D(cartesianX, cartesianY);
    }
    
    private final Matching matching;
    private ArrayList<Point2D> trajectory1;
    private ArrayList<Point2D> trajectory2;
    
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    public MatchingPlot(Matching matching) {
        super(new BorderLayout());

        // Store data to plot
        this.matching = matching;

        this.minX = Double.MAX_VALUE;
        this.minY = Double.MAX_VALUE;

        findMinOn(matching.getTrajectory1());
        findMinOn(matching.getTrajectory2());
        
        makeTrajectoriesNullbased();
        this.minX = 0.0;
        this.minY = 0.0;
        
        this.maxX = Double.MIN_VALUE;
        this.maxY = Double.MIN_VALUE;
        findMaxOn(trajectory1);
        findMaxOn(trajectory2);

        // TODO plot them
        // TODO Add panel to plot by calling add()
    }
    
    private void makeTrajectoriesNullbased() {
        double[][] t1 = matching.getTrajectory1();
        this.trajectory1 = new ArrayList<>();
        for (double[] point : t1) {
            trajectory1.add(new Point2D(point[0] - minX, point[1] - minY));
        }
        double[][] t2 = matching.getTrajectory2();
        this.trajectory2 = new ArrayList<>();
        for (double[] point : t2) {
            trajectory2.add(new Point2D(point[0] - minX, point[1] - minY));
        }
    }

    private void findMinOn(double[][] trajectory) {
        for (int i=0; i<trajectory.length; i++) {
            double xValue = trajectory[i][0];
            if (minX > xValue) {
                minX = xValue;
            }
            double yValue = trajectory[i][1];
            if (minY > yValue) {
                minY = yValue;
            }
        }
    }
    
    private void findMaxOn(ArrayList<Point2D> trajectory) {
        for (Point2D point : trajectory) {
            if (point.x > maxX) {
                maxX = point.x;
            }
            if (point.y > maxY) {
                maxY = point.y;
            }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(600, 300);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Dimension paintDimension = getSize();
        int width = paintDimension.width;
        int height = paintDimension.height;
        
        paintTrajectories(width, height);
        drawMatching(width, height);
        
    }

    private void paintTrajectories(int width, int height) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void drawMatching(int width, int height) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
