import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.Graphics;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PacmanTest {

    @Mock
    private Board mockBoard;

    @Mock
    private Graphics mockGraphics;

    private Pacman pacman;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pacman = new Pacman(100, 100, mockBoard);
    }

    @Test
    void testConstructor() {
        assertEquals(100, pacman.getX());
        assertEquals(100, pacman.getY());
        assertEquals(0, pacman.getScore());
    }

    @Test
    void testGetX() {
        assertEquals(100, pacman.getX());
    }

    @Test
    void testGetY() {
        assertEquals(100, pacman.getY());
    }

    @Test
    void testGetScore() {
        assertEquals(0, pacman.getScore());
    }

    @Test
    void testAddScore() {
        pacman.addScore(10);
        assertEquals(10, pacman.getScore());
        pacman.addScore(5);
        assertEquals(15, pacman.getScore());
    }

    @Test
    void testDraw() {
        // Draw should not throw any exceptions
        assertDoesNotThrow(() -> pacman.draw(mockGraphics));
        verify(mockGraphics).setColor(any());
        verify(mockGraphics).fillArc(eq(100), eq(100), eq(20), eq(20), anyInt(), eq(300));
    }

    @Test
    void testKeyPressedLeft() {
        KeyEvent leftKey = mock(KeyEvent.class);
        when(leftKey.getKeyCode()).thenReturn(KeyEvent.VK_LEFT);
        
        pacman.keyPressed(leftKey);
        // We can't directly check nextDirection, but we can test by moving
    }

    @Test
    void testKeyPressedRight() {
        KeyEvent rightKey = mock(KeyEvent.class);
        when(rightKey.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
        
        pacman.keyPressed(rightKey);
    }

    @Test
    void testKeyPressedUp() {
        KeyEvent upKey = mock(KeyEvent.class);
        when(upKey.getKeyCode()).thenReturn(KeyEvent.VK_UP);
        
        pacman.keyPressed(upKey);
    }

    @Test
    void testKeyPressedDown() {
        KeyEvent downKey = mock(KeyEvent.class);
        when(downKey.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
        
        pacman.keyPressed(downKey);
    }

    @Test
    void testKeyPressedOtherKey() {
        KeyEvent spaceKey = mock(KeyEvent.class);
        when(spaceKey.getKeyCode()).thenReturn(KeyEvent.VK_SPACE);
        
        pacman.keyPressed(spaceKey);
        // No exception should be thrown
    }

    @Test
    void testMoveLeftWhenCanMove() {
        // Initial direction is LEFT
        when(mockBoard.canMove(95, 100, 20)).thenReturn(true);
        when(mockBoard.wrapX(95)).thenReturn(95);
        
        pacman.move();
        
        assertEquals(95, pacman.getX());
        assertEquals(100, pacman.getY());
    }

    @Test
    void testMoveRightWhenCanMove() {
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        KeyEvent rightKey = mock(KeyEvent.class);
        when(rightKey.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
        pacman.keyPressed(rightKey);
        
        pacman.move();
        
        assertEquals(105, pacman.getX());
    }

    @Test
    void testMoveUpWhenCanMove() {
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        KeyEvent upKey = mock(KeyEvent.class);
        when(upKey.getKeyCode()).thenReturn(KeyEvent.VK_UP);
        pacman.keyPressed(upKey);
        
        pacman.move();
        
        assertEquals(95, pacman.getY());
    }

    @Test
    void testMoveDownWhenCanMove() {
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        KeyEvent downKey = mock(KeyEvent.class);
        when(downKey.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
        pacman.keyPressed(downKey);
        
        pacman.move();
        
        assertEquals(105, pacman.getY());
    }

    @Test
    void testMoveWhenCantChangeDirection() {
        // Can't move in next direction but can continue in current direction
        when(mockBoard.canMove(95, 100, 20)).thenReturn(true);
        when(mockBoard.wrapX(95)).thenReturn(95);
        
        // Try to change to UP but it's blocked
        KeyEvent upKey = mock(KeyEvent.class);
        when(upKey.getKeyCode()).thenReturn(KeyEvent.VK_UP);
        pacman.keyPressed(upKey);
        
        when(mockBoard.canMove(100, 95, 20)).thenReturn(false); // UP blocked
        
        pacman.move();
        
        // Should continue LEFT (original direction)
        assertEquals(95, pacman.getX());
        assertEquals(100, pacman.getY());
    }

    @Test
    void testMoveWhenBlocked() {
        // Can't move in any direction
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(false);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        pacman.move();
        
        // Position should remain the same
        assertEquals(100, pacman.getX());
        assertEquals(100, pacman.getY());
    }

    @Test
    void testMoveWithWrapAround() {
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        // Simulate wrap-around: going left wraps to right side
        when(mockBoard.wrapX(95)).thenReturn(380); // Wrap to right side
        
        pacman.move();
        
        assertEquals(380, pacman.getX());
    }

    @Test
    void testMoveWithNegativeWrapAround() {
        Pacman pacmanAtEdge = new Pacman(-15, 100, mockBoard);
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(-20)).thenReturn(360);
        
        pacmanAtEdge.move();
        
        assertEquals(360, pacmanAtEdge.getX());
    }

    @Test
    void testMoveRightContinuesWhenNextDirectionBlocked() {
        // First set current direction to RIGHT
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        KeyEvent rightKey = mock(KeyEvent.class);
        when(rightKey.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
        pacman.keyPressed(rightKey);
        pacman.move(); // Now direction is RIGHT
        
        // Reset to try changing to UP (blocked)
        reset(mockBoard);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        KeyEvent upKey = mock(KeyEvent.class);
        when(upKey.getKeyCode()).thenReturn(KeyEvent.VK_UP);
        pacman.keyPressed(upKey);
        
        // Block UP but allow RIGHT
        when(mockBoard.canMove(105, 95, 20)).thenReturn(false); // UP blocked
        when(mockBoard.canMove(110, 100, 20)).thenReturn(true); // RIGHT allowed
        
        int xBefore = pacman.getX();
        pacman.move();
        
        // Should continue RIGHT
        assertTrue(pacman.getX() > xBefore);
    }

    @Test
    void testMoveUpContinuesWhenNextDirectionBlocked() {
        // First set current direction to UP
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        KeyEvent upKey = mock(KeyEvent.class);
        when(upKey.getKeyCode()).thenReturn(KeyEvent.VK_UP);
        pacman.keyPressed(upKey);
        pacman.move(); // Now direction is UP, position is (100, 95)
        
        // Reset to try changing to LEFT (blocked)
        reset(mockBoard);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        KeyEvent leftKey = mock(KeyEvent.class);
        when(leftKey.getKeyCode()).thenReturn(KeyEvent.VK_LEFT);
        pacman.keyPressed(leftKey);
        
        // Block LEFT but allow UP (pacman is now at y=95)
        when(mockBoard.canMove(95, 95, 20)).thenReturn(false); // LEFT blocked
        when(mockBoard.canMove(100, 90, 20)).thenReturn(true); // UP allowed
        
        int yBefore = pacman.getY();
        pacman.move();
        
        // Should continue UP
        assertTrue(pacman.getY() < yBefore);
    }

    @Test
    void testMoveDownContinuesWhenNextDirectionBlocked() {
        // First set current direction to DOWN
        when(mockBoard.canMove(anyInt(), anyInt(), eq(20))).thenReturn(true);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        KeyEvent downKey = mock(KeyEvent.class);
        when(downKey.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
        pacman.keyPressed(downKey);
        pacman.move(); // Now direction is DOWN
        
        // Reset to try changing to RIGHT (blocked)
        reset(mockBoard);
        when(mockBoard.wrapX(anyInt())).thenAnswer(i -> i.getArgument(0));
        
        KeyEvent rightKey = mock(KeyEvent.class);
        when(rightKey.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
        pacman.keyPressed(rightKey);
        
        // Block RIGHT but allow DOWN
        when(mockBoard.canMove(105, 105, 20)).thenReturn(false); // RIGHT blocked
        when(mockBoard.canMove(100, 110, 20)).thenReturn(true); // DOWN allowed
        
        int yBefore = pacman.getY();
        pacman.move();
        
        // Should continue DOWN
        assertTrue(pacman.getY() > yBefore);
    }
}
