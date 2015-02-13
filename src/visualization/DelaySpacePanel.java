/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import delayspace.DelaySpace;
import frechet.Matching;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import static utils.Utils.roundDouble;

/**
 *
 * @author max
 */
public final class DelaySpacePanel extends GenericPlottingPanel {
    // FIXME Use a heated body color map and draw the delay space in a custom panel.
    public static final Color TRAJECT_BLUE = new Color(0x0000FF);
    public static final Color TRAJECT_RED = new Color(0xFF0000);
    
    private EdgeCursor selectedEdge;
    private final int delayThreshold;
    private double samplingRate;
    private Matching matching;
    private final DelaySpace delaySpace;
    private final boolean logScaled;
    
    public DelaySpacePanel(DelaySpace delaySpace, Matching matching, int delayThreshold, double samplingRate, boolean logScaled) {
        this.delaySpace = delaySpace;
        this.selectedEdge = EdgeCursor.INVALID_CURSOR;
        this.matching = matching;
        this.delayThreshold = delayThreshold;
        this.samplingRate = samplingRate;
        this.logScaled = logScaled;
    }
    
    public void updateSelection(EdgeCursor selection) {
        this.selectedEdge = selection;
    }
    
    private boolean isTraject1Ahead() {
        if (selectedEdge.isTrajAAhead()) {
            return selectedEdge.isDelayLargerOrEqualThan(delayThreshold);
        } else {
            return false;
        }
    }
    
    private boolean isTraject2Ahead() {
        if (selectedEdge.isTrajBAhead()) {
            return selectedEdge.isDelayLargerOrEqualThan(delayThreshold);
        } else {
            return false;
        }
    }

    @Override
    public double maxX() {
        return getWidth();
    }

    @Override
    public double maxY() {
        return getHeight();
    }
    
    public static BufferedImage resize(BufferedImage image, int width, int height) {
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        return bi;
    }
    
    @Override
    public Dimension getPreferredSize() {
        // TODO fix dimension to correct size
        return new Dimension(getWidth(), getHeight());
//        return new Dimension(freeSpaceImage.getWidth(), freeSpaceImage.getHeight());
    }
    
    @Override
    public void paintComponent(Graphics g) {
        // TODO use the delayspace to plot the delay space.
        super.paintComponent(g);
        
        g.clearRect(0, 0, getWidth(), getHeight());

        int width = plotWidth();
        int height = plotHeight();
        
        // Draw legends on the axes using the sampling rate.
        g.setColor(Color.BLACK);
        g.drawString("0 s", 0, height);
        g.drawString("0 s", leftColumn(), getHeight());
        String maxValString = String.format("%.2f s", (matching.getLength() - 1) * samplingRate);
        g.drawString(maxValString, 0, upperRow());
        g.drawString(maxValString, width, height + upperRow());
        //g.drawString(Integer.toString(selectedIndexTraject2), xCoord, getHeight());
        
        if (selectedEdge.isValid()) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            g.setColor(Color.green);
            int lengthMatching = matching.getLength();
            double xPoint = ((double) width) * ((double) selectedEdge.getIndexTrajB() + 0.5) / lengthMatching;
            xPoint += leftColumn();
            double yPoint = height - ((double) height) * ((double) selectedEdge.getIndexTrajA() + 0.5) / lengthMatching;
            int xCoord = roundDouble(xPoint);
            int yCoord = roundDouble(yPoint);
            // Drawing the horizontial line.
            g.drawLine(leftColumn(), yCoord, xCoord, yCoord);
            // Drawing the vertical line.
            g.drawLine(xCoord, plotHeight(), xCoord, yCoord);
            
            // Drawing of indices on the axes.
            g.setColor(Color.BLACK);
            String secondsTraject1 = String.format("%.2f s", selectedEdge.getIndexTrajA() * samplingRate);
            g.drawString(secondsTraject1, 0, yCoord);
            String secondsTraject2 = String.format("%.2f s", selectedEdge.getIndexTrajB() * samplingRate);
            g.drawString(secondsTraject2, xCoord, getHeight());
            
            // Draw a translucent legend next to the point.
            int xCoordLegend = xCoord + 5;
            int diameter = 6;
            int offset = diameter / 2;
            g2.setStroke(new BasicStroke(5));
            if (isTraject1Ahead()) {
                g.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2.fillRect(xCoordLegend, yCoord, 20, 40);
                g.setColor(TRAJECT_BLUE);
                Polygon triangle = new Polygon();
                int triangleX = xCoordLegend + 10;
                int triangleY = yCoord + 10;
                triangle.addPoint(triangleX, triangleY + offset);
                triangle.addPoint(triangleX - offset, triangleY - offset);
                triangle.addPoint(triangleX + offset, triangleY - offset);
                g.drawPolygon(triangle);
                g.setColor(TRAJECT_RED);
                g.drawRect(xCoordLegend + 10 - offset, yCoord + 30 - offset, diameter, diameter);
            } else if (isTraject2Ahead()) {
                g.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2.fillRect(xCoordLegend, yCoord, 20, 40);
                g.setColor(TRAJECT_BLUE);
                Polygon triangle = new Polygon();
                int triangleX = xCoordLegend + 10;
                int triangleY = yCoord + 30;
                triangle.addPoint(triangleX, triangleY + offset);
                triangle.addPoint(triangleX - offset, triangleY - offset);
                triangle.addPoint(triangleX + offset, triangleY - offset);
                g.drawPolygon(triangle);
                g.setColor(TRAJECT_RED);
                g.drawRect(xCoordLegend + 10 - offset, yCoord + 10 - offset, diameter, diameter);
            } else {
                g.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2.fillRect(xCoordLegend, yCoord, 40, 20);
                g.setColor(TRAJECT_RED);
                g.drawRect(xCoordLegend + 10 - offset, yCoord + 10 - offset, diameter, diameter);
                g.setColor(TRAJECT_BLUE);
                Polygon triangle = new Polygon();
                int triangleX = xCoordLegend + 30;
                int triangleY = yCoord + 10;
                triangle.addPoint(triangleX, triangleY + offset);
                triangle.addPoint(triangleX - offset, triangleY - offset);
                triangle.addPoint(triangleX + offset, triangleY - offset);
                g.drawPolygon(triangle);
            }
        }
    }

    @Override
    public int leftColumn() {
        return 50;
    }

    @Override
    public int upperRow() {
        return 15;
    }

    void updateMatching(Matching matching) {
        this.matching = matching;
    }

    @Override
    public int rightColumn() {
        return 0;
    }

    @Override
    public int bottomRow() {
        return 0;
    }
    
}
