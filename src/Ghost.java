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
    private static final int CHASE_UPDATE_FREQUENCY = 15;
    private int moveCounter = 0;

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
        moveCounter++;
        
        // Cambiar dirección hacia Pacman cada ciertos movimientos
        if (moveCounter % CHASE_UPDATE_FREQUENCY == 0) {
            chaseDirection();
        }
        
        // Intentar moverse en la dirección actual
        if (!tryMoveInDirection(direction)) {
            // No puede continuar, intentar perseguir o cambiar de dirección
            chaseDirection();
            if (!tryMoveInDirection(direction)) {
                changeDirection();
            }
        }
        
        // Verificar si hay que teletransportar (túneles)
        wrapAround();
    }
    
    private boolean tryMoveInDirection(Direction dir) {
        int nextX = x;
        int nextY = y;
        
        switch (dir) {
            case LEFT: nextX = x - SPEED; break;
            case RIGHT: nextX = x + SPEED; break;
            case UP: nextY = y - SPEED; break;
            case DOWN: nextY = y + SPEED; break;
        }
        
        if (board.canMove(nextX, nextY, SIZE)) {
            x = nextX;
            y = nextY;
            return true;
        }
        return false;
    }
    
    private void chaseDirection() {
        Point pacmanPos = board.getPacmanPosition();
        
        int dx = pacmanPos.x - x;
        int dy = pacmanPos.y - y;
        
        // Crear lista de direcciones preferidas basadas en la posición de Pacman
        Direction[] preferredDirs = new Direction[4];
        int index = 0;
        
        // Priorizar la dirección que más nos acerca a Pacman
        if (Math.abs(dx) > Math.abs(dy)) {
            // Priorizar horizontal
            if (dx > 0) {
                preferredDirs[index++] = Direction.RIGHT;
                preferredDirs[index++] = (dy > 0) ? Direction.DOWN : Direction.UP;
                preferredDirs[index++] = (dy > 0) ? Direction.UP : Direction.DOWN;
                preferredDirs[index++] = Direction.LEFT;
            } else {
                preferredDirs[index++] = Direction.LEFT;
                preferredDirs[index++] = (dy > 0) ? Direction.DOWN : Direction.UP;
                preferredDirs[index++] = (dy > 0) ? Direction.UP : Direction.DOWN;
                preferredDirs[index++] = Direction.RIGHT;
            }
        } else {
            // Priorizar vertical
            if (dy > 0) {
                preferredDirs[index++] = Direction.DOWN;
                preferredDirs[index++] = (dx > 0) ? Direction.RIGHT : Direction.LEFT;
                preferredDirs[index++] = (dx > 0) ? Direction.LEFT : Direction.RIGHT;
                preferredDirs[index++] = Direction.UP;
            } else {
                preferredDirs[index++] = Direction.UP;
                preferredDirs[index++] = (dx > 0) ? Direction.RIGHT : Direction.LEFT;
                preferredDirs[index++] = (dx > 0) ? Direction.LEFT : Direction.RIGHT;
                preferredDirs[index++] = Direction.DOWN;
            }
        }
        
        // Intentar la primera dirección válida
        for (Direction dir : preferredDirs) {
            if (canMoveInDirection(dir)) {
                direction = dir;
                return;
            }
        }
    }
    
    private void wrapAround() {
        int boardPixelWidth = Board.BOARD_WIDTH * Board.TILE_SIZE;
        
        // Si sale por la izquierda, aparece por la derecha
        if (x + SIZE <= 0) {
            x = boardPixelWidth - SPEED;
        }
        // Si sale por la derecha, aparece por la izquierda
        else if (x >= boardPixelWidth) {
            x = -SIZE + SPEED;
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