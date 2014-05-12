package visualization;

import frechet.Matching;

import javax.swing.*;
import java.awt.*;

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
    private final double[][] trajectory1;
    private final double[][] trajectory2;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    public MatchingPlot(Matching matching) {
        super(new BorderLayout());

        // Store data to plot
        this.matching = matching;
        this.trajectory1 = matching.getTrajectory1();
        this.trajectory2 = matching.getTrajectory2();

        this.minX = Double.MAX_VALUE;
        this.maxX = Double.MIN_VALUE;

        this.minY = Double.MAX_VALUE;
        this.maxY = Double.MIN_VALUE;

        findMinAndMaxOn(trajectory1);
        findMinAndMaxOn(trajectory2);

        // TODO plot them
        // TODO Add panel to plot by calling add()
    }

    private void findMinAndMaxOn(double[][] trajectory) {
        for (int i=0; i<trajectory.length; i++) {
            double xValue = trajectory[i][0];
            if (minX > xValue) {
                minX = xValue;
            } else if (maxX < xValue) {
                maxX = xValue;
            }
            double yValue = trajectory[i][1];
            if (minY > yValue) {
                minY = yValue;
            } else if (maxY < yValue) {
                maxY = yValue;
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
