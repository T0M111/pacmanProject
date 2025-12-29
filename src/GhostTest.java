import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.Color;

/**
 * Unit tests for the Ghost class.
 */
public class GhostTest {

    private Ghost ghost;
    private Board board;
    private Pacman pacman;

    @BeforeEach
    public void setUp() {
        board = new Board();
        pacman = new Pacman(100, 100, board);
        ghost = new Ghost(200, 200, Color.RED, board, pacman, 1);
    }

    @Test
    public void testInitialPosition() {
        Assertions.assertEquals(200, ghost.getX());
        Assertions.assertEquals(200, ghost.getY());
    }

    @Test
    public void testGettersReturnCorrectValues() {
        Ghost testGhost = new Ghost(150, 250, Color.PINK, board, pacman, 2);
        Assertions.assertEquals(150, testGhost.getX());
        Assertions.assertEquals(250, testGhost.getY());
    }

    @Test
    public void testGhostWithDifferentColors() {
        Ghost redGhost = new Ghost(100, 100, Color.RED, board, pacman, 1);
        Ghost pinkGhost = new Ghost(100, 100, Color.PINK, board, pacman, 1);
        Ghost cyanGhost = new Ghost(100, 100, Color.CYAN, board, pacman, 1);
        
        assertNotNull(redGhost);
        assertNotNull(pinkGhost);
        assertNotNull(cyanGhost);
    }

    @Test
    public void testGhostWithDifferentIntelligenceLevels() {
        Ghost level1Ghost = new Ghost(100, 100, Color.RED, board, pacman, 1);
        Ghost level2Ghost = new Ghost(100, 100, Color.PINK, board, pacman, 2);
        Ghost level3Ghost = new Ghost(100, 100, Color.CYAN, board, pacman, 3);
        
        assertNotNull(level1Ghost);
        assertNotNull(level2Ghost);
        assertNotNull(level3Ghost);
    }

    @Test
    public void testPositionAfterCreation() {
        Ghost ghost1 = new Ghost(180, 220, Color.ORANGE, board, pacman, 1);
        Assertions.assertEquals(180, ghost1.getX());
        Assertions.assertEquals(220, ghost1.getY());
    }
}
