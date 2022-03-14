package com.danilavlebedev;

public class ThreadMultiplier extends Thread {
    private final int firstRow;
    private final int lastRow;
    private final int[][] matrix1;
    private final int[][] matrix2;
    private final int[][] matrix3;

    public ThreadMultiplier(int[][] matrix1, int[][] matrix2, int[][] matrix3, int firstRow, int lastRow) {
        this.matrix1 = matrix1;
        this.matrix2 = matrix2;
        this.matrix3 = matrix3;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
    }

    /**
     * Calculating cells of the specified rows.
     */
    @Override
    public void run() {
        for (int i = firstRow; i < lastRow; i++) {
            for (int j = 0; j < matrix3[i].length; j++) {
                matrix3[i][j] = calculate(i, j);
            }
        }
    }

    /**
     * Calculating cell values.
     */
    private int calculate(int i, int j) {
        int cell = 0;
        for (int k = 0; k < matrix2.length; k++) {
            cell += matrix1[i][k] * matrix2[k][j];
        }
        return cell;
    }
}
