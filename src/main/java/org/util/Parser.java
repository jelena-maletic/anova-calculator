package org.util;

public class Parser {

    public static double[][] parseAndTransposeMatrix(String input) {
        if (input.trim().isEmpty()) {
            return null;
        }
        try {
            String[] rows = input.split("\n");
            int numRows = rows.length;
            int numColumns = -1;
            double[][] matrix = null;
            for (int i = 0; i < numRows; i++) {
                String[] columns = rows[i].trim().split(",");
                if (numColumns == -1) {
                    numColumns = columns.length;
                    matrix = new double[numColumns][numRows];
                }
                if (columns.length != numColumns) {
                    return null;
                }
                for (int j = 0; j < numColumns; j++) {
                    try {
                        matrix[j][i] = Double.parseDouble(columns[j].trim());
                    }
                    catch (NumberFormatException e) {
                        return null;
                    }
                }
            }
            return matrix;
        }
        catch (Exception e) {
            return null;
        }
    }
}
