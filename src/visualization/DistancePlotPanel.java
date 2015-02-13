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
import utils.Utils;
import static utils.Utils.roundDouble;

/**
 *
 * @author max
 */
public final class DistancePlotPanel extends GenericPlottingPanel {
    private double[] normalizedDistances;
    private EdgeCursor selectedEdge;
    private double maxDistanceNormalized;
    private double[] distancesOnMatching;
    private double minDistanceNormalized;
    private ColorMap heatedBodyColorMap;
    private double maxDistance;
    private double minDistance;
    
    public DistancePlotPanel(Matching matching, double[] distances) {
        this.selectedEdge = EdgeCursor.INVALID_CURSOR;
        updateMatching(matching, distances);
    }
    
    public void updateMatching(Matching matching, double[] distancesOnMatching) {
        this.distancesOnMatching = distancesOnMatching;
        this.normalizedDistances = Utils.normalizeValues(distancesOnMatching);
        this.maxDistanceNormalized = Double.MIN_VALUE;
        this.minDistanceNormalized = Double.MAX_VALUE;
        this.maxDistance = Double.MIN_VALUE;
        this.minDistance = Double.MAX_VALUE;
        
        for (double currentDistance : normalizedDistances) {
            if (currentDistance > maxDistanceNormalized) {
                maxDistanceNormalized = currentDistance;
            }
            if (currentDistance < minDistanceNormalized) {
                minDistanceNormalized = currentDistance;
            }
        }
        for (double currentDistance : distancesOnMatching) {
            if (currentDistance > maxDistance) {
                maxDistance = currentDistance;
            }
            if (currentDistance < minDistance) {
                minDistance = currentDistance;
            }
        }
        this.heatedBodyColorMap = ColorMap.createHeatedBodyColorMap(minDistance, maxDistance);
        this.repaint();
    }
    
    private double[] linspace(int steps) {
        double difference = maxDistance - minDistance;
        double step = difference / steps;
        double[] values = new double[steps];
        double currentValue = minDistance;
        for (int k = 0; k < steps; k++) {
            values[k] = currentValue;
            currentValue += step;
        }
        return values;
    }
    
    public void updateSelection(EdgeCursor selection) {
        this.selectedEdge = selection;
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
        return maxDistanceNormalized;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(1));
        DoublePoint2D previousPoint = new DoublePoint2D(0, normalizedDistances[0]);
        DoublePoint2D transformedPreviousPoint = cartesianToPanelPoint(previousPoint);
        for (int i=1; i<maxX(); i++) {
            DoublePoint2D currentPoint = new DoublePoint2D(i, normalizedDistances[i]);
            DoublePoint2D transformedCurrentPoint = cartesianToPanelPoint(currentPoint);
            int fromX = roundDouble(transformedPreviousPoint.x);
            int fromY = roundDouble(transformedPreviousPoint.y);
            int toX = roundDouble(transformedCurrentPoint.x);
            int toY = roundDouble(transformedCurrentPoint.y);
            
            // Draw a black line segment from previous point the consecutive one.
            g.setColor(Color.black);
            g.drawLine(fromX, fromY, toX, toY);
            
            transformedPreviousPoint = transformedCurrentPoint;
        }
        
        // Draw a line left the plot to visualize the different values.
        double[] bucketValues = linspace(1000);
        for (int i=0; i <bucketValues.length; i++) {
            double currentValue = bucketValues[i];
            double normalizedValue = currentValue / maxDistance;
            int yCoord = roundDouble(cartesianToPanelPoint(new DoublePoint2D(0, normalizedValue)).y);
            Color heatedColor = heatedBodyColorMap.getColor(currentValue);
            g.setColor(heatedColor);
            g.drawLine(0, yCoord, axisWidth(), yCoord);
        }
                
        
        // Set label for max distance.
        g.setColor(Color.black);
        DoublePoint2D maxPoint = cartesianToPanelPoint(new DoublePoint2D(0, maxDistanceNormalized));
        int maxY = roundDouble(maxPoint.y);
        String maxDistanceString = String.format("%.3f", maxDistance);
        g.drawString(maxDistanceString, 0, maxY);
        
        // Dashed line for max value.
        Stroke oldStroke = g2.getStroke();
        float dash1[] = {10.0f};
        BasicStroke dashed = new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, dash1, 0.0f);
        g2.setStroke(dashed);
        g.drawLine(0, maxY, getWidth(), maxY);

        // Restore old stroke style.
        g2.setStroke(oldStroke);
        
        // Set label for min distance.
        g.setColor(Color.white);
        DoublePoint2D minPoint = cartesianToPanelPoint(new DoublePoint2D(0, minDistanceNormalized));
        int minY = roundDouble(minPoint.y);
        String minDistanceString = String.format("%.3f", minDistance);
        g.drawString(minDistanceString, 0, minY);
        
        // Draw axes and selected distance.
        if (selectedEdge.isValid()) {
            g2.setStroke(new BasicStroke(1));
            g.setColor(Color.black);
            int selectedIndex = selectedEdge.getPosition();
            DoublePoint2D selectedPoint = new DoublePoint2D(selectedIndex, normalizedDistances[selectedIndex]);
            DoublePoint2D drawablePoint = cartesianToPanelPoint(selectedPoint);
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
