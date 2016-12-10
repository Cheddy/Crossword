package com.cheddy.structure;

/**
 * @author : Cheddy
 */
public class Clue {


    String word;
    String clue;
    ClueType type = ClueType.ALL;

    public Clue(String word, String clue) {
        this.word = word;
        this.clue = clue;
    }

    public Clue(String word, String clue, ClueType type) {
        this.word = word;
        this.clue = clue;
        this.type = type;
    }

    public String word() {
        return word.trim();
    }

    public void setWord(String word) {
        this.word = word.trim();
    }

    public String clue() {
        return clue.trim();
    }

    public void setClue(String clue) {
        this.clue = clue.trim();
    }

    public ClueType type() {
        return type;
    }

    public void setType(ClueType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return word();
    }
}
