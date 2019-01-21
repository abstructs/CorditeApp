package com.cordite.cordite.Validators;

// contains helper methods for building validation functions
abstract class Validator {
    protected static boolean validLength(String field, int minLength, int maxLength) {
        return field.length() >= minLength && field.length() <= maxLength;
    }
}
