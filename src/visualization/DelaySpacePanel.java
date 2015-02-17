/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import colormap.ColorMap;
import delayspace.DelaySpace;
import frechet.Matching;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import utils.DoublePoint2D;
import utils.IntPoint2D;
import static utils.Utils.roundDouble;

/**
 *
 * @author max
 */
public final class DelaySpacePanel extends GenericPlottingPanel {
    // FIXME Use a heated body color map and draw the delay space in a custom panel.
    public static final Color TRAJECT_BLUE_COLOR = new Color(0x0000FF);
    public static final Color TRAJECT_RED_COLOR = new Color(0xFF0000);
    public static final Color MATCHING_COLOR = Color.GREEN;
    
    private EdgeCursor selectedEdge;
    private final int delayThreshold;
    private double samplingRate;
    private Matching matching;
    private DelaySpace delaySpace; // FIXME Use the delay space to plot.
    private final boolean logScaled; // TODO Enable to turn logscaling on and off
    private ColorMap heatedBodyColorMap;
    
    public DelaySpacePanel(DelaySpace delaySpace, Matching matching, int delayThreshold, double samplingRate, boolean logScaled) {
        this.delaySpace = delaySpace;
        this.selectedEdge = EdgeCursor.INVALID_CURSOR;
        this.matching = matching;
        this.delayThreshold = delayThreshold;
        this.samplingRate = samplingRate;
        this.logScaled = logScaled;
        this.heatedBodyColorMap = computeHeatMap();
    }
    
    public void updateDelaySpace(DelaySpace newDelaySpace) {
        this.delaySpace = newDelaySpace;
        this.heatedBodyColorMap = computeHeatMap();
        // TODO repaint?
    }
    
    public ColorMap computeHeatMap() {
        return ColorMap.createHeatedBodyColorMap(delaySpace.getMinValue(), delaySpace.getMaxValue());
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
        return delaySpace.numberRows();
    }

    @Override
    public double maxY() {
        return delaySpace.numberColumns();
    }
    
//    @Override
//    public Dimension getPreferredSize() {
//        // TODO fix dimension to correct size
//        return new Dimension(getWidth(), getHeight());
////        return new Dimension(freeSpaceImage.getWidth(), freeSpaceImage.getHeight());
//    }
    
    @Override
    public void paintComponent(Graphics g) {
        // TODO use the delayspace to plot the delay space.
        super.paintComponent(g);
        
        g.clearRect(0, 0, getWidth(), getHeight());

        int width = plotWidth();
        int height = plotHeight();
        
        drawDelaySpace(g, width, height);
        drawMatching(g, width, height);
        drawLegends(g, height, width);
        drawCursorAndGlyph(g, width, height);
    }

    private void drawDelaySpace(Graphics g, int width, int height) {
        int cellWidth = roundDouble((double) width / delaySpace.numberRows());
        int cellHeight = roundDouble((double) height / delaySpace.numberColumns());
        
        for (int i = 0; i < delaySpace.numberRows(); i++) {
            for (int j = 0; j < delaySpace.numberColumns(); j++) {
                double cellValue = delaySpace.get(i, j);
                // 1. Convert cell value into a color of the heat map.
                Color cellColor = heatedBodyColorMap.getColor(cellValue);
                g.setColor(cellColor);
                // 2. Draw a rectangle as the cell at (i,j) with its color.
                IntPoint2D lowerLeftPoint = cartesianToPanelPoint(new DoublePoint2D(i, j));
                g.fillRect(lowerLeftPoint.x, lowerLeftPoint.y,
                        cellWidth, cellHeight);
            }
        }
    }
    
    private void drawMatching(Graphics g, int width, int height) {
        IntPoint2D previousPoint = cartesianToPanelPoint(new DoublePoint2D(matching.i[0], matching.j[0]));
        for (int k = 1; k < matching.getLength(); k++) {
            IntPoint2D currentPoint = cartesianToPanelPoint(new DoublePoint2D(matching.i[k] + 0.5, matching.j[k] - 0.5));
            g.setColor(MATCHING_COLOR);
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(3.5f));
            g.drawLine(previousPoint.x, previousPoint.y, currentPoint.x, currentPoint.y);
            previousPoint = currentPoint;
        }
    }

    private void drawCursorAndGlyph(Graphics g, int width, int height) {
        if (selectedEdge.isValid()) {
            // TODO Refactor this ugly code below.
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            g.setColor(Color.green);
            IntPoint2D cursorPoint = cartesianToPanelPoint(new DoublePoint2D(selectedEdge.getIndexTrajA() + 0.5, selectedEdge.getIndexTrajB() - 0.5 ));
            int xCoord = cursorPoint.x;
            int yCoord = cursorPoint.y;
            // Drawing the horizontial line.
            g.drawLine(leftColumn(), yCoord, xCoord, yCoord);
            // Drawing the vertical line.
            g.drawLine(xCoord, plotHeight()+upperRow(), xCoord, yCoord);
            
            // Drawing of indices on the axes.
            g.setColor(Color.BLACK);
            String secondsTraject2 = String.format("%.2f s", selectedEdge.getIndexTrajB() * samplingRate);
            g.drawString(secondsTraject2, 0, yCoord);
            String secondsTraject1 = String.format("%.2f s", selectedEdge.getIndexTrajA() * samplingRate);
            g.drawString(secondsTraject1, xCoord, getHeight());
            
            // Draw a translucent legend next to the point.
            int xCoordLegend = xCoord + 5;
            int diameter = 6;
            int offset = diameter / 2;
            g2.setStroke(new BasicStroke(5));
            if (isTraject1Ahead()) {
                g.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2.fillRect(xCoordLegend, yCoord, 20, 40);
                g.setColor(TRAJECT_BLUE_COLOR);
                Polygon triangle = new Polygon();
                int triangleX = xCoordLegend + 10;
                int triangleY = yCoord + 10;
                triangle.addPoint(triangleX, triangleY + offset);
                triangle.addPoint(triangleX - offset, triangleY - offset);
                triangle.addPoint(triangleX + offset, triangleY - offset);
                g.drawPolygon(triangle);
                g.setColor(TRAJECT_RED_COLOR);
                g.drawRect(xCoordLegend + 10 - offset, yCoord + 30 - offset, diameter, diameter);
            } else if (isTraject2Ahead()) {
                g.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2.fillRect(xCoordLegend, yCoord, 20, 40);
                g.setColor(TRAJECT_BLUE_COLOR);
                Polygon triangle = new Polygon();
                int triangleX = xCoordLegend + 10;
                int triangleY = yCoord + 30;
                triangle.addPoint(triangleX, triangleY + offset);
                triangle.addPoint(triangleX - offset, triangleY - offset);
                triangle.addPoint(triangleX + offset, triangleY - offset);
                g.drawPolygon(triangle);
                g.setColor(TRAJECT_RED_COLOR);
                g.drawRect(xCoordLegend + 10 - offset, yCoord + 10 - offset, diameter, diameter);
            } else {
                g.setColor(Color.white);
                g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
                g2.fillRect(xCoordLegend, yCoord, 40, 20);
                g.setColor(TRAJECT_RED_COLOR);
                g.drawRect(xCoordLegend + 10 - offset, yCoord + 10 - offset, diameter, diameter);
                g.setColor(TRAJECT_BLUE_COLOR);
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

    private void drawLegends(Graphics g, int height, int width) {
        // Draw legends on the axes using the sampling rate.
        g.setColor(Color.BLACK);
        g.drawString("0 s", 0, height);
        g.drawString("0 s", leftColumn(), getHeight());
        String maxValString = String.format("%.2f s", (matching.getLength() - 1) * samplingRate);
        g.drawString(maxValString, 0, upperRow());
        g.drawString(maxValString, width, height + upperRow());
        //g.drawString(Integer.toString(selectedIndexTraject2), xCoord, getHeight());
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
    
}
