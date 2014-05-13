/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import frechet.Matching;
import java.awt.Dimension;
import java.awt.Graphics;
import utils.Utils;

/**
 *
 * @author max
 */
public class DelayPlotPanel extends GenericPlottingPanel {
    private final double[] normalizedDelay;
    
    public DelayPlotPanel(Matching matching) {
        double[] delaysWithEuclideanNorm = Utils.delayWithEuclideanNorm(matching);
        this.normalizedDelay = Utils.normalizedDelay(delaysWithEuclideanNorm);
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
        return 1.0;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
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
    
}
