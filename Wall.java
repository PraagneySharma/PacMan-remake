
import java.awt.*;
import java.util.ArrayList;

public class Wall {

    private int x, y, width, height;
    
    public static final int wall  = 1;
    public static final int straightWall = 5;
    public static final int gate = 4;
    private static Image defaultTile = Util.loadImage("tiles/tile001.png");

    private static Image []tileImages;

    public Wall(int xx, int yy, int width, int height) {
        if(tileImages == null){
            tileImages = new Image[17];
            for(int i=0; i<17; i++){
                 tileImages[i] = Util.loadImage(String.format("tiles/tile%03d.png", i+1));
            }
        }
        x = xx;
        y = yy;
        this.width = width;
        this.height = height;
        
    }
    public static void addWallToGrid(int[][] grid, ArrayList<Wall> WallsList) {
        int width = defaultTile.getWidth(null);
        int height = defaultTile.getHeight(null);
        int tileX = 0;
        int tileY = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                //GENERATION OF WALLS
                if (grid[i][j] == wall || grid[i][j] == gate || grid[i][j] == straightWall) {
                    WallsList.add(new Wall(tileX, tileY, width, height));
                    tileX += width;
                    if (tileX >= GamePanel.WIDTH) { // 400 is the width of the screen, if the tile is at the end of the screen, move to the next line
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

    public int getX() {
        // return x of tile
        return x;
    }

    public int getY() {
        // return y of tile
        return y;
    }

    public int getWidth() {
        // return width of tile
        return width;
    }

    public int getHeight() {
        // return height of tile
        return height;
    }
    public Image[] getTileImages(){
        return tileImages;
    } 
    public Rectangle getRect(){
        return new Rectangle(x, y, width, height);
    }
    

    public void draw(Graphics g, int[][] grid) {
        int CORRESPONDING_WALL = 0;
        int GATE_IMAGE = 16;
        int STRAIGHT_WALL_IMAGE = 2;
        
        
        if (grid[y / height][x / width] == wall) {
            // finding where tile is in the grid, checking if there is any other tiles adjacent to it
            if (x > 0 && grid[y / height][x / width - 1] == wall || x>0 && grid[y / height][x / width - 1] == straightWall) {
                CORRESPONDING_WALL += 1; // IF THERE IS WALL TO LEFT
            }
            if (x < GamePanel.WIDTH - width && grid[y / height][x / width + 1] == wall || x<GamePanel.WIDTH - width && grid[y / height][x / width + 1] == straightWall) {
                CORRESPONDING_WALL += 2; // IF THERE IS WALL TO RIGHT
            }
            if (y > 0 && grid[y / height - 1][x / width] == wall) {
                CORRESPONDING_WALL += 4; // IF THERE IS WALL ABOVE
            }
            if (y < GamePanel.HEIGHT - height && grid[y / height + 1][x / width] == wall) {
                CORRESPONDING_WALL += 8; // IF THERE IS WALL BELOW
            }
            g.drawImage(tileImages[CORRESPONDING_WALL-1], x, y, x + width, y + height, 0, 0, width, height, null);
        }
        else if (grid[y / height][x / width] == gate) {
            g.drawImage(tileImages[GATE_IMAGE], x, y, x + width, y + height, 0, 0, width, height, null);// IF THERE IS GATE BELOW
        }
        else if (grid[y / height][x / width] == straightWall) {
            g.drawImage(tileImages[STRAIGHT_WALL_IMAGE], x, y, x + width, y + height, 0, 0, width, height, null);// IF THERE IS STRAIGHT WALL
        }
        
        
    }
    public static void main(String[] arguments) {
        new Main();
    }
}
