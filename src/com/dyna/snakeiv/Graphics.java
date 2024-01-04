package com.dyna.snakeiv;

import com.dyna.snakeiv.food.Apple;
import com.dyna.snakeiv.food.Bomb;
import com.dyna.snakeiv.food.Orange;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.util.Random;

public class Graphics extends JPanel implements ActionListener {
    private static final Random random = new Random();
    public static final int WIDTH = 800;
    public static final int HEIGHT = 800;
    public static final int TICK_SIZE = 50;
    public static final int BOARD_SIZE = (WIDTH * HEIGHT) / (TICK_SIZE * TICK_SIZE);

    final Font font = new Font("TimesRoman", Font.BOLD, 30);

    int[] snakePosX = new int[BOARD_SIZE];
    int[] snakePosY = new int[BOARD_SIZE];
    int snakeLength;
    String highestScore = "";

    Apple apple;
    Orange orange;
    Bomb bomb;
    int applesEaten;
    int orangesEaten;
    int bombsEaten;
    int bombsEaten2;

    int scoreHeight = WIDTH / 2;

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
        orangesEaten = 0;
        bombsEaten = 0;
        bombsEaten2 = 0;
        direction = 'R';
        isMoving = true;
        spawnFood();
        spawnFood2();
        spawnBomb();
        timer.start();
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        String newline = System.lineSeparator();

        if (isMoving) {
            //apple
            g.setColor(Color.RED);
            g.fillRect(apple.getPosX(), apple.getPosY(), TICK_SIZE, TICK_SIZE);

            //orange
            g.setColor(Color.ORANGE);
            g.fillRect(orange.getPosX(), orange.getPosY(), TICK_SIZE, TICK_SIZE);

            //bomb
            g.setColor(Color.DARK_GRAY);
            g.fillRect(bomb.getPosX(), bomb.getPosY(), TICK_SIZE, TICK_SIZE);

            //snake
            g.setColor(Color.GREEN);
            for (int i = 0; i < snakeLength; i++) {
                g.fillRect(snakePosX[i], snakePosY[i], TICK_SIZE, TICK_SIZE);
            }
        } else {
            if (!highestScore.equals("")) {
                highestScore = this.getHighestScore();

                String scoreText = String.format("score: " + ((applesEaten + (orangesEaten * 2)) - ((bombsEaten - bombsEaten2))));
                String highscoreText = String.format("high: " + highestScore);
                System.out.println("score: " + ((applesEaten + (orangesEaten * 2)) - ((bombsEaten - bombsEaten2))));
                System.out.println("high: " + highestScore);
                g.setColor(Color.WHITE);
                g.setFont(font);
                g.drawString(scoreText, (WIDTH - getFontMetrics(g.getFont()).stringWidth(scoreText)) / 2, scoreHeight);
                g.drawString(highscoreText, (WIDTH - getFontMetrics(g.getFont()).stringWidth(highscoreText)) / 2, scoreHeight + 25);
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
        bomb = new Bomb();
    }

    protected void spawnFood2() {
        orange = new Orange();
        bomb = new Bomb();
    }

    protected void spawnBomb() {
        bomb = new Bomb();
    }

    protected void eatApple() {
        if ((snakePosX[0] == apple.getPosX()) && (snakePosY[0] == apple.getPosY())) {
            snakeLength++;
            applesEaten++;
            spawnFood();
        }
    }

    protected void eatOrange() {
        if ((snakePosX[0] == orange.getPosX()) && (snakePosY[0] == orange.getPosY())) {
            snakeLength++;
            orangesEaten++;
            spawnFood2();
        }
    }

    protected void eatBomb() {
        if ((snakePosX[0] == bomb.getPosX()) && (snakePosY[0] == bomb.getPosY())) {
            bombsEaten++;
            spawnBomb();
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
            eatApple();
            eatOrange();
            eatBomb();
        }
        if (!isMoving) {
            checkLongest();
        }
        repaint();
    }

    public void checkLongest() {
        if (highestScore.equals("") || snakeLength > Integer.parseInt((highestScore.split(": ")[1]))) {
            String name = JOptionPane.showInputDialog("New High! What is your name?");
            highestScore = name + ": " + snakeLength;

            File longestSnakeHighFile = new File("high.txt");
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
                writer.write(this.highestScore);
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

    private String getHighestScore() {
        FileReader readFile = null;
        BufferedReader reader = null;
        try {
            readFile = new FileReader("high.txt");
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
