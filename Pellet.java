import java.util.ArrayList;

import java.awt.*;
import java.awt.Color;

public class Pellet extends Wall {
    public static final int pellet  = 3;
    private int framecounter;
    public boolean frightenedMode;
    
    public Pellet(int xx, int yy, int width, int height) {
        super(xx, yy, width, height); // calls the constructor of the superclass
        frightenedMode = false;
    }
    public static void addPelletToGrid(int[][] grid,ArrayList<Pellet> PelletList){
        PelletList.clear();
        int width = GamePanel.TILEWIDTH;
        int height = GamePanel.TILEHEIGHT;
        int tileX = 0;
        int tileY = 0;
        //GENERATION OF COINS
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == pellet) {
                    PelletList.add(new Pellet(tileX, tileY, width, height));
                    tileX += width;
                    if (tileX >= GamePanel.WIDTH) {
                        tileX = 0;
                        tileY += height;
                    }
                }
                else{
                    tileX += width;
                    if (tileX >= GamePanel.WIDTH) {
                        tileX = 0;
                        tileY += height;
                    }
                }
            }
        }
    }
    public void collectPellet(ArrayList<Pellet> PelletList, Pacman pacman){
        Rectangle playerRect = pacman.getRect();
        for(int i = 0; i < PelletList.size(); i++){
            Rectangle PelletRect = PelletList.get(i).getRect();
            if(playerRect.intersects(PelletRect)){
                PelletList.remove(i);
            }
        }
    }

    @Override
    public void draw(Graphics g, int[][] grid) {
        g.setColor(Color.black);
        framecounter++;
        int PELLET_IMAGE = 15;
        if (grid[getY() / getHeight()][getX() / getWidth()] == pellet){
            if (framecounter/10 % 2 == 0) { //to make it seem like the pellet is blinking
                g.drawImage(getTileImages()[PELLET_IMAGE], getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0, 0, getWidth(), getHeight(), null);
            }
        }
    }
}