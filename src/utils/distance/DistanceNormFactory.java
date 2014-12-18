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
public class DistanceNormFactory {
    public static final DistanceNorm EuclideanDistance = new AbstractPNorm() {

        @Override
        public int getOrder() {
            return 2;
        }

        @Override
        public DistanceNormType getType() {
            return DistanceNormType.EUCLIDEAN;
        }
    };

    public static final DistanceNorm L1Distance = new AbstractPNorm() {

        @Override
        public int getOrder() {
            return 1;
        }

        @Override
        public DistanceNormType getType() {
            return DistanceNormType.L1_NORM;
        }
    };

    public static final DistanceNorm LInfDistance = new DistanceNorm() {

        @Override
        public double distance(double[] pointP, double[] pointQ) {
            double maxDistance = Double.MIN_VALUE;
            for (int i = 0; i < pointP.length; i++) {
                double diff = Math.abs(pointP[i] - pointQ[i]);
                maxDistance = Math.max(maxDistance, diff);
            }
            return maxDistance;
        }
        
        @Override
        public String toString() {
            return "NormInf";
        }

        @Override
        public DistanceNormType getType() {
            return DistanceNormType.LInf_Norm;
        }
    };

    public static DistanceNorm selectDistanceNorm(int order) {
        switch (order) {
            case 1:
                return L1Distance;
            case 2:
                return EuclideanDistance;
            case Integer.MAX_VALUE:
                return LInfDistance;
            default:
                throw new RuntimeException("order=" + order + " is not a valid norm");
        }
    }

    public static DistanceNorm getDistanceNorm(DistanceNormType normType) {
        switch(normType) {
            case L1_NORM:
                return L1Distance;
            case EUCLIDEAN:
                return EuclideanDistance;
            case LInf_Norm:
                return LInfDistance;
            default:
                throw new RuntimeException("normtype=" + normType + "is not supported");
        }
    }
}
