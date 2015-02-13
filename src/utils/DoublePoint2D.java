/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author max
 */
public class DoublePoint2D {
    public final double x;
    public final double y;

    public DoublePoint2D(double x, double y) {
        this.x = x;
        this.y = y;
    }
    
    public IntPoint2D toIntPoint2D() {
        int roundedX = Utils.roundDouble(x);
        int roundedY = Utils.roundDouble(y);
        return new IntPoint2D(roundedX, roundedY);
    }
}
