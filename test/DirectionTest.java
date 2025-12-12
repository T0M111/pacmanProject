import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Direction enum.
 */
public class DirectionTest {

    @Test
    public void testLeftDirection() {
        Direction left = Direction.LEFT;
        assertEquals(180, left.getAngle());
    }

    @Test
    public void testRightDirection() {
        Direction right = Direction.RIGHT;
        assertEquals(0, right.getAngle());
    }

    @Test
    public void testUpDirection() {
        Direction up = Direction.UP;
        assertEquals(90, up.getAngle());
    }

    @Test
    public void testDownDirection() {
        Direction down = Direction.DOWN;
        assertEquals(270, down.getAngle());
    }

    @Test
    public void testAllDirectionsExist() {
        Direction[] directions = Direction.values();
        assertEquals(4, directions.length);
        assertNotNull(Direction.valueOf("LEFT"));
        assertNotNull(Direction.valueOf("RIGHT"));
        assertNotNull(Direction.valueOf("UP"));
        assertNotNull(Direction.valueOf("DOWN"));
    }
}
