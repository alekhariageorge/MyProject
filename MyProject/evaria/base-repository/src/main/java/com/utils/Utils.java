package com.utils;

public class Utils {

    public static Class<?> mapXmlTypeToJavaType(String dataType) {
        if (dataType == null || dataType.isBlank()) {
            return Object.class;
        }

        return switch (dataType.toLowerCase()) {
            case "string" -> String.class;
            case "double" -> Double.class;
            case "boolean" -> Boolean.class;
            case "int", "integer" -> Integer.class;
            case "long" -> Long.class;
            case "date", "timestamp" -> java.util.Date.class;
            case "list", "array" -> java.util.List.class;
            default -> Object.class;
        };
    }
}