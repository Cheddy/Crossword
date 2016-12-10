package com.cheddy.gui;

import com.cheddy.data.Constants;
import com.cheddy.data.Data;
import com.cheddy.structure.Clue;
import com.cheddy.structure.ClueType;
import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.List;

/**
 * @author : Cheddy
 */
public class EditPanel extends JPanel {

    private JTextField word, clue, title, definition;
    private JComboBox<ClueType> typeComboBox;
    private JList<Clue> clueList;
    private DefaultListModel<Clue> listModel;
    private MainFrame frame;

    public EditPanel(MainFrame frame) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.frame = frame;
        listModel = new DefaultListModel<>();
        title = new JTextField(15);
        definition = new JTextField(15);
        word = new JTextField(15);
        word.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                word.selectAll();
            }
        });
        word.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    clue.requestFocus();
                }
            }
        });
        clue = new JTextField(15);
        clue.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                clue.selectAll();
            }
        });


        clueList = new JList<>(listModel);
        clueList.addListSelectionListener(e -> {
            Clue cluee = clueList.getSelectedValue();
            if (cluee != null) {
                word.setText(cluee.word());
                clue.setText(cluee.clue());
                typeComboBox.setSelectedItem(cluee.type());
                word.requestFocus();
                word.selectAll();
            }else{
                word.setText("");
                clue.setText("");
                typeComboBox.setSelectedItem(ClueType.ALL);
                word.requestFocus();
            }
        });

        typeComboBox = new JComboBox(ClueType.values());

        ActionListener saveListener = e -> {
            if(!word.getText().isEmpty() && !clue.getText().isEmpty()) {
                Clue cluee = clueList.getSelectedValue();
                if (cluee != null) {
                    cluee.setWord(word.getText());
                    cluee.setClue(clue.getText());
                    cluee.setType((ClueType) typeComboBox.getSelectedItem());
                } else {
                    cluee = new Clue(word.getText(), clue.getText());
                    cluee.setType((ClueType) typeComboBox.getSelectedItem());
                    listModel.addElement(cluee);
                }
                clueList.clearSelection();
                clueList.repaint();
                word.setText("");
                clue.setText("");
                typeComboBox.setSelectedItem(ClueType.ALL);
                word.requestFocus();
            }
        };

        JButton saveClue = new JButton("Save Clue");
        saveClue.addActionListener(saveListener);

        clue.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    saveListener.actionPerformed(new ActionEvent(saveClue, ActionEvent.ACTION_PERFORMED, "Clicked"));
                }
            }
        });

        JButton deleteClue = new JButton("Delete Clue");
        deleteClue.addActionListener(e -> {
            List<Clue> clues = clueList.getSelectedValuesList();
            for(Clue clue : clues){
                listModel.removeElement(clue);
            }
            clueList.clearSelection();
            clueList.repaint();
            word.setText("");
            clue.setText("");
            word.requestFocus();
        });

        JButton save = new JButton("Save");
        save.addActionListener(e -> {
            frame.clueList().clues().clear();
            Enumeration<Clue> enumeration = listModel.elements();
            while (enumeration.hasMoreElements()){
                frame.clueList().clues().add(enumeration.nextElement());
            }
            frame.clueList().setTitle(title.getText());
            frame.clueList().setDescription(definition.getText());
            Gson gson = new Gson();
            File file = new File(frame.clueList().title() + ".json");
            try {
                Files.write(file.toPath(), gson.toJson(frame.clueList()).getBytes(Constants.DEFAULT_CHARSET));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            Data.instance().setLastUsedFile(file.getAbsolutePath());
            Data.save();
            frame.finishEdit();
        });

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(e -> {
            frame.finishEdit();
        });

        JPanel titleBox = new JPanel();
        titleBox.setLayout(new BoxLayout(titleBox, BoxLayout.X_AXIS));
        titleBox.add(new JLabel("Title: "));
        titleBox.add(title);
        titleBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, title.getPreferredSize().height));

        JPanel definitionBox = new JPanel();
        definitionBox.setLayout(new BoxLayout(definitionBox, BoxLayout.X_AXIS));
        definitionBox.add(new JLabel("Definition: "));
        definitionBox.add(definition);
        definitionBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, definition.getPreferredSize().height));

        JPanel wordBox = new JPanel();
        wordBox.setLayout(new BoxLayout(wordBox, BoxLayout.X_AXIS));
        wordBox.add(new JLabel("Word: "));
        wordBox.add(Box.createHorizontalGlue());
        wordBox.add(word);
        wordBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, word.getPreferredSize().height));

        JPanel clueBox = new JPanel();
        clueBox.setLayout(new BoxLayout(clueBox, BoxLayout.X_AXIS));
        clueBox.add(new JLabel("Clue: "));
        clueBox.add(Box.createHorizontalGlue());
        clueBox.add(clue);
        clueBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, clue.getPreferredSize().height));

        JPanel typeBox = new JPanel();
        typeBox.setLayout(new BoxLayout(typeBox, BoxLayout.X_AXIS));
        typeBox.add(new JLabel("Type: "));
        typeBox.add(Box.createHorizontalGlue());
        typeBox.add(typeComboBox);
        typeBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, clue.getPreferredSize().height));


        JPanel editButtonBox = new JPanel();
        editButtonBox.setLayout(new BoxLayout(editButtonBox, BoxLayout.X_AXIS));
        editButtonBox.add(Box.createHorizontalGlue());
        editButtonBox.add(saveClue);
        editButtonBox.add(Box.createHorizontalStrut(5));
        editButtonBox.add(deleteClue);
        editButtonBox.add(Box.createHorizontalGlue());
        editButtonBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, saveClue.getPreferredSize().height));

        JPanel editBox = new JPanel();
        editBox.setLayout(new BoxLayout(editBox, BoxLayout.Y_AXIS));
        editBox.setBorder(BorderFactory.createEtchedBorder());
        editBox.add(wordBox);
        editBox.add(Box.createVerticalStrut(5));
        editBox.add(clueBox);
        editBox.add(Box.createVerticalStrut(5));
        editBox.add(typeBox);
        editBox.add(editButtonBox);

        JPanel listBox = new JPanel();
        listBox.setLayout(new GridLayout(1, 2, 5, 0));
        listBox.add(new JScrollPane(clueList));
        listBox.add(editBox);

        JPanel buttonBox = new JPanel();
        buttonBox.setLayout(new BoxLayout(buttonBox, BoxLayout.X_AXIS));
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.add(save);
        buttonBox.add(Box.createHorizontalStrut(5));
        buttonBox.add(cancel);
        buttonBox.add(Box.createHorizontalGlue());
        buttonBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, save.getPreferredSize().height));

        add(titleBox);
        add(Box.createVerticalStrut(5));
        add(definitionBox);
        add(Box.createVerticalStrut(5));
        add(listBox);
        add(Box.createVerticalStrut(5));
        add(buttonBox);
        setBorder(BorderFactory.createLoweredBevelBorder());
        reload();
    }

    public void reload(){
        if(frame.clueList() != null){
            listModel.clear();
            for(Clue clue : frame.clueList().clues())
                listModel.addElement(clue);
            title.setText(frame.clueList().title());
            definition.setText(frame.clueList().description());
        }
    }
}
