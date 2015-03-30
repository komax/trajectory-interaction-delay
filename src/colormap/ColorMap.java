/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package colormap;

import static colormap.ColorCodes.HEATED_BODY_COLOR_CODES;
import static colormap.ColorCodes.LIGHT_GRAY_TO_PURE_BLUE_COLOR_CODES;
import static colormap.ColorCodes.LIGHT_GRAY_TO_PURE_RED_COLOR_CODES;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author max
 */
public class ColorMap {
    // TODO Have a look on the static fields. Restructuring of the colormaps? 
    private final double minValue;
    private final double maxValue;
    private List<Color> colorSpectrum; 
    
    public static Color getColorFromRGB(int red, int green, int blue) {
        return new Color(red, green, blue);
    }
    
    public static Color getColorFromRGB(int colorValue) {
        return new Color(colorValue);
    }
    
    public static Color getColorFromRGB(int red, int green, int blue, int alpha) {
        return new Color(red, green, blue, alpha);
    }
    
    public static Color getColorFromRGB(int colorValue, int alpha) {
        Color colorWithoutAlpha = getColorFromRGB(colorValue);
        int red = colorWithoutAlpha.getRed();
        int green = colorWithoutAlpha.getGreen();
        int blue = colorWithoutAlpha.getBlue();
        return getColorFromRGB(red, green, blue, alpha);
    }
    
    public static ColorMap createColorMap(ColorMapType colorMapType, double minValue,
            double maxValue, int numberOfColors) {
        // TODO support a color map with a particular number of colors
        return null;
    }
    
    public static ColorMap createHeatedBodyColorMap(double minValue, double maxValue) {
        List<Color> heatedColorMap = new ArrayList<>();
        float[][] colorCodes = HEATED_BODY_COLOR_CODES;
        for (int i = 0; i < colorCodes.length; i++) {
            float[] currentColor = colorCodes[i];
            heatedColorMap.add(new Color(currentColor[0], currentColor[1], currentColor[2]));
        }
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
    
    public static ColorMap createRedColormap(double minValue, double maxValue) {
        List<Color> colors = new ArrayList<>();
        colors.add(getColorFromRGB(255, 245, 240));
        colors.add(getColorFromRGB(254, 224, 210));
        colors.add(getColorFromRGB(252, 187, 161));
        colors.add(getColorFromRGB(252, 146, 114));
        colors.add(getColorFromRGB(251, 106, 74));
        colors.add(getColorFromRGB(239, 59, 44));
        colors.add(getColorFromRGB(230, 24, 29));
        colors.add(getColorFromRGB(165, 15, 21));
        colors.add(getColorFromRGB(103, 0, 13));
        return new ColorMap(minValue, maxValue, colors);
    }
    
    public static ColorMap createGreenColormap(double minValue, double maxValue) {
        List<Color> colors = new ArrayList<>();
        colors.add(getColorFromRGB(247, 252, 253));
        colors.add(getColorFromRGB(229, 245, 249));
        colors.add(getColorFromRGB(204, 236, 230));
        colors.add(getColorFromRGB(153, 216, 201));
        colors.add(getColorFromRGB(102, 194, 164));
        colors.add(getColorFromRGB(65, 174, 118));
        colors.add(getColorFromRGB(35, 139, 69));
        colors.add(getColorFromRGB(0, 109, 44));
        colors.add(getColorFromRGB(0, 68, 27));
        return new ColorMap(minValue, maxValue, colors);
    }
    
    public static ColorMap createGrayToRedColormap(double minValue, double maxValue) {
        List<Color> colors = new ArrayList<>();
        int[] colorValues = LIGHT_GRAY_TO_PURE_RED_COLOR_CODES;
        for (int i=colorValues.length-1; i>= 0; i--) {
            colors.add(getColorFromRGB(colorValues[i]));
        }
        return new ColorMap(minValue, maxValue, colors);
    }
    
    public static ColorMap createGrayToBlueColormap(double minValue, double maxValue) {
        List<Color> colors = new ArrayList<>();
        int[] colorValues = LIGHT_GRAY_TO_PURE_BLUE_COLOR_CODES;
        for (int i=colorValues.length-1; i>= 0; i--) {
            colors.add(getColorFromRGB(colorValues[i]));
        }
        return new ColorMap(minValue, maxValue, colors);
    }
    
    public static ColorMap createGrayToRedTransparentColormap(double minValue, double maxValue) {
        List<Color> colors = new ArrayList<>();
        int[] colorValues = LIGHT_GRAY_TO_PURE_RED_COLOR_CODES;
        int alphaValue = 150;
        for (int i=colorValues.length-1; i>= 0; i--) {
            colors.add(getColorFromRGB(colorValues[i], alphaValue));
        }
        return new ColorMap(minValue, maxValue, colors);
    }
    
    public static ColorMap createGrayToBlueTransparentColormap(double minValue, double maxValue) {
        List<Color> colors = new ArrayList<>();
        int[] colorValues = LIGHT_GRAY_TO_PURE_BLUE_COLOR_CODES;
        int alphaValue = 150;
        for (int i=colorValues.length-1; i>= 0; i--) {
            colors.add(getColorFromRGB(colorValues[i], alphaValue));
        }
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
        if (valuesRange() == 0.0) {
            return colorSpectrum.get(0);
        }
        Double expectedIndex = nullBasedValue * numberOfColors / valuesRange();
        int index = expectedIndex.intValue();
        if (index >= numberOfColors) {
            index = numberOfColors - 1;
        }
        return colorSpectrum.get(index);
    }
    
    public Color getMinColor() {
        return getColor(getMinValue());
    }
    
    public Color getMaxColor() {
        return getColor(getMaxValue());
    }
    
    public void halfColorSpectrum() {
        List<Color> newColorSpectrum = new ArrayList<>();
        newColorSpectrum.add(colorSpectrum.get(0));
        for (int i=1; i<colorSpectrum.size(); i++) {
            if (i % 2 != 0) {
                newColorSpectrum.add(colorSpectrum.get(i));
            }
        }
        colorSpectrum = newColorSpectrum;
    }
    
    public double getMinValue() {
        return minValue;
    }
    
    public double getMaxValue() {
        return maxValue;
    }
    
}
