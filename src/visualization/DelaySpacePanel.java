/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author max
 */
public final class DelaySpacePanel extends GenericPlottingPanel {
    public static final Color TRAJECT_BLUE = new Color(0x0000FF);
    public static final Color TRAJECT_RED = new Color(0xFF0000);
    
    private int selectedIndexTraject1;
    private int selectedIndexTraject2;
    private BufferedImage freeSpaceImage;
    private final int lengthMatching;
    private final int delayThreshold;
    
    public DelaySpacePanel(String fileName, int lengthTrajectory, int delayThreshold) {
        this.selectedIndexTraject1 = -1;
        this.selectedIndexTraject2 = -1;
        this.lengthMatching = lengthTrajectory;
        this.delayThreshold = delayThreshold;
        updateImage(fileName);
    }
    
    public void updateImage(String fileName) {
        try {
            this.freeSpaceImage = ImageIO.read(new File(fileName));
        } catch (IOException ex) {
            Logger.getLogger(DelaySpacePanel.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void setSelectedIndices(int indexTraject1, int indexTraject2) {
        this.selectedIndexTraject1 = indexTraject1;
        this.selectedIndexTraject2 = indexTraject2;
    }
    
    private boolean isTraject1Ahead() {
        if (selectedIndexTraject1 >= 0 && selectedIndexTraject2 >= 0) {
            int difference = selectedIndexTraject1 - selectedIndexTraject2;
            if (difference >= delayThreshold) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    
    private boolean isTraject2Ahead() {
        if (selectedIndexTraject1 >= 0 && selectedIndexTraject2 >= 0) {
            int difference = selectedIndexTraject2 - selectedIndexTraject1;
            if (difference >= delayThreshold) {
                return true;
            } else {
                return false;
            }
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
        return new Dimension(freeSpaceImage.getWidth(), freeSpaceImage.getHeight());
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        g.clearRect(0, 0, getWidth(), getHeight());

        int width = plotWidth();
        int height = plotHeight();
        BufferedImage scaledImage = resize(freeSpaceImage, width, height);
        g.drawImage(scaledImage, axisWidth(), 0, null);
        
        if (selectedIndexTraject1 >= 0 && selectedIndexTraject2 >= 0) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            g.setColor(Color.green);
            double xPoint = ((double) width) * (double) selectedIndexTraject2 / (lengthMatching-1);
            xPoint += axisWidth();
            double yPoint = height - ((double) height) * selectedIndexTraject1 / (lengthMatching-1);
            int xCoord = roundDouble(xPoint);
            int yCoord = roundDouble(yPoint);
            // Drawing the horizontial line.
            g.drawLine(axisWidth(), yCoord, xCoord, yCoord);
            // Drawing the vertical line.
            g.drawLine(xCoord, plotHeight(), xCoord, yCoord);
            
            // Drawing of indices on the axes.
            g.drawString(Integer.toString(selectedIndexTraject1), 0, yCoord);
            g.drawString(Integer.toString(selectedIndexTraject2), xCoord, getHeight());
            
            // Draw a translucent legend next to the point.
            int xCoordLegend = xCoord + 5;
            int diameter = 6;
            int offset = diameter / 2;
            g2.setStroke(new BasicStroke(5));
            if (isTraject1Ahead()) {
                
            } else if (isTraject2Ahead()) {
                
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
    public int axisWidth() {
        return 30;
    }

    @Override
    public int axisHeight() {
        return 15;
    }
    
}
