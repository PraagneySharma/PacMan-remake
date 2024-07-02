/**
 * @(#)Player.java
 *
 *
 * @author 
 * @version 1.00 2024/4/5
 */
import java.awt.*;
import java.util.ArrayList;

public class CyanGhost extends Ghost{
    public static final int gate = 4;
    public int counter = 0;
    public int numberOfMovementsCounter = 0; //counter for the number of movements has to make to exit the gate
    public int totalNumberOfMovements; // total number of movements to exit the gate
    public int cyanFrightenedLength; //length of time the ghost is in frightened mode
    public boolean cyanFrightenedMode = false;
    public boolean addScore = false;
    

    private static Image []ghostImages;
    
    
    public CyanGhost(int ghostStartingX, int ghostStartingY, int speed, int releaseTime){ 
        super(ghostStartingX, ghostStartingY, speed, releaseTime);

        if(ghostImages == null){
            ghostImages = new Image[10];
            for(int i=0; i<10; i++){
                 ghostImages[i] = Util.loadImage(String.format("GhostAnimations/cyan/ghost%03d.png", i)); //load the images for the ghost
            }
        }
        cyanFrightenedLength = GamePanel.frightenedLength[GamePanel.level];

    }

    public void move(boolean []keys, ArrayList<Wall> WallsList, int[][] grid, Pacman pacman){
        totalNumberOfMovements =GamePanel.TILEHEIGHT*2 / getSpeed(); //it has to cover a distance of 2 tiles at the speed of the ghost
        setLevelDelayFrames(getLevelDelayFrames()-1);
        if (getLevelDelayFrames()>0){ //if the level hasnt started yet, dont move
            return;
        }
        setReleaseTime(getReleaseTime()-1);
        if (getReleaseTime()>0){ // if the ghosts are in their cage, move back and forth until their time is up to be released
            if (getCurrentDirection() == "LEFT" && !collideWall(WallsList, getX()-getSpeed(), getY(), getCurrentDirection())){
                setX(getX() - getSpeed());
            }
            else{
                setCurrentDirection("RIGHT");
            }
            if (getCurrentDirection() == "RIGHT" && !collideWall(WallsList, getX()+getSpeed(), getY(), getCurrentDirection())){
                setX(getX() + getSpeed());
            }
            else{
                setCurrentDirection("LEFT");
            }
            return;
        }
        if (cyanFrightenedMode){ // if pacman eats a power pellet, the ghosts go into frightened mode
            frightenedCollide(pacman); //starts the timer for the frightened mode
        }
        if (isCentered()){ // if the ghost is centered in the tile, and it needs to exit gate, move up
            if (collideWithGate(grid)){
                setExitingGate(true);
                setCurrentDirection("UP");   
            }
        }
        if (getExitingGate()){ // this is to make the ghost move up until it exits the gate
            if (numberOfMovementsCounter < totalNumberOfMovements){
                setY(getY()-getSpeed());
                numberOfMovementsCounter++;
            }
            else{
                setExitingGate(false);
                numberOfMovementsCounter = 0;
            }
        }
        
        if (!getExitingGate()){ // movement of the ghost
            if (getCurrentDirection() == "LEFT" && !collideWall(WallsList, getX(), getY(), getCurrentDirection())){
                setX(getX()-getSpeed());
            }
            if (getCurrentDirection() == "RIGHT" && !collideWall(WallsList, getX(), getY(), getCurrentDirection())){
                setX(getX()+getSpeed());
            }
            if (getCurrentDirection() == "UP" && !collideWall(WallsList, getX(), getY(), getCurrentDirection())){
                setY(getY()-getSpeed());
            }
            if (getCurrentDirection() == "DOWN" && !collideWall(WallsList, getX(), getY(), getCurrentDirection())){
                setY(getY()+getSpeed());
            }
            if (collideWall(WallsList, getX(), getY(), getCurrentDirection()) || isAtIntersection(WallsList)){
                if (cyanFrightenedMode){
                    setCurrentDirection(getAvailableDirections(WallsList).get(Util.randint(0, getAvailableDirections(WallsList).size()-1)));
                }
                else{
                    setCurrentDirection(getBestDirection(pacman, grid, WallsList));
                }
            }
        }
        if(getX()<=-getWidth()/2){ //if the player goes off the screen to the left, wrap around to the right
            setX(GamePanel.WIDTH - getWidth());
        }
        else if(getX()>=GamePanel.WIDTH-getWidth()/2){ //if the player goes off the screen to the right, wrap around to the left
            setX(0);
        }
    }
    public void reset(){ //reset the ghost to its original position
        setX(GamePanel.cyanGhostStartingX);
        setY(GamePanel.cyanGhostStartingY);
        setExitingGate(false);
        setCurrentDirection("LEFT");
        setReleaseTime(GamePanel.cyanghostRelease[GamePanel.level]);
        setLevelDelayFrames(GamePanel.levelDelayFrames);
    }
    public void frightenedCollide(Pacman pacman){ //if the ghost collides with pacman while in frightened mode, reset the ghost
        cyanFrightenedLength--;
        if (cyanFrightenedLength==0){
            setCyanFrightenedLength(GamePanel.frightenedLength[GamePanel.level]);
            cyanFrightenedMode = false;
        }
        if (getRect().intersects(pacman.getRect())){
            addScore = true;
            reset();
            cyanFrightenedMode = false;
        }
    }
    // getters and setters
    public boolean getCyanFrightenedMode(){
        return cyanFrightenedMode;
    }
    public void setCyanFrightenedMode(boolean cyanFrightenedMode){
        this.cyanFrightenedMode = cyanFrightenedMode;
    }
    public int getCyanFrightenedLength(){
        return cyanFrightenedLength;
    }
    public void setCyanFrightenedLength(int cyanFrightenedLength){
        this.cyanFrightenedLength = cyanFrightenedLength;
    }
    public boolean getAddScore(){
        return addScore;
    }
    public void setAddScore(boolean addScore){
        this.addScore = addScore;
    }

    public void draw(Graphics g, ArrayList<Wall> WallList, Pacman pacman){
        int ghostImage = updateGhostImage(WallList);
        counter++;
        if (counter == 16){
                counter = 0;
            }
        if (getCyanFrightenedMode()){ //if the ghost is in frightened mode, draw the frightened ghost
            // make it seems like the ghosts are bobbing around
            if(counter < 8){
                g.drawImage(getGhostImage1(), getX()+4, getY(), getX()+getWidth(), getY()+getHeight(), 0, 0, getWidth(), getHeight(), null);
            }
            else{
                g.drawImage(getGhostImage2(), getX()+4, getY(), getX()+getWidth(), getY()+getHeight(), 0, 0, getWidth(), getHeight(), null);           
            }
        }
        else{  // normal ghost drawing
            if(counter < 8){
                g.drawImage(ghostImages[ghostImage], getX()+4, getY(), getX()+getWidth(), getY()+getHeight(), 0, 0, getWidth(), getHeight(), null);
            }
            else{
                g.drawImage(ghostImages[ghostImage+1], getX()+4, getY(), getX()+getWidth(), getY()+getHeight(), 0, 0, getWidth(), getHeight(), null);           
            }
        }
    }
    public Rectangle getRect(){
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    public static void main(String[] arguments) {
        new Main();
    }
    
}