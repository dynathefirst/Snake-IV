package com.dyna.snakeiv;

import com.dyna.snakeiv.gfx.Graphics;

import javax.swing.*;

public class Game extends JFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Snake IV");
        frame.add(new Graphics());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }
}
