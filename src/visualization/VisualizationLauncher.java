package visualization;

import frechet.Matching;

import javax.swing.*;
import matlabconversion.MatchingReader;
import utils.DistanceNorm;
import utils.Utils;

/**
 * Created by max on 28-4-14.
 */
public class VisualizationLauncher {
    
    public static void main(String[] args) {
        Matching matching = MatchingReader.readMatching("batsMatching.dump");
        launchMatchingPlot(matching);
        launchDelayPlot(matching, Utils.EuclideanDistance);
    }

    public static void launchMatchingPlot(final Matching matching) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowMatchingPlot(matching);
            }
        });
    }
    
    public static void launchDelayPlot(final Matching matching, final DistanceNorm distance) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowDelayPlot(matching, distance);
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
    
    private static void createAndShowDelayPlot(Matching matching, DistanceNorm distance) {
        JFrame frame = new JFrame("Plotting Normalized Delays");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(new DistancePlotPanel(matching, distance));
        frame.pack();
        frame.setVisible(true);
    }
}
