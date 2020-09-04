package com.tekrevol.mantra.enums;

public enum DBModelTypes {
    SCHEDULED_MANTRA;


    public String canonicalForm() {
        return this.name().toLowerCase();
    }


    public static DBModelTypes fromCanonicalForm(String canonical) {
        return valueOf(DBModelTypes.class, canonical.toUpperCase());
    }
}