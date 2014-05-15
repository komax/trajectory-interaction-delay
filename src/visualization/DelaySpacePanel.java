/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import frechet.Matching;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
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
    private final Matching matching;
    private ImageIcon delaySpaceIcon;
    
    public DelaySpacePanel(Matching matching) {
        this.selectedIndexTraject1 = -1;
        this.selectedIndexTraject2 = -1;
        this.matching = matching;
        try {
            BufferedImage freeSpaceImage = ImageIO.read(new File("delay_space_bats.png"));
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
        return matching.i.length;
    }

    @Override
    public double maxY() {
        return matching.j.length;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Image img = delaySpaceIcon.getImage();
        ImageObserver imgObserver = delaySpaceIcon.getImageObserver();
        g.drawImage(img, 0, 0, imgObserver);
        
        if (selectedIndexTraject1 >= 0 && selectedIndexTraject2 >= 0) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            g.setColor(Color.green);
            Point2D pointOfMatching = new Point2D(selectedIndexTraject1, selectedIndexTraject2);
            Point2D pointInPanel = cartesianToPanelPoint(pointOfMatching);
            int xPoint = roundDouble(pointInPanel.x);
            int yPoint = roundDouble(pointInPanel.y);
            // Drawing the horizontial line.
            g.drawLine(0, yPoint, xPoint, yPoint);
            // Drawing the vertical line.
            g.drawLine(xPoint, getHeight(), xPoint, yPoint);
            
        }
    }
    
}
