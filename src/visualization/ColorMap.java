/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package visualization;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author max
 */
public class ColorMap {
    private final double minValue;
    private final double maxValue;
    private List<Color> colorSpectrum;
    
    public static Color getColorFromRGB(int red, int green, int blue) {
        return new Color(red, green, blue);
    }
    
    public static Color getColorFromRGB(int colorValue) {
        return new Color(colorValue);
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
        int[] colorValues = new int[] {
          0x67000D,
 	  0x680613,
 	  0x6A0D18,
 	  0x6B131E,
 	  0x6C1A24,
 	  0x6D202A,
 	  0x6E2630,
 	  0x702D35,
 	  0x71333B,
 	  0x723A41,
 	  0x744046,
 	  0x75464C,
 	  0x764D52,
 	  0x775358,
 	  0x785A5E,
 	  0x7A6063,
 	  0x7B6669,
 	  0x7C6D6F,
 	  0x7E7374,
 	  0x7F7A7A,
          0x808080
        };
        for (int i=colorValues.length-1; i>= 0; i--) {
            colors.add(getColorFromRGB(colorValues[i]));
        }
        return new ColorMap(minValue, maxValue, colors);
    }
    
    public static ColorMap createGrayToBlueColormap(double minValue, double maxValue) {
        List<Color> colors = new ArrayList<>();
        int[] colorValues = new int[] {
          0x023858,
 	  0x083C5A,
 	  0x0F3F5C,
 	  0x15435E,
 	  0x1B4660,
 	  0x224A62,
 	  0x284E64,
 	  0x2E5166,
 	  0x345568,
 	  0x3B586A,
 	  0x415C6C,
 	  0x47606E,
 	  0x4E6370,
 	  0x546772,
 	  0x5A6A74,
 	  0x606E76,
 	  0x677278,
 	  0x6D757A,
 	  0x73797C,
 	  0x7A7C7E,
          0x808080
        };
        for (int i=colorValues.length-1; i>= 0; i--) {
            colors.add(getColorFromRGB(colorValues[i]));
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
        Double expectedIndex = nullBasedValue * numberOfColors / valuesRange();
        int index = expectedIndex.intValue();
        if (index >= numberOfColors) {
            index = numberOfColors - 1;
        }
        return colorSpectrum.get(index);
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
    
}
