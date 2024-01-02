package com.dyna.snakeiv.gfx;

import com.dyna.snakeiv.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Graphics extends JPanel implements ActionListener {
    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    public static final int TICK = 50;
    public static final int BOARD = (WIDTH * HEIGHT) / (TICK * TICK);

    public int[] snakeX = new int[BOARD];
    public int[] snakeY = new int[BOARD];
    public int length;

    public char direction = 'R';
    public boolean isMoving = false;
    public Timer timer = new Timer(150, this)

    public Graphics() {
        Game game = new Game();
        game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        game.setBackground(Color.DARK_GRAY);
        game.setFocusable(true);
    }

    protected void start() {
        snakeX = new int[BOARD];
        snakeY = new int[BOARD];
        length = 5;
        direction = 'R';
        isMoving = true;
        timer.start();
    }
}
