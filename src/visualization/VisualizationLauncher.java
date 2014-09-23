package visualization;

import frechet.Matching;

import javax.swing.*;
import matlabconversion.MatchingReader;
import utils.Utils;

/**
 * Created by max on 28-4-14.
 */
public class VisualizationLauncher {
    
    public static void main(String[] args) {
        Matching matching = MatchingReader.readMatching("results/bats/matchingNorm2.dump");
        launchMatchingPlot(matching);
        double[] distances = Utils.distancesOnMatching(matching, Utils.EuclideanDistance);
        launchDistancePlot(matching, distances);
    }

    public static void launchMatchingPlot(final Matching matching) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowMatchingPlot(matching);
            }
        });
    }
    
    public static void launchDistancePlot(final Matching matching, final double[] distances) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowDistancePlot(matching, distances);
            }
        });
    }

    private static void createAndShowMatchingPlot(Matching matching) {
        JFrame frame = new JFrame("Plotting Locally Correct Frechet Matching");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new MatchingPlot(matching, 1, 700));
        frame.pack();
        frame.setVisible(true);
    }
    
    private static void createAndShowDistancePlot(Matching matching, double[] distances) {
        JFrame frame = new JFrame("Plotting Normalized Delays");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new DistancePlotPanel(matching, distances));
        frame.pack();
        frame.setVisible(true);
    }
}
