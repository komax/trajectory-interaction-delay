package visualization;

import colormap.ColorMap;
import frechet.Matching;
import java.awt.*;
import utils.DoublePoint2D;
import utils.IntPoint2D;
import utils.Trajectory;
import utils.Utils;

/**
 * Created by max on 25-4-14.
 */
public final class MatchingPlot extends GenericPlottingPanel {
    // TODO Parameterize this plot with the alpha values?
    private static final int TRANSLUCENT_ALPHA = 20;
    private static final int VISIBLE_ALPHA = 150;
    
    private Matching matching;
    private Trajectory trajectory1;
    private Trajectory trajectory2;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;
    private EdgeCursor selectedEdge;
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

    public MatchingPlot(Matching matching, Trajectory trajectory1, Trajectory trajectory2, int thresholdDelay, int translucentFocus) {
        this.selectedEdge = EdgeCursor.INVALID_CURSOR;
        updateMatching(matching, thresholdDelay, translucentFocus, trajectory1, trajectory2);
    }
    
    public void updateMatching(Matching matching, int thresholdDelay, int translucentFocus, Trajectory trajectory1, Trajectory trajectory2) {
        // Store data to plot
        this.trajectory1 = trajectory1;
        this.trajectory2 = trajectory2;
        DoublePoint2D minValuesTraject1 = minTraject1();
        DoublePoint2D minValuesTraject2 = minTraject2();
        this.minX = Math.min(minValuesTraject1.x, minValuesTraject2.x);
        this.minY = Math.min(minValuesTraject1.y, minValuesTraject2.y);

        makeTrajectoriesNullbased();
        this.minX = 0.0;
        this.minY = 0.0;
        
        DoublePoint2D maxValuesTraject1 = maxTraject1();
        DoublePoint2D maxValuesTraject2 = maxTraject2();
        this.maxX = Math.max(maxValuesTraject1.x, maxValuesTraject2.x);
        this.maxY = Math.max(maxValuesTraject1.y, maxValuesTraject2.y);
        
        this.matching = matching;
        this.thresholdDelay = thresholdDelay;
        this.translucentFocus = translucentFocus;
        
        if (matching.getLength() > 0) {
            this.delaysInTimestamps = utils.Utils.delayInTimestamps(matching);
            this.isTraject1Ahead = Utils.trajectroy1IsAhead(matching, thresholdDelay);
            this.isTraject2Ahead = Utils.trajectroy2IsAhead(matching, thresholdDelay);
            this.maxDelay = Integer.MIN_VALUE;
            for (int delay : delaysInTimestamps) {
                if (delay > maxDelay) {
                    maxDelay = delay;
                }
            }

            if (maxDelay > 0) {
                this.positiveColors = ColorMap.createGrayToBlueTransparentColormap(thresholdDelay, maxDelay);
                this.negativeColors = ColorMap.createGrayToRedTransparentColormap(thresholdDelay, maxDelay);
            } else {
                this.positiveColors = ColorMap.createGrayToBlueTransparentColormap(maxDelay, maxDelay);
                this.negativeColors = ColorMap.createGrayToRedTransparentColormap(maxDelay, maxDelay);

            }
            this.positiveColors.halfColorSpectrum();
            this.positiveColors.halfColorSpectrum();
            this.negativeColors.halfColorSpectrum();
            this.negativeColors.halfColorSpectrum();

            this.repaint();
        }
    }

    private void makeTrajectoriesNullbased() {
        Trajectory t1 = new Trajectory();
        Trajectory t2 = new Trajectory();
        for (double[] point : trajectory1) {
            double[] nullBasedPoint = new double[2];
            nullBasedPoint[0] = point[0] - minX;
            nullBasedPoint[1] = point[1] - minY;
            t1.addPoint(nullBasedPoint);
        }
        
        for (double[] point : trajectory2) {
            double[] nullBasedPoint = new double[2];
            nullBasedPoint[0] = point[0] - minX;
            nullBasedPoint[1] = point[1] - minY;
            t2.addPoint(nullBasedPoint);
        }
        trajectory1 = t1;
        trajectory2 = t2;
    }
    
    private DoublePoint2D minTraject1() {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        for (double[] point : trajectory1) {
            minX = Math.min(minX, point[0]);
            minY = Math.min(minY, point[1]);
        }
        return new DoublePoint2D(minX, minY);
    }
    
    private DoublePoint2D minTraject2() {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        for (double[] point : trajectory2) {
            minX = Math.min(minX, point[0]);
            minY = Math.min(minY, point[1]);
        }
        return new DoublePoint2D(minX, minY);
    }
    
    private DoublePoint2D maxTraject1() {
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        for (double[] point : trajectory1) {
            maxX = Math.max(maxX, point[0]);
            maxY = Math.max(maxY, point[1]);
        }
        return new DoublePoint2D(maxX, maxY);
    }
    
    private DoublePoint2D maxTraject2() {
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        for (double[] point : trajectory2) {
            maxX = Math.max(maxX, point[0]);
            maxY = Math.max(maxY, point[1]);
        }
        return new DoublePoint2D(maxX, maxY);
    }
    
    public void updateSelection(EdgeCursor selection) {
        if (matching.getLength() > 0) {
            this.selectedEdge = selection;
            int newIndex = selection.getPosition();
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
    
    private Color getColorTraject1(Color color, int index) {
        if (index < startFocusTraject1 || index > endFocusTraject1) {
            return ColorMap.getColorFromRGB(color.getRGB(), TRANSLUCENT_ALPHA);
        } else if (selectedEdge.isValid()) {
            int valueRange = VISIBLE_ALPHA - TRANSLUCENT_ALPHA;
            int deltaFocus;
            int intervalLength;
            int indexI = selectedEdge.getIndexTrajA();
            if (index >= indexI) {
                deltaFocus = endFocusTraject1 - index;
                intervalLength = endFocusTraject1 - indexI;
            } else {
                deltaFocus = index - startFocusTraject1;
                intervalLength = indexI - startFocusTraject1;
            }
            Double alpha = ((double) deltaFocus) * valueRange / intervalLength;
            int calculatedAlpha = alpha.intValue() + TRANSLUCENT_ALPHA;
            return ColorMap.getColorFromRGB(color.getRGB(), calculatedAlpha);
        } else {
            return ColorMap.getColorFromRGB(color.getRGB(), TRANSLUCENT_ALPHA);
        }
    }
    
    private Color getColorTraject2(Color color, int index) {
        if (index < startFocusTraject2 || index > endFocusTraject2) {
            return ColorMap.getColorFromRGB(color.getRGB(), TRANSLUCENT_ALPHA);
        } else if (selectedEdge.isValid()) {
            int valueRange = VISIBLE_ALPHA - TRANSLUCENT_ALPHA;
            int deltaFocus;
            int intervalLength;
            int indexJ = selectedEdge.getIndexTrajB();
            if (index >= indexJ) {
            deltaFocus = endFocusTraject2 - index;
                intervalLength = endFocusTraject2 - indexJ;
            } else {
                deltaFocus = index - startFocusTraject2;
                intervalLength = indexJ - startFocusTraject2;
            }
            Double alpha = ((double) deltaFocus) * valueRange / intervalLength;
            int calculatedAlpha = alpha.intValue() + TRANSLUCENT_ALPHA;
            return ColorMap.getColorFromRGB(color.getRGB(), calculatedAlpha);
        } else {
            return ColorMap.getColorFromRGB(color.getRGB(), TRANSLUCENT_ALPHA);
        }   
    }

    private void paintTrajectories(Graphics g) {
        drawTrajectory(trajectory1, g);
        drawTrajectory(trajectory2, g);
    }

    private void drawTrajectory(Trajectory trajectory, Graphics g) {
        if (trajectory.length() > 0) {
            DoublePoint2D previousPoint = trajectory.getPointObject(0);
            IntPoint2D transformedPreviousPoint = cartesianToPanelPoint(previousPoint);
            for (int i = 1; i < trajectory.length(); i++) {
                if (trajectory == trajectory1) {
                    g.setColor(getColorTraject1(Color.black, i));
                } else if (trajectory == trajectory2) {
                    g.setColor(getColorTraject2(Color.black, i));
                }
                DoublePoint2D currentPoint = trajectory.getPointObject(i);
                IntPoint2D transformedCurrentPoint = cartesianToPanelPoint(currentPoint);
                int fromX = transformedPreviousPoint.x;
                int fromY = transformedPreviousPoint.y;
                int toX = transformedCurrentPoint.x;
                int toY = transformedCurrentPoint.y;
                g.drawLine(fromX, fromY, toX, toY);

                transformedPreviousPoint = transformedCurrentPoint;
            }
        }
    }

    private void drawMatching(Graphics g) {
        if (matching.getLength() > 0) {
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
                // TODO Patching drawing in an separate method?
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
                        int delay = delaysInTimestamps[k - 1];
                        if (isTraject1Ahead[k - 1]) {
                            chosenColor = positiveColors.getColor(delay);
                        } else if (isTraject2Ahead[k - 1]) {
                            chosenColor = negativeColors.getColor(delay);
                        } else {
                            chosenColor = positiveColors.getMinColor();
                        }
                        if (currentIndexTraject1 < startFocusTraject1 || currentIndexTraject1 > endFocusTraject1) {
                            g.setColor(getColorTraject1(chosenColor, currentIndexTraject1));
                        } else if (currentIndexTraject2 < startFocusTraject2 || currentIndexTraject2 > endFocusTraject2) {
                            g.setColor(getColorTraject2(chosenColor, currentIndexTraject2));
                        } else {
                            g.setColor(chosenColor);
                        }
                        // 3a. Draw a simple line.
                        DoublePoint2D pointTraj1 = trajectory1.getPointObject(startIndexTraject1);
                        IntPoint2D convPointTraj1 = cartesianToPanelPoint(pointTraj1);
                        DoublePoint2D pointTraj2 = trajectory2.getPointObject(startIndexTraject2);
                        IntPoint2D convPointTraj2 = cartesianToPanelPoint(pointTraj2);
                        g.drawLine(convPointTraj1.x, convPointTraj1.y, convPointTraj2.x, convPointTraj2.y);
                    } else {
                        // Compute the color interpolation of the patch.
                        if (singleIndexTraject1) {
                            IntPoint2D startTraject2 = cartesianToPanelPoint(trajectory2.getPointObject(startIndexTraject2));
                            int startTraject2X = startTraject2.x;
                            int startTraject2Y = startTraject2.y;
                            int startIndex = Utils.findMatchingIndex(matching, startIndexTraject1, startIndexTraject2);
                            int startDelay = delaysInTimestamps[startIndex];
                            Color beginColor;
                            if (startDelay < thresholdDelay) {
                                beginColor = negativeColors.getMinColor();
                            } else {
                                beginColor = negativeColors.getColor(startDelay);
                            }

                            if (startIndexTraject1 < startFocusTraject1 || endIndexTraject1 > endFocusTraject1) {
                                beginColor = getColorTraject1(beginColor, startIndexTraject1);
                            } else if (startIndexTraject2 < startFocusTraject2 || endIndexTraject2 > endFocusTraject2) {
                                beginColor = getColorTraject2(beginColor, startIndexTraject2);
                            }

                            IntPoint2D endTraject2 = cartesianToPanelPoint(trajectory2.getPointObject(endIndexTraject2));
                            int endTraject2X = endTraject2.x;
                            int endTraject2Y = endTraject2.y;
                            int endIndex = Utils.findMatchingIndex(matching, endIndexTraject1, endIndexTraject2);
                            int endDelay = delaysInTimestamps[endIndex];
                            Color endColor;
                            if (endDelay < thresholdDelay) {
                                endColor = negativeColors.getMinColor();
                            } else {
                                endColor = negativeColors.getColor(endDelay);
                            }
                            if (startIndexTraject1 < startFocusTraject1 || endIndexTraject1 > endFocusTraject1) {
                                endColor = getColorTraject1(endColor, endIndexTraject1);
                            } else if (startIndexTraject2 < startFocusTraject2 || endIndexTraject2 > endFocusTraject2) {
                                endColor = getColorTraject2(endColor, endIndexTraject2);
                            }

                            GradientPaint gradientPaint = new GradientPaint(startTraject2X, startTraject2Y, beginColor, endTraject2X, endTraject2Y, endColor);
                            Graphics2D g2 = (Graphics2D) g;
                            g2.setPaint(gradientPaint);
                        } else {

                            IntPoint2D startTraject1 = cartesianToPanelPoint(trajectory1.getPointObject(startIndexTraject1));
                            int startTraject1X = startTraject1.x;
                            int startTraject1Y = startTraject1.y;
                            int startIndex = Utils.findMatchingIndex(matching, startIndexTraject1, startIndexTraject2);
                            int startDelay = delaysInTimestamps[startIndex];
                            Color beginColor;
                            if (startDelay < thresholdDelay) {
                                beginColor = positiveColors.getMinColor();
                            } else {
                                beginColor = positiveColors.getColor(startDelay);
                            }

                            if (startIndexTraject1 < startFocusTraject1 || endIndexTraject1 > endFocusTraject1) {
                                beginColor = getColorTraject1(beginColor, startIndexTraject1);
                            } else if (startIndexTraject2 < startFocusTraject2 || endIndexTraject2 > endFocusTraject2) {
                                beginColor = getColorTraject2(beginColor, startIndexTraject2);
                            }

                            IntPoint2D endTraject1 = cartesianToPanelPoint(trajectory1.getPointObject(endIndexTraject1));
                            int endTraject1X = endTraject1.x;
                            int endTraject1Y = endTraject1.y;
                            int endIndex = Utils.findMatchingIndex(matching, endIndexTraject1, endIndexTraject2);
                            int endDelay = delaysInTimestamps[endIndex];
                            Color endColor;
                            if (endDelay < thresholdDelay) {
                                endColor = positiveColors.getMinColor();
                            } else {
                                endColor = positiveColors.getColor(endDelay);
                            }
                            if (startIndexTraject1 < startFocusTraject1 || endIndexTraject1 > endFocusTraject1) {
                                endColor = getColorTraject1(endColor, endIndexTraject1);
                            } else if (startIndexTraject2 < startFocusTraject2 || endIndexTraject2 > endFocusTraject2) {
                                endColor = getColorTraject2(endColor, endIndexTraject2);
                            }

                            GradientPaint gradientPaint = new GradientPaint(startTraject1X, startTraject1Y, beginColor, endTraject1X, endTraject1Y, endColor);
                            Graphics2D g2 = (Graphics2D) g;
                            g2.setPaint(gradientPaint);
                        }
                        // 3b. Build the polygon.
                        Polygon patch = new Polygon();
                        for (int l = startIndexTraject1; l <= endIndexTraject1; l++) {
                            DoublePoint2D point = trajectory1.getPointObject(l);
                            IntPoint2D convertedPoint = cartesianToPanelPoint(point);
                            patch.addPoint(convertedPoint.x, convertedPoint.y);
                        }
                        for (int l = startIndexTraject2; l <= endIndexTraject2; l++) {
                            DoublePoint2D point = trajectory2.getPointObject(l);
                            IntPoint2D convertedPoint = cartesianToPanelPoint(point);
                            patch.addPoint(convertedPoint.x, convertedPoint.y);
                        }
                        // Draw the polygon.
                        g.fillPolygon(patch);
                    }
                    // Reset the indices.
                    startIndexTraject1 = endIndexTraject1 = currentIndexTraject1;
                    startIndexTraject2 = endIndexTraject2 = currentIndexTraject2;
                }
            }
            if (selectedEdge.isValid()) {
                int selectedIndex = selectedEdge.getPosition();
                drawPoints(g, selectedIndex);
            }
        }
    }
    
    private void drawPoints(Graphics g, int index) {
        if (matching.getLength() > 0) {
            Graphics2D g2 = (Graphics2D) g;
            int diameter = 6;
            int offset = diameter / 2;

            DoublePoint2D pointTraject1 = trajectory1.getPointObject(matching.i[index]);
            IntPoint2D panelPoint = cartesianToPanelPoint(pointTraject1);
            int panelX = panelPoint.x;
            int panelY = panelPoint.y;
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

            DoublePoint2D pointTraject2 = trajectory2.getPointObject(matching.j[index]);
            panelPoint = cartesianToPanelPoint(pointTraject2);
            panelX = panelPoint.x;
            panelY = panelPoint.y;
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
    public int leftColumn() {
        return 0;
    }

    @Override
    public int upperRow() {
        return 0;
    }

}
