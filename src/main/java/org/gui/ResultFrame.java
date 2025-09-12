package org.gui;

import javax.swing.*;
import java.awt.*;

import org.apache.commons.math3.util.Pair;
import org.calculation.*;


public class ResultFrame extends JFrame {

    public ResultFrame(){
        JFrame resultFrame = new JFrame("ANOVA Analysis Results");
        resultFrame.setSize(500, 500);
        resultFrame.setLocationRelativeTo(null);
        resultFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        resultFrame.setVisible(true);

        JTextArea resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Arial", Font.PLAIN, 14));
        resultArea.setBackground(Color.decode("#DFD4BD"));
        JScrollPane scrollPane = new JScrollPane(resultArea);

        String testResult;
        double calculatedF = Anova.calculateF(Main.matrix);
        double tabulatedF = Anova.getTabulatedF(Main.matrix);
        if (Anova.compareComputedAndTabulatedF(calculatedF, tabulatedF)) {
            testResult = "There is a statistically significant difference between alternatives.";
        } else {
            testResult = "There is no statistically significant difference between alternatives.";
        }
        Pair<Double, Double>[][] contrastMatrix = Anova.calculateAllContrasts(Main.matrix);
        StringBuilder contrastsResult = new StringBuilder();
        for (int i = 0; i < contrastMatrix.length; i++) {
            for (int j = 0; j < contrastMatrix[i].length; j++) {
                if (i != j && contrastMatrix[i][j] != null) {
                    Pair<Double, Double> element = contrastMatrix[i][j];
                    contrastsResult.append(i + 1)
                            .append(" - ").append(j + 1)
                            .append(": [").append(String.format("%.4f", element.getFirst()))
                            .append(", ").append(String.format("%.4f", element.getSecond()))
                            .append("] -> ");
                    if (Anova.doesIntervalContainZero(element.getFirst(), element.getSecond())) {
                        contrastsResult.append("The difference between alternatives is not statistically significant\n");
                    }
                    else {
                        contrastsResult.append("The difference between alternatives is statistically significant\n");
                    }
                }
            }
        }

        resultArea.setText(
                "ANOVA Analysis Results:\n" +
                        "Number of alternatives: " + Main.matrix[0].length + "\n" +
                        "Number of measurements: " + Main.matrix.length + "\n" +
                        "Degrees of freedom: " + "\n" +
                        "     alternatives -> " + (Main.matrix[0].length - 1) + "\n" +
                        "     errors -> " + Main.matrix[0].length * (Main.matrix.length - 1) + "\n" +
                        "     total -> " + (Main.matrix[0].length * Main.matrix.length - 1) + "\n" +
                        "SSA: " + String.format("%.4f", Anova.calculateSSA(Main.matrix)) + "\n" +
                        "SSE: " + String.format("%.4f", Anova.calculateSSE(Main.matrix)) + "\n" +
                        "SST: " + String.format("%.4f", Anova.calculateSST(Main.matrix)) + "\n" +
                        "Alternatives variance: " + String.format("%.4f", Anova.calculateAlternativesVariance(Main.matrix)) + "\n" +
                        "Errors variance: " + String.format("%.4f", Anova.calculateErrorsVariance(Main.matrix)) + "\n" +
                        "Calculated F value: " + String.format("%.4f", Anova.calculateF(Main.matrix)) + "\n" +
                        "Tabulated F value: " + String.format("%.4f", Anova.getTabulatedF(Main.matrix)) + "\n" +
                        "--------------------------------------------------------\n" +
                        testResult + "\n\n" +
                        "CONTRASTS: \n" + contrastsResult
        );

        resultFrame.add(scrollPane);
    }

}
