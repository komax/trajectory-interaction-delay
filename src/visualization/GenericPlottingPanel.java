/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 *
 * @author max
 */
public abstract class GenericPlottingPanel extends JPanel {
    protected static int roundDouble(double number) {
        return (int) Math.round(number);
    }

    public static class Point2D {

        public final double x;
        public final double y;

        public Point2D(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    
    public GenericPlottingPanel() {
        super(new BorderLayout());
        setBackground(Color.WHITE);
    }

    protected Point2D cartesianToPanelPoint(Point2D cartesianPoint) {
        int width = plotWidth();
        int height = plotHeight();
        double panelX = cartesianPoint.x / maxX() * width;
        double panelY = height - cartesianPoint.y / maxY() * height;
        panelX += axisWidth();
        panelY += axisHeight();
        return new Point2D(panelX, panelY);
    }

    protected Point2D panelToCartesianPoint(Point2D panelPoint) {
        int plotWidth = plotWidth();
        int plotHeight = plotHeight();
        double cartesianX = panelPoint.x / plotWidth * maxX();
        double cartesianY = panelPoint.y / plotHeight * maxY();
        return new Point2D(cartesianX, cartesianY);
    }
    
    protected int plotWidth() {
        return getWidth() - axisWidth();
    }
    
    protected int plotHeight() {
        return getHeight() - axisHeight();
    }
    
    public abstract double maxX();
    public abstract double maxY();
    
    public abstract int axisWidth();
    public abstract int axisHeight();
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
    }

    
}
