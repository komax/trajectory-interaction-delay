/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import frechet.Matching;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import utils.DoublePoint2D;
import static utils.Utils.roundDouble;

/**
 *
 * @author max
 */
public final class DelayPlotPanel extends GenericPlottingPanel {
    private int[] delaysInTimestamps;
    private int lengthMatching;
    private int maxDelay;
    private Matching matching;
    private EdgeCursor selectedEdge;
    private ColorMap positiveColors;
    private ColorMap negativeColors;
    private int threshold;
    private double delayUnit;
    
    public DelayPlotPanel(Matching matching, int threshold, double delayUnit) {
        this.selectedEdge = EdgeCursor.INVALID_CURSOR;
        updateMatching(matching, threshold, delayUnit);
    }
    
    public void updateMatching(Matching matching, int threshold, double delayUnit) {
        this.maxDelay = Integer.MIN_VALUE;
        this.matching = matching;
        this.lengthMatching = matching.i.length;
        this.delaysInTimestamps = utils.Utils.delayInTimestamps(matching);
        this.threshold = threshold;
        this.delayUnit = delayUnit;
        for (int delay: delaysInTimestamps) {
            if (delay > maxDelay) {
                maxDelay = delay;
            }
        }
        this.positiveColors = ColorMap.createGrayToBlueTransparentColormap(threshold, maxDelay);
        this.positiveColors.halfColorSpectrum();
        this.positiveColors.halfColorSpectrum();
        this.negativeColors = ColorMap.createGrayToRedTransparentColormap(threshold, maxDelay);
        this.negativeColors.halfColorSpectrum();
        this.negativeColors.halfColorSpectrum();
        repaint();
    }
    
    public void updateSelection(EdgeCursor selection) {
        this.selectedEdge = selection;
    }
    
    @Override
    public double maxX() {
        return lengthMatching;
    }

    @Override
    public double maxY() {
        // 2x for negative and postive value range.
        return 2 * maxDelay;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 300);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        // Draw zero-axis.
        Stroke oldStroke = g2.getStroke();
        float dash1[] = {10.0f};
        BasicStroke dashed = new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, dash1, 0.0f);
        g2.setStroke(dashed);
        Point2D origin = new Point2D(0, maxDelay);
        Point2D drawableOrigin = cartesianToPanelPoint(origin);
        int xCoord = roundDouble(drawableOrigin.x);
        int yCoord = roundDouble(drawableOrigin.y);
        // Dashed lines for -max_value, 0 and max_value.
        g.drawLine(xCoord, yCoord, getWidth(), yCoord);
        g.drawLine(xCoord, axisHeight(), getWidth(), axisHeight());
        g.drawLine(xCoord, plotHeight(), getWidth(), plotHeight());
        // Put label for max delay if trajectory1 is ahead.
        Color traject1AheadColor = positiveColors.getMaxColor();
        g.setColor(traject1AheadColor);       
        String maxDelayString = String.format("+%.2f s", maxDelay * delayUnit);
        g.drawString(maxDelayString, 0, axisHeight());
        // Put label for max delay if trajectory2 is ahead.
        Color traject2AheadColor = negativeColors.getMaxColor();
        g.setColor(traject2AheadColor);       
        String minDelayString = String.format("-%.2f s", maxDelay * delayUnit);
        g.drawString(minDelayString, 0, getHeight() - axisHeight());
        // Put label for zero delay.
        g.setColor(Color.BLACK);
        String zeroDelayString = "   0.00 s";
        g.drawString(zeroDelayString, 0, yCoord);
        
        // Restore old stroke style.
        g2.setStroke(oldStroke);
        
        if (selectedEdge.isValid()) {
            // Draw selected delay in the plot.
            g2.setStroke(new BasicStroke(1));
            double delay = maxDelay;
            String currentDelayString = "";
            int selectedIndex = selectedEdge.getPosition();
            int currentDelay = delaysInTimestamps[selectedIndex];
            // Put label for current delay if the delay succeeds the threshold.
            if (currentDelay >= threshold) {
                if (matching.i[selectedIndex] > matching.j[selectedIndex]) {
                    delay += currentDelay;
                    currentDelayString = String.format("+%.2f s", currentDelay * delayUnit);
                    g.setColor(traject1AheadColor);
                } else if (matching.i[selectedIndex] < matching.j[selectedIndex]) {
                    delay -= currentDelay;
                    currentDelayString = String.format("-%.2f s", currentDelay * delayUnit);
                    g.setColor(traject2AheadColor);
                }
            } else {
                g.setColor(Color.BLACK);
            }
            Point2D selectedPoint = new Point2D(selectedIndex, delay);
            Point2D drawablePoint = cartesianToPanelPoint(selectedPoint);
            xCoord = roundDouble(drawablePoint.x);
            yCoord = roundDouble(drawablePoint.y);
            // Put the label for selected delay.
            g.drawString(currentDelayString, 0, yCoord);
            
            // Put the label for the corresponding index of the delay.
            g.setColor(Color.black);
            g.drawString(Integer.toString(selectedIndex), xCoord, axisHeight());
            
            // Draw the lines for current selection.
            g.drawLine(axisWidth(), yCoord,xCoord, yCoord);
            g.drawLine(xCoord, axisHeight(), xCoord, getHeight());
            
            g.drawLine(axisWidth(), yCoord, xCoord, yCoord);
            g.drawLine(xCoord, axisHeight(), xCoord, getHeight());
            
            g2.setStroke(new BasicStroke(2));
        }
        
        boolean previousDelayIsTraj1Ahead = matching.i[0] > matching.j[0];
        boolean previousDelayIsTraj2Ahead = matching.j[0] > matching.j[0];
        int previousDelay = delaysInTimestamps[0];
        boolean previousDelaySucceedsThreshold = previousDelay >= threshold;
        
        // Plotting of the delays.
        for (int k=1; k<lengthMatching; k++) {
            boolean currentDelayTraj1IsAhead = matching.i[k] > matching.j[k];
            boolean currentDelayTraj2IsAhead = matching.j[k] > matching.i[k];
            int currentDelay = delaysInTimestamps[k];
            boolean currentDelaySucceedsThreshold = currentDelay >= threshold;
            DoublePoint2D currentDataPoint = null;
            if (currentDelaySucceedsThreshold) {
                if (currentDelayTraj1IsAhead) {
                    // Color that trajectory 1 is ahead.
                    Color color = positiveColors.getColor(currentDelay);
                    g.setColor(color);
                    currentDataPoint = new DoublePoint2D(k, maxDelay + currentDelay);
                } else if (currentDelayTraj2IsAhead) {
                    // Color that trajectory 2 is ahead.
                    Color color = negativeColors.getColor(currentDelay);
                    g.setColor(color);
                    currentDataPoint = new DoublePoint2D(k, maxDelay - currentDelay);
                }
            } else {
                // No delay is detected.
                g.setColor(Color.lightGray);
                currentDataPoint = new DoublePoint2D(k, maxDelay);
            }
            // Draw data point into the plot.
            DoublePoint2D currentDrawablePoint = cartesianToPanelPoint(currentDataPoint);
            int currentX = roundDouble(currentDrawablePoint.x);
            int currentY = roundDouble(currentDrawablePoint.y);
            g.drawLine(currentX, currentY, currentX, currentY);
        }
    }  

    @Override
    public int axisWidth() {
        return 55;
    }

    @Override
    public int axisHeight() {
        return 10;
    }
}