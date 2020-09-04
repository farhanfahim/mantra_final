package com.tekrevol.mantra.enums;

public enum CategoryType {
    MEDICALHISTORY,
    FAMILYHISTORY,
    SOCIALHISTORY,
    SMOKINGBEHAVIOR;

    public String canonicalForm() {
        return this.name();
    }

    public static CategoryType fromCanonicalForm(String canonical) {
        return valueOf(CategoryType.class, canonical);
    }
}