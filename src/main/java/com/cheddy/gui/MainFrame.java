package com.cheddy.gui;

import com.cheddy.data.Constants;
import com.cheddy.data.Data;
import com.cheddy.structure.ClueList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author : Cheddy
 */
public class MainFrame extends JFrame {

    public EditPanel editPanel;
    public DisplayPanel displayPanel;
    public JPanel contentPane;
    public CardLayout layout;

    ClueList clueList;

    public MainFrame() {
        setLayout(new GridLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        displayPanel = new DisplayPanel(this, 20 , 20);
        editPanel = new EditPanel(this);

        layout = new CardLayout();

        contentPane = new JPanel();
        contentPane.setLayout(layout);
        setContentPane(contentPane);

        contentPane.add(editPanel, "Edit");
        contentPane.add(displayPanel, "Display");
        layout.show(contentPane, "Edit");


        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem openMenuItem = new JMenuItem("Open");
        openMenuItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser(Constants.HOME_DIRECTORY.getAbsolutePath());
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                load(file);
            }
        });

        fileMenu.add(openMenuItem);
        menuBar.add(fileMenu);

        String lastFile = Data.instance().lastUsedFile();
        if(lastFile != null){
            load(new File(lastFile));
        }

        setJMenuBar(menuBar);
        setPreferredSize(new Dimension(1300, 700));
        setMinimumSize(new Dimension(500, 400));
        pack();
        setVisible(true);
    }

    public void load(File file){
        if (file != null && file.exists()) {
            Data.instance().setLastUsedFile(file.getAbsolutePath());
            Data.save();
            try {
                byte[] bytes = Files.readAllBytes(file.toPath());
                if (bytes != null) {
                    Gson gson = new GsonBuilder().setLenient().create();
                    clueList = gson.fromJson(new String(bytes, Constants.DEFAULT_CHARSET), ClueList.class);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        if (clueList == null) {
            clueList = new ClueList("", "");
        }
        editPanel.reload();
    }

    public void finishEdit() {
        displayPanel.reload();
        layout.show(contentPane, "Display");
    }

    public ClueList clueList() {
        return clueList;
    }

}
