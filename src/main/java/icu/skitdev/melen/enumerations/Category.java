package icu.skitdev.melen.enumerations;

public enum Category {
    UTILITAIRE("Utilitaire"),
    INFORMATIVE("Informative"),
    MODERATION("Modération"),
    FUN("Fun"),
    AUTRE("Autre");

    private final String name;

    Category(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
