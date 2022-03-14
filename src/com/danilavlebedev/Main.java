package com.danilavlebedev;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Main {

    /**
     * Generator of two random matrices.
     */
    public static List<int[][]> generateMatrix(int rows, int cols) {
        Random random = new Random();
        List<int[][]> matrices = new ArrayList<>();
        int[][] matrix1 = new int[rows][cols];
        int[][] matrix2 = new int[cols][rows];
        matrices.add(matrix1);
        matrices.add(matrix2);
        for (int[][] matrix : matrices) {
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    matrix[i][j] = random.nextInt(100);
                }
            }
        }
        return matrices;
    }

    /**
     * Ordinary matrices multiplication.
     */
    public static int[][] multiply(int[][] matrix1, int[][] matrix2) {
        int[][] matrix3 = new int[matrix1.length][matrix2[0].length];
        for (int i = 0; i < matrix3.length; i++) {
            for (int j = 0; j < matrix3[i].length; j++) {
                int cell = 0;
                for (int k = 0; k < matrix2.length; k++) {
                    cell += matrix1[i][k] * matrix2[k][j];
                }
                matrix3[i][j] = cell;
            }
        }
        return matrix3;
    }

    /**
     * The total amount of threads is equal to the amount of rows in the first matrix.
     * If it is more than the available amount of threads, the total amount equates to the available amount.
     * In this case we may have a remainder of the division, which means the amount of rows,
     * that will not be calculated, so that number of additional rows is being given to the first thread.
     */
    public static int[][] threadMultiply(int[][] matrix1, int[][] matrix2) {
        int[][] matrix3 = new int[matrix1.length][matrix2[0].length];
        int threadsCount = matrix1.length;
        int rowsPerThread = 1;
        int remainingRows = 0;
        if (threadsCount > Runtime.getRuntime().availableProcessors()) {
            threadsCount = Runtime.getRuntime().availableProcessors();
            rowsPerThread = matrix1.length / threadsCount;
            remainingRows = matrix1.length % threadsCount;
        }
        ThreadMultiplier[] threads = new ThreadMultiplier[threadsCount];
        int firstRow = 0;
        for (int i = 0; i < threadsCount; i++) {
            int lastRow = firstRow + rowsPerThread;
            if (i == 0) {
                lastRow += remainingRows;
            }
            threads[i] = new ThreadMultiplier(matrix1, matrix2, matrix3, firstRow, lastRow);
            threads[i].start();
            firstRow = lastRow;
        }
        try {
            for (ThreadMultiplier thread : threads) {
                thread.join();
            }
        } catch (InterruptedException e) {
            System.out.println("Multiplying was interrupted!");
        }
        return matrix3;
    }

    /**
     * You can generate random matrices or use matrix1 and matrix2, in which case
     * the result must be {{26, 12, 43, 12}, {17, 10, 30, 17}, {36, 16, 59, 14}}.
     */
    public static void main(String[] args) {
//        int[][] matrix1 = {{1, 5}, {2, 3}, {1, 7}};
//        int[][] matrix2 = {{1, 2, 3, 7}, {5, 2, 8, 1}};
//        List<int[][]> matrices = new ArrayList<>();
//        matrices.add(matrix1);
//        matrices.add(matrix2);
        List<int[][]> matrices = generateMatrix(5, 10);
        int[][] matrix3 = multiply(matrices.get(0), matrices.get(1));
        int[][] matrix3Thread = threadMultiply(matrices.get(0), matrices.get(1));
        for (int i = 0; i < matrices.size(); i++) {
            System.out.println("Matrix " + (i + 1) + ": \n" + Arrays.deepToString(matrices.get(i)));
        }
        System.out.println("Calculated by ordinary multiplication: \n" + Arrays.deepToString(matrix3));
        System.out.println("Calculated by thread multiplication: \n" + Arrays.deepToString(matrix3Thread));
        System.out.println("Results are equal: " + Arrays.deepToString(matrix3).equals(Arrays.deepToString(matrix3Thread)));
    }
}
