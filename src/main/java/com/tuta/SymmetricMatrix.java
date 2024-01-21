package com.tuta;

import java.util.Arrays;

/**
 * <p>
 * Represents a symmetric matrix and provides methods for setting and retrieving values.
 * The matrix is optimized for space by storing only the lower triangular part (excluding the diagonal),
 * as the diagonal values are always assumed to be 1.
 * The matrix is internally stored as a one-dimensional array.
 * </p>
 */
public class SymmetricMatrix {
    /**
     * The one-dimensional array storing the data of the symmetric matrix.
     */
    private final float[] data;

    /**
     * The size of the symmetric matrix (number of rows or columns).
     */
    private final int symMatrixSize;

    /**
     * Constructs a symmetric matrix with the specified size.
     *
     * @param symMatrixSize The size (number of rows or columns) of the symmetric matrix.
     */
    public SymmetricMatrix(int symMatrixSize) {
        // Calculate the size needed for the one-dimensional array
        this.symMatrixSize = symMatrixSize;
        final int auxSize = symMatrixSize * (symMatrixSize - 1) / 2;
        this.data = new float[auxSize];
        Arrays.fill(this.data, Float.NaN);
    }

    /**
     * Returns the one-dimensional array containing the data of the symmetric matrix.
     * The data represents the lower triangular part of the matrix stored in a one-dimensional array.
     *
     * @return The data array of the symmetric matrix.
     */
    public float[] getData() {
        return data;
    }

    /**
     * Sets the value at the specified row and column indices in the symmetric matrix.
     *
     * @param row   The row index.
     * @param col   The column index.
     * @param value The value to be set.
     * @throws IllegalArgumentException If the row or column indices are invalid. Indices must be non-negative
     *                                  and less than the original matrix size.
     */
    public void set(int row, int col, float value) {
        // Ensure row and col are valid indices
        if (row < 0 || col < 0 || row >= symMatrixSize || col >= symMatrixSize) {
            throw new IllegalArgumentException("Invalid indices");
        }

        int index = mapIndex(row, col);
        data[index] = value;
    }

    /**
     * Gets the value at the specified row and column indices in the symmetric matrix.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The value at the specified indices.
     * @throws IllegalArgumentException If the row or column indices are invalid. Indices must be non-negative
     *                                  and less than the original matrix size.
     */
    public float get(int row, int col) {
        // Ensure row and col are valid indices
        if (row < 0 || col < 0 || row >= symMatrixSize || col >= symMatrixSize) {
            throw new IllegalArgumentException("Invalid indices");
        }

        if (row == col) return 1;

        int index = mapIndex(row, col);
        return data[index];
    }

    /**
     * Helper method to map row and col indices to the one-dimensional array index.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The one-dimensional array index corresponding to the row and column indices.
     * @throws IllegalArgumentException If the indices are invalid for the lower triangular part.
     */
    private int mapIndex(int row, int col) {
        if (col == row) {
            throw new IllegalArgumentException("Invalid indices for the lower triangular part");
        }

        // Ensure col is less than row to handle the lower triangular part
        if (col > row) {
            int temp = col;
            col = row;
            row = temp;
        }

        // Calculate the index in the one-dimensional array
        return row * (row - 1) / 2 + col;
    }

    public void printMatrix(boolean fullMatrix) {
        System.out.println("\n--> Matrix");
        for (int i = 0; i < this.symMatrixSize; i++) {
            for (int j = 0; j < this.symMatrixSize; j++) {
                if (fullMatrix || j <= i) System.out.printf("%-8.2f", this.get(i, j));
                else System.out.print("~");
            }
        System.out.println();
        }
    }
}
