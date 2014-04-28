package visualization;

import frechet.Matching;

import javax.swing.*;

/**
 * Created by max on 28-4-14.
 */
public class VisualizationLauncher {

    public static void launchMatchingPlot(final Matching matching) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowMatchingPlot(matching);
            }
        });
    }

    private static void createAndShowMatchingPlot(Matching matching) {
        JFrame frame = new JFrame("Plotting Locally Correct Frechet Matching");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new MatchingPlot(matching));
        frame.pack();
        frame.setVisible(true);
    }
}
