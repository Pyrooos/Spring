package com.aluracursos.screenmatch.model;

public enum Category {
    ACTION("Action"),
    ROMANCE("Romance"),
    COMEDY("Comedy"),
    DRAMA("Drama"),
    CRIME("Crime"),;
    private String categoryOmdb;
    Category(String categoryOmbd){
        this.categoryOmdb = categoryOmbd;
    }
/*
This code snippet is a static method within a class. The purpose of this method is to convert a text string into
an instance of the Category enumeration. Therefore, you could call it a "conversion method" or "parsing method."
 */
    public static Category fromString(String text) {
        for (Category category : Category.values()) {
            if (category.categoryOmdb.equalsIgnoreCase(text)) {
                return category;
            }
        }
        throw new IllegalArgumentException("No category found" + text);
    }


}
