import javax.swing.JFrame;

public class Game extends JFrame {
    public Game() {
        Board board = new Board();
        add(board);
        setTitle("Pac-Man");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.setVisible(true);
    }
}