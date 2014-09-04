package visualization;

import frechet.Matching;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import utils.Utils;

/**
 * Created by max on 25-4-14.
 */
public final class MatchingPlot extends GenericPlottingPanel {
    private static final int TRANSLUCENT_ALPHA = 50;
    
    private Matching matching;
    private ArrayList<Point2D> trajectory1;
    private ArrayList<Point2D> trajectory2;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private int selectedIndex;
    private int[] delaysInTimestamps;
    private int maxDelay;
    private ColorMap positiveColors;
    private ColorMap negativeColors;
    private boolean[] isTraject1Ahead;
    private boolean[] isTraject2Ahead;
    private int thresholdDelay;
    private int translucentFocus;
    private int startFocusTraject1;
    private int endFocusTraject1;
    private int startFocusTraject2;
    private int endFocusTraject2;

    public MatchingPlot(Matching matching, int thresholdDelay, int translucentFocus) {
        // Store data to plot
        this.selectedIndex = -1;
        updateMatching(matching, thresholdDelay, translucentFocus);
    }
    
    public void updateMatching(Matching matching, int thresholdDelay, int translucentFocus) {
        this.matching = matching;
        this.thresholdDelay = thresholdDelay;
        this.translucentFocus = translucentFocus;
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
        this.isTraject1Ahead = Utils.trajectroy1IsAhead(matching, thresholdDelay);
        this.isTraject2Ahead = Utils.trajectroy2IsAhead(matching, thresholdDelay);
        this.maxDelay = Integer.MIN_VALUE;
        for (int delay: delaysInTimestamps) {
            if (delay > maxDelay) {
                maxDelay = delay;
            }
        }
        this.positiveColors = ColorMap.createGrayToBlueTransparentColormap(thresholdDelay, maxDelay);
        this.positiveColors.halfColorSpectrum();
        this.positiveColors.halfColorSpectrum();
      //  this.positiveColors.halfColorSpectrum();
        this.negativeColors = ColorMap.createGrayToRedTransparentColormap(thresholdDelay, maxDelay);
        this.negativeColors.halfColorSpectrum();
       // this.negativeColors.halfColorSpectrum();
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
        int halfRange = translucentFocus / 2;
        int startIndex;
        if (newIndex < halfRange) {
            startIndex = 0;
        } else {
            startIndex = newIndex - halfRange;
        }
        
        int endIndex;
        if (newIndex + halfRange < matching.i.length) {
            endIndex = newIndex + halfRange;
        } else {
            endIndex = matching.i.length - 1;
        }
        this.startFocusTraject1 = matching.i[startIndex];
        this.endFocusTraject1 = matching.i[endIndex];
        this.startFocusTraject2 = matching.j[startIndex];
        this.endFocusTraject2 = matching.j[endIndex];
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
    
    private void setTranslucentColor(Graphics g, Color color) {
        Color translucentColor = ColorMap.getColorFromRGB(color.getRGB(), TRANSLUCENT_ALPHA);
        g.setColor(translucentColor);
    }

    private void paintTrajectories(Graphics g) {
        drawTrajectory(trajectory1, g);
        drawTrajectory(trajectory2, g);
    }

    private void drawTrajectory(List<Point2D> trajectory, Graphics g) {
        Point2D previousPoint = trajectory.get(0);
        Point2D transformedPreviousPoint = cartesianToPanelPoint(previousPoint);
        for (int i = 1; i < trajectory.size(); i++) {
            if (trajectory == trajectory1) {
                if (i < startFocusTraject1 || i > endFocusTraject1) {
                    setTranslucentColor(g, Color.black);
                } else {
                    g.setColor(Color.black);
                }
            } else if (trajectory == trajectory2) {
                if (i < startFocusTraject2 || i > endFocusTraject2) {
                    setTranslucentColor(g, Color.black);
                } else {
                    g.setColor(Color.black);
                }                
            }
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
                        chosenColor = positiveColors.getMinColor();
                    }
                    if (currentIndexTraject1 < startFocusTraject1 || currentIndexTraject1 > endFocusTraject1) {
                        setTranslucentColor(g, chosenColor);
                    } else if (currentIndexTraject2 < startFocusTraject2 || currentIndexTraject2 > endFocusTraject2) {
                        setTranslucentColor(g, chosenColor);
                    } else {
                        g.setColor(chosenColor);
                    }
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
                        Color beginColor;
                        if (startDelay < thresholdDelay) {
                            beginColor = negativeColors.getMinColor();
                        } else {
                            beginColor = negativeColors.getColor(startDelay);
                        }
                        
                        Point2D endTraject2 = cartesianToPanelPoint(trajectory2.get(endIndexTraject2));
                        int endTraject2X = roundDouble(endTraject2.x);
                        int endTraject2Y = roundDouble(endTraject2.y);
                        int endIndex = Utils.findMatchingIndex(matching, endIndexTraject1, endIndexTraject2);
                        int endDelay = delaysInTimestamps[endIndex];
                        Color endColor;
                        if (endDelay < thresholdDelay) {
                            endColor = negativeColors.getMinColor();
                        } else {
                            endColor = negativeColors.getColor(endDelay);
                        }
                        
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
                        Color beginColor;
                        if (startDelay < thresholdDelay) {
                            beginColor = positiveColors.getMinColor();
                        } else {
                            beginColor = positiveColors.getColor(startDelay);
                        }
                        
                        Point2D endTraject1 = cartesianToPanelPoint(trajectory1.get(endIndexTraject1));
                        int endTraject1X = roundDouble(endTraject1.x);
                        int endTraject1Y = roundDouble(endTraject1.y);
                        int endIndex = Utils.findMatchingIndex(matching, endIndexTraject1, endIndexTraject2);
                        int endDelay = delaysInTimestamps[endIndex];
                        Color endColor;
                        if (endDelay < thresholdDelay) {
                            endColor = positiveColors.getMinColor();
                        } else {
                            endColor = positiveColors.getColor(endDelay);
                        }
                        
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
            drawPoints(g, selectedIndex);
        }
    }
    
    private void drawPoints(Graphics g, int index) {
        Graphics2D g2 = (Graphics2D) g;
        int diameter = 6;
        int offset = diameter / 2;
        
        Point2D pointTraject1 = trajectory1.get(matching.i[index]);
        Point2D panelPoint = cartesianToPanelPoint(pointTraject1);
        int panelX = roundDouble(panelPoint.x);
        int panelY = roundDouble(panelPoint.y);
        if (isTraject1Ahead[index]) {
            Color maxColor = positiveColors.getMaxColor();
            g2.setStroke(new BasicStroke(5));
            g.setColor(maxColor);
            Polygon triangle = new Polygon();
            triangle.addPoint(panelX, panelY + offset);
            triangle.addPoint(panelX - offset, panelY - offset);
            triangle.addPoint(panelX + offset, panelY - offset);
            g.drawPolygon(triangle);
        } else {
            g2.setStroke(new BasicStroke(5));
            g.setColor(Color.lightGray);
            g.drawOval(panelX - offset, panelY - offset, diameter, diameter);
            g2.setStroke(new BasicStroke(3));
            g.setColor(Color.white);
            g.drawOval(panelX - offset, panelY - offset, diameter, diameter);
        }
        
        
        Point2D pointTraject2 = trajectory2.get(matching.j[index]);
        panelPoint = cartesianToPanelPoint(pointTraject2);
        panelX = roundDouble(panelPoint.x);
        panelY = roundDouble(panelPoint.y);
        if (isTraject2Ahead[index]) {
            Color maxColor = negativeColors.getMaxColor();
            g2.setStroke(new BasicStroke(5));
            g.setColor(maxColor);
            g.drawRect(panelX - offset, panelY - offset, diameter, diameter);
        } else {
            g2.setStroke(new BasicStroke(5));
            g.setColor(Color.lightGray);
            g.drawOval(panelX - offset, panelY - offset, diameter, diameter);
            g2.setStroke(new BasicStroke(3));
            g.setColor(Color.white);
            g.drawOval(panelX - offset, panelY - offset, diameter, diameter);
        }
    }

    @Override
    public double maxX() {
        return this.maxX;
    }

    @Override
    public double maxY() {
        return this.maxY;
    }

    @Override
    public int axisWidth() {
        return 0;
    }

    @Override
    public int axisHeight() {
        return 0;
    }

}
