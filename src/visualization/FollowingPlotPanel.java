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
public class FollowingPlotPanel extends GenericPlottingPanel {
    private final int[] delaysInTimestamps;
    private final int lengthMatching;
    private int maxDelay;
    private final Matching matching;
    private int selectedIndex;
    private final ColorMap positiveColors;
    private final ColorMap negativeColors;
    
    public FollowingPlotPanel(Matching matching) {
        this.matching = matching;
        this.lengthMatching = matching.i.length;
        this.selectedIndex = -1;
        this.delaysInTimestamps = utils.Utils.delayInTimestamps(matching);
        this.maxDelay = Integer.MIN_VALUE;
        for (int delay: delaysInTimestamps) {
            if (delay > maxDelay) {
                maxDelay = delay;
            }
        }
        this.positiveColors = ColorMap.createGrayToBlueTransparentColormap(0.0, maxDelay);
        this.positiveColors.halfColorSpectrum();
        this.positiveColors.halfColorSpectrum();
        this.negativeColors = ColorMap.createGrayToRedTransparentColormap(0.0, maxDelay);
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
        
        // Restore old stroke style.
        g2.setStroke(oldStroke);
        
        if (selectedIndex >= 0) {
            // Draw selected delay in the plot.
            g2.setStroke(new BasicStroke(1));
            double delay = maxDelay;
            if (matching.i[selectedIndex] > matching.j[selectedIndex]) {
                delay += delaysInTimestamps[selectedIndex];
            } else if (matching.i[selectedIndex] < matching.j[selectedIndex]) {
                delay -= delaysInTimestamps[selectedIndex];
            }
            Point2D selectedPoint = new Point2D(selectedIndex, delay);
            Point2D drawablePoint = cartesianToPanelPoint(selectedPoint);
            xCoord = roundDouble(drawablePoint.x);
            yCoord = roundDouble(drawablePoint.y);
            
            g.drawLine(0, yCoord,xCoord, yCoord);
            g.drawLine(xCoord, 0, xCoord, getHeight());
            
            g2.setStroke(new BasicStroke(2));
        }
        
        for (int k=0; k<lengthMatching; k++) {
            boolean traj1IsAhead = matching.i[k] > matching.j[k];
            boolean traj2IsAhead = matching.j[k] > matching.i[k];
            int currentDelay = delaysInTimestamps[k];
            Point2D dataPoint = null;
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
}
