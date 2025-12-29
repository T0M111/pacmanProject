import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Ghost {
    private int x, y;
    private Direction direction;
    private Color color;
    private Random random = new Random();
    private Board board;
    private Pacman pacman;
    private int intelligenceLevel;
    private static final int SIZE = 20;
    private static final int SPEED = 2;

    public Ghost(int x, int y, Color color, Board board, Pacman pacman, int intelligenceLevel) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.board = board;
        this.pacman = pacman;
        this.intelligenceLevel = intelligenceLevel;
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
        // Determinar si el fantasma debe perseguir a Pacman basado en el nivel de inteligencia
        boolean shouldChase = shouldChasePacman();
        
        if (shouldChase) {
            // Movimiento inteligente: intentar acercarse a Pacman
            moveTowardsPacman();
        } else {
            // Movimiento aleatorio original
            moveRandomly();
        }
        
        // Aplicar efecto túnel (wrap-around horizontal)
        x = board.wrapX(x);
    }
    
    private boolean shouldChasePacman() {
        // Probabilidad de perseguir basada en el nivel de inteligencia
        // Nivel 1: 10% (movimiento mayormente aleatorio pero con algo de persecución para salir del área inicial)
        // Nivel 2: 30% de probabilidad de perseguir
        // Nivel 3+: 60% + 10% por cada nivel adicional (máximo 60%)
        int chaseChance;
        switch (intelligenceLevel) {
            case 1:
                chaseChance = 10;
                break;
            case 2:
                chaseChance = 30;
                break;
            default:
                chaseChance = Math.min(60 + (intelligenceLevel - 3) * 10, 60);
                break;
        }
        return random.nextInt(100) < chaseChance;
    }
    
    private void moveTowardsPacman() {
        int pacX = pacman.getX();
        int pacY = pacman.getY();
        
        // Calcular la mejor dirección hacia Pacman
        Direction bestDirection = calculateBestDirection(pacX, pacY);
        
        // Intentar moverse en la mejor dirección
        int nextX = x;
        int nextY = y;
        
        switch (bestDirection) {
            case LEFT: nextX = x - SPEED; break;
            case RIGHT: nextX = x + SPEED; break;
            case UP: nextY = y - SPEED; break;
            case DOWN: nextY = y + SPEED; break;
        }
        
        if (board.canMove(nextX, nextY, SIZE)) {
            x = nextX;
            y = nextY;
            direction = bestDirection;
        } else {
            // Si no puede moverse hacia Pacman, intentar movimiento alternativo
            moveRandomly();
        }
    }
    
    private Direction calculateBestDirection(int targetX, int targetY) {
        int deltaX = targetX - x;
        int deltaY = targetY - y;
        
        // Obtener direcciones válidas ordenadas por preferencia
        List<Direction> validDirections = new ArrayList<>();
        
        // Nivel 3+: usar algoritmo más inteligente (priorizar la dirección con mayor diferencia)
        if (intelligenceLevel >= 3) {
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                // Priorizar movimiento horizontal
                if (deltaX > 0 && canMoveInDirection(Direction.RIGHT)) {
                    validDirections.add(Direction.RIGHT);
                } else if (deltaX < 0 && canMoveInDirection(Direction.LEFT)) {
                    validDirections.add(Direction.LEFT);
                }
                // Añadir direcciones verticales como alternativa
                if (deltaY > 0 && canMoveInDirection(Direction.DOWN)) {
                    validDirections.add(Direction.DOWN);
                } else if (deltaY < 0 && canMoveInDirection(Direction.UP)) {
                    validDirections.add(Direction.UP);
                }
            } else {
                // Priorizar movimiento vertical
                if (deltaY > 0 && canMoveInDirection(Direction.DOWN)) {
                    validDirections.add(Direction.DOWN);
                } else if (deltaY < 0 && canMoveInDirection(Direction.UP)) {
                    validDirections.add(Direction.UP);
                }
                // Añadir direcciones horizontales como alternativa
                if (deltaX > 0 && canMoveInDirection(Direction.RIGHT)) {
                    validDirections.add(Direction.RIGHT);
                } else if (deltaX < 0 && canMoveInDirection(Direction.LEFT)) {
                    validDirections.add(Direction.LEFT);
                }
            }
        } else {
            // Nivel 2: elegir simplemente la dirección más directa
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (deltaX > 0) {
                    validDirections.add(Direction.RIGHT);
                } else {
                    validDirections.add(Direction.LEFT);
                }
            } else {
                if (deltaY > 0) {
                    validDirections.add(Direction.DOWN);
                } else {
                    validDirections.add(Direction.UP);
                }
            }
        }
        
        // Si hay direcciones válidas, usar la primera; si no, mantener dirección actual
        if (!validDirections.isEmpty()) {
            return validDirections.get(0);
        }
        return direction;
    }
    
    private void moveRandomly() {
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