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
import utils.DistanceNorm;
import utils.Utils;

/**
 *
 * @author max
 */
public final class DistancePlotPanel extends GenericPlottingPanel {
    private double[] normalizedDistances;
    private int selectedIndex;
    private double maxDistance;
    private double[] distancesOnMatching;
    private double minDistance;
    private ColorMap heatedBodyColorMap;
    
    public DistancePlotPanel(Matching matching, DistanceNorm distance) {
        this.selectedIndex = -1;
        updateMatching(matching, distance);
    }
    
    public void updateMatching(Matching matching, DistanceNorm distance) {
        this.distancesOnMatching = Utils.distancesOnMatching(matching, distance);
        this.normalizedDistances = Utils.normalizedDelay(distancesOnMatching);
        this.maxDistance = Double.MIN_VALUE;
        this.minDistance = Double.MAX_VALUE;
        
        for (double currentDistance : normalizedDistances) {
            if (currentDistance > maxDistance) {
                maxDistance = currentDistance;
            }
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
            }
        }
        this.heatedBodyColorMap = ColorMap.createHeatedBodyColorMap(minDistance, maxDistance);
    }
    
    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 300);
    }

    @Override
    public double maxX() {
        return normalizedDistances.length;
    }

    @Override
    public double maxY() {
        return maxDistance;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        Point2D previousPoint = new Point2D(0, normalizedDistances[0]);
        Point2D transformedPreviousPoint = cartesianToPanelPoint(previousPoint);
        for (int i=1; i<maxX(); i++) {
            Point2D currentPoint = new Point2D(i, normalizedDistances[i]);
            Point2D transformedCurrentPoint = cartesianToPanelPoint(currentPoint);
            int fromX = roundDouble(transformedPreviousPoint.x);
            int fromY = roundDouble(transformedPreviousPoint.y);
            int toX = roundDouble(transformedCurrentPoint.x);
            int toY = roundDouble(transformedCurrentPoint.y);
            
            Color heatedColor = heatedBodyColorMap.getColor(normalizedDistances[i]);
            g.setColor(heatedColor);
            g.drawLine(0, fromY, axisWidth(), fromY);
            
            g.setColor(Color.black);
            g.drawLine(fromX, fromY, toX, toY);
            
            transformedPreviousPoint = transformedCurrentPoint;
        }
        
        if (selectedIndex >= 0) {
            g2.setStroke(new BasicStroke(1));
            Point2D selectedPoint = new Point2D(selectedIndex, normalizedDistances[selectedIndex]);
            Point2D drawablePoint = cartesianToPanelPoint(selectedPoint);
            int xCoord = roundDouble(drawablePoint.x);
            int yCoord = roundDouble(drawablePoint.y);
            
            g.drawLine(axisWidth(), yCoord, xCoord, yCoord);
            g.drawLine(xCoord, axisHeight(), xCoord, getHeight());
            
            String currentDistance = String.format("%.3f", distancesOnMatching[selectedIndex]);
            g.setColor(Color.green);
            g.drawString(currentDistance, 0, yCoord);
            g.setColor(Color.BLACK);
            g.drawString(Integer.toString(selectedIndex), xCoord, axisHeight());
            
            g2.setStroke(new BasicStroke(2));
        }
    }

    @Override
    public int axisWidth() {
        return 40;
    }

    @Override
    public int axisHeight() {
        return 10;
    }
    
}
