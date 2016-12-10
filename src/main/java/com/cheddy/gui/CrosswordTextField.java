package com.cheddy.gui;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author : Cheddy
 */
public class CrosswordTextField extends JTextField {

    private int number;
    private char correctText;
    private  int row, col;
    private boolean vertical;
    private DisplayPanel parent;
    private String userText;
    private boolean showAnswers;

    public CrosswordTextField(DisplayPanel parent, char correctText, int number, int row, int col) {
        this.correctText = correctText;
        this.number = number;
        this.parent = parent;
        this.row = row;
        this.col = col;
        this.vertical = false;
        this.showAnswers = false;
        this.userText = "";

        setDocument(new LimitDocument(1));
        setHorizontalAlignment(CENTER);
        if(correctText == 0){
            setBackground(Color.BLACK);
            setForeground(Color.BLACK);
            setFocusable(false);
            setEditable(false);
        }

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                selectAll();
                if(e.getOppositeComponent() instanceof CrosswordTextField){
                    CrosswordTextField old = (CrosswordTextField) e.getOppositeComponent();
                    if(old.col() == col() && old.row() == row() - 1){
                        setVertical(true);
                        return;
                    }
                }
                setVertical(false);
            }
        });
        setSize(new Dimension(25, 25));
    }

    public char correctText() {
        return correctText;
    }

    public int row() {
        return row;
    }

    public int col() {
        return col;
    }

    public boolean vertical() {
        return vertical;
    }

    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    @Override
    public void paintChildren(Graphics g) {
        super.paintChildren(g);
        if(number != -1) {
            int fontHeight = g.getFontMetrics().getAscent();
            g.drawString(number + ".", 3, fontHeight + 1);
        }
    }

    public boolean correct(){
        if(correctText == 0)
            return true;
        String text = getText();
        if(text != null && text.length() == 1){
            return text.charAt(0) == correctText;
        }
        return false;
    }

    public void toggleAnswers(){
        if(showAnswers){
            showAnswers = false;
            setText(userText);
            setEditable(true);
        }else{
            showAnswers = true;
            userText = getText();
            setText(String.valueOf(correctText));
            setEditable(false);
        }
    }

    public boolean showAnswers() {
        return showAnswers;
    }

    private class LimitDocument extends PlainDocument{

        private int limit;
        public LimitDocument(int limit) {
            this.limit = limit;
        }

        @Override
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if(str == null)
                return;
            if(getLength() + str.length() <= limit) {
                super.insertString(offs, str.toUpperCase(), a);
                parent.transferFocus(CrosswordTextField.this);
            }
        }
    }
}
