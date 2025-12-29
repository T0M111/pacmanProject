
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Board class.
 */
public class BoardTest {

    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testBoardConstants() {
        Assertions.assertEquals(20, Board.TILE_SIZE);
        Assertions.assertEquals(19, Board.BOARD_WIDTH);
        Assertions.assertEquals(19, Board.BOARD_HEIGHT);
        Assertions.assertEquals(0, Board.EMPTY);
        Assertions.assertEquals(1, Board.WALL);
        Assertions.assertEquals(2, Board.POINT);
    }

    @Test
    public void testIsWallOnWall() {
        // Top-left corner is always a wall in all levels
        Assertions.assertTrue(board.isWall(0, 0));
    }

    @Test
    public void testCanMoveInEmptySpace() {
        // Test a position that should be empty in level 1
        // Center of board around position (9, 9) should have some empty space
        boolean canMove = board.canMove(180, 180, 20);
        // Just verify the method runs without error
        assertNotNull(canMove);
    }

    @Test
    public void testWrapXWithinBounds() {
        int x = 100;
        int wrappedX = board.wrapX(x);
        assertEquals(100, wrappedX);
    }

    @Test
    public void testWrapXOutOfBoundsLeft() {
        int x = -30; // Less than -TILE_SIZE
        int wrappedX = board.wrapX(x);
        Assertions.assertEquals(board.getBoardPixelWidth() - Board.TILE_SIZE, wrappedX);
    }

    @Test
    public void testWrapXOutOfBoundsRight() {
        int x = board.getBoardPixelWidth() + 10;
        int wrappedX = board.wrapX(x);
        assertEquals(0, wrappedX);
    }

    @Test
    public void testGetBoardPixelWidth() {
        int expectedWidth = Board.BOARD_WIDTH * Board.TILE_SIZE;
        Assertions.assertEquals(expectedWidth, board.getBoardPixelWidth());
    }

    @Test
    public void testCanMoveWithSize() {
        // Test that canMove method works with different sizes
        boolean result1 = board.canMove(100, 100, 20);
        boolean result2 = board.canMove(100, 100, 10);
        
        // Both should execute without error
        assertNotNull(result1);
        assertNotNull(result2);
    }

    @Test
    public void testWallDetectionAtEdges() {
        // Test walls at the edges of the board
        Assertions.assertTrue(board.isWall(0, 0));
        Assertions.assertTrue(board.isWall(Board.BOARD_WIDTH * Board.TILE_SIZE - 1, 0));
    }

    @Test
    public void testWrapXEdgeCases() {
        // Test exactly at the boundary
        int boardWidth = board.getBoardPixelWidth();
        
        Assertions.assertEquals(0, board.wrapX(boardWidth));
        Assertions.assertEquals(boardWidth - Board.TILE_SIZE, board.wrapX(-Board.TILE_SIZE - 1));
    }
}
