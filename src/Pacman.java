import java.awt.*;
import java.awt.event.*;

public class Pacman {
    private int x, y;
    private Direction direction = Direction.LEFT;
    private Direction nextDirection = Direction.LEFT;
    private int score = 0;
    private Board board;
    private static final int SIZE = 20;
    private static final int SPEED = 2;

    public Pacman(int x, int y, Board board) {
        this.x = x;
        this.y = y;
        this.board = board;
    }

    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillArc(x, y, SIZE, SIZE, direction.getAngle(), 300);
    }

    public void move() {
        // Intentar cambiar a la siguiente dirección si es posible
        int nextX = x;
        int nextY = y;
        
        switch (nextDirection) {
            case LEFT: nextX = x - SPEED; break;
            case RIGHT: nextX = x + SPEED; break;
            case UP: nextY = y - SPEED; break;
            case DOWN: nextY = y + SPEED; break;
        }
        
        if (board.canMove(nextX, nextY, SIZE)) {
            direction = nextDirection;
            x = nextX;
            y = nextY;
        } else {
            // Si no puede cambiar de dirección, continuar en la dirección actual
            nextX = x;
            nextY = y;
            
            switch (direction) {
                case LEFT: nextX = x - SPEED; break;
                case RIGHT: nextX = x + SPEED; break;
                case UP: nextY = y - SPEED; break;
                case DOWN: nextY = y + SPEED; break;
            }
            
            if (board.canMove(nextX, nextY, SIZE)) {
                x = nextX;
                y = nextY;
            }
        }
    }

    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT: nextDirection = Direction.LEFT; break;
            case KeyEvent.VK_RIGHT: nextDirection = Direction.RIGHT; break;
            case KeyEvent.VK_UP: nextDirection = Direction.UP; break;
            case KeyEvent.VK_DOWN: nextDirection = Direction.DOWN; break;
        }
    }

    public int getScore() {
        return score;
    }
    
    public void addScore(int points) {
        score += points;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}