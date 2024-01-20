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
    private final int size;

    /**
     * Constructs a symmetric matrix with the specified size.
     *
     * @param size The size (number of rows or columns) of the symmetric matrix.
     */
    public SymmetricMatrix(int size) {
        // Calculate the size needed for the one-dimensional array
        this.size = size * (size - 1) / 2;
        this.data = new float[this.size];
        Arrays.fill(this.data, Float.NaN);
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
        if (row < 0 || col < 0 || row >= size || col >= size) {
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
        if (row < 0 || col < 0 || row >= size || col >= size) {
            throw new IllegalArgumentException("Invalid indices");
        }

        int index = mapIndex(row, col);
        return (row == col) ? 1 : data[index];
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
}
