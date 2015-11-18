/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package frechet;

import utils.IntTriple;
import utils.Trajectory;
import utils.distance.DistanceNorm;
import utils.distance.DistanceNormFactory;

/**
 *
 * @author max
 */
public class FrechetDistanceDPTriplet {
    private final Trajectory traject1;
    private final Trajectory traject2;
    private final Trajectory traject3;
    private final int lengthX;
    private final int lengthY;
    private final int lengthZ;
    private final IntTriple leftEnd;
    private final double[][][] frechetDistances;
    private final IntTriple[][][] bottleneckIndices;
    
    private static final DistanceNorm EuclideanDistance = DistanceNormFactory.EuclideanDistance;
    
    private static double maxTriplet(double val1, double val2, double val3) {
        double maxVal = Math.max(val1, val2);
        maxVal = Math.max(maxVal, val3);
        return maxVal;
    }
    
    public FrechetDistanceDPTriplet(IntTriple leftEnd, IntTriple rightEnd, Trajectory traject1, Trajectory traject2, Trajectory traject3) {
        this.traject1 = traject1;
        this.traject2 = traject2;
        this.traject3 = traject3;
        
        this.leftEnd = leftEnd;
        
        this.lengthX = rightEnd.i - leftEnd.i  + 1;
        this.lengthY = rightEnd.j - leftEnd.j + 1;
        this.lengthZ = rightEnd.k - leftEnd.k + 1;
        
        this.frechetDistances = new double[lengthX][lengthY][lengthZ];
        this.bottleneckIndices = new IntTriple[lengthX][lengthY][lengthZ];
    }
    
    private double euclideanDistanceTraject12(int i, int j) {
        double[] pointI = traject1.getPoint(i + leftEnd.i);
        double[] pointJ = traject2.getPoint(j + leftEnd.j);
        return EuclideanDistance.distance(pointI, pointJ);
    }

    private double euclideanDistanceTraject23(int j, int k) {
        double[] pointJ = traject2.getPoint(j + leftEnd.j);
        double[] pointK = traject3.getPoint(k + leftEnd.k);
        return EuclideanDistance.distance(pointJ, pointK);
    }
    
    private double euclideanDistanceTraject13(int i, int k) {
        double[] pointI = traject1.getPoint(i + leftEnd.i);
        double[] pointK = traject3.getPoint(k + leftEnd.k);
        return EuclideanDistance.distance(pointI, pointK);
    }
    
    private double longestLeashOnTriplet(int i, int j, int k) {
        double distance12 = euclideanDistanceTraject12(i, j);
        double distance23 = euclideanDistanceTraject23(j, k);
        double distance13 = euclideanDistanceTraject13(i, k);
    //    return maxTriplet(distance12, distance23, distance13);
        return distance12 * distance12 + distance13 * distance13 + distance23 * distance23;
    }
    
    @Deprecated
    private double minPreviousValues(int i, int j, int k) {
        double bestMinValue = Math.min(frechetDistances[i][j][k-1], frechetDistances[i][j-1][k]);
        bestMinValue = Math.min(bestMinValue, frechetDistances[i][j-1][k-1]);
        bestMinValue = Math.min(bestMinValue, frechetDistances[i-1][j][k]);
        bestMinValue = Math.min(bestMinValue, frechetDistances[i-1][j-1][k]);
        bestMinValue = Math.min(bestMinValue, frechetDistances[i-1][j][k-1]);
        bestMinValue = Math.min(bestMinValue, frechetDistances[i-1][j-1][k-1]);
        return bestMinValue;
    }
    
    private IntTriple minPreviousIndices(int i, int j, int k) {
        IntTriple bestParentIndices = IntTriple.NULL_TRIPLE;
        double bestMinValue = Double.MAX_VALUE;
        if (k >= 1 && frechetDistances[i][j][k-1] < bestMinValue && frechetDistances[i][j][k-1] != Double.MIN_VALUE) {
            bestMinValue = frechetDistances[i][j][k-1];
            bestParentIndices = IntTriple.createIntTriple(i, j, k-1);
        }
        if (j >= 1 && frechetDistances[i][j-1][k] < bestMinValue && frechetDistances[i][j-1][k] != Double.MIN_VALUE) {
            bestMinValue = frechetDistances[i][j-1][k];
            bestParentIndices = IntTriple.createIntTriple(i, j-1, k);
        }
        if (j >= 1 && k >= 1 && frechetDistances[i][j-1][k-1] < bestMinValue && frechetDistances[i][j-1][k-1] != Double.MIN_VALUE) {
            bestMinValue = frechetDistances[i][j-1][k-1];
            bestParentIndices = IntTriple.createIntTriple(i, j-1, k-1);
        }
        if (i >= 1 && frechetDistances[i-1][j][k] < bestMinValue && frechetDistances[i-1][j][k] != Double.MIN_VALUE) {
            bestMinValue = frechetDistances[i-1][j][k];
            bestParentIndices = IntTriple.createIntTriple(i-1, j, k);
        }
        if (i >= 1 && j >= 1 && frechetDistances[i-1][j-1][k] < bestMinValue && frechetDistances[i-1][j-1][k] != Double.MIN_VALUE) {
            bestMinValue = frechetDistances[i-1][j-1][k];
            bestParentIndices = IntTriple.createIntTriple(i-1, j-1, k);
        }
        if (i >= 1 && k >= 1 && frechetDistances[i-1][j][k-1] < bestMinValue && frechetDistances[i-1][j][k-1] != Double.MIN_VALUE) {
            bestMinValue = frechetDistances[i-1][j][k-1];
            bestParentIndices = IntTriple.createIntTriple(i-1, j, k-1);
        }
        if (i >= 1 && j >= 1 && k >= 1 && frechetDistances[i-1][j-1][k-1] < bestMinValue && frechetDistances[i-1][j-1][k-1] != Double.MIN_VALUE) {
            bestMinValue = frechetDistances[i-1][j-1][k-1];
            bestParentIndices = IntTriple.createIntTriple(i-1, j-1, k-1);
        }
        //System.out.println("current index = ("+ i +", " + j + ", " + k + ") best parent = " + bestParentIndices + " with value = " + bestMinValue);
        return bestParentIndices;
    }
    
//    private IntTriple minNextIndices(int i, int j, int k) {
//        IntTriple bestParentIndices = IntTriple.createIntTriple(i, j, k+1);
//        double bestMinValue = frechetDistances[i][j][k+1];
//        if (frechetDistances[i][j+1][k] < bestMinValue) {
//            bestMinValue = frechetDistances[i][j+1][k];
//            bestParentIndices = IntTriple.createIntTriple(i, j+1, k);
//        }
//        if (frechetDistances[i][j+1][k+1]  < bestMinValue) {
//            bestMinValue = frechetDistances[i][j+1][k+1];
//            bestParentIndices = IntTriple.createIntTriple(i, j+1, k+1);
//        }
//        if (frechetDistances[i+1][j][k]  < bestMinValue) {
//            bestMinValue = frechetDistances[i+1][j][k];
//            bestParentIndices = IntTriple.createIntTriple(i+1, j, k);
//        }
//        if (frechetDistances[i+1][j+1][k]  < bestMinValue) {
//            bestMinValue = frechetDistances[i+1][j+1][k];
//            bestParentIndices = IntTriple.createIntTriple(i+1, j+1, k);
//        }
//        if (frechetDistances[i+1][j][k+1]  < bestMinValue) {
//            bestMinValue = frechetDistances[i+1][j][k+1];
//            bestParentIndices = IntTriple.createIntTriple(i+1, j, k+1);
//        }
//        if (frechetDistances[i+1][j+1][k+1]  < bestMinValue) {
//            bestMinValue = frechetDistances[i+1][j+1][k+1];
//            bestParentIndices = IntTriple.createIntTriple(i+1, j+1, k+1);
//        }
//        //System.out.println("current index = ("+ i +", " + j + ", " + k + ") best parent = " + bestParentIndices + " with value = " + bestMinValue);
//        return bestParentIndices;
//    }
    
    public void computeFrechetDistance() {
        // Initialization
        // Omit longest leash value for (0,0,0).
        frechetDistances[0][0][0] = Double.MIN_VALUE;

        for (int i = 1; i <= lengthX - 1; i++) {
            frechetDistances[i][0][0] = longestLeashOnTriplet(i, 0, 0);
            bottleneckIndices[i][0][0] = IntTriple.createIntTriple(i, 0, 0);
        }
        
        for (int j = 1; j <= lengthY - 1; j++) {
            frechetDistances[0][j][0] = longestLeashOnTriplet(0, j, 0);
            bottleneckIndices[0][j][0] = IntTriple.createIntTriple(0, j, 0);
        }
                
        for (int k = 1; k <= lengthZ - 1; k++) {
            frechetDistances[0][0][k] = longestLeashOnTriplet(0, 0, k);
            bottleneckIndices[0][0][k] = IntTriple.createIntTriple(0, 0, k);
        }
        
        for (int j = 1; j <= lengthY - 1; j++) {
            for (int k = 1; k <= lengthZ - 1; k++) {
                frechetDistances[0][j][k] = longestLeashOnTriplet(0, j, k);
                bottleneckIndices[0][j][k] = IntTriple.createIntTriple(0, j, k);
            }
        }
        
        for (int i = 1; i <= lengthX - 1; i++) {
            for (int k = 1; k <= lengthZ - 1; k++) {
                frechetDistances[i][0][k] = longestLeashOnTriplet(i, 0, k);
                bottleneckIndices[i][0][k] = IntTriple.createIntTriple(i, 0, k);
            }
        }
        
        for (int i = 1; i <= lengthX - 1; i++) {
            for (int j = 1; j <= lengthY - 1; j++) {
                frechetDistances[i][j][0] = longestLeashOnTriplet(i, j, 0);
                bottleneckIndices[i][j][0] = IntTriple.createIntTriple(i, j, 0);
            }
        }

        // Dynamic Program to compute the Frechet bottleneck.
        for (int i = 1; i <= lengthX - 1; i++) {
            for (int j = 1; j <= lengthY - 1; j++) {
                for (int k = 1; k <= lengthZ - 1; k++) {
                    double frechetEntry;
                    if (i == lengthX-1 && j == lengthY-1 && k == lengthZ-1) {
                        // Skip longest leash value from (n-1, n-1, n-1)
                        frechetEntry = Double.MIN_VALUE;
                    } else {
                        frechetEntry = longestLeashOnTriplet(i, j, k);
                    }
                    IntTriple bestParentIndices = minPreviousIndices(i, j, k);
                    double bestParentVal = frechetDistances[bestParentIndices.i][bestParentIndices.j][bestParentIndices.k];
                    if (bestParentVal > frechetEntry) {
                        bottleneckIndices[i][j][k] = bottleneckIndices[bestParentIndices.i][bestParentIndices.j][bestParentIndices.k];
                        frechetDistances[i][j][k] = bestParentVal;
                    } else {
                        bottleneckIndices[i][j][k] = IntTriple.createIntTriple(i, j, k);
                        frechetDistances[i][j][k] = frechetEntry;
                    }
                }
            }
        }
    }
    
    double getFrechetDistance() {
        // Return the Frechet distance in the last cell.
        return frechetDistances[lengthX - 1][lengthY - 1][lengthZ - 1];
    }
    
    public IntTriple getBottleneck() {
        IntTriple anchorBottleneck = bottleneckIndices[lengthX - 1][lengthY - 1][lengthZ-1];
        IntTriple correctBottleneck = followParentsWithSameFrechetValue(anchorBottleneck);
        return IntTriple.createIntTriple(leftEnd.i + correctBottleneck.i, leftEnd.j + correctBottleneck.j, leftEnd.k + correctBottleneck.k);
    }
    
    public IntTriple getLeftBottleneck() {
        IntTriple anchorBottleneck = bottleneckIndices[lengthX - 1][lengthY - 1][lengthZ-1];
        IntTriple bottleneck = followParentsWithSameFrechetValue(anchorBottleneck);
        IntTriple leftBottleneck = minPreviousIndices(bottleneck.i, bottleneck.j, bottleneck.k);
        if (leftBottleneck  == IntTriple.NULL_TRIPLE) {
            return leftBottleneck;
        }
        return IntTriple.createIntTriple(leftEnd.i + leftBottleneck.i, leftEnd.j + leftBottleneck.j, leftEnd.k + leftBottleneck.k);
    }
    
//    public IntTriple getRightBottleneck() {
//        IntTriple anchorBottleneck = bottleneckIndices[lengthX - 1][lengthY - 1][lengthZ-1];
//        IntTriple bottleneck = followParentsWithSameFrechetValue(anchorBottleneck);
//        IntTriple rightBottleneck = minNextIndices(bottleneck.i, bottleneck.j, bottleneck.k);
//        return IntTriple.createIntTriple(leftEnd.i + rightBottleneck.i, leftEnd.j + rightBottleneck.j, leftEnd.k + rightBottleneck.k);
//    }
    
    private IntTriple followParentsWithSameFrechetValue(IntTriple bottleneckIndex) {
        double frechetBottleneck = getFrechetDistance();
        IntTriple parentIndex = parentWithSameFrechetValue(bottleneckIndex, frechetBottleneck);
        while (parentIndex != IntTriple.NULL_TRIPLE) {
            bottleneckIndex = parentIndex;
            parentIndex = parentWithSameFrechetValue(bottleneckIndex, frechetBottleneck);
        }
        return bottleneckIndex;
    }
    
    private IntTriple parentWithSameFrechetValue(IntTriple bottleneckIndex, double frechetValue) {
        int i = bottleneckIndex.i;
        int j = bottleneckIndex.j;
        int k = bottleneckIndex.k;
        
        if (k >= 1 && frechetDistances[i][j][k-1] ==  frechetValue) {
            return IntTriple.createIntTriple(i, j, k-1);
        }
        if (j >= 1 && frechetDistances[i][j-1][k] ==  frechetValue) {
            return IntTriple.createIntTriple(i, j-1, k);
        }
        if (j >= 1 && k >= 1 && frechetDistances[i][j-1][k-1] == frechetValue) {
            return IntTriple.createIntTriple(i, j-1, k-1);
        }
        if (i >= 1 && frechetDistances[i-1][j][k] == frechetValue) {
            return IntTriple.createIntTriple(i-1, j, k);
        }
        if (i >= 1 && j >= 1 && frechetDistances[i-1][j-1][k] == frechetValue) {
            return IntTriple.createIntTriple(i-1, j-1, k);
        }
        if (i >= 1 && k >= 1 && frechetDistances[i-1][j][k-1] == frechetValue) {
            return IntTriple.createIntTriple(i-1, j, k-1);
        }
        if (i >= 1 && j >= 1 && k >= 1 && frechetDistances[i-1][j-1][k-1] == frechetValue) {
            return IntTriple.createIntTriple(i-1, j-1, k-1);
        }
        return IntTriple.NULL_TRIPLE;
    }
    
}
