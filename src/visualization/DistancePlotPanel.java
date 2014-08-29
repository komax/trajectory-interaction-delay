/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import frechet.Matching;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import utils.DistanceNorm;
import utils.Utils;

/**
 *
 * @author max
 */
public final class DistancePlotPanel extends GenericPlottingPanel {
    private double[] normalizedDelay;
    private int selectedIndex;
    private double maxDelay;
    private double[] distancesOnMatching;
    
    public DistancePlotPanel(Matching matching, DistanceNorm distance) {
        this.selectedIndex = -1;
        updateMatching(matching, distance);
    }
    
    public void updateMatching(Matching matching, DistanceNorm distance) {
        this.distancesOnMatching = Utils.distancesOnMatching(matching, distance);
        this.normalizedDelay = Utils.normalizedDelay(distancesOnMatching);
        this.maxDelay = Double.MIN_VALUE;
        
        for (double delay : normalizedDelay) {
            if (delay > maxDelay) {
                maxDelay = delay;
            }
        }        
    }
    
    public void setSelectedDelay(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 300);
    }

    @Override
    public double maxX() {
        return normalizedDelay.length;
    }

    @Override
    public double maxY() {
        return maxDelay;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (selectedIndex >= 0) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            Point2D selectedPoint = new Point2D(selectedIndex, normalizedDelay[selectedIndex]);
            Point2D drawablePoint = cartesianToPanelPoint(selectedPoint);
            int xCoord = roundDouble(drawablePoint.x);
            int yCoord = roundDouble(drawablePoint.y);
            
            g.drawLine(0, yCoord,xCoord, yCoord);
            g.drawLine(xCoord, 0, xCoord, getHeight());
            
            g2.setStroke(new BasicStroke(2));
        }
        
        Point2D previousPoint = new Point2D(0, normalizedDelay[0]);
        Point2D transformedPreviousPoint = cartesianToPanelPoint(previousPoint);
        for (int i=1; i<maxX(); i++) {
            Point2D currentPoint = new Point2D(i, normalizedDelay[i]);
            Point2D transformedCurrentPoint = cartesianToPanelPoint(currentPoint);
            int fromX = roundDouble(transformedPreviousPoint.x);
            int fromY = roundDouble(transformedPreviousPoint.y);
            int toX = roundDouble(transformedCurrentPoint.x);
            int toY = roundDouble(transformedCurrentPoint.y);
            g.drawLine(fromX, fromY, toX, toY);
            
            transformedPreviousPoint = transformedCurrentPoint;
        }
    }

    @Override
    public int axisWidth() {
        return 10;
    }

    @Override
    public int axisHeight() {
        return 10;
    }
    
}
