package org.calculation;

import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.util.Pair;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Anova {

    public static double[] calculateColumnMeans(double[][] matrix) {
        int numColumns = matrix[0].length; //k
        int numRows = matrix.length; //n
        double[] columnMeans = new double[numColumns];
        for(int i=0;i<numColumns;i++) {
            double sum = 0.0;
            for(int j=0;j<numRows;j++) {
                sum+=matrix[j][i];
            }
            columnMeans[i]=sum/numRows;
        }
        return columnMeans;
    }

    public static double calculateOverallMean(double[][] matrix) {
        int numColumns = matrix[0].length; //k
        int numRows = matrix.length; //n
        double overallMean = 0.0;
        for(int i=0;i<numRows;i++) {
            for(int j=0;j<numColumns;j++) {
                overallMean+=matrix[i][j];
            }
        }
        overallMean/=numRows*numColumns;
        return overallMean;
    }

    public static double calculateSSE(double[][] matrix) {
        double[] columnMean = calculateColumnMeans(matrix);
        int numColumns = matrix[0].length; //k
        int numRows = matrix.length; //n
        double SSE = 0.0;
        for(int j=0;j<numColumns;j++) {
            double mean=columnMean[j];
            for(int i=0;i<numRows;i++) {
                SSE+=pow(matrix[i][j]-mean,2);
            }
        }
        return SSE;
    }

    public static double[] calculateEffects(double[][] matrix){
        int numColumns = matrix[0].length; //k
        double[] columnMean = calculateColumnMeans(matrix);
        double overallMean = calculateOverallMean(matrix);
        double[] effects = new double[numColumns];
        for(int i=0;i<numColumns;i++) {
            effects[i]=columnMean[i]-overallMean;
        }
        return effects;
    }

    public static double calculateSSA(double[][] matrix) {
        double[] effects = calculateEffects(matrix);
        int numColumns = matrix[0].length; //k
        int numRows = matrix.length; //n
        double SSA = 0.0;
        for(int i=0;i<numColumns;i++) {
            SSA+=pow(effects[i],2);
        }
        SSA*=numRows;
        return SSA;
    }

    public static double calculateSST(double[][] matrix) {
        int numColumns = matrix[0].length; //k
        int numRows = matrix.length; //n
        double SST = 0.0;
        double overallMean = calculateOverallMean(matrix);
        for(int i=0;i<numColumns;i++) {
            for(int j=0;j<numRows;j++) {
                SST+=pow(matrix[j][i]-overallMean,2);
            }
        }
        return SST;
    }

    public static double calculateAlternativesVariance(double[][] matrix){
        double SSA = calculateSSA(matrix);
        int numColumns = matrix[0].length; //k
        return SSA/(numColumns-1);
    }

    public static double calculateErrorsVariance(double[][] matrix){
        double SSE = calculateSSE(matrix);
        int numColumns = matrix[0].length; //k
        int numRows = matrix.length; //n
        return SSE/(numColumns*(numRows-1));
    }

    public static double calculateF(double[][] matrix){
        return calculateAlternativesVariance(matrix)/calculateErrorsVariance(matrix);
    }

    public static double getTabulatedF(double[][] matrix){
        int numColumns = matrix[0].length; //k
        int numRows = matrix.length; //n
        FDistribution fDistribution = new FDistribution(numColumns-1, numColumns*(numRows-1));
        return fDistribution.inverseCumulativeProbability(0.95);
    }

    public static boolean compareComputedAndTabulatedF(double computedF, double tabulatedF){
        return computedF > tabulatedF;
    }

    public static double getTDistribution(double[][] matrix){
        int numColumns = matrix[0].length; //k
        int numRows = matrix.length; //n
        TDistribution tDistribution = new TDistribution(numColumns*(numRows-1));
        return tDistribution.inverseCumulativeProbability(0.95);
    }

    public static double calculateContrastVarianceSc(double[][] matrix){
        int numColumns = matrix[0].length; //k
        int numRows = matrix.length; //n
        return sqrt(calculateErrorsVariance(matrix))*sqrt(2.0/(numRows*numColumns));
    }

    public static Pair<Double, Double> calculateContrast(double[][] matrix, int system1, int system2){
        double[] effects = calculateEffects(matrix);
        double c = effects[system1] - effects[system2];
        double sc = calculateContrastVarianceSc(matrix);
        double t = getTDistribution(matrix);
        double c1 = c-t*sc;
        double c2 = c+t*sc;
        return new Pair<Double, Double>(c1,c2);
    }

    public static boolean doesIntervalContainZero(double c1, double c2) {
        return (c1 < 0 && c2 > 0) || (c1 > 0 && c2 < 0);
    }

    public static Pair<Double, Double>[][] calculateAllContrasts(double[][] matrix){
        int numColumns = matrix[0].length; //k
        Pair<Double, Double>[][] contrastMatrix = new Pair[numColumns][numColumns];
        for (int i = 0; i < numColumns; i++) {
            for (int j = i + 1; j < numColumns; j++) {
                contrastMatrix[i][j] = calculateContrast(matrix, i, j);
            }
        }
        return contrastMatrix;
    }

}
