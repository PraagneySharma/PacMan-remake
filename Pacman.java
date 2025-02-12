/**
 * @(#)Player.java
 *
 *
 * @author 
 * @version 1.00 2024/4/5
 */
import java.awt.*;
import java.util.ArrayList;

public class Pacman {
    private int x, y, leftKey, rightKey, upKey, downKey, width, height, speed, levelDelayFrames, imgFrames, playerImage, defaultPacmanImage;
    private String NEXT_DIRECTION;
    private String CURRENT_DIRECTION;

    private static Image []playerImages;
    private static Image []playerDeathImages;


    private int lives;
    
    
    public Pacman(int left, int right, int up, int down, int speed, int x, int y, int levelDelayFrames) { 
        if(playerImages == null){
            playerImages = new Image[9];
            for(int i=0; i<9; i++){
                 playerImages[i] = Util.loadImage(String.format("PacmanAnimations/pacman/player%03d.png", i));
            }
        } 
        if(playerDeathImages == null){
            playerDeathImages = new Image[4];
            for(int i=0; i<4; i++){
                playerDeathImages[i] = Util.loadImage(String.format("PacmanAnimations/death/player%03d.png", i));
            }
        }    
        this.x = x;
        this.y = y;
        this.speed = speed;
        leftKey = left;
        rightKey = right;
        upKey = up;
        downKey = down;
        this.levelDelayFrames = levelDelayFrames;
        NEXT_DIRECTION = "LEFT";
        CURRENT_DIRECTION = "LEFT";
        width = GamePanel.TILEWIDTH;  //24
        height = GamePanel.TILEHEIGHT; //24
        lives =  3;
    }
    
    public void move(boolean []keys, ArrayList<Wall> WallsList, ArrayList<Pellet> PelletList, RedGhost redGhost, PinkGhost pinkGhost, CyanGhost cyanGhost, OrangeGhost orangeGhost){
        //if an arrow key is pressed, queue the next direction to be that direction
        if(keys[leftKey]){ 
            NEXT_DIRECTION = "LEFT";
        }
        if(keys[rightKey]){ 
            NEXT_DIRECTION = "RIGHT";
        }
        if(keys[upKey]){ 
            NEXT_DIRECTION = "UP";
        }
        if(keys[downKey]){ 
            NEXT_DIRECTION = "DOWN";
        }
        if(x<=-width/2){ //if the player goes off the screen to the left, wrap around to the right
            x = GamePanel.WIDTH - width;
        }
        else if(x>=GamePanel.WIDTH-width/2){ //if the player goes off the screen to the right, wrap around to the left
            x=0;
        }
        levelDelayFrames--;
        if (levelDelayFrames>0){ //if the level is still loading, return
            return;
        }
       // if the player is not colliding with a wall, set the next direction to the current direction
        if (NEXT_DIRECTION =="LEFT" && !collideWall(WallsList, x, y, NEXT_DIRECTION)){
            setCurrentDirection(NEXT_DIRECTION);
        }
        if (NEXT_DIRECTION =="RIGHT" && !collideWall(WallsList, x, y, NEXT_DIRECTION)){
            setCurrentDirection(NEXT_DIRECTION);
        }
        if (NEXT_DIRECTION == "UP" && !collideWall(WallsList, x, y, NEXT_DIRECTION)){
            setCurrentDirection(NEXT_DIRECTION);
        }
        if (NEXT_DIRECTION =="DOWN" && !collideWall(WallsList, x, y, NEXT_DIRECTION)){
            setCurrentDirection(NEXT_DIRECTION);
        }
        //MOVEMENT
        if (CURRENT_DIRECTION == "LEFT" && !collideWall(WallsList, x, y, CURRENT_DIRECTION)){
            x -= speed;
        }
        if (CURRENT_DIRECTION == "RIGHT" && !collideWall(WallsList, x, y, CURRENT_DIRECTION)){
            x += speed;
        }
        if (CURRENT_DIRECTION == "UP" && !collideWall(WallsList, x, y, CURRENT_DIRECTION)){
            y -= speed;
        }
        if (CURRENT_DIRECTION == "DOWN" && !collideWall(WallsList, x, y, CURRENT_DIRECTION)){
            y += speed;
        }
        activatePower(PelletList, redGhost, pinkGhost, cyanGhost, orangeGhost);
    }

    public boolean collideWall(ArrayList<Wall> WallsList, int pacmanX, int pacmanY, String CURRENT_DIRECTION){
        if (CURRENT_DIRECTION == "LEFT"){ 
            pacmanX-=speed;
        }
        if (CURRENT_DIRECTION == "UP"){ 
            pacmanY-=speed;
        }
        if (CURRENT_DIRECTION == "RIGHT"){ 
            pacmanX+=speed;
        }
        if (CURRENT_DIRECTION == "DOWN"){
            pacmanY+=speed;
        }
        Rectangle futurePlayerRect = new Rectangle(pacmanX, pacmanY, width, height); //creating a rectangle for the future position of the player to see if that would collide with a wall
        for(int i = 0; i < WallsList.size(); i++){
            Rectangle tileRect = WallsList.get(i).getRect();
            if(futurePlayerRect.intersects(tileRect)){
                return true; //if the future position of the player collides with a wall, return true
            } 
        } 
        return false;    
    }
    public void activatePower(ArrayList<Pellet> PelletList, RedGhost redGhost, PinkGhost pinkGhost, CyanGhost cyanGhost, OrangeGhost orangeGhost){
        Rectangle playerRect = getRect();
        for(int i = 0; i < PelletList.size(); i++){
            Rectangle PelletRect = PelletList.get(i).getRect();
            if(playerRect.intersects(PelletRect)){
                redGhost.setRedFrightenedMode(true);
                pinkGhost.setPinkFrightenedMode(true);
                cyanGhost.setCyanFrightenedMode(true);
                orangeGhost.setOrangeFrightenedMode(true);
            }
        }
    }

    public void reset(){
        levelDelayFrames = GamePanel.levelDelayFrames;
        x = GamePanel.pacmanStartingX;
        y = GamePanel.pacmanStartingY;
    }

    public int updatePlayerImage(ArrayList<Wall> WallsList){ //finds the neccessary image to display for the player
        defaultPacmanImage = 0;
        imgFrames++;

        if (levelDelayFrames < 50 && levelDelayFrames >0){ //if the level is still loading, return the default image
            return defaultPacmanImage;
        }
        if(collideWall(WallsList, x, y, CURRENT_DIRECTION)){ //if the player is colliding with a wall, return the default image
            return defaultPacmanImage;          
        } 
        if (CURRENT_DIRECTION == "RIGHT"){
            playerImage = 1;
        }
        if (CURRENT_DIRECTION == "UP"){
            playerImage = 3;
        }
        if (CURRENT_DIRECTION == "LEFT"){
            playerImage = 5;
        }
        if (CURRENT_DIRECTION == "DOWN"){
            playerImage = 7;
        }
        // open and close mouth animation every 6 frames
        if (imgFrames > 6){  
            playerImage++;
        }
        if (imgFrames > 12){
            playerImage = defaultPacmanImage;
        }
        if (imgFrames > 18){
            imgFrames = 0;
        }

        return playerImage;
    }
    
    public void draw(Graphics g, ArrayList<Wall> WallList, Ghost ghost){
        // display image of pacman in the correct direction
        
        playerImage = updatePlayerImage(WallList);
        g.drawImage(playerImages[playerImage], x+4, y, x+width, y+height, 0, 0, width, height, null);
        
        // draw arrows indicating where the player is going to move next
        g.setColor(Color.YELLOW);
        if (NEXT_DIRECTION == "LEFT"){
            g.fillPolygon(new int[] {x, x-4, x}, new int[] {y+6, y+11, y+16}, 3);
        }
        if (NEXT_DIRECTION == "RIGHT"){
            g.fillPolygon(new int[] {x+width+2, x+width+6, x+width+2}, new int[] {y+6, y+10, y+width-10}, 3);
        }
        if (NEXT_DIRECTION == "UP"){
            g.fillPolygon(new int[] {x+9, x+13, x+18}, new int[] {y-2, y-7, y-2}, 3);
        }
        if (NEXT_DIRECTION == "DOWN"){
            g.fillPolygon(new int[] {x+10, x+13, x+width-7}, new int[] {y+width+2, y+width+6, y+width+2}, 3);
        }
        
    }
    

    public Rectangle getRect(){
        return new Rectangle(x, y, width, height); //return a rectangle representing the player
    }


    //SETTERS AND GETTERS
    public int getLives(){
        return lives;
    }
    public void setLives(int lives){
        this.lives = lives;
    }
    public void setY(int y){ //set the y position of the player
        this.y = y;
    }
    public void setX(int x){ //set the x position of the player
        this.x = x;
    }

    public int getX() { //get the x position of the player
        return x;
    }

    public int getY() { //get the y position of the player
        return y;
    }

    public int getWidth() { //get the width of the player
        return width;
    }

    public int getHeight() { //get the height of the player
        return height;
    }
    public void setSpeed(int speed){ //set the speed of the player
        this.speed = speed;
    }
    public int getSpeed(){ //get the speed of the player
        return speed;
    }
    public void setCurrentDirection(String CURRENT_DIRECTION){ //set the current direction the player is moving in
        this.CURRENT_DIRECTION = CURRENT_DIRECTION;
    }
    public String getCurrentDirection(){ //get the current direction the player is moving in
        return CURRENT_DIRECTION;
    }
    public void setNextDirection(String NEXT_DIRECTION){ //set the next direction the player wants to move in
        this.NEXT_DIRECTION = NEXT_DIRECTION;
    }
    public String getNextDirection(){ //get the next direction the player wants to move in
        return NEXT_DIRECTION;
    }
    public void setLevelDelayFrames(int levelDelayFrames){ //set the level delay frames
        this.levelDelayFrames = levelDelayFrames;
    }
    public int getLevelDelayFrames(){ //get the level delay frames
        return levelDelayFrames;
    }



    public static void main(String[] arguments) {
        new Main();
    }
    
}