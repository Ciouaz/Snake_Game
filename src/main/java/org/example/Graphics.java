package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class Graphics extends JPanel implements ActionListener {

    static final int PIXEL = 30;
    static final int WIDTH = PIXEL * 15;
    static final int HEIGHT = PIXEL * 15;
    int SPEED = 100;

    public void setSPEED(int SPEED) {
        this.SPEED = SPEED;
    }

    static final int BOARD_SIZE = (WIDTH * HEIGHT) / (PIXEL * PIXEL);

    final Font font = new Font("Kristen ITC", Font.PLAIN, 22);

    int[] snakePosX = new int[BOARD_SIZE];
    int[] snakePosY = new int[BOARD_SIZE];
    int snakeLength;
    Timer timer = new Timer(this.SPEED, this);
    Food food;
    int foodEaten;
    boolean changeColorOfSnake = false;
    char direction = 'R';
    char nextDirection = 'R';
    boolean isMoving = false;
    Color foodColor = randomColor();
    java.awt.Graphics g;
    int keyPressed = 0;
    boolean turnCollision;

    public Graphics() {

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());

    }

    protected void start() {
        timer.stop();
        snakePosX = new int[BOARD_SIZE];
        snakePosY = new int[BOARD_SIZE];
        snakeLength = 4;
        foodEaten = 0;
        direction = 'R';
        nextDirection = 'R';
        isMoving = true;
        spawnFood();
        timer = new Timer(this.SPEED, this);
        timer.start();
    }

    protected void move() {

       for (int i = snakeLength; i > 0; i--) {
            snakePosX[i] = snakePosX[i - 1];
            snakePosY[i] = snakePosY[i - 1];
        }

        if (direction == 'U') {
            snakePosY[0] -= PIXEL;
        } else if (direction == 'D') {
            snakePosY[0] += PIXEL;
        } else if (direction == 'L') {
            snakePosX[0] -= PIXEL;
        } else if (direction == 'R') {
            snakePosX[0] += PIXEL;
        }
    }

    protected void spawnFood() {

        food = new Food();

        for (int snakeX : snakePosX) {
            for (int snakeY : snakePosY) {

               if (snakeX == food.getPosX() && snakeY == food.getPosY()) {

                   food = new Food();

                   break;
               }
            }
        }
    }

    protected void eatFood() {
        changeColorOfSnake = false;
        if ((snakePosX[0] == food.getPosX()) && (snakePosY[0] == food.getPosY())) {
            snakeLength++;
            foodEaten++;
            spawnFood();
            foodColor = randomColor();
            changeColorOfSnake = true;
        }
    }

    protected void collisionTest() {

        //(snakeLength - 1) is just for that snake could chase his own tail when wall collision is off, because it is nice

        for (int i = snakeLength - 1; i > 0; i--) {
            if ((snakePosX[0] == snakePosX[i]) && (snakePosY[0] == snakePosY[i])) {
                isMoving = false;
                break;
            }
        }

        if ((snakePosX[0] < 0) || (snakePosX[0] > WIDTH - PIXEL) || (snakePosY[0] < 0) || (snakePosY[0] > HEIGHT - PIXEL)) {
            if (turnCollision) {
                isMoving = false;
            } else {
                if(snakePosX[0] < 0) {
                    snakePosX[0] = WIDTH - PIXEL;
                } else if (snakePosX[0] > WIDTH - PIXEL) {
                    snakePosX[0] = 0;
                } else if (snakePosY[0] < 0) {
                    snakePosY[0] = HEIGHT - PIXEL;
                } else if (snakePosY[0] > HEIGHT - PIXEL) {
                    snakePosY[0] = 0;
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    if (isMoving) {
            move();
            collisionTest();
            eatFood();

            //this is added, so you can really quickly press two direction in one cycle. Without it, if you press for example
            // up ann left in one cycle the snake would go back "into himself" and finish the game. Condition
            //direction != nextDirection is added because without this you could boost the snake

            if (keyPressed >= 2 && direction != nextDirection){
                direction = nextDirection;
                move();
                collisionTest();
                eatFood();
                repaint();
            }

            keyPressed = 0;
        }

        repaint();

    }

    public int getFoodEaten() {
        return foodEaten;
    }

    public class MyKeyAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (isMoving) {

                keyPressed +=1;

                switch (e.getKeyCode()) {

                    case KeyEvent.VK_LEFT:
                        if (direction != 'R' && keyPressed == 1) {
                            direction = 'L';
                        }
                        if (direction != 'R') {
                            nextDirection = 'L';
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != 'L' && keyPressed == 1) {
                            direction = 'R';
                        }
                        if (direction != 'L') {
                            nextDirection = 'R';
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != 'D' && keyPressed == 1) {
                            direction = 'U';
                        }
                        if (direction != 'D') {
                            nextDirection = 'U';
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != 'U' && keyPressed == 1) {
                            direction = 'D';
                        }
                        if (direction != 'U') {
                            nextDirection = 'D';
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        start();
                        break;
                }

            } else {
                start();
            }
        }
    }

    @Override
    protected void paintComponent(java.awt.Graphics g) {
        this.g = g;
        super.paintComponent(g);

        if (!timer.isRunning()) {

            String welcomeText1 = "WELCOME!";
            String welcomeText2 = "Press any key to play";
            g.setColor(Color.WHITE);
            g.setFont(font);
            g.drawString(welcomeText1, (WIDTH - getFontMetrics(g.getFont()).stringWidth(welcomeText1)) / 2, HEIGHT / 2 - 25);
            g.drawString(welcomeText2, (WIDTH - getFontMetrics(g.getFont()).stringWidth(welcomeText2)) / 2, HEIGHT / 2 + 25);

        } else {

            if (isMoving) {
                g.setColor(foodColor);
                g.fillOval(food.getPosX(), food.getPosY(), PIXEL, PIXEL);

                g.setColor(new Color(147, 168, 9));
                g.fillRect(snakePosX[0], snakePosY[0], PIXEL, PIXEL);

                if (changeColorOfSnake) {
                    g.setColor(new Color(194, 33, 4, 255));
                    g.fillRect(snakePosX[0], snakePosY[0], PIXEL, PIXEL);
                }


                for (int i = 1; i < snakeLength; i++) {
                    if (i%2 == 0) {
                        g.setColor(new Color(11, 61, 20));
                        g.fillRect(snakePosX[i], snakePosY[i], PIXEL, PIXEL);
                    } else {
                        g.setColor(new Color(5, 93, 22));
                        g.fillRect(snakePosX[i], snakePosY[i], PIXEL , PIXEL);
                    }
                }

            } else {
                String scoreText1 = "YOU LOSE!";
                String scoreText2 = String.format("Your score: %d", foodEaten);
                String scoreText3 = "Press any key to play again";
                g.setColor(Color.WHITE);
                g.setFont(font);
                g.drawString(scoreText1, (WIDTH - getFontMetrics(g.getFont()).stringWidth(scoreText1)) / 2, HEIGHT / 2 - 50);
                g.drawString(scoreText2, (WIDTH - getFontMetrics(g.getFont()).stringWidth(scoreText2)) / 2, HEIGHT / 2);
                g.drawString(scoreText3, (WIDTH - getFontMetrics(g.getFont()).stringWidth(scoreText3)) / 2, HEIGHT / 2 + 50);
            }
        }
    }

    private Color randomColor() {

            Random random = new Random();
            return new Color(random.nextInt(50,255), random.nextInt(50,255), random.nextInt(50,255));

    }

    public void setTurnCollision(boolean turnCollision) {
        this.turnCollision = turnCollision;
    }

}
