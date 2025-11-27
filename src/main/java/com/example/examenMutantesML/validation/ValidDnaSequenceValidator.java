package com.example.examenMutantesML.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidDnaSequenceValidator implements ConstraintValidator<ValidDnaSequence, String[]> {

    @Override
    public boolean isValid(String[] dna, ConstraintValidatorContext context) {
        if (dna == null || dna.length == 0) {
            return false;
        }

        int n = dna.length;

        Pattern pattern = Pattern.compile("^[ATCG]+$");

        final int MIN_SIZE = 4;
        if (n < MIN_SIZE) {
            return false;
        }

        for (String row : dna) {
            if (row == null || row.length() != n) return false; //No es matriz NxN
            if (!pattern.matcher(row).matches()) return false;

            for (char c : row.toCharArray()) {
                if (c != 'A' && c != 'T' && c != 'C' && c != 'G') {
                    return false;
                }
            }
        }
        return true;
    }
}
