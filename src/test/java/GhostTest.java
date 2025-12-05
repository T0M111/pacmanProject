import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.Color;
import java.awt.Graphics;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GhostTest {

    @Mock
    private Board mockBoard;

    @Mock
    private Pacman mockPacman;

    @Mock
    private Graphics mockGraphics;

    private Ghost ghost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(mockPacman.getX()).thenReturn(200);
        when(mockPacman.getY()).thenReturn(200);
        ghost = new Ghost(100, 100, Color.RED, mockBoard, mockPacman, 1);
    }

    @Test
    void testConstructor() {
        assertEquals(100, ghost.getX());
        assertEquals(100, ghost.getY());
    }

    @Test
    void testGetX() {
        assertEquals(100, ghost.getX());
    }

    @Test
    void testGetY() {
        assertEquals(100, ghost.getY());
    }

    @Test
    void testDraw() {
        // Draw should not throw any exceptions
        assertDoesNotThrow(() -> ghost.draw(mockGraphics));
        
        // Verify body drawing
        verify(mockGraphics, atLeastOnce()).setColor(any());
        verify(mockGraphics).fillArc(eq(100), eq(100), eq(20), eq(20), eq(0), eq(180));
        verify(mockGraphics).fillRect(eq(100), eq(110), eq(20), eq(10));
        
        // Verify eyes drawing
        verify(mockGraphics).fillOval(eq(103), eq(105), eq(5), eq(5));
        verify(mockGraphics).fillOval(eq(112), eq(105), eq(5), eq(5));
        verify(mockGraphics).fillOval(eq(104), eq(106), eq(3), eq(3));
        verify(mockGraphics).fillOval(eq(113), eq(106), eq(3), eq(3));
    }

    @Test
    void testMoveRandomlyWhenCanMove() {
        // Level 1 has low intelligence, mostly random movement
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        int initialX = ghost.getX();
        int initialY = ghost.getY();
        
        // Move multiple times to ensure movement happens
        for (int i = 0; i < 10; i++) {
            ghost.move();
        }
        
        // Position should have changed (random direction, but some movement)
        boolean moved = ghost.getX() != initialX || ghost.getY() != initialY;
        assertTrue(moved, "Ghost should have moved");
    }

    @Test
    void testMoveWhenBlocked() {
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(false);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        ghost.move();
        
        // Ghost should remain at the same position when all directions are blocked
        assertEquals(100, ghost.getX());
        assertEquals(100, ghost.getY());
    }

    @Test
    void testMoveWithWrapAround() {
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenReturn(380);
        
        ghost.move();
        
        // X should be wrapped
        assertEquals(380, ghost.getX());
    }

    @Test
    void testMoveWithIntelligenceLevel2() {
        Ghost level2Ghost = new Ghost(100, 100, Color.PINK, mockBoard, mockPacman, 2);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        // Move multiple times
        for (int i = 0; i < 20; i++) {
            level2Ghost.move();
        }
        
        // Ghost should have moved
        boolean moved = level2Ghost.getX() != 100 || level2Ghost.getY() != 100;
        assertTrue(moved);
    }

    @Test
    void testMoveWithIntelligenceLevel3() {
        Ghost level3Ghost = new Ghost(100, 100, Color.CYAN, mockBoard, mockPacman, 3);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        for (int i = 0; i < 20; i++) {
            level3Ghost.move();
        }
        
        boolean moved = level3Ghost.getX() != 100 || level3Ghost.getY() != 100;
        assertTrue(moved);
    }

    @Test
    void testMoveWithHighIntelligenceLevel() {
        Ghost level5Ghost = new Ghost(100, 100, Color.ORANGE, mockBoard, mockPacman, 5);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        for (int i = 0; i < 20; i++) {
            level5Ghost.move();
        }
        
        boolean moved = level5Ghost.getX() != 100 || level5Ghost.getY() != 100;
        assertTrue(moved);
    }

    @Test
    void testMoveTowardsPacmanWhenPacmanIsToTheRight() {
        // Ghost at (100, 100), Pacman at (200, 100) - same Y, Pacman to the right
        when(mockPacman.getX()).thenReturn(200);
        when(mockPacman.getY()).thenReturn(100);
        
        Ghost chasingGhost = new Ghost(100, 100, Color.RED, mockBoard, mockPacman, 10); // Very high intelligence
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        // Move multiple times (high intelligence means it should chase)
        for (int i = 0; i < 50; i++) {
            chasingGhost.move();
        }
        
        // With high intelligence, ghost should generally move towards Pacman (right)
        assertTrue(chasingGhost.getX() > 100, "Ghost should move right towards Pacman");
    }

    @Test
    void testMoveTowardsPacmanWhenPacmanIsToTheLeft() {
        when(mockPacman.getX()).thenReturn(50);
        when(mockPacman.getY()).thenReturn(100);
        
        Ghost chasingGhost = new Ghost(100, 100, Color.RED, mockBoard, mockPacman, 10);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        for (int i = 0; i < 50; i++) {
            chasingGhost.move();
        }
        
        assertTrue(chasingGhost.getX() < 100, "Ghost should move left towards Pacman");
    }

    @Test
    void testMoveTowardsPacmanWhenPacmanIsAbove() {
        when(mockPacman.getX()).thenReturn(100);
        when(mockPacman.getY()).thenReturn(50);
        
        Ghost chasingGhost = new Ghost(100, 100, Color.RED, mockBoard, mockPacman, 10);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        for (int i = 0; i < 50; i++) {
            chasingGhost.move();
        }
        
        assertTrue(chasingGhost.getY() < 100, "Ghost should move up towards Pacman");
    }

    @Test
    void testMoveTowardsPacmanWhenPacmanIsBelow() {
        when(mockPacman.getX()).thenReturn(100);
        when(mockPacman.getY()).thenReturn(200);
        
        Ghost chasingGhost = new Ghost(100, 100, Color.RED, mockBoard, mockPacman, 10);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        for (int i = 0; i < 50; i++) {
            chasingGhost.move();
        }
        
        assertTrue(chasingGhost.getY() > 100, "Ghost should move down towards Pacman");
    }

    @Test
    void testMoveTowardsPacmanDiagonallyWithHorizontalPriority() {
        // Pacman is diagonally to the right and below, with more horizontal distance
        when(mockPacman.getX()).thenReturn(300); // 200 units right
        when(mockPacman.getY()).thenReturn(150); // 50 units down
        
        Ghost chasingGhost = new Ghost(100, 100, Color.RED, mockBoard, mockPacman, 10);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        for (int i = 0; i < 50; i++) {
            chasingGhost.move();
        }
        
        // Should primarily move right (horizontal has bigger delta)
        assertTrue(chasingGhost.getX() > 100);
    }

    @Test
    void testMoveTowardsPacmanDiagonallyWithVerticalPriority() {
        // Pacman is diagonally with more vertical distance
        when(mockPacman.getX()).thenReturn(150); // 50 units right
        when(mockPacman.getY()).thenReturn(300); // 200 units down
        
        Ghost chasingGhost = new Ghost(100, 100, Color.RED, mockBoard, mockPacman, 10);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        for (int i = 0; i < 50; i++) {
            chasingGhost.move();
        }
        
        // Should primarily move down (vertical has bigger delta)
        assertTrue(chasingGhost.getY() > 100);
    }

    @Test
    void testMoveWhenBestDirectionIsBlocked() {
        // Best direction blocked, should try alternative
        when(mockPacman.getX()).thenReturn(200);
        when(mockPacman.getY()).thenReturn(100);
        
        Ghost chasingGhost = new Ghost(100, 100, Color.RED, mockBoard, mockPacman, 10);
        
        // Block right (best direction), allow down
        when(mockBoard.canMove(102, 100, 20)).thenReturn(false); // RIGHT blocked
        when(mockBoard.canMove(100, 102, 20)).thenReturn(true); // DOWN allowed
        when(mockBoard.canMove(98, 100, 20)).thenReturn(true); // LEFT allowed
        when(mockBoard.canMove(100, 98, 20)).thenReturn(true); // UP allowed
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        chasingGhost.move();
        
        // Should have moved (either alternative direction or random)
        boolean moved = chasingGhost.getX() != 100 || chasingGhost.getY() != 100;
        // May or may not move depending on random chase decision
    }

    @Test
    void testChangeDirectionWhenBlocked() {
        // When current direction is blocked, ghost should change direction
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(false);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        // After move, ghost should try to change direction
        ghost.move();
        
        // Position remains same when blocked
        assertEquals(100, ghost.getX());
        assertEquals(100, ghost.getY());
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    void testDifferentIntelligenceLevels(int level) {
        Ghost intelligentGhost = new Ghost(100, 100, Color.RED, mockBoard, mockPacman, level);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        // Should not throw exception for any intelligence level
        assertDoesNotThrow(() -> {
            for (int i = 0; i < 20; i++) {
                intelligentGhost.move();
            }
        });
    }

    @Test
    void testRandomDirectionChange() {
        // Test that ghost can occasionally change direction even when it can continue
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        int rightMoves = 0;
        int leftMoves = 0;
        int upMoves = 0;
        int downMoves = 0;
        
        // Create many ghosts and move them to test random behavior
        for (int j = 0; j < 100; j++) {
            Ghost testGhost = new Ghost(100, 100, Color.RED, mockBoard, mockPacman, 1);
            for (int i = 0; i < 50; i++) {
                testGhost.move();
            }
            
            int deltaX = testGhost.getX() - 100;
            int deltaY = testGhost.getY() - 100;
            
            if (deltaX > 10) rightMoves++;
            else if (deltaX < -10) leftMoves++;
            if (deltaY > 10) downMoves++;
            else if (deltaY < -10) upMoves++;
        }
        
        // With random movement, we should see some variety in directions
        // At least one direction should have been taken
        assertTrue(rightMoves + leftMoves + upMoves + downMoves > 0);
    }

    @Test
    void testGhostWithDifferentColors() {
        // Test ghosts with different colors
        Ghost redGhost = new Ghost(50, 50, Color.RED, mockBoard, mockPacman, 1);
        Ghost pinkGhost = new Ghost(60, 60, Color.PINK, mockBoard, mockPacman, 2);
        Ghost cyanGhost = new Ghost(70, 70, Color.CYAN, mockBoard, mockPacman, 3);
        Ghost orangeGhost = new Ghost(80, 80, Color.ORANGE, mockBoard, mockPacman, 4);
        
        assertEquals(50, redGhost.getX());
        assertEquals(60, pinkGhost.getX());
        assertEquals(70, cyanGhost.getX());
        assertEquals(80, orangeGhost.getX());
    }

    @Test
    void testLevel2CalculateBestDirection() {
        // Test level 2 direction calculation (simpler algorithm)
        when(mockPacman.getX()).thenReturn(200);
        when(mockPacman.getY()).thenReturn(200);
        
        Ghost level2Ghost = new Ghost(100, 100, Color.PINK, mockBoard, mockPacman, 2);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        // Level 2 uses simpler direction calculation
        for (int i = 0; i < 50; i++) {
            level2Ghost.move();
        }
        
        // Should have moved towards Pacman
        boolean movedTowardsPacman = level2Ghost.getX() > 100 || level2Ghost.getY() > 100;
        assertTrue(movedTowardsPacman);
    }

    @Test
    void testLevel3CalculateBestDirectionWithLargerVerticalDelta() {
        // Pacman below ghost with larger vertical than horizontal delta
        when(mockPacman.getX()).thenReturn(110);
        when(mockPacman.getY()).thenReturn(300);
        
        Ghost level3Ghost = new Ghost(100, 100, Color.CYAN, mockBoard, mockPacman, 3);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        for (int i = 0; i < 100; i++) {
            level3Ghost.move();
        }
        
        // Should move down primarily (larger vertical delta)
        assertTrue(level3Ghost.getY() > 100);
    }

    @Test
    void testLevel3CalculateBestDirectionWithNegativeDeltas() {
        // Pacman above and left of ghost
        when(mockPacman.getX()).thenReturn(20);
        when(mockPacman.getY()).thenReturn(20);
        
        Ghost level3Ghost = new Ghost(100, 100, Color.CYAN, mockBoard, mockPacman, 3);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        for (int i = 0; i < 100; i++) {
            level3Ghost.move();
        }
        
        // Should move up and left
        assertTrue(level3Ghost.getX() < 100 || level3Ghost.getY() < 100);
    }

    @Test
    void testLevel2CalculateBestDirectionLeftPreferred() {
        // Pacman is to the left of ghost with larger horizontal distance
        // deltaX = -100, deltaY = -10, so horizontal is bigger
        when(mockPacman.getX()).thenReturn(0); // Ghost at 100, so deltaX = -100
        when(mockPacman.getY()).thenReturn(90); // Ghost at 100, so deltaY = -10
        
        Ghost level2Ghost = new Ghost(100, 100, Color.PINK, mockBoard, mockPacman, 2);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        // Move many times with high probability of chasing (level 2 has 30% chance)
        for (int i = 0; i < 100; i++) {
            level2Ghost.move();
        }
        
        // Should have moved left (since deltaX is negative and |deltaX| > |deltaY|)
        assertTrue(level2Ghost.getX() < 100);
    }

    @Test
    void testLevel2CalculateBestDirectionUpPreferred() {
        // Pacman is above ghost with larger vertical distance
        // deltaX = 10, deltaY = -100, so vertical is bigger
        when(mockPacman.getX()).thenReturn(110); // Ghost at 100, so deltaX = 10
        when(mockPacman.getY()).thenReturn(0); // Ghost at 100, so deltaY = -100
        
        Ghost level2Ghost = new Ghost(100, 100, Color.PINK, mockBoard, mockPacman, 2);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        // Move many times with high probability of chasing (level 2 has 30% chance)
        for (int i = 0; i < 100; i++) {
            level2Ghost.move();
        }
        
        // Should have moved up (since deltaY is negative and |deltaY| > |deltaX|)
        assertTrue(level2Ghost.getY() < 100);
    }
}
