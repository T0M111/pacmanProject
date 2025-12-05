import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {
    private Timer timer;
    private Pacman pacman;
    private Ghost[] ghosts;
    
    // Constantes del tablero
    public static final int TILE_SIZE = 20;
    public static final int BOARD_WIDTH = 19;
    public static final int BOARD_HEIGHT = 19;
    
    // Tipos de celda
    public static final int EMPTY = 0;
    public static final int WALL = 1;
    public static final int POINT = 2;
    
    // Nivel actual y puntos totales
    private int currentLevel = 1;
    private int totalPoints;
    private int pointsEaten;
    private boolean gameWon = false;
    private boolean gameLost = false;
    
    // Mapa del nivel actual
    private int[][] levelMap;
    
    // Diseños de los 3 niveles (1 = pared, 2 = punto, 0 = vacío)
    private static final int[][][] LEVELS = {
        // Nivel 1 - Laberinto simple
        {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,2,1,2,1,1,1,1,1,2,1,2,1,1,2,1},
            {1,2,2,2,2,1,2,2,2,1,2,2,2,1,2,2,2,2,1},
            {1,1,1,1,2,1,1,1,0,1,0,1,1,1,2,1,1,1,1},
            {0,0,0,1,2,1,0,0,0,0,0,0,0,1,2,1,0,0,0},
            {1,1,1,1,2,1,0,1,1,0,1,1,0,1,2,1,1,1,1},
            {0,0,0,0,2,0,0,1,0,0,0,1,0,0,2,0,0,0,0},
            {1,1,1,1,2,1,0,1,1,1,1,1,0,1,2,1,1,1,1},
            {0,0,0,1,2,1,0,0,0,0,0,0,0,1,2,1,0,0,0},
            {1,1,1,1,2,1,0,1,1,1,1,1,0,1,2,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,1,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,2,1,1,1,2,1,2,1,1,1,2,1,1,2,1},
            {1,2,2,1,2,2,2,2,2,2,2,2,2,2,2,1,2,2,1},
            {1,1,2,1,2,1,2,1,1,1,1,1,2,1,2,1,2,1,1},
            {1,2,2,2,2,1,2,2,2,1,2,2,2,1,2,2,2,2,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        },
        // Nivel 2 - Laberinto intermedio
        {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,1,1,1,1,1,1,2,1,1,1,2,1},
            {1,2,1,0,0,2,0,0,0,1,0,0,0,2,0,0,1,2,1},
            {1,2,1,0,1,2,1,1,0,1,0,1,1,2,1,0,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,0,1,2,1,0,0,0,0,0,1,2,1,0,1,2,1},
            {1,2,1,0,0,2,1,0,1,1,1,0,1,2,0,0,1,2,1},
            {1,2,1,1,1,2,1,0,1,0,1,0,1,2,1,1,1,2,1},
            {1,2,2,2,2,2,0,0,0,0,0,0,0,2,2,2,2,2,1},
            {1,2,1,1,1,2,1,0,1,0,1,0,1,2,1,1,1,2,1},
            {1,2,1,0,0,2,1,0,1,1,1,0,1,2,0,0,1,2,1},
            {1,2,1,0,1,2,1,0,0,0,0,0,1,2,1,0,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,0,1,2,1,1,0,1,0,1,1,2,1,0,1,2,1},
            {1,2,1,0,0,2,0,0,0,1,0,0,0,2,0,0,1,2,1},
            {1,2,1,1,1,2,1,1,1,1,1,1,1,2,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        },
        // Nivel 3 - Laberinto avanzado
        {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,2,2,2,1,2,2,2,2,2,2,2,2,2,1,2,2,2,1},
            {1,2,1,2,1,2,1,1,1,1,1,1,1,2,1,2,1,2,1},
            {1,2,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1,2,1},
            {1,2,1,1,1,1,1,1,2,1,2,1,1,1,1,1,1,2,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,1,1,2,1,1,1,0,1,1,1,0,1,1,1,2,1,1,1},
            {0,0,1,2,1,0,0,0,0,0,0,0,0,0,1,2,1,0,0},
            {1,1,1,2,1,0,1,1,0,0,0,1,1,0,1,2,1,1,1},
            {2,2,2,2,0,0,1,0,0,0,0,0,1,0,0,2,2,2,2},
            {1,1,1,2,1,0,1,1,1,1,1,1,1,0,1,2,1,1,1},
            {0,0,1,2,1,0,0,0,0,0,0,0,0,0,1,2,1,0,0},
            {1,1,1,2,1,1,1,0,1,1,1,0,1,1,1,2,1,1,1},
            {1,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,1},
            {1,2,1,1,1,1,1,1,2,1,2,1,1,1,1,1,1,2,1},
            {1,2,1,2,2,2,2,2,2,1,2,2,2,2,2,2,1,2,1},
            {1,2,1,2,1,1,1,1,1,1,1,1,1,1,1,2,1,2,1},
            {1,2,2,2,1,2,2,2,2,2,2,2,2,2,1,2,2,2,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        }
    };

    public Board() {
        setFocusable(true);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(BOARD_WIDTH * TILE_SIZE, BOARD_HEIGHT * TILE_SIZE + 30));
        loadLevel(currentLevel);
        timer = new Timer(40, this);
        timer.start();
        addKeyListener(new PacmanKeyAdapter());
    }
    
    private void loadLevel(int level) {
        // Copiar el mapa del nivel
        int levelIndex = (level - 1) % 3;
        levelMap = new int[BOARD_HEIGHT][BOARD_WIDTH];
        totalPoints = 0;
        pointsEaten = 0;
        
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                levelMap[y][x] = LEVELS[levelIndex][y][x];
                if (levelMap[y][x] == POINT) {
                    totalPoints++;
                }
            }
        }
        
        // Inicializar Pacman y fantasmas
        pacman = new Pacman(9 * TILE_SIZE, 15 * TILE_SIZE, this);
        ghosts = new Ghost[] {
            new Ghost(9 * TILE_SIZE, 9 * TILE_SIZE, Color.RED, this),
            new Ghost(8 * TILE_SIZE, 9 * TILE_SIZE, Color.PINK, this),
            new Ghost(10 * TILE_SIZE, 9 * TILE_SIZE, Color.CYAN, this)
        };
        
        gameWon = false;
        gameLost = false;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBoard(g);
        pacman.draw(g);
        for (Ghost ghost : ghosts) {
            ghost.draw(g);
        }
        drawStatus(g);
    }

    private void drawBoard(Graphics g) {
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                int cellValue = levelMap[y][x];
                int pixelX = x * TILE_SIZE;
                int pixelY = y * TILE_SIZE;
                
                if (cellValue == WALL) {
                    // Dibujar pared
                    g.setColor(Color.BLUE);
                    g.fillRect(pixelX, pixelY, TILE_SIZE, TILE_SIZE);
                    g.setColor(Color.BLUE.darker());
                    g.drawRect(pixelX, pixelY, TILE_SIZE - 1, TILE_SIZE - 1);
                } else if (cellValue == POINT) {
                    // Dibujar punto
                    g.setColor(Color.WHITE);
                    int dotSize = 4;
                    g.fillOval(pixelX + TILE_SIZE / 2 - dotSize / 2, 
                              pixelY + TILE_SIZE / 2 - dotSize / 2, 
                              dotSize, dotSize);
                }
            }
        }
    }
    
    private void drawStatus(Graphics g) {
        g.setColor(Color.YELLOW);
        g.drawString("Score: " + pacman.getScore(), 10, BOARD_HEIGHT * TILE_SIZE + 20);
        g.drawString("Level: " + currentLevel, 150, BOARD_HEIGHT * TILE_SIZE + 20);
        
        if (gameWon) {
            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("LEVEL COMPLETE!", BOARD_WIDTH * TILE_SIZE / 2 - 80, BOARD_HEIGHT * TILE_SIZE / 2);
        } else if (gameLost) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("GAME OVER!", BOARD_WIDTH * TILE_SIZE / 2 - 60, BOARD_HEIGHT * TILE_SIZE / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameWon && !gameLost) {
            pacman.move();
            for (Ghost ghost : ghosts) {
                ghost.move();
            }
            checkPointCollision();
            checkGhostCollision();
            checkLevelComplete();
        }
        repaint();
    }
    
    private void checkPointCollision() {
        int pacTileX = (pacman.getX() + TILE_SIZE / 2) / TILE_SIZE;
        int pacTileY = (pacman.getY() + TILE_SIZE / 2) / TILE_SIZE;
        
        if (pacTileX >= 0 && pacTileX < BOARD_WIDTH && pacTileY >= 0 && pacTileY < BOARD_HEIGHT) {
            if (levelMap[pacTileY][pacTileX] == POINT) {
                levelMap[pacTileY][pacTileX] = EMPTY;
                pacman.addScore(10);
                pointsEaten++;
            }
        }
    }
    
    private void checkGhostCollision() {
        Rectangle pacRect = new Rectangle(pacman.getX(), pacman.getY(), TILE_SIZE, TILE_SIZE);
        for (Ghost ghost : ghosts) {
            Rectangle ghostRect = new Rectangle(ghost.getX(), ghost.getY(), TILE_SIZE, TILE_SIZE);
            if (pacRect.intersects(ghostRect)) {
                gameLost = true;
                timer.stop();
            }
        }
    }
    
    private void checkLevelComplete() {
        if (pointsEaten >= totalPoints) {
            gameWon = true;
            timer.stop();
            // Avanzar al siguiente nivel después de un breve delay
            Timer levelTimer = new Timer(2000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ((Timer) e.getSource()).stop();
                    currentLevel++;
                    loadLevel(currentLevel);
                    timer.start();
                    repaint();
                }
            });
            levelTimer.setRepeats(false);
            levelTimer.start();
        }
    }
    
    // Método para verificar si hay una pared en una posición dada
    public boolean isWall(int x, int y) {
        int tileX = x / TILE_SIZE;
        int tileY = y / TILE_SIZE;
        
        if (tileX < 0 || tileX >= BOARD_WIDTH || tileY < 0 || tileY >= BOARD_HEIGHT) {
            return true; // Fuera de límites se considera pared
        }
        return levelMap[tileY][tileX] == WALL;
    }
    
    // Método para verificar si un movimiento es válido (no hay pared)
    public boolean canMove(int x, int y, int size) {
        // Verificar las 4 esquinas del sprite
        return !isWall(x, y) && 
               !isWall(x + size - 1, y) && 
               !isWall(x, y + size - 1) && 
               !isWall(x + size - 1, y + size - 1);
    }

    private class PacmanKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            pacman.keyPressed(e);
        }
    }
}