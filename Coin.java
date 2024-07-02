import java.util.ArrayList;

import java.awt.*;

public class Coin extends Wall {
    public static final int coin  = 2;
    private static java.awt.Image defaultTile = Util.loadImage("tiles/tile001.png");
    
    public Coin(int xx, int yy, int width, int height) {
        super(xx, yy, width, height); // calls the constructor of the superclass
    }
    public static void addCoinToGrid(int[][] grid,ArrayList<Coin> CoinsList){
        CoinsList.clear();
        int width = defaultTile.getWidth(null);
        int height = defaultTile.getHeight(null);
        int tileX = 0;
        int tileY = 0;
        //GENERATION OF COINS
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == coin) {
                    CoinsList.add(new Coin(tileX, tileY, width, height));
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
    public boolean collectCoin(ArrayList<Coin> CoinsList, Pacman pacman){
        Rectangle playerRect = pacman.getRect();
        for(int i = 0; i < CoinsList.size(); i++){
            Rectangle CoinRect = CoinsList.get(i).getRect();
            if(playerRect.intersects(CoinRect)){
                CoinsList.remove(i);
                return true;
            }
        }
        return false;
    }
    

    @Override
    public void draw(Graphics g, int[][] grid) {
        int COIN_IMAGE = 14;
        if (grid[getY() / getHeight()][getX() / getWidth()] == coin) {
            g.drawImage(getTileImages()[COIN_IMAGE], getX(), getY(), getX() + getWidth(), getY() + getHeight(), 0, 0, getWidth(), getHeight(), null);
        }
    }
}