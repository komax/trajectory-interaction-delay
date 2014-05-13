/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import frechet.Matching;
import utils.Utils;

/**
 *
 * @author max
 */
public class DelayPlotPanel extends GenericPlottingPanel {
    private final double[] normalizedDelay;
    
    public DelayPlotPanel(Matching matching) {
        double[] delaysWithEuclideanNorm = Utils.delayWithEuclideanNorm(matching);
        this.normalizedDelay = Utils.normalizedDelay(delaysWithEuclideanNorm);
    }

    @Override
    public double maxX() {
        return 1.0;
    }

    @Override
    public double maxY() {
        return normalizedDelay.length;
    }
    
}
