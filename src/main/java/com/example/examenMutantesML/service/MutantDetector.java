package com.example.examenMutantesML.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MutantDetector {
    private static final int SEQUENCE_LENGTH = 4;
    private static final Set<Character> VALID_CHARS = Set.of('A', 'T', 'C', 'G');


    public boolean isMutant(String[] dna) {
        if (dna == null || dna.length == 0) {
            return false;
        }

        final int n = dna.length;
        int sequenceCount = 0;

        char[][] matrix = new char[n][];
        for (int i = 0; i < n; i++) {
            String row = dna[i];
            if (row == null || row.length() != n) {
                return false;
            }
            matrix[i] = row.toCharArray();
            for (char c : matrix[i]) {
                if (c != 'A' && c != 'T' && c != 'C' && c != 'G') {
                    return false; // Carácter inválido
                }
            }
        }

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                if (row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalDescending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH) {
                    if (checkDiagonalAscending(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > 1) return true;
                    }
                }

            }
        }
        return false;
    }

    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row][col + 1] == base &&
                matrix[row][col + 2] == base &&
                matrix[row][col + 3] == base;
    }

    private boolean checkVertical(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col] == base &&
                matrix[row + 2][col] == base &&
                matrix[row + 3][col] == base;
    }

    private boolean checkDiagonalDescending(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row + 1][col + 1] == base &&
                matrix[row + 2][col + 2] == base &&
                matrix[row + 3][col + 3] == base;
    }

    private boolean checkDiagonalAscending(char[][] matrix, int row, int col) {
        final char base = matrix[row][col];
        return matrix[row - 1][col + 1] == base &&
                matrix[row - 2][col + 2] == base &&
                matrix[row - 3][col + 3] == base;
    }
}
