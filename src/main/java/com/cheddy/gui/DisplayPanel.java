package com.cheddy.gui;

import com.cheddy.structure.Crossword;

import javax.swing.*;
import java.awt.*;

/**
 * @author : Cheddy
 */
public class DisplayPanel extends JPanel{


    private JScrollPane clueScrollPane;
    private MainFrame frame;
    private Crossword crossword;
    private JPanel gridPanel;
    private JButton checkButton, regenButton;
    private JToggleButton showAnswersToggle;
    private JTextArea clueArea;
    private int width, height;

    public DisplayPanel(MainFrame frame, int width, int height) {
        this.frame = frame;
        this.width = width;
        this.height = height;
        setLayout(new BorderLayout());
        gridPanel = new JPanel(new GridLayout(height,width));
        gridPanel.setPreferredSize(new Dimension(width * 25, height * 25));
        gridPanel.setMaximumSize(new Dimension(width * 25, height * 25));
        checkButton = new JButton("Check Answers");
        checkButton.addActionListener(e -> {
            for(int i = 0 ; i < gridPanel.getComponentCount(); i++){
                CrosswordTextField field = (CrosswordTextField) gridPanel.getComponent(i);
                if(!field.correct()){
                    JOptionPane.showMessageDialog(this, "You were unsuccessful this time!", "Failure", JOptionPane.PLAIN_MESSAGE);
                    return;
                }
            }
            JOptionPane.showMessageDialog(this, "You were successful this time!", "Success", JOptionPane.PLAIN_MESSAGE);
        });

        showAnswersToggle = new JToggleButton("Show Answers");
        showAnswersToggle.addItemListener(e -> {
            if(showAnswersToggle.isSelected()){
                checkButton.setEnabled(false);
                showAnswersToggle.setText("Hide Answers");
            }else{
                showAnswersToggle.setText("Show Answers");
                checkButton.setEnabled(true);
            }
            for(int i = 0 ; i < gridPanel.getComponentCount(); i++){
                ((CrosswordTextField) gridPanel.getComponent(i)).toggleAnswers();
            }
        });

        regenButton = new JButton("Regenerate");
        regenButton.addActionListener(e -> reload());

        JPanel buttonArea = new JPanel(new FlowLayout());
        buttonArea.add(checkButton);
        buttonArea.add(showAnswersToggle);
        buttonArea.add(regenButton);

        clueArea = new JTextArea();
        clueArea.setEditable(false);
        clueArea.setLineWrap(true);
        clueScrollPane = new JScrollPane(clueArea);
        clueScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(gridPanel, BorderLayout.NORTH);
        mainPanel.add(clueScrollPane, BorderLayout.CENTER);

        add(buttonArea, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        clueArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

    }

    public void reload(){
        gridPanel.removeAll();
        showAnswersToggle.setSelected(false);
        crossword = new Crossword(width, height);
        crossword.generate(frame.clueList());
        for(int i = 0; i < height; i++){
            for(int q = 0; q < width; q++){
                int number = -1;
                for(Crossword.CrosswordClue clue : crossword.clues){
                    if(clue.x() == q && clue.y() == i){
                        number = clue.number();
                        break;
                    }
                }
                gridPanel.add(new CrosswordTextField(this, crossword.grid[i][q], number, i, q));
            }
        }
        clueArea.setText(crossword.crosswordClueString());
        gridPanel.revalidate();
        SwingUtilities.invokeLater(() -> clueScrollPane.getVerticalScrollBar().setValue(0));
    }

    public CrosswordTextField fieldAt(int row, int col){
        return (CrosswordTextField) gridPanel.getComponent(row * height + col);
    }

    public void transferFocus(CrosswordTextField field){
        int row = field.row();
        int col = field.col();
        if(field.vertical()) {
            int newRow = row + 1;
            if (newRow < height && col < width){
                CrosswordTextField newField = fieldAt(newRow, col);
                if(newField != null && newField.correctText() != 0){
                    newField.setVertical(true);
                    newField.requestFocus();
                    return;
                }
            }
            int newCol = col + 1;
            if (newCol < width && row < height){
                CrosswordTextField newField = fieldAt(row, newCol);
                if(newField != null && newField.correctText() != 0){
                    newField.setVertical(false);
                    newField.requestFocus();
                    return;
                }
            }
        }else{
            int newCol = col + 1;
            if (newCol < width && row < height){
                CrosswordTextField newField = fieldAt(row, newCol);
                if(newField != null && newField.correctText() != 0){
                    newField.setVertical(false);
                    newField.requestFocus();
                    return;
                }
            }
            int newRow = row + 1;
            if (newRow < height && col < width){
                CrosswordTextField newField = fieldAt(newRow, col);
                if(newField != null && newField.correctText() != 0){
                    newField.setVertical(true);
                    newField.requestFocus();
                    return;
                }
            }
        }
        field.transferFocus();
    }

}
