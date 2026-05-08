package com.utils;

public class Utils {

    public static Class<?> mapXmlTypeToJavaType(String dataType) {
        return switch (dataType.toLowerCase()) {
            case "string" -> String.class;
            case "double" -> Double.class;
            case "boolean" -> Boolean.class;
            case "integer" -> Integer.class;
            case "long" -> Long.class;
            case "date" -> java.util.Date.class;
            case "list" -> java.util.List.class;
            default -> Object.class;
        };
    }
}
