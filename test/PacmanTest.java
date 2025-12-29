import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.awt.event.KeyEvent;

/**
 * Unit tests for the Pacman class.
 */
public class PacmanTest {

    private Pacman pacman;
    private Board board;

    @BeforeEach
    public void setUp() {
        board = new Board();
        pacman = new Pacman(100, 100, board);
    }

    @Test
    public void testInitialPosition() {
        assertEquals(100, pacman.getX());
        assertEquals(100, pacman.getY());
    }

    @Test
    public void testInitialScore() {
        assertEquals(0, pacman.getScore());
    }

    @Test
    public void testAddScore() {
        pacman.addScore(10);
        assertEquals(10, pacman.getScore());
        
        pacman.addScore(20);
        assertEquals(30, pacman.getScore());
    }

    @Test
    public void testAddNegativeScore() {
        pacman.addScore(50);
        pacman.addScore(-10);
        assertEquals(40, pacman.getScore());
    }

    @Test
    public void testAddMultipleScores() {
        for (int i = 0; i < 10; i++) {
            pacman.addScore(5);
        }
        assertEquals(50, pacman.getScore());
    }

    @Test
    public void testGettersReturnCorrectValues() {
        Pacman testPacman = new Pacman(200, 300, board);
        assertEquals(200, testPacman.getX());
        assertEquals(300, testPacman.getY());
        assertEquals(0, testPacman.getScore());
    }
}
