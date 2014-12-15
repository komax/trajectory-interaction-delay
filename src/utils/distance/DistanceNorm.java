/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils.distance;

/**
 *
 * @author max
 */
public interface DistanceNorm {
    public double distance(double[] pointP, double[] pointQ);
    public DistanceNormType getType();
}
