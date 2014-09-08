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

/**
 *
 * @author max
 */
public final class FollowingPlotPanel extends GenericPlottingPanel {
    private int[] delaysInTimestamps;
    private int lengthMatching;
    private int maxDelay;
    private Matching matching;
    private int selectedIndex;
    private ColorMap positiveColors;
    private ColorMap negativeColors;
    private int threshold;
    private double delayUnit;
    
    public FollowingPlotPanel(Matching matching, int threshold, double delayUnit) {
        this.selectedIndex = -1;
        updateMatching(matching, threshold, delayUnit);
    }
    
    public void updateMatching(Matching matching, int threshold, double delayUnit) {
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
    }
    
    public void setSelectedIndex(int newIndex) {
        this.selectedIndex = newIndex;
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
        g.drawLine(xCoord, yCoord, getWidth(), yCoord);
        g.drawLine(xCoord, axisHeight(), getWidth(), axisHeight());
        g.drawLine(xCoord, plotHeight(), getWidth(), plotHeight());
        
        Color traject1AheadColor = positiveColors.getMaxColor();
        g.setColor(traject1AheadColor);       
        String maxDelayString = String.format("+%.2f s", maxDelay * delayUnit);
        g.drawString(maxDelayString, 0, axisHeight());
        
        Color traject2AheadColor = negativeColors.getMaxColor();
        g.setColor(traject2AheadColor);       
        String minDelayString = String.format("-%.2f s", maxDelay * delayUnit);
        g.drawString(minDelayString, 0, getHeight() - axisHeight());       
        
        // Restore old stroke style.
        g2.setStroke(oldStroke);
        
        if (selectedIndex >= 0) {
            // Draw selected delay in the plot.
            g2.setStroke(new BasicStroke(1));
            double delay = maxDelay;
            String currentDelay;
            if (matching.i[selectedIndex] > matching.j[selectedIndex]) {
                delay += delaysInTimestamps[selectedIndex];
                currentDelay = String.format("+%.2f s", delaysInTimestamps[selectedIndex] * delayUnit);
                g.setColor(traject1AheadColor);
            } else if (matching.i[selectedIndex] < matching.j[selectedIndex]) {
                delay -= delaysInTimestamps[selectedIndex];
                currentDelay = String.format("-%.2f s", delaysInTimestamps[selectedIndex] * delayUnit);
                g.setColor(traject2AheadColor);
            } else {
                g.setColor(Color.BLACK);
                currentDelay = "   0.00 s";
            }
            Point2D selectedPoint = new Point2D(selectedIndex, delay);
            Point2D drawablePoint = cartesianToPanelPoint(selectedPoint);
            xCoord = roundDouble(drawablePoint.x);
            yCoord = roundDouble(drawablePoint.y);

            g.drawString(currentDelay, 0, yCoord);
            
            g.setColor(Color.black);
            g.drawString(Integer.toString(selectedIndex), xCoord, axisHeight());
            
            g.drawLine(axisWidth(), yCoord,xCoord, yCoord);
            g.drawLine(xCoord, axisHeight(), xCoord, getHeight());
            
            g.drawLine(axisWidth(), yCoord, xCoord, yCoord);
            g.drawLine(xCoord, axisHeight(), xCoord, getHeight());
            
            g2.setStroke(new BasicStroke(2));
        }
        
        for (int k=0; k<lengthMatching; k++) {
            boolean traj1IsAhead = matching.i[k] > matching.j[k];
            boolean traj2IsAhead = matching.j[k] > matching.i[k];
            int currentDelay = delaysInTimestamps[k];
            boolean delaySucceedsThreshold = currentDelay >= threshold;
            Point2D dataPoint = null;
            if (delaySucceedsThreshold) {
                if (traj1IsAhead) {
                    // Color that trajectory 1 is ahead.
                    Color color = positiveColors.getColor(currentDelay);
                    g.setColor(color);
                    dataPoint = new Point2D(k, maxDelay + currentDelay);
                } else if (traj2IsAhead) {
                    // Color that trajectory 2 is ahead.
                    Color color = negativeColors.getColor(currentDelay);
                    g.setColor(color);
                    dataPoint = new Point2D(k, maxDelay - currentDelay);
                }
            } else {
                // No delay is detected.
                g.setColor(Color.lightGray);
                dataPoint = new Point2D(k, maxDelay);
            }
            // Draw data point into the plot.
            Point2D drawablePoint = cartesianToPanelPoint(dataPoint);
            int x = roundDouble(drawablePoint.x);
            int y = roundDouble(drawablePoint.y);
            g.drawLine(x, y, x, y);
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
