/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import colormap.ColorMap;
import delayspace.DelaySpace;
import frechet.Matching;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import utils.DoublePoint2D;
import utils.IntPoint2D;
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
    private boolean logScaled = false;
    
    public DistancePlotPanel(Matching matching, DelaySpace delaySpace) {
        this.selectedEdge = EdgeCursor.INVALID_CURSOR;
        update(matching, delaySpace);
    }
    public void update(Matching matching, DelaySpace delaySpace) {
        this.selectedEdge = EdgeCursor.INVALID_CURSOR;
        
        this.distancesOnMatching = Utils.distancesOnMatching(matching, delaySpace);
        this.normalizedDistances = Utils.normalizeValues(distancesOnMatching);

        this.maxDistance = delaySpace.getMaxValue();
        this.minDistance = delaySpace.getMinValue();
        this.maxDistanceNormalized = maxDistance / maxDistance;
        this.minDistanceNormalized = minDistance / minDistance;
        if (logScaled) {
            this.heatedBodyColorMap = ColorMap.createHeatedBodyColorMap(Math.log(minDistance), Math.log(maxDistance));
        } else {
            this.heatedBodyColorMap = ColorMap.createHeatedBodyColorMap(minDistance, maxDistance);
        }
        this.repaint();
    }
    
    public void setLogScaled(boolean logScaled) {
        this.logScaled = logScaled;
        if (logScaled) {
            this.heatedBodyColorMap = ColorMap.createHeatedBodyColorMap(Math.log(minDistance), Math.log(maxDistance));
        } else {
            this.heatedBodyColorMap = ColorMap.createHeatedBodyColorMap(minDistance, maxDistance);
        }
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
        IntPoint2D transformedPreviousPoint = cartesianToPanelPoint(previousPoint);
        for (int i=1; i<maxX(); i++) {
            // TODO Compress the code below a bit.
            DoublePoint2D currentPoint = new DoublePoint2D(i, normalizedDistances[i]);
            IntPoint2D transformedCurrentPoint = cartesianToPanelPoint(currentPoint);
            int fromX = transformedPreviousPoint.x;
            int fromY = transformedPreviousPoint.y;
            int toX = transformedCurrentPoint.x;
            int toY = transformedCurrentPoint.y;
            
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
            int yCoord = cartesianToPanelPoint(new DoublePoint2D(0, normalizedValue)).y;
            if (logScaled) {
                currentValue = Math.log(currentValue);
            }
            Color heatedColor = heatedBodyColorMap.getColor(currentValue);
            g.setColor(heatedColor);
            g.drawLine(0, yCoord, leftColumn(), yCoord);
        }
                
        
        // Set label for max distance.
        g.setColor(Color.black);
        IntPoint2D maxPoint = cartesianToPanelPoint(new DoublePoint2D(0, maxDistanceNormalized));
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
        IntPoint2D minPoint = cartesianToPanelPoint(new DoublePoint2D(0, minDistanceNormalized));
        int minY = minPoint.y;
        String minDistanceString = String.format("%.3f", minDistance);
        g.drawString(minDistanceString, 0, minY);
        
        // Draw axes and selected distance.
        if (selectedEdge.isValid()) {
            g2.setStroke(new BasicStroke(1));
            g.setColor(Color.black);
            int selectedIndex = selectedEdge.getPosition();
            DoublePoint2D selectedPoint = new DoublePoint2D(selectedIndex, normalizedDistances[selectedIndex]);
            IntPoint2D drawablePoint = cartesianToPanelPoint(selectedPoint);
            int xCoord = drawablePoint.x;
            int yCoord = drawablePoint.y;
            
            g.drawLine(leftColumn(), yCoord, xCoord, yCoord);
            g.drawLine(xCoord, upperRow(), xCoord, getHeight());
            
            String currentDistance = String.format("%.3f", distancesOnMatching[selectedIndex]);
            g.setColor(Color.green);
            g.drawString(currentDistance, 0, yCoord);
            g.setColor(Color.BLACK);
            g.drawString(Integer.toString(selectedIndex), xCoord, upperRow());
            
            g2.setStroke(new BasicStroke(2));
        }
    }

    @Override
    public int leftColumn() {
        return 40;
    }

    @Override
    public int upperRow() {
        return 10;
    }
    
}
