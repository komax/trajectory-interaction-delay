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
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author max
 */
public class DelaySpacePanel extends GenericPlottingPanel {
    private int selectedIndexTraject1;
    private int selectedIndexTraject2;
    private ImageIcon delaySpaceIcon;
    private BufferedImage freeSpaceImage;
    private final int lengthMatching;
    
    public DelaySpacePanel(int lengthMatching) {
        this.selectedIndexTraject1 = -1;
        this.selectedIndexTraject2 = -1;
        this.lengthMatching = lengthMatching;
        try {
            this.freeSpaceImage = ImageIO.read(new File("delay_space_bats.png"));
            this.delaySpaceIcon = new ImageIcon(freeSpaceImage);
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
        return new Dimension(lengthMatching, lengthMatching);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Image img = delaySpaceIcon.getImage();
        ImageObserver imgObserver = delaySpaceIcon.getImageObserver();
        BufferedImage scaledImage = resize(freeSpaceImage, getWidth(), getHeight());
        g.drawImage(scaledImage, 0, 0, null);
        
        if (selectedIndexTraject1 >= 0 && selectedIndexTraject2 >= 0) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            g.setColor(Color.green);
            double scaledPixelTraject1 = maxY() / lengthMatching * selectedIndexTraject1;
            double scaledPixelTraject2 = maxX() / lengthMatching * selectedIndexTraject2;
            Point2D pointOfMatching = new Point2D(selectedIndexTraject2, selectedIndexTraject1);
            Point2D pointInPanel = cartesianToPanelPoint(pointOfMatching);
            int xPoint = roundDouble(pointInPanel.x);
            //xPoint = selectedIndexTraject2;
            int yPoint = roundDouble(pointInPanel.y);
            //yPoint = selectedIndexTraject1;
            // Drawing the horizontial line.
            g.drawLine(0, yPoint, xPoint, yPoint);
            // Drawing the vertical line.
            g.drawLine(xPoint, getHeight(), xPoint, yPoint);
            
        }
    }
    
}
