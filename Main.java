import javax.swing.*;

public class Main extends JFrame{
    GamePanel game = new GamePanel();
    
    public Main() {
        super("Pacman!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(game);
        pack();  
        setVisible(true);
    }    
    public static void main(String[] arguments) {
        new Main();
    }
}

