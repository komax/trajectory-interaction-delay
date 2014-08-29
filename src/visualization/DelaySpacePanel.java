/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
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
    private int selectedIndexTraject1;
    private int selectedIndexTraject2;
    private BufferedImage freeSpaceImage;
    private final int lengthMatching;
    
    public DelaySpacePanel(String fileName, int lengthTrajectory) {
        this.selectedIndexTraject1 = -1;
        this.selectedIndexTraject2 = -1;
        this.lengthMatching = lengthTrajectory;
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
            // Drawing the horizontial line.
            g.drawLine(axisWidth(), roundDouble(yPoint), roundDouble(xPoint), roundDouble(yPoint));
            // Drawing the vertical line.
            g.drawLine(roundDouble(xPoint), plotHeight(), roundDouble(xPoint), roundDouble(yPoint));
            
            g.drawString(Integer.toString(selectedIndexTraject1), 0, roundDouble(yPoint));
            g.drawString(Integer.toString(selectedIndexTraject2), roundDouble(xPoint), getHeight());
            
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
