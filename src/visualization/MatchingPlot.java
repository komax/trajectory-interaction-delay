package visualization;

import frechet.Matching;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import utils.Utils;

/**
 * Created by max on 25-4-14.
 */
public class MatchingPlot extends GenericPlottingPanel {
    private final Matching matching;
    private ArrayList<Point2D> trajectory1;
    private ArrayList<Point2D> trajectory2;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private int selectedIndex;
    private final int[] delaysInTimestamps;
    private int maxDelay;
    private final ColorMap positiveColors;
    private final ColorMap negativeColors;

    public MatchingPlot(Matching matching) {
        // Store data to plot
        this.matching = matching;
        this.selectedIndex = -1;

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
        
        this.delaysInTimestamps = utils.Utils.delayInTimestamps(matching);
        this.maxDelay = Integer.MIN_VALUE;
        for (int delay: delaysInTimestamps) {
            if (delay > maxDelay) {
                maxDelay = delay;
            }
        }
        this.positiveColors = ColorMap.createGrayToBlueColormap(0.0, maxDelay);
        this.positiveColors.halfColorSpectrum();
        this.positiveColors.halfColorSpectrum();
        this.positiveColors.halfColorSpectrum();
        this.negativeColors = ColorMap.createGrayToRedColormap(0.0, maxDelay);
        this.negativeColors.halfColorSpectrum();
        this.negativeColors.halfColorSpectrum();
        this.negativeColors.halfColorSpectrum();

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
    
    public void setSelectedIndex(int newIndex) {
        this.selectedIndex = newIndex;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 300);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        paintTrajectories(g);
        drawMatching(g);

    }

    private void paintTrajectories(Graphics g) {
        drawTrajectory(trajectory1, g);
        drawTrajectory(trajectory2, g);
    }

    private void drawTrajectory(List<Point2D> trajectory, Graphics g) {
        Point2D previousPoint = trajectory.get(0);
        Point2D transformedPreviousPoint = cartesianToPanelPoint(previousPoint);
        for (int i = 1; i < trajectory.size(); i++) {
            Point2D currentPoint = trajectory.get(i);
            Point2D transformedCurrentPoint = cartesianToPanelPoint(currentPoint);
            int fromX = roundDouble(transformedPreviousPoint.x);
            int fromY = roundDouble(transformedPreviousPoint.y);
            int toX = roundDouble(transformedCurrentPoint.x);
            int toY = roundDouble(transformedCurrentPoint.y);
            g.drawLine(fromX, fromY, toX, toY);

            transformedPreviousPoint = transformedCurrentPoint;
        }
    }

    private void drawMatching(Graphics g) {
        int lengthMatching = matching.i.length;
        int startIndexTraject1 = matching.i[0];
        int startIndexTraject2 = matching.j[0];
        int endIndexTraject1 = startIndexTraject1;
        int endIndexTraject2 = startIndexTraject2;
        boolean[] isTraject1Ahead = Utils.trajectroy1IsAhead(matching);
        boolean[] isTraject2Ahead = Utils.trajectroy2IsAhead(matching);
        for (int k = 1; k <= lengthMatching; k++) {
            int currentIndexTraject1;
            int currentIndexTraject2;
            if (k == lengthMatching) {
                currentIndexTraject1 = -1;
                currentIndexTraject2 = -1;
            } else {
                currentIndexTraject1 = matching.i[k];
                currentIndexTraject2 = matching.j[k];
            }
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
                    // Choose correct color from the colormaps.
                    Color chosenColor;
                    int delay = delaysInTimestamps[k-1];
                    if (isTraject1Ahead[k-1]) {
                        chosenColor = positiveColors.getColor(delay);
                    } else if (isTraject2Ahead[k-1]) {
                        chosenColor = negativeColors.getColor(delay);
                    } else {
                        chosenColor = Color.GRAY;
                    }
                    g.setColor(chosenColor);
                    // 3a. Draw a simple line.
                    Point2D pointTraj1 = trajectory1.get(startIndexTraject1);
                    Point2D convPointTraj1 = cartesianToPanelPoint(pointTraj1);
                    Point2D pointTraj2 = trajectory2.get(startIndexTraject2);
                    Point2D convPointTraj2 = cartesianToPanelPoint(pointTraj2);
                    g.drawLine(roundDouble(convPointTraj1.x), roundDouble(convPointTraj1.y),
                            roundDouble(convPointTraj2.x), roundDouble(convPointTraj2.y));
                } else {
                    // Compute the color interpolation of the patch.
                    if (startIndexTraject1 == endIndexTraject1) {
                        Point2D pointTraject1 = cartesianToPanelPoint(trajectory1.get(startIndexTraject1));
                        int traject1PointX = roundDouble(pointTraject1.x);
                        int traject1PointY = roundDouble(pointTraject1.y);
           
                        Point2D startTraject2 = cartesianToPanelPoint(trajectory2.get(startIndexTraject2));
                        int startTraject2X = roundDouble(startTraject2.x);
                        int startTraject2Y = roundDouble(startTraject2.y);
                        int startIndex = Utils.findMatchingIndex(matching, startIndexTraject1, startIndexTraject2);
                        int startDelay = delaysInTimestamps[startIndex];
                        Color beginColor = negativeColors.getColor(startDelay);
                        
                        Point2D endTraject2 = cartesianToPanelPoint(trajectory2.get(endIndexTraject2));
                        int endTraject2X = roundDouble(endTraject2.x);
                        int endTraject2Y = roundDouble(endTraject2.y);
                        int endIndex = Utils.findMatchingIndex(matching, endIndexTraject1, endIndexTraject2);
                        int endDelay = delaysInTimestamps[endIndex];
                        Color endColor = negativeColors.getColor(endDelay);
                        
                        GradientPaint gradientPaint = new GradientPaint(startTraject2X, startTraject2Y, beginColor, endTraject2X, endTraject2Y, endColor);
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setPaint(gradientPaint);
                    } else {
                        Point2D pointTraject2 = cartesianToPanelPoint(trajectory2.get(startIndexTraject2));
                        int traject2PointX = roundDouble(pointTraject2.x);
                        int traject2PointY = roundDouble(pointTraject2.y);
           
                        Point2D startTraject1 = cartesianToPanelPoint(trajectory1.get(startIndexTraject1));
                        int startTraject1X = roundDouble(startTraject1.x);
                        int startTraject1Y = roundDouble(startTraject1.y);
                        int startIndex = Utils.findMatchingIndex(matching, startIndexTraject1, startIndexTraject2);
                        int startDelay = delaysInTimestamps[startIndex];
                        Color beginColor = positiveColors.getColor(startDelay);
                        
                        Point2D endTraject1 = cartesianToPanelPoint(trajectory1.get(endIndexTraject1));
                        int endTraject1X = roundDouble(endTraject1.x);
                        int endTraject1Y = roundDouble(endTraject1.y);
                        int endIndex = Utils.findMatchingIndex(matching, endIndexTraject1, endIndexTraject2);
                        int endDelay = delaysInTimestamps[endIndex];
                        Color endColor = positiveColors.getColor(endDelay);
                        
                        GradientPaint gradientPaint = new GradientPaint(startTraject1X, startTraject1Y, beginColor, endTraject1X, endTraject1Y, endColor);
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setPaint(gradientPaint);
                    }
                    // 3b. Build the polygon.
                    Polygon ribbon = new Polygon();
                    for (int l = startIndexTraject1; l <= endIndexTraject1; l++) {
                        Point2D point = trajectory1.get(l);
                        Point2D convertedPoint = cartesianToPanelPoint(point);
                        ribbon.addPoint(roundDouble(convertedPoint.x), roundDouble(convertedPoint.y));
                    }
                    for (int l = startIndexTraject2; l <= endIndexTraject2; l++) {
                        Point2D point = trajectory2.get(l);
                        Point2D convertedPoint = cartesianToPanelPoint(point);
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
        if (selectedIndex >= 0) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));
            g.setColor(Color.red);
            drawPoints(g, selectedIndex);
        }
    }
    
    private void drawPoints(Graphics g, int index) {
        int diameter = 6;
        int radius = diameter / 2;
        Point2D pointTraject1 = trajectory1.get(matching.i[index]);
        Point2D panelPoint = cartesianToPanelPoint(pointTraject1);
        g.drawOval(roundDouble(panelPoint.x) - radius, roundDouble(panelPoint.y) - radius, diameter, diameter);
        
        Point2D pointTraject2 = trajectory2.get(matching.j[index]);
        panelPoint = cartesianToPanelPoint(pointTraject2);
        g.drawOval(roundDouble(panelPoint.x) - radius, roundDouble(panelPoint.y) - radius, diameter, diameter);
    }

    @Override
    public double maxX() {
        return this.maxX;
    }

    @Override
    public double maxY() {
        return this.maxY;
    }

}
