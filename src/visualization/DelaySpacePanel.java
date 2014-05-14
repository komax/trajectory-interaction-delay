/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 *
 * @author max
 */
public class DelaySpacePanel extends GenericPlottingPanel {
    private BufferedImage freeSpaceImage;
    
    public DelaySpacePanel() {
        try {
            this.freeSpaceImage = ImageIO.read(new File("delay_space_bats.png"));
            JLabel freeSpaceLabel = new JLabel(new ImageIcon(freeSpaceImage));
            add(freeSpaceLabel);
        } catch (IOException ex) {
            Logger.getLogger(DelaySpacePanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public double maxX() {
        return freeSpaceImage.getWidth();
    }

    @Override
    public double maxY() {
        return freeSpaceImage.getHeight();
    }
    
}
