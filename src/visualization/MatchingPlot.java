package visualization;

import frechet.Matching;

import javax.swing.*;
import java.awt.*;

/**
 * Created by max on 25-4-14.
 */
public class MatchingPlot extends JPanel {
    private final Matching matching;
    private final double[][] trajectory1;
    private final double[][] trajectory2;

    public MatchingPlot(Matching matching) {
        super(new BorderLayout());

        // Store data to plot
        this.matching = matching;
        this.trajectory1 = matching.getTrajectory1();
        this.trajectory2 = matching.getTrajectory2();

        // TODO plot them
        // TODO Add panel to plot by calling add()
    }

    public Dimension getPreferredSize() {
        return new Dimension(250, 200);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawString(matching.toString(), 10, 20);
    }
}
