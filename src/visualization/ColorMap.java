/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import java.awt.Color;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author max
 */
public class ColorMap {
    private final double minValue;
    private final double maxValue;
    private final List<Color> colorSpectrum;
    
    public ColorMap(double minValue, double maxValue, List<Color> colorSpectrum) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.colorSpectrum = colorSpectrum;
    }
    
    private double valuesRange() {
        return maxValue - minValue;
    }
    
    private void checkValue(double value) {
        if (value < minValue || value > maxValue) {
            throw new RuntimeException("Value "+value+"is not within the range of ["+minValue+","+maxValue+"]");
        }
    }
    
    public Color getColor(double value) {
        checkValue(value);
        int numberOfColors = colorSpectrum.size();
        double nullBasedValue = value - minValue;
        Double expectedIndex = nullBasedValue * numberOfColors / valuesRange();
        int index = expectedIndex.intValue();
        return colorSpectrum.get(index);
    }
    
}
