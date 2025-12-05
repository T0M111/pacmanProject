import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BoardTest {

    private Board board;

    @BeforeEach
    void setUp() {
        board = new Board();
    }

    @Test
    void testBoardConstants() {
        assertEquals(20, Board.TILE_SIZE);
        assertEquals(19, Board.BOARD_WIDTH);
        assertEquals(19, Board.BOARD_HEIGHT);
        assertEquals(0, Board.EMPTY);
        assertEquals(1, Board.WALL);
        assertEquals(2, Board.POINT);
    }

    @Test
    void testBoardDimensions() {
        assertEquals(Board.BOARD_WIDTH * Board.TILE_SIZE, board.getPreferredSize().width);
        assertEquals(Board.BOARD_HEIGHT * Board.TILE_SIZE + 30, board.getPreferredSize().height);
    }

    @Test
    void testIsWallAtWallPosition() {
        // Corner position (0, 0) should be a wall
        assertTrue(board.isWall(0, 0));
    }

    @Test
    void testIsWallAtEmptyPosition() {
        // Position (1, 1) tile is (20, 20) pixels - should be a point, not a wall
        // Looking at level 1 map, position [1][1] is 2 (POINT)
        assertFalse(board.isWall(20, 20));
    }

    @Test
    void testIsWallOutOfBoundsTop() {
        assertTrue(board.isWall(100, -10));
    }

    @Test
    void testIsWallOutOfBoundsBottom() {
        assertTrue(board.isWall(100, 400));
    }

    @Test
    void testIsWallOutOfBoundsLeftWithWallBorder() {
        // Out of bounds left, check if there's a wall at the border
        // Looking at level 1 map, row 9 (y = 180) has empty cells at edges
        // Position [9][0] is 0 (EMPTY in level 1) - it's a tunnel row
        // Let's check a row where the border is a wall
        assertTrue(board.isWall(-10, 0)); // Row 0 has walls at borders
    }

    @Test
    void testIsWallOutOfBoundsRightWithWallBorder() {
        assertTrue(board.isWall(390, 0)); // Beyond right edge at row 0
    }

    @Test
    void testIsWallTunnelLeft() {
        // Check tunnel row (row 9 has tunnel)
        // y = 9 * 20 = 180, x = -10 (out of bounds left)
        // Row 9 in level 1: {0,0,0,0,2,0,0,1,0,0,0,1,0,0,2,0,0,0,0}
        // Position [9][0] = 0 (EMPTY), so tunnel should be open
        assertFalse(board.isWall(-10, 180));
    }

    @Test
    void testIsWallTunnelRight() {
        // Check tunnel row going right
        // Position [9][18] = 0 (EMPTY in level 1)
        // x = 390 (beyond right edge at row 9)
        assertFalse(board.isWall(390, 180));
    }

    @Test
    void testIsWallOutOfBoundsXAndY() {
        // When both x and y are out of bounds (y out of bounds while x is out of bounds)
        // This should trigger the return true at line 281
        assertTrue(board.isWall(-10, -10)); // Both out of bounds
        assertTrue(board.isWall(-10, 400)); // x out left, y out bottom
        assertTrue(board.isWall(400, -10)); // x out right, y out top
        assertTrue(board.isWall(400, 400)); // Both out on opposite side
    }

    @Test
    void testCanMoveValidPosition() {
        // Position inside the board where there's no wall
        // Tile (1, 1) which is a point
        assertTrue(board.canMove(20, 20, 20));
    }

    @Test
    void testCanMoveIntoWall() {
        // Corner is a wall
        assertFalse(board.canMove(0, 0, 20));
    }

    @Test
    void testCanMovePartiallyOverlappingWall() {
        // If any corner is in a wall, can't move
        // Position that partially overlaps with wall at (0,0)
        assertFalse(board.canMove(5, 5, 20));
    }

    @Test
    void testWrapXLeftEdge() {
        // Going left off the screen
        int wrapped = board.wrapX(-25);
        assertEquals(Board.BOARD_WIDTH * Board.TILE_SIZE - Board.TILE_SIZE, wrapped);
    }

    @Test
    void testWrapXRightEdge() {
        // Going right off the screen
        int wrapped = board.wrapX(Board.BOARD_WIDTH * Board.TILE_SIZE);
        assertEquals(0, wrapped);
    }

    @Test
    void testWrapXMiddle() {
        // Normal position, no wrapping
        int wrapped = board.wrapX(100);
        assertEquals(100, wrapped);
    }

    @Test
    void testWrapXAtLeftBoundary() {
        // At the exact left boundary threshold
        int wrapped = board.wrapX(-Board.TILE_SIZE);
        assertEquals(-Board.TILE_SIZE, wrapped); // Not wrapped yet, threshold is -TILE_SIZE - 1
    }

    @Test
    void testGetBoardPixelWidth() {
        assertEquals(Board.BOARD_WIDTH * Board.TILE_SIZE, board.getBoardPixelWidth());
    }

    @Test
    void testActionPerformed() {
        ActionEvent mockEvent = mock(ActionEvent.class);
        
        // Should not throw exception
        assertDoesNotThrow(() -> board.actionPerformed(mockEvent));
    }

    @Test
    void testPaintComponent() throws Exception {
        // Create a BufferedImage and get its Graphics context
        BufferedImage image = new BufferedImage(
            Board.BOARD_WIDTH * Board.TILE_SIZE,
            Board.BOARD_HEIGHT * Board.TILE_SIZE + 30,
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = image.createGraphics();
        
        // Use reflection to call paintComponent directly
        Method paintMethod = Board.class.getDeclaredMethod("paintComponent", Graphics.class);
        paintMethod.setAccessible(true);
        
        assertDoesNotThrow(() -> paintMethod.invoke(board, g));
        
        g.dispose();
    }

    @Test
    void testPaintComponentWithGameWon() throws Exception {
        // Set gameWon to true
        Field gameWonField = Board.class.getDeclaredField("gameWon");
        gameWonField.setAccessible(true);
        gameWonField.set(board, true);
        
        BufferedImage image = new BufferedImage(
            Board.BOARD_WIDTH * Board.TILE_SIZE,
            Board.BOARD_HEIGHT * Board.TILE_SIZE + 30,
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = image.createGraphics();
        
        Method paintMethod = Board.class.getDeclaredMethod("paintComponent", Graphics.class);
        paintMethod.setAccessible(true);
        
        assertDoesNotThrow(() -> paintMethod.invoke(board, g));
        
        g.dispose();
    }

    @Test
    void testPaintComponentWithGameLost() throws Exception {
        // Set gameLost to true
        Field gameLostField = Board.class.getDeclaredField("gameLost");
        gameLostField.setAccessible(true);
        gameLostField.set(board, true);
        
        BufferedImage image = new BufferedImage(
            Board.BOARD_WIDTH * Board.TILE_SIZE,
            Board.BOARD_HEIGHT * Board.TILE_SIZE + 30,
            BufferedImage.TYPE_INT_ARGB
        );
        Graphics2D g = image.createGraphics();
        
        Method paintMethod = Board.class.getDeclaredMethod("paintComponent", Graphics.class);
        paintMethod.setAccessible(true);
        
        assertDoesNotThrow(() -> paintMethod.invoke(board, g));
        
        g.dispose();
    }

    @Test
    void testKeyAdapter() throws Exception {
        // Get the key adapter from the board's key listeners
        java.awt.event.KeyListener[] listeners = board.getKeyListeners();
        assertTrue(listeners.length > 0, "Board should have key listeners");
        
        KeyEvent mockEvent = mock(KeyEvent.class);
        when(mockEvent.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
        
        // Should not throw exception
        assertDoesNotThrow(() -> listeners[0].keyPressed(mockEvent));
    }

    @Test
    void testLevelLoading() throws Exception {
        // Verify level 1 is loaded initially
        Field currentLevelField = Board.class.getDeclaredField("currentLevel");
        currentLevelField.setAccessible(true);
        int currentLevel = (int) currentLevelField.get(board);
        assertEquals(1, currentLevel);
    }

    @Test
    void testGameNotWonInitially() throws Exception {
        Field gameWonField = Board.class.getDeclaredField("gameWon");
        gameWonField.setAccessible(true);
        boolean gameWon = (boolean) gameWonField.get(board);
        assertFalse(gameWon);
    }

    @Test
    void testGameNotLostInitially() throws Exception {
        Field gameLostField = Board.class.getDeclaredField("gameLost");
        gameLostField.setAccessible(true);
        boolean gameLost = (boolean) gameLostField.get(board);
        assertFalse(gameLost);
    }

    @Test
    void testPacmanInitialized() throws Exception {
        Field pacmanField = Board.class.getDeclaredField("pacman");
        pacmanField.setAccessible(true);
        Pacman pacman = (Pacman) pacmanField.get(board);
        assertNotNull(pacman);
    }

    @Test
    void testGhostsInitialized() throws Exception {
        Field ghostsField = Board.class.getDeclaredField("ghosts");
        ghostsField.setAccessible(true);
        Ghost[] ghosts = (Ghost[]) ghostsField.get(board);
        assertNotNull(ghosts);
        assertEquals(3, ghosts.length);
    }

    @Test
    void testTotalPointsCalculation() throws Exception {
        Field totalPointsField = Board.class.getDeclaredField("totalPoints");
        totalPointsField.setAccessible(true);
        int totalPoints = (int) totalPointsField.get(board);
        assertTrue(totalPoints > 0, "Total points should be greater than 0");
    }

    @Test
    void testPointsEatenInitially() throws Exception {
        Field pointsEatenField = Board.class.getDeclaredField("pointsEaten");
        pointsEatenField.setAccessible(true);
        int pointsEaten = (int) pointsEatenField.get(board);
        assertEquals(0, pointsEaten);
    }

    @Test
    void testTimerInitialized() throws Exception {
        Field timerField = Board.class.getDeclaredField("timer");
        timerField.setAccessible(true);
        javax.swing.Timer timer = (javax.swing.Timer) timerField.get(board);
        assertNotNull(timer);
        assertTrue(timer.isRunning());
    }

    @Test
    void testLevelMapCopied() throws Exception {
        Field levelMapField = Board.class.getDeclaredField("levelMap");
        levelMapField.setAccessible(true);
        int[][] levelMap = (int[][]) levelMapField.get(board);
        assertNotNull(levelMap);
        assertEquals(Board.BOARD_HEIGHT, levelMap.length);
        assertEquals(Board.BOARD_WIDTH, levelMap[0].length);
    }

    @Test
    void testIsWallForAllTileTypes() {
        // Test wall detection for different tile types
        // We know corner is wall
        assertTrue(board.isWall(0, 0), "Corner should be a wall");
        
        // Find a point position (1, 1) in level 1
        assertFalse(board.isWall(25, 25), "Point tile should not be a wall");
    }

    @Test
    void testCanMoveWithAllFourCorners() {
        // Test canMove checks all 4 corners of the sprite
        // A position where top-left is valid but bottom-right hits a wall
        
        // Tile (1,1) is a point, tile (0,0) is wall
        // Position (10, 10) with size 20 would overlap wall at (0,0)
        assertFalse(board.canMove(10, 10, 20));
    }

    @Test
    void testIsWallBetweenBounds() {
        // Test various positions within bounds
        for (int y = 0; y < Board.BOARD_HEIGHT; y++) {
            for (int x = 0; x < Board.BOARD_WIDTH; x++) {
                int pixelX = x * Board.TILE_SIZE;
                int pixelY = y * Board.TILE_SIZE;
                
                // Should not throw exception
                assertDoesNotThrow(() -> board.isWall(pixelX, pixelY));
            }
        }
    }

    @Test
    void testMultipleActionPerformed() {
        ActionEvent mockEvent = mock(ActionEvent.class);
        
        // Multiple actions should work
        for (int i = 0; i < 10; i++) {
            assertDoesNotThrow(() -> board.actionPerformed(mockEvent));
        }
    }

    @Test
    void testWrapXBoundaryConditions() {
        // Test exact boundaries
        assertEquals(0, board.wrapX(Board.BOARD_WIDTH * Board.TILE_SIZE));
        assertEquals(-Board.TILE_SIZE, board.wrapX(-Board.TILE_SIZE));
        
        // Test just inside boundaries
        int insideRight = Board.BOARD_WIDTH * Board.TILE_SIZE - 1;
        assertEquals(insideRight, board.wrapX(insideRight));
        
        int insideLeft = 0;
        assertEquals(insideLeft, board.wrapX(insideLeft));
    }

    @Test
    void testCheckPointCollisionMethod() throws Exception {
        // Get pacman and move it to a point position
        Field pacmanField = Board.class.getDeclaredField("pacman");
        pacmanField.setAccessible(true);
        Pacman pacman = (Pacman) pacmanField.get(board);
        
        int initialScore = pacman.getScore();
        
        // Simulate several game ticks
        ActionEvent mockEvent = mock(ActionEvent.class);
        for (int i = 0; i < 50; i++) {
            board.actionPerformed(mockEvent);
        }
        
        // Score might have increased if pacman collected points
        // This depends on pacman's movement and collision with points
    }

    @Test
    void testDrawBoardMethod() throws Exception {
        Graphics mockGraphics = mock(Graphics.class);
        
        Method drawBoardMethod = Board.class.getDeclaredMethod("drawBoard", Graphics.class);
        drawBoardMethod.setAccessible(true);
        
        assertDoesNotThrow(() -> drawBoardMethod.invoke(board, mockGraphics));
    }

    @Test
    void testDrawStatusMethod() throws Exception {
        Graphics mockGraphics = mock(Graphics.class);
        
        Method drawStatusMethod = Board.class.getDeclaredMethod("drawStatus", Graphics.class);
        drawStatusMethod.setAccessible(true);
        
        assertDoesNotThrow(() -> drawStatusMethod.invoke(board, mockGraphics));
    }

    @Test
    void testDrawStatusWhenGameWon() throws Exception {
        Graphics mockGraphics = mock(Graphics.class);
        
        // Set gameWon to true
        Field gameWonField = Board.class.getDeclaredField("gameWon");
        gameWonField.setAccessible(true);
        gameWonField.set(board, true);
        
        Method drawStatusMethod = Board.class.getDeclaredMethod("drawStatus", Graphics.class);
        drawStatusMethod.setAccessible(true);
        
        assertDoesNotThrow(() -> drawStatusMethod.invoke(board, mockGraphics));
    }

    @Test
    void testDrawStatusWhenGameLost() throws Exception {
        Graphics mockGraphics = mock(Graphics.class);
        
        // Set gameLost to true
        Field gameLostField = Board.class.getDeclaredField("gameLost");
        gameLostField.setAccessible(true);
        gameLostField.set(board, true);
        
        Method drawStatusMethod = Board.class.getDeclaredMethod("drawStatus", Graphics.class);
        drawStatusMethod.setAccessible(true);
        
        assertDoesNotThrow(() -> drawStatusMethod.invoke(board, mockGraphics));
    }

    @Test
    void testCheckPointCollision() throws Exception {
        Method checkPointCollisionMethod = Board.class.getDeclaredMethod("checkPointCollision");
        checkPointCollisionMethod.setAccessible(true);
        
        assertDoesNotThrow(() -> checkPointCollisionMethod.invoke(board));
    }

    @Test
    void testCheckGhostCollision() throws Exception {
        Method checkGhostCollisionMethod = Board.class.getDeclaredMethod("checkGhostCollision");
        checkGhostCollisionMethod.setAccessible(true);
        
        assertDoesNotThrow(() -> checkGhostCollisionMethod.invoke(board));
    }

    @Test
    void testCheckLevelComplete() throws Exception {
        Method checkLevelCompleteMethod = Board.class.getDeclaredMethod("checkLevelComplete");
        checkLevelCompleteMethod.setAccessible(true);
        
        assertDoesNotThrow(() -> checkLevelCompleteMethod.invoke(board));
    }

    @Test
    void testLoadLevelMethod() throws Exception {
        Method loadLevelMethod = Board.class.getDeclaredMethod("loadLevel", int.class);
        loadLevelMethod.setAccessible(true);
        
        // Load level 2
        assertDoesNotThrow(() -> loadLevelMethod.invoke(board, 2));
        
        // Load level 3
        assertDoesNotThrow(() -> loadLevelMethod.invoke(board, 3));
        
        // Load level 4 (should wrap to level 1)
        assertDoesNotThrow(() -> loadLevelMethod.invoke(board, 4));
    }

    @Test
    void testGameStopsOnLoss() throws Exception {
        Field gameLostField = Board.class.getDeclaredField("gameLost");
        gameLostField.setAccessible(true);
        gameLostField.set(board, true);
        
        ActionEvent mockEvent = mock(ActionEvent.class);
        
        // Get pacman position before
        Field pacmanField = Board.class.getDeclaredField("pacman");
        pacmanField.setAccessible(true);
        Pacman pacman = (Pacman) pacmanField.get(board);
        int xBefore = pacman.getX();
        int yBefore = pacman.getY();
        
        // Action performed should not move anything when game is lost
        board.actionPerformed(mockEvent);
        
        assertEquals(xBefore, pacman.getX());
        assertEquals(yBefore, pacman.getY());
    }

    @Test
    void testGameStopsOnWin() throws Exception {
        Field gameWonField = Board.class.getDeclaredField("gameWon");
        gameWonField.setAccessible(true);
        gameWonField.set(board, true);
        
        ActionEvent mockEvent = mock(ActionEvent.class);
        
        // Get pacman position before
        Field pacmanField = Board.class.getDeclaredField("pacman");
        pacmanField.setAccessible(true);
        Pacman pacman = (Pacman) pacmanField.get(board);
        int xBefore = pacman.getX();
        int yBefore = pacman.getY();
        
        // Action performed should not move anything when game is won
        board.actionPerformed(mockEvent);
        
        assertEquals(xBefore, pacman.getX());
        assertEquals(yBefore, pacman.getY());
    }

    @Test
    void testGhostCollisionDetection() throws Exception {
        // Move pacman to a ghost position to trigger collision
        Field pacmanField = Board.class.getDeclaredField("pacman");
        pacmanField.setAccessible(true);
        
        Field ghostsField = Board.class.getDeclaredField("ghosts");
        ghostsField.setAccessible(true);
        Ghost[] ghosts = (Ghost[]) ghostsField.get(board);
        
        // Create a pacman at ghost position
        Pacman collisionPacman = new Pacman(ghosts[0].getX(), ghosts[0].getY(), board);
        pacmanField.set(board, collisionPacman);
        
        Method checkGhostCollisionMethod = Board.class.getDeclaredMethod("checkGhostCollision");
        checkGhostCollisionMethod.setAccessible(true);
        checkGhostCollisionMethod.invoke(board);
        
        // Game should be lost
        Field gameLostField = Board.class.getDeclaredField("gameLost");
        gameLostField.setAccessible(true);
        boolean gameLost = (boolean) gameLostField.get(board);
        assertTrue(gameLost);
    }

    @Test
    void testLevelCompleteDetection() throws Exception {
        // Set pointsEaten to equal totalPoints
        Field pointsEatenField = Board.class.getDeclaredField("pointsEaten");
        pointsEatenField.setAccessible(true);
        
        Field totalPointsField = Board.class.getDeclaredField("totalPoints");
        totalPointsField.setAccessible(true);
        int totalPoints = (int) totalPointsField.get(board);
        
        pointsEatenField.set(board, totalPoints);
        
        Method checkLevelCompleteMethod = Board.class.getDeclaredMethod("checkLevelComplete");
        checkLevelCompleteMethod.setAccessible(true);
        checkLevelCompleteMethod.invoke(board);
        
        // Game should be won
        Field gameWonField = Board.class.getDeclaredField("gameWon");
        gameWonField.setAccessible(true);
        boolean gameWon = (boolean) gameWonField.get(board);
        assertTrue(gameWon);
    }

    @Test
    void testPointCollisionIncreasesScore() throws Exception {
        Field pacmanField = Board.class.getDeclaredField("pacman");
        pacmanField.setAccessible(true);
        
        Field levelMapField = Board.class.getDeclaredField("levelMap");
        levelMapField.setAccessible(true);
        int[][] levelMap = (int[][]) levelMapField.get(board);
        
        // Find a point position
        int pointX = -1, pointY = -1;
        for (int y = 0; y < Board.BOARD_HEIGHT && pointX == -1; y++) {
            for (int x = 0; x < Board.BOARD_WIDTH && pointX == -1; x++) {
                if (levelMap[y][x] == Board.POINT) {
                    pointX = x * Board.TILE_SIZE;
                    pointY = y * Board.TILE_SIZE;
                }
            }
        }
        
        if (pointX != -1) {
            // Move pacman to point position
            Pacman pacmanAtPoint = new Pacman(pointX, pointY, board);
            pacmanField.set(board, pacmanAtPoint);
            
            int initialScore = pacmanAtPoint.getScore();
            
            Method checkPointCollisionMethod = Board.class.getDeclaredMethod("checkPointCollision");
            checkPointCollisionMethod.setAccessible(true);
            checkPointCollisionMethod.invoke(board);
            
            // Score should increase by 10
            assertEquals(initialScore + 10, pacmanAtPoint.getScore());
        }
    }

    @Test
    void testPointRemovedAfterCollection() throws Exception {
        Field pacmanField = Board.class.getDeclaredField("pacman");
        pacmanField.setAccessible(true);
        
        Field levelMapField = Board.class.getDeclaredField("levelMap");
        levelMapField.setAccessible(true);
        int[][] levelMap = (int[][]) levelMapField.get(board);
        
        // Find a point position
        int pointTileX = -1, pointTileY = -1;
        for (int y = 0; y < Board.BOARD_HEIGHT && pointTileX == -1; y++) {
            for (int x = 0; x < Board.BOARD_WIDTH && pointTileX == -1; x++) {
                if (levelMap[y][x] == Board.POINT) {
                    pointTileX = x;
                    pointTileY = y;
                }
            }
        }
        
        if (pointTileX != -1) {
            // Move pacman to point position
            int pixelX = pointTileX * Board.TILE_SIZE;
            int pixelY = pointTileY * Board.TILE_SIZE;
            Pacman pacmanAtPoint = new Pacman(pixelX, pixelY, board);
            pacmanField.set(board, pacmanAtPoint);
            
            Method checkPointCollisionMethod = Board.class.getDeclaredMethod("checkPointCollision");
            checkPointCollisionMethod.setAccessible(true);
            checkPointCollisionMethod.invoke(board);
            
            // Point should be replaced with EMPTY
            assertEquals(Board.EMPTY, levelMap[pointTileY][pointTileX]);
        }
    }

    @Test
    void testCheckPointCollisionOutOfBounds() throws Exception {
        Field pacmanField = Board.class.getDeclaredField("pacman");
        pacmanField.setAccessible(true);
        
        // Move pacman to out of bounds position
        Pacman outOfBoundsPacman = new Pacman(-100, -100, board);
        pacmanField.set(board, outOfBoundsPacman);
        
        Method checkPointCollisionMethod = Board.class.getDeclaredMethod("checkPointCollision");
        checkPointCollisionMethod.setAccessible(true);
        
        // Should not throw exception for out of bounds
        assertDoesNotThrow(() -> checkPointCollisionMethod.invoke(board));
    }

    @Test
    void testCheckPointCollisionOnEmptyTile() throws Exception {
        Field pacmanField = Board.class.getDeclaredField("pacman");
        pacmanField.setAccessible(true);
        
        Field levelMapField = Board.class.getDeclaredField("levelMap");
        levelMapField.setAccessible(true);
        int[][] levelMap = (int[][]) levelMapField.get(board);
        
        // Find an empty (not point, not wall) position
        int emptyTileX = -1, emptyTileY = -1;
        for (int y = 0; y < Board.BOARD_HEIGHT && emptyTileX == -1; y++) {
            for (int x = 0; x < Board.BOARD_WIDTH && emptyTileX == -1; x++) {
                if (levelMap[y][x] == Board.EMPTY) {
                    emptyTileX = x;
                    emptyTileY = y;
                }
            }
        }
        
        if (emptyTileX != -1) {
            // Move pacman to empty tile position
            int pixelX = emptyTileX * Board.TILE_SIZE;
            int pixelY = emptyTileY * Board.TILE_SIZE;
            Pacman pacmanOnEmpty = new Pacman(pixelX, pixelY, board);
            pacmanField.set(board, pacmanOnEmpty);
            
            int initialScore = pacmanOnEmpty.getScore();
            
            Method checkPointCollisionMethod = Board.class.getDeclaredMethod("checkPointCollision");
            checkPointCollisionMethod.setAccessible(true);
            checkPointCollisionMethod.invoke(board);
            
            // Score should NOT increase (no point on this tile)
            assertEquals(initialScore, pacmanOnEmpty.getScore());
        }
    }

    @Test
    void testCheckPointCollisionOnWallTile() throws Exception {
        Field pacmanField = Board.class.getDeclaredField("pacman");
        pacmanField.setAccessible(true);
        
        // Pacman is at corner (which is a wall) - score should not change
        // This tests the path where tile is in bounds but is not a POINT
        Pacman pacmanOnWall = new Pacman(0, 0, board);
        pacmanField.set(board, pacmanOnWall);
        
        int initialScore = pacmanOnWall.getScore();
        
        Method checkPointCollisionMethod = Board.class.getDeclaredMethod("checkPointCollision");
        checkPointCollisionMethod.setAccessible(true);
        checkPointCollisionMethod.invoke(board);
        
        // Score should NOT increase
        assertEquals(initialScore, pacmanOnWall.getScore());
    }

    @Test
    void testLevelCompleteTimerFires() throws Exception {
        // Set pointsEaten to equal totalPoints to trigger level completion
        Field pointsEatenField = Board.class.getDeclaredField("pointsEaten");
        pointsEatenField.setAccessible(true);
        
        Field totalPointsField = Board.class.getDeclaredField("totalPoints");
        totalPointsField.setAccessible(true);
        int totalPoints = (int) totalPointsField.get(board);
        
        Field currentLevelField = Board.class.getDeclaredField("currentLevel");
        currentLevelField.setAccessible(true);
        int initialLevel = (int) currentLevelField.get(board);
        
        pointsEatenField.set(board, totalPoints);
        
        Method checkLevelCompleteMethod = Board.class.getDeclaredMethod("checkLevelComplete");
        checkLevelCompleteMethod.setAccessible(true);
        checkLevelCompleteMethod.invoke(board);
        
        // Wait for the timer to fire (timer delay is 2000ms, wait a bit longer)
        Thread.sleep(2500);
        
        // The level should have advanced
        int newLevel = (int) currentLevelField.get(board);
        assertEquals(initialLevel + 1, newLevel);
        
        // Game should no longer be won (reset for new level)
        Field gameWonField = Board.class.getDeclaredField("gameWon");
        gameWonField.setAccessible(true);
        boolean gameWon = (boolean) gameWonField.get(board);
        assertFalse(gameWon);
    }
}
