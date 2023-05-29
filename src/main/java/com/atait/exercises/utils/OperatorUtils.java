package com.atait.exercises.utils;

import com.atait.exercises.exception.ValidationException;

public class OperatorUtils {

    public static String convertToComparisonOperator(String abbreviation) {
        return switch (abbreviation.toLowerCase()) {
            case "gt" -> ">";
            case "lt" -> "<";
            case "eq" -> "=";
            case "gte" -> ">=";
            case "lte" -> "<=";
            default -> throw new ValidationException("operator supports only [gt],[lt],[eq],[gte],[lte]");
        };
    }
}
