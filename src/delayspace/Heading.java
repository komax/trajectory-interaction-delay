/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package delayspace;

import utils.Utils;

/**
 *
 * @author max
 */
public final class Heading extends DelaySpace {

    public Heading(double[][] trajectory1, double[][] trajectory2) {
        // FIXME passing null yields an exception. Code does not work.
        super(trajectory1, trajectory2);
    }

    @Override
    protected void computeDelaySpace() {
        for (int i = 0; i < trajectory1.length; i++) {
            for (int j = 0; j < trajectory2.length; j++) {
            double[] pointI = trajectory1[i];
            double[] pointJ = trajectory2[j];
            double[] followPointI;
            double[] followPointJ;
            if (i < (trajectory1.length - 1)) {
                followPointI = trajectory1[i + 1];
            } else {
                followPointI = trajectory1[trajectory1.length -1];
            }
            if (j < (trajectory2.length - 1)) {
                followPointJ = trajectory2[j + 1];
            } else {
                followPointJ = trajectory2[trajectory2.length -1];
            }
            double angleI = Utils.computeHeadingAngle(pointI, followPointI);
            double angleJ = Utils.computeHeadingAngle(pointJ, followPointJ);
            double headingValue = 1.0 - Math.cos(angleI - angleJ);
            delaySpace[i][j] = headingValue;
            }
        }
    }

    @Override
    public boolean isDirectional() {
        return true;
    }
}
