package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SnakeGame extends JFrame {

    private JPanel pnlSettings;
    private JPanel pnlMain;
    private Graphics pnlGraphic;
    private JLabel lbScore;
    private JRadioButton EASYRadioButton;
    private JRadioButton NORMALRadioButton;
    private JRadioButton HARDRadioButton;
    private JRadioButton wallCollision;
    private JPanel wallPanel;

    int speed;

    public SnakeGame() {

        this.setTitle("Snake Game");
        setContentPane(pnlMain);
        setMinimumSize(new Dimension(500, 600));
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setLocationRelativeTo(null);

        setSpeed();
        NORMALRadioButton.doClick();

        wallCollision.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (wallCollision.isSelected()) {
                    pnlGraphic.setTurnCollision(true);
                    wallPanel.setBackground(new Color(236, 248, 255));
                } else {
                    pnlGraphic.setTurnCollision(false);
                    wallPanel.setBackground(new Color(19, 19, 19));
                }
            }
        });

        wallCollision.doClick();

        while (true) {

            lbScore.setText("SCORE: " + String.valueOf(pnlGraphic.getFoodEaten()));
            this.setVisible(true);
        }

    }

    private void setSpeed() {

        EASYRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlGraphic.setSPEED(150);
            }
        });

        NORMALRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlGraphic.setSPEED(100);
            }
        });

        HARDRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pnlGraphic.setSPEED(50);
            }
        });
    }
}
