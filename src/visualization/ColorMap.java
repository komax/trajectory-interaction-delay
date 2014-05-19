/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author max
 */
public class ColorMap {
    private final double minValue;
    private final double maxValue;
    private final List<Color> colorSpectrum;
    
    public static Color getColorFromRGB(int red, int green, int blue) {
        return new Color(red, green, blue);
    }
    
    public static ColorMap createHeatedBodyColorMap(double minValue, double maxValue) {
        // TODO create colorSpectrum
        List<Color> heatedColorMap = null;
        return new ColorMap(minValue, maxValue, heatedColorMap);
    }
    
    public static ColorMap createBlueColormap(double minValue, double maxValue) {
        List<Color> colors = new ArrayList<>();
        colors.add(getColorFromRGB(255, 247, 251));
        colors.add(getColorFromRGB(236, 231, 242));
        colors.add(getColorFromRGB(208, 209, 230));
        colors.add(getColorFromRGB(166, 189, 219));
        colors.add(getColorFromRGB(116, 169, 207));
        colors.add(getColorFromRGB(54, 144, 192));
        colors.add(getColorFromRGB(5, 112, 176));
        colors.add(getColorFromRGB(4, 90, 141));
        colors.add(getColorFromRGB(2, 56, 88));
        return new ColorMap(minValue, maxValue, colors);
    }

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
