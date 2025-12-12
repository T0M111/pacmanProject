import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Game class.
 * Note: These tests are limited because Game extends JFrame which requires a display.
 */
public class GameTest {

    @Test
    public void testGameClassExists() {
        // Simple test to verify the Game class can be loaded
        Class<?> gameClass = Game.class;
        assertNotNull(gameClass);
        assertEquals("Game", gameClass.getSimpleName());
    }

    @Test
    public void testGameExtendsJFrame() {
        // Verify Game is a subclass of JFrame without instantiating
        assertTrue(javax.swing.JFrame.class.isAssignableFrom(Game.class));
    }

    @Test
    public void testGameHasMainMethod() throws NoSuchMethodException {
        // Verify the main method exists
        java.lang.reflect.Method mainMethod = Game.class.getMethod("main", String[].class);
        assertNotNull(mainMethod);
        assertEquals(void.class, mainMethod.getReturnType());
        assertTrue(java.lang.reflect.Modifier.isStatic(mainMethod.getModifiers()));
    }
}
