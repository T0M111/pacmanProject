import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class DirectionTest {

    @Test
    void testLeftAngle() {
        assertEquals(180, Direction.LEFT.getAngle());
    }

    @Test
    void testRightAngle() {
        assertEquals(0, Direction.RIGHT.getAngle());
    }

    @Test
    void testUpAngle() {
        assertEquals(90, Direction.UP.getAngle());
    }

    @Test
    void testDownAngle() {
        assertEquals(270, Direction.DOWN.getAngle());
    }

    @Test
    void testDirectionValues() {
        Direction[] directions = Direction.values();
        assertEquals(4, directions.length);
        assertEquals(Direction.LEFT, Direction.valueOf("LEFT"));
        assertEquals(Direction.RIGHT, Direction.valueOf("RIGHT"));
        assertEquals(Direction.UP, Direction.valueOf("UP"));
        assertEquals(Direction.DOWN, Direction.valueOf("DOWN"));
    }

    @ParameterizedTest
    @EnumSource(Direction.class)
    void testAllDirectionsHaveAngles(Direction direction) {
        int angle = direction.getAngle();
        assertTrue(angle >= 0 && angle < 360, "Angle should be between 0 and 360 degrees");
    }
}
