import java.awt.*;
import java.io.*;
import javax.swing.*;

class Util {
    /*
     * Util.java
     * Praagney Sharma
     * utility functions that are used in the rest of the game
     */

    public static int randint(int a, int b) {
        // returns random int between a and b
        return (int) (Math.random() * (b - a + 1) + a);
    }

    public static Image loadImage(String img) {
        // loads image
        return new ImageIcon(img).getImage();
    }

    public static Font loadFont(String name, int size) {
        // loads font
        Font font = null;
        try {
            File fntFile = new File(name);
            font = Font.createFont(Font.TRUETYPE_FONT, fntFile).deriveFont((float) size);
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (FontFormatException ex) {
            System.out.println(ex);
        }
        return font;
    }
}
