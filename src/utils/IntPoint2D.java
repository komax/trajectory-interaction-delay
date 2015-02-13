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
public class IntPoint2D {

    public final int x;
    public final int y;
    
    public static IntPoint2D createIntPoint2D(double x, double y) {
        return new IntPoint2D(Utils.roundDouble(x), Utils.roundDouble(y));
    }

    public IntPoint2D(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public DoublePoint2D toDoublePoint() {
        return new DoublePoint2D((double) x, (double) y);
    }
}
