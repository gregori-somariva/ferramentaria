package com.mademil.ferramentaria.enums;

public enum FormType {
    STANDARD,
    MILL,
    DOUBLETURRET;

    public static boolean isValidFormType(String formType) {
        try {
            FormType.valueOf(formType.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false; 
        }
    }
}
