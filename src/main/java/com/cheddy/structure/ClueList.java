package com.cheddy.structure;

import java.util.ArrayList;

/**
 * @author : Cheddy
 */
public class ClueList{

    String title = "", description = "";
    ArrayList<Clue> clues = new ArrayList<>();

    public ClueList(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String title() {
        return title == null? "" : title.trim();
    }

    public void setTitle(String title) {
        this.title = title.trim();
    }

    public String description() {
        return description == null? "" : description.trim();
    }

    public void setDescription(String description) {
        this.description = description.trim();
    }

    public ArrayList<Clue> clues() {
        return clues;
    }

    public void setClues(ArrayList<Clue> clues) {
        this.clues = clues;
    }
}
