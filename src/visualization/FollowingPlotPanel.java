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

/**
 *
 * @author max
 */
public class FollowingPlotPanel extends GenericPlottingPanel {
    private final int[] delaysInTimestamps;
    private final int lengthMatching;
    private int maxDelay;
    private final Matching matching;
    
    public FollowingPlotPanel(Matching matching) {
        this.matching = matching;
        this.lengthMatching = matching.i.length;
        this.delaysInTimestamps = utils.Utils.delayInTimestamps(matching);
        this.maxDelay = Integer.MIN_VALUE;
        for (int delay: delaysInTimestamps) {
            if (delay > maxDelay) {
                maxDelay = delay;
            }
        }
    }
    
    @Override
    public double maxX() {
        return lengthMatching;
    }

    @Override
    public double maxY() {
        // 2x for negative and postive value range.
        return 2 * maxDelay;
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 300);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        // Draw zero-axis.
        Stroke oldStroke = g2.getStroke();
        float dash1[] = {10.0f};
        BasicStroke dashed = new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, dash1, 0.0f);
        g2.setStroke(dashed);
        g.drawLine(0, maxDelay, getWidth(), maxDelay);
        
        // Restore old stroke style.
        g2.setStroke(oldStroke);
        
        for (int k=0; k<lengthMatching; k++) {
            boolean traj1IsAhead = matching.i[k] > matching.j[k];
            boolean traj2IsAhead = matching.j[k] > matching.i[k];
            int yCoord = maxDelay;
            if (traj1IsAhead) {
                // Color that trajectory 1 is ahead.
                g.setColor(Color.blue);
                yCoord += delaysInTimestamps[k];
            } else if (traj2IsAhead) {
                // Color that trajectory 2 is ahead.
                g.setColor(Color.red);
                yCoord -= delaysInTimestamps[k];
            } else {
                // No delay is detected.
                g.setColor(Color.gray);
            }
            // Draw data point into the plot.
            g.drawLine(k, yCoord, k, yCoord);
        }
    }
    
}
