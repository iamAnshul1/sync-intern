import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    private static final int TILE_SIZE = 20;
    private static final int GRID_WIDTH = 20;
    private static final int GRID_HEIGHT = 20;
    private ArrayList<Point> snake;
    private Point food;
    private int direction;
    private boolean isMoving;
    private boolean isGameOver;

    public SnakeGame() {
        snake = new ArrayList<>();
        snake.add(new Point(5, 5));
        direction = KeyEvent.VK_RIGHT;

        Timer timer = new Timer(100, this);
        timer.start();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if ((key == KeyEvent.VK_LEFT) && (direction != KeyEvent.VK_RIGHT)) {
                    direction = KeyEvent.VK_LEFT;
                } else if ((key == KeyEvent.VK_RIGHT) && (direction != KeyEvent.VK_LEFT)) {
                    direction = KeyEvent.VK_RIGHT;
                } else if ((key == KeyEvent.VK_UP) && (direction != KeyEvent.VK_DOWN)) {
                    direction = KeyEvent.VK_UP;
                } else if ((key == KeyEvent.VK_DOWN) && (direction != KeyEvent.VK_UP)) {
                    direction = KeyEvent.VK_DOWN;
                } else if (key == KeyEvent.VK_R && isGameOver) {
                    restartGame();
                }
            }
        });

        setFocusable(true);
        generateFood();
    }

    public void generateFood() {
        Random rand = new Random();
        int x = rand.nextInt(GRID_WIDTH);
        int y = rand.nextInt(GRID_HEIGHT);
        food = new Point(x, y);
    }

    public void move() {
        Point head = snake.get(0);
        Point newHead = (Point) head.clone();

        if (direction == KeyEvent.VK_LEFT) {
            newHead.x--;
        } else if (direction == KeyEvent.VK_RIGHT) {
            newHead.x++;
        } else if (direction == KeyEvent.VK_UP) {
            newHead.y--;
        } else if (direction == KeyEvent.VK_DOWN) {
            newHead.y++;
        }

        if (newHead.equals(food)) {
            snake.add(0, newHead);
            generateFood();
        } else {
            snake.add(0, newHead);
            snake.remove(snake.size() - 1);
        }

        checkCollision();
    }

    public void checkCollision() {
        Point head = snake.get(0);

        if (head.x < 0 || head.x >= GRID_WIDTH || head.y < 0 || head.y >= GRID_HEIGHT) {
            isGameOver = true;
            return;
        }

        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                isGameOver = true;
                return;
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (!isGameOver) {
            for (Point p : snake) {
                g.setColor(Color.GREEN);
                g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

            g.setColor(Color.RED);
            g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        } else {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over. Press 'R' to restart.", GRID_WIDTH * TILE_SIZE / 2 - 150, GRID_HEIGHT * TILE_SIZE / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver) {
            move();
            repaint();
        }
    }

    public void restartGame() {
        snake.clear();
        snake.add(new Point(5, 5));
        direction = KeyEvent.VK_RIGHT;
        isGameOver = false;
        generateFood();
        repaint();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Enhanced Snake Game");
        SnakeGame game = new SnakeGame();

        frame.add(game);
        frame.setSize(GRID_WIDTH * TILE_SIZE, GRID_HEIGHT * TILE_SIZE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
