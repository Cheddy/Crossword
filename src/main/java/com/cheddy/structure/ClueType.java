package com.cheddy.structure;

/**
 * @author : Cheddy
 */
public enum ClueType {
    ALL,
    CROSSWORD_ONLY,
    QUESTION_ONLY;

    ClueType() {
    }

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase().replace('_', ' ');
    }
}
