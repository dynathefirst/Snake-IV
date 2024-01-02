package com.dyna.snakeiv;

import com.dyna.snakeiv.food.Apple;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

public class Graphics extends JPanel implements ActionListener {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int TICK_SIZE = 50;
    public static final int BOARD_SIZE = (WIDTH * HEIGHT) / (TICK_SIZE * TICK_SIZE);

    final Font font = new Font("TimesRoman", Font.BOLD, 30);

    int[] snakePosX = new int[BOARD_SIZE];
    int[] snakePosY = new int[BOARD_SIZE];
    int snakeLength;
    String longestSnakeLength = "";

    Apple apple;
    int applesEaten;

    char direction = 'R';
    boolean isMoving = false;
    final Timer timer = new Timer(150, this);

    public Graphics() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (isMoving) {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_A:
                            if (direction != 'R') {
                                direction = 'L';
                            }
                            break;
                        case KeyEvent.VK_D:
                            if (direction != 'L') {
                                direction = 'R';
                            }
                            break;
                        case KeyEvent.VK_W:
                            if (direction != 'D') {
                                direction = 'U';
                            }
                            break;
                        case KeyEvent.VK_S:
                            if (direction != 'U') {
                                direction = 'D';
                            }
                            break;
                    }
                } else {
                    start();
                }
            }
        });

        start();
    }

    protected void start() {
        snakePosX = new int[BOARD_SIZE];
        snakePosY = new int[BOARD_SIZE];
        snakeLength = 1;
        applesEaten = 0;
        direction = 'R';
        isMoving = true;
        spawnFood();
        timer.start();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        String newline = System.lineSeparator();

        if (isMoving) {
            g.setColor(Color.RED);
            g.fillRect(apple.getPosX(), apple.getPosY(), TICK_SIZE, TICK_SIZE);

            g.setColor(Color.GREEN);
            for (int i = 0; i < snakeLength; i++) {
                g.fillRect(snakePosX[i], snakePosY[i], TICK_SIZE, TICK_SIZE);
            }
        } else {
            if (longestSnakeLength.equals("")) {
                longestSnakeLength = this.getLongestSnake();

                String scoreText = String.format("score: " + applesEaten);
                String highscoreText = String.format("high: " + longestSnakeLength);
                System.out.println("score: " + applesEaten);
                System.out.println("high: " + longestSnakeLength);
                g.setColor(Color.WHITE);
                g.setFont(font);
                g.drawString(scoreText, (WIDTH - getFontMetrics(g.getFont()).stringWidth(scoreText)) / 2, HEIGHT / 2);
            }
        }
    }

    protected void move() {
        for (int i = snakeLength; i > 0; i--) {
            snakePosX[i] = snakePosX[i - 1];
            snakePosY[i] = snakePosY[i - 1];
        }

        switch (direction) {
            case 'U' -> snakePosY[0] -= TICK_SIZE;
            case 'D' -> snakePosY[0] += TICK_SIZE;
            case 'L' -> snakePosX[0] -= TICK_SIZE;
            case 'R' -> snakePosX[0] += TICK_SIZE;
        }
    }

    protected void spawnFood() {
        apple = new Apple();
    }

    protected void eatFood() {
        if ((snakePosX[0] == apple.getPosX()) && (snakePosY[0] == apple.getPosY())) {
            snakeLength++;
            applesEaten++;
            spawnFood();
        }
    }

    protected void onCollision() {
        for (int i = snakeLength; i > 0; i--) {
            if ((snakePosX[0] == snakePosX[i]) && (snakePosY[0] == snakePosY[i])) {
                isMoving = false;
                break;
            }
        }

        if (snakePosX[0] < 0 || snakePosX[0] > WIDTH - TICK_SIZE || snakePosY[0] < 0 || snakePosY[0] > HEIGHT - TICK_SIZE) {
            isMoving = false;
        }

        if (!isMoving) {
            timer.stop();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isMoving) {
            move();
            onCollision();
            eatFood();
        }
        if (!isMoving) {
            checkLongest();
        }
        repaint();
    }

    public void checkLongest() {
        if (snakeLength > Integer.parseInt((longestSnakeLength.split(": ")[1]))) {
            String name = JOptionPane.showInputDialog("New High! What is your name?");
            longestSnakeLength = name + ": " + snakeLength;

            File longestSnakeHighFile = new File("longestsnake.txt");
            if (!longestSnakeHighFile.exists()) {
                try {
                    longestSnakeHighFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            FileWriter writeFile = null;
            BufferedWriter writer = null;
            try {
                writeFile = new FileWriter(longestSnakeHighFile);
                writer = new BufferedWriter(writeFile);
                writer.write(this.longestSnakeLength);
            } catch (Exception e) {
                //errors
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private String getLongestSnake() {
        FileReader readFile = null;
        BufferedReader reader = null;
        try {
            readFile = new FileReader("longestsnake.txt");
            reader = new BufferedReader(readFile);
            return reader.readLine();
        } catch (Exception e) {
            return "0";
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
