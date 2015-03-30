/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import colormap.ColorMap;
import frechet.Matching;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import utils.DoublePoint2D;
import utils.IntPoint2D;
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
        if (maxDelay > 0) {
            this.positiveColors = ColorMap.createGrayToBlueTransparentColormap(threshold, maxDelay);
            this.positiveColors.halfColorSpectrum();
            this.positiveColors.halfColorSpectrum();
            this.negativeColors = ColorMap.createGrayToRedTransparentColormap(threshold, maxDelay);
            this.negativeColors.halfColorSpectrum();
            this.negativeColors.halfColorSpectrum();
        } else {
            this.positiveColors = ColorMap.createGrayToBlueTransparentColormap(maxDelay, maxDelay);
            this.positiveColors.halfColorSpectrum();
            this.positiveColors.halfColorSpectrum();
            this.negativeColors = ColorMap.createGrayToRedTransparentColormap(maxDelay, maxDelay);
            this.negativeColors.halfColorSpectrum();
            this.negativeColors.halfColorSpectrum();
        }
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
        DoublePoint2D origin = new DoublePoint2D(0, maxDelay);
        IntPoint2D drawableOrigin = cartesianToPanelPoint(origin);
        int xCoord = roundDouble(drawableOrigin.x);
        int yCoord = roundDouble(drawableOrigin.y);
        // Dashed lines for -max_value, 0 and max_value.
        g.drawLine(xCoord, yCoord, getWidth(), yCoord);
        g.drawLine(xCoord, upperRow(), getWidth(), upperRow());
        g.drawLine(xCoord, plotHeight(), getWidth(), plotHeight());
        // Put label for max delay if trajectory1 is ahead.
        Color traject1AheadColor = positiveColors.getMaxColor();
        g.setColor(traject1AheadColor);       
        String maxDelayString = String.format("+%.2f s", maxDelay * delayUnit);
        g.drawString(maxDelayString, 0, upperRow());
        // Put label for max delay if trajectory2 is ahead.
        Color traject2AheadColor = negativeColors.getMaxColor();
        g.setColor(traject2AheadColor);       
        String minDelayString = String.format("-%.2f s", maxDelay * delayUnit);
        g.drawString(minDelayString, 0, getHeight() - upperRow());
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
            DoublePoint2D selectedPoint = new DoublePoint2D(selectedIndex, delay);
            IntPoint2D drawablePoint = cartesianToPanelPoint(selectedPoint);
            xCoord = roundDouble(drawablePoint.x);
            yCoord = roundDouble(drawablePoint.y);
            // Put the label for selected delay.
            g.drawString(currentDelayString, 0, yCoord);
            
            // Put the label for the corresponding index of the delay.
            g.setColor(Color.black);
            g.drawString(Integer.toString(selectedIndex), xCoord, upperRow());
            
            // Draw the lines for current selection.
            g.drawLine(leftColumn(), yCoord,xCoord, yCoord);
            g.drawLine(xCoord, upperRow(), xCoord, getHeight());
            
            g.drawLine(leftColumn(), yCoord, xCoord, yCoord);
            g.drawLine(xCoord, upperRow(), xCoord, getHeight());
            
            g2.setStroke(new BasicStroke(2));
        }
        
        
        // TODO Compress the code a bit.
        boolean previousDelayIsTraj1Ahead = matching.i[0] > matching.j[0];
        boolean previousDelayIsTraj2Ahead = matching.j[0] > matching.j[0];
        int previousDelay = delaysInTimestamps[0];
        boolean previousDelaySucceedsThreshold = previousDelay >= threshold;
        DoublePoint2D previousDataPoint = new DoublePoint2D(0, maxDelay);
        Color previousDelayColor = Color.lightGray;
        if (previousDelaySucceedsThreshold) {
            if (previousDelayIsTraj1Ahead) {
                // Color that trajectory 1 is ahead.
                previousDelayColor = positiveColors.getColor(previousDelay);
                previousDataPoint = new DoublePoint2D(0, maxDelay + previousDelay);
            } else if (previousDelayIsTraj2Ahead) {
                // Color that trajectory 2 is ahead.
                previousDelayColor = negativeColors.getColor(previousDelay);
                previousDataPoint = new DoublePoint2D(0, maxDelay - previousDelay);
            }
        }
        // Draw data point into the plot.
        IntPoint2D previousDrawablePoint = cartesianToPanelPoint(previousDataPoint);
        
        // FIXME Max value are larger than the axis. Incorporate the belowColumn.
        // Plotting of the delays.
        for (int k=1; k<lengthMatching; k++) {
            boolean currentDelayTraj1IsAhead = matching.i[k] > matching.j[k];
            boolean currentDelayTraj2IsAhead = matching.j[k] > matching.i[k];
            int currentDelay = delaysInTimestamps[k];
            boolean currentDelaySucceedsThreshold = currentDelay >= threshold;
            DoublePoint2D currentDataPoint = new DoublePoint2D(k, maxDelay);
            Color currentDelayColor = Color.lightGray;
            if (currentDelaySucceedsThreshold) {
                if (currentDelayTraj1IsAhead) {
                    // Color that trajectory 1 is ahead.
                    currentDelayColor = positiveColors.getColor(currentDelay);
                    currentDataPoint = new DoublePoint2D(k, maxDelay + currentDelay);
                } else if (currentDelayTraj2IsAhead) {
                    // Color that trajectory 2 is ahead.
                    currentDelayColor = negativeColors.getColor(currentDelay);
                    currentDataPoint = new DoublePoint2D(k, maxDelay - currentDelay);
                }
            }
            // Draw data point into the plot.
            IntPoint2D currentDrawablePoint = cartesianToPanelPoint(currentDataPoint);
            int previousX = roundDouble(previousDrawablePoint.x);
            int previousY = roundDouble(previousDrawablePoint.y);
            int currentX = roundDouble(currentDrawablePoint.x);
            int currentY = roundDouble(currentDrawablePoint.y);
            GradientPaint gradientPaint = new GradientPaint(previousX, previousY, previousDelayColor, currentX, currentY, currentDelayColor);
            g2.setPaint(gradientPaint);
            g.drawLine(previousX, previousY, currentX, currentY);
            previousDrawablePoint = currentDrawablePoint;
        }
    }  

    @Override
    public int leftColumn() {
        return 55;
    }

    @Override
    public int upperRow() {
        return 5;
    }

}
