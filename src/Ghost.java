import java.awt.*;
import java.util.Random;

public class Ghost {
    private int x, y;
    private Direction direction;
    private Color color;
    private Random random = new Random();
    private Board board;
    private static final int SIZE = 20;
    private static final int SPEED = 2;

    public Ghost(int x, int y, Color color, Board board) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.board = board;
        this.direction = Direction.values()[random.nextInt(4)];
    }

    public void draw(Graphics g) {
        g.setColor(color);
        // Dibujar cuerpo del fantasma
        g.fillArc(x, y, SIZE, SIZE, 0, 180);
        g.fillRect(x, y + SIZE / 2, SIZE, SIZE / 2);
        // Ojos
        g.setColor(Color.WHITE);
        g.fillOval(x + 3, y + 5, 5, 5);
        g.fillOval(x + 12, y + 5, 5, 5);
        g.setColor(Color.BLACK);
        g.fillOval(x + 4, y + 6, 3, 3);
        g.fillOval(x + 13, y + 6, 3, 3);
    }

    public void move() {
        // Intentar moverse en la dirección actual
        int nextX = x;
        int nextY = y;
        
        switch (direction) {
            case LEFT: nextX = x - SPEED; break;
            case RIGHT: nextX = x + SPEED; break;
            case UP: nextY = y - SPEED; break;
            case DOWN: nextY = y + SPEED; break;
        }
        
        // Si puede moverse, hacerlo; si no, cambiar de dirección
        if (board.canMove(nextX, nextY, SIZE)) {
            x = nextX;
            y = nextY;
            // Ocasionalmente cambiar de dirección incluso si puede continuar
            if (random.nextInt(50) == 0) {
                changeDirection();
            }
        } else {
            // No puede continuar, cambiar de dirección
            changeDirection();
        }
    }
    
    private void changeDirection() {
        // Intentar encontrar una dirección válida
        Direction[] directions = Direction.values();
        Direction newDirection;
        int attempts = 0;
        
        do {
            newDirection = directions[random.nextInt(4)];
            attempts++;
        } while (attempts < 10 && !canMoveInDirection(newDirection));
        
        if (attempts < 10) {
            direction = newDirection;
        }
    }
    
    private boolean canMoveInDirection(Direction dir) {
        int nextX = x;
        int nextY = y;
        
        switch (dir) {
            case LEFT: nextX = x - SPEED; break;
            case RIGHT: nextX = x + SPEED; break;
            case UP: nextY = y - SPEED; break;
            case DOWN: nextY = y + SPEED; break;
        }
        
        return board.canMove(nextX, nextY, SIZE);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
}