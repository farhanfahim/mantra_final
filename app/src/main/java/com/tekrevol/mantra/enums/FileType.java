package com.tekrevol.mantra.enums;

public enum FileType {
    IMAGE,
    VIDEO,
    AUDIO,
    DOCUMENT;


    public String canonicalForm() {
        return this.name().toLowerCase();
    }

    public static FileType fromCanonicalForm(String canonical) {
        return valueOf(FileType.class, canonical.toUpperCase());
    }
}