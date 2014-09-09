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
import utils.DistanceNorm;
import utils.Utils;
import static visualization.GenericPlottingPanel.roundDouble;

/**
 *
 * @author max
 */
public final class DistancePlotPanel extends GenericPlottingPanel {
    private double[] normalizedDistances;
    private int selectedIndex;
    private double maxDistanceNormalized;
    private double[] distancesOnMatching;
    private double minDistanceNormalized;
    private ColorMap heatedBodyColorMap;
    private double maxDistance;
    private double minDistance;
    
    public DistancePlotPanel(Matching matching, DistanceNorm distance) {
        this.selectedIndex = -1;
        updateMatching(matching, distance);
    }
    
    public void updateMatching(Matching matching, DistanceNorm distance) {
        this.distancesOnMatching = Utils.distancesOnMatching(matching, distance);
        this.normalizedDistances = Utils.normalizedDelay(distancesOnMatching);
        this.maxDistanceNormalized = Double.MIN_VALUE;
        this.minDistanceNormalized = Double.MAX_VALUE;
        
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
        this.heatedBodyColorMap = ColorMap.createHeatedBodyColorMap(minDistanceNormalized, maxDistanceNormalized);
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
        return maxDistanceNormalized;
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
        
        g.setColor(Color.black);
        Point2D maxPoint = cartesianToPanelPoint(new Point2D(0, maxDistanceNormalized));
        int maxY = roundDouble(maxPoint.y);
        String maxDistanceString = String.format("%.3f", maxDistance);
        g.drawString(maxDistanceString, 0, maxY);
        Stroke oldStroke = g2.getStroke();
        float dash1[] = {10.0f};
        BasicStroke dashed = new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, dash1, 0.0f);
        g2.setStroke(dashed);
        Point2D origin = new Point2D(0, maxDistanceNormalized);
        Point2D drawableOrigin = cartesianToPanelPoint(origin);
        int xCoord = roundDouble(drawableOrigin.x);
        int yCoord = roundDouble(drawableOrigin.y);
        g.drawLine(xCoord, yCoord, getWidth(), yCoord);

        // Restore old stroke style.
        g2.setStroke(oldStroke);
        
        g.setColor(Color.white);
        Point2D minPoint = cartesianToPanelPoint(new Point2D(0, minDistanceNormalized));
        int minY = roundDouble(minPoint.y);
        String minDistanceString = String.format("%.3f", minDistance);
        g.drawString(minDistanceString, 0, minY);
        
        if (selectedIndex >= 0) {
            g2.setStroke(new BasicStroke(1));
            g.setColor(Color.black);
            Point2D selectedPoint = new Point2D(selectedIndex, normalizedDistances[selectedIndex]);
            Point2D drawablePoint = cartesianToPanelPoint(selectedPoint);
            xCoord = roundDouble(drawablePoint.x);
            yCoord = roundDouble(drawablePoint.y);
            
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
