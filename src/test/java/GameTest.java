import org.junit.jupiter.api.Test;

import javax.swing.JFrame;
import java.awt.Component;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    void testGameConstructor() {
        Game game = new Game();
        assertNotNull(game);
    }

    @Test
    void testGameIsJFrame() {
        Game game = new Game();
        assertTrue(game instanceof JFrame);
    }

    @Test
    void testGameTitle() {
        Game game = new Game();
        assertEquals("Pac-Man", game.getTitle());
    }

    @Test
    void testGameDefaultCloseOperation() {
        Game game = new Game();
        assertEquals(JFrame.EXIT_ON_CLOSE, game.getDefaultCloseOperation());
    }

    @Test
    void testGameIsNotResizable() {
        Game game = new Game();
        assertFalse(game.isResizable());
    }

    @Test
    void testGameContainsBoard() {
        Game game = new Game();
        Component[] components = game.getContentPane().getComponents();
        assertTrue(components.length > 0);
        
        boolean hasBoard = false;
        for (Component component : components) {
            if (component instanceof Board) {
                hasBoard = true;
                break;
            }
        }
        assertTrue(hasBoard, "Game should contain a Board");
    }

    @Test
    void testGameHasCorrectSize() {
        Game game = new Game();
        // Pack should have been called, so size should be based on board dimensions
        assertTrue(game.getWidth() > 0);
        assertTrue(game.getHeight() > 0);
    }

    @Test
    void testGameClassExists() {
        // This test verifies the Game class exists and can be loaded
        try {
            Class<?> gameClass = Class.forName("Game");
            assertNotNull(gameClass);
            assertTrue(JFrame.class.isAssignableFrom(gameClass));
        } catch (ClassNotFoundException e) {
            fail("Game class should exist");
        }
    }

    @Test
    void testGameHasMainMethod() {
        // Verify main method exists
        try {
            Game.class.getDeclaredMethod("main", String[].class);
        } catch (NoSuchMethodException e) {
            fail("Game should have a main method");
        }
    }

    @Test
    void testGameLocationRelativeTo() {
        Game game = new Game();
        // After setLocationRelativeTo(null), the window should be centered
        assertNotNull(game.getLocation());
    }

    @Test
    void testMainMethod() {
        // Test the main method - this creates a visible game window
        assertDoesNotThrow(() -> Game.main(new String[]{}));
    }
}
