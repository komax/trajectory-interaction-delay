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
    
    private static int roundDouble(double number) {
        return (int) Math.round(number);
    }

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
        double panelX = cartesianPoint.x / maxX * width;
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
        setBackground(Color.WHITE);

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
        for (int i = 0; i < trajectory.length; i++) {
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

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 300);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);

        Dimension paintDimension = getSize();
        int width = paintDimension.width;
        int height = paintDimension.height;

        paintTrajectories(g, width, height);
        drawMatching(g, width, height);

    }

    private void paintTrajectories(Graphics g, int width, int height) {
        drawTrajectory(trajectory1, g, width, height);
        drawTrajectory(trajectory2, g, width, height);
    }

    private void drawTrajectory(List<Point2D> trajectory, Graphics g, int width, int height) {
        Point2D previousPoint = trajectory.get(0);
        Point2D transformedPreviousPoint = cartesianToPanelPoint(previousPoint, width, height);
        for (int i = 1; i < trajectory.size(); i++) {
            Point2D currentPoint = trajectory.get(i);
            Point2D transformedCurrentPoint = cartesianToPanelPoint(currentPoint, width, height);
            int fromX = roundDouble(transformedPreviousPoint.x);
            int fromY = roundDouble(transformedPreviousPoint.y);
            int toX = roundDouble(transformedCurrentPoint.x);
            int toY = roundDouble(transformedCurrentPoint.y);
            g.drawLine(fromX, fromY, toX, toY);

            transformedPreviousPoint = transformedCurrentPoint;
        }
    }

    private void drawMatching(Graphics g, int width, int height) {
        int lengthMatching = matching.i.length;
        int startIndexTraject1 = matching.i[0];
        int startIndexTraject2 = matching.j[0];
        int endIndexTraject1 = startIndexTraject1;
        int endIndexTraject2 = startIndexTraject2;
        for (int k = 1; k < lengthMatching; k++) {
            int currentIndexTraject1 = matching.i[k];
            int currentIndexTraject2 = matching.j[k];
            final boolean singleIndexTraject1 = startIndexTraject1 == endIndexTraject1;
            final boolean singleIndexTraject2 = startIndexTraject2 == endIndexTraject2;
            if (singleIndexTraject1 && startIndexTraject1 == currentIndexTraject1) {
                // 1. ribbon case
                endIndexTraject2 = currentIndexTraject2;
            } else if (singleIndexTraject2 && startIndexTraject2 == currentIndexTraject2) {
                // 2. the other way round
                endIndexTraject1 = currentIndexTraject1;
            } else {
                // 3. Drawing part.
                g.setColor(Color.blue);
                if (singleIndexTraject1 && singleIndexTraject2) {
                    // 3a. Draw a simple line.
                    Point2D pointTraj1 = trajectory1.get(startIndexTraject1);
                    Point2D convPointTraj1 = cartesianToPanelPoint(pointTraj1, width, height);
                    Point2D pointTraj2 = trajectory2.get(startIndexTraject2);
                    Point2D convPointTraj2 = cartesianToPanelPoint(pointTraj2, width, height);
                    g.drawLine(roundDouble(convPointTraj1.x), roundDouble(convPointTraj1.y),
                            roundDouble(convPointTraj2.x), roundDouble(convPointTraj2.y));
                } else {
                    // 3b. Build the polygon.
                    Polygon ribbon = new Polygon();
                    for (int l = startIndexTraject1; l <= endIndexTraject1; l++) {
                        Point2D point = trajectory1.get(l);
                        Point2D convertedPoint = cartesianToPanelPoint(point, width, height);
                        ribbon.addPoint(roundDouble(convertedPoint.x), roundDouble(convertedPoint.y));
                    }
                    for (int l = startIndexTraject2; l <= endIndexTraject2; l++) {
                        Point2D point = trajectory2.get(l);
                        Point2D convertedPoint = cartesianToPanelPoint(point, width, height);
                        ribbon.addPoint(roundDouble(convertedPoint.x), roundDouble(convertedPoint.y));
                    }
                    // Draw the polygon.
                    g.fillPolygon(ribbon);
                }
                // Reset the indices.
                startIndexTraject1 = endIndexTraject1 = currentIndexTraject1;
                startIndexTraject2 = endIndexTraject2 = currentIndexTraject2;
            }
        }
    }

}
