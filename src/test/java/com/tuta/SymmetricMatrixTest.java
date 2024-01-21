package com.tuta;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SymmetricMatrixTest {

    private SymmetricMatrix symmetricMatrix;
    private int expectedSize;

    @BeforeEach
    void setUp() {
        int size = 2;
        expectedSize = size * (size - 1) / 2;
        symmetricMatrix = new SymmetricMatrix(size);
    }

    @AfterEach
    void printMatrix(){
        symmetricMatrix.printMatrix(false);
    }

    @Test
    void checkConstructor(){
        final float[] oneDimensionArray = symmetricMatrix.getData();
        assertThat(oneDimensionArray).hasSize(expectedSize);
        assertThat(oneDimensionArray).as("Array should be initialized with Float.NaN values")
                .containsOnly(Float.NaN);
    }

    @ParameterizedTest(name = "Test case {index} - Row: {0} Col: {1} Value: {2})")
    @CsvSource({"0, 1, 2f", "1, 0, 3f"})
    void setAndGet(int row, int col, float value) {
        symmetricMatrix.set(row, col, value);
        assertThat(symmetricMatrix.get(row, col)).isEqualTo(value);
    }

    @ParameterizedTest(name = "Test case {index} - Row: {0} Col: {1} Value: {2})")
    @CsvSource({
            "-1, 1, 5f",
            "1, -1, 6f",
            "2, 0, 7f",
            "0, 2, 8f"
    })
    void setInvalidIndices(int row, int col, float value) {
        assertThatThrownBy(() -> symmetricMatrix.set(row, col, value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid indices");
    }

    @ParameterizedTest(name = "Test case {index} - Row: {0} Col: {1}")
    @CsvSource({ "-1, 1", "1, -1", "2, 0", "0, 2" })
    void getInvalidIndices(int row, int col) {
        assertThatThrownBy(() -> symmetricMatrix.get(row, col))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid indices");
    }

    @Test
    void mapIndexInvalidIndices() {
        assertThatThrownBy(() -> symmetricMatrix.set(1, 1, 3f))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid indices for the lower triangular part");
    }
}