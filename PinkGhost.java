/**
 * Comments from this file are the same from Cyan ghost class
 */
import java.awt.*;
import java.util.ArrayList;

public class PinkGhost extends Ghost{
    public static final int gate = 4;
    public int counter = 0;
    public int numberOfMovementsCounter = 0;
    public int totalNumberOfMovements;
    public int pinkFrightenedLength;
    public boolean pinkFrightenedMode = false;
    public boolean addScore = false;
    

    private static Image []ghostImages;
    
    
    public PinkGhost(int ghostStartingX, int ghostStartingY, int speed, int releaseTime) { 
        super(ghostStartingX, ghostStartingY, speed, releaseTime);

        if(ghostImages == null){
            ghostImages = new Image[10];
            for(int i=0; i<10; i++){
                 ghostImages[i] = Util.loadImage(String.format("GhostAnimations/pink/ghost%03d.png", i));
            }
        }
        pinkFrightenedLength = GamePanel.frightenedLength[GamePanel.level];

    }

    public void move(boolean []keys, ArrayList<Wall> WallsList, int[][] grid, Pacman pacman){
        totalNumberOfMovements =GamePanel.TILEHEIGHT*2 / getSpeed();
        setLevelDelayFrames(getLevelDelayFrames()-1);
        if (getLevelDelayFrames()>0){
            return;
        }
        setReleaseTime(getReleaseTime()-1);
        if (getReleaseTime()>0){
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
        if (pinkFrightenedMode){
            frightenedCollide(pacman);
        }
        if (isCentered()){
            if (collideWithGate(grid)){
                setExitingGate(true);
                setCurrentDirection("UP");   
            }
        }
        if (getExitingGate()){
            if (numberOfMovementsCounter < totalNumberOfMovements){
                setY(getY()-getSpeed());
                numberOfMovementsCounter++;
            }
            else{
                setExitingGate(false);
                numberOfMovementsCounter = 0;
            }
        }
        else
        if (!getExitingGate()){
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
                if (pinkFrightenedMode){
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
    public void reset(){
        setX(GamePanel.pinkGhostStartingX);
        setY(GamePanel.pinkGhostStartingY);
        setExitingGate(false);
        setCurrentDirection("LEFT");
        setReleaseTime(GamePanel.pinkghostRelease[GamePanel.level]);
        setLevelDelayFrames(GamePanel.levelDelayFrames);
    }
    public void frightenedCollide(Pacman pacman){
        pinkFrightenedLength--;
        if (pinkFrightenedLength==0){
            setPinkFrightenedLength(GamePanel.frightenedLength[GamePanel.level]);
            pinkFrightenedMode = false;
        }
        if (getRect().intersects(pacman.getRect())){
            addScore = true;
            reset();
            pinkFrightenedMode = false;
        }
    }
    public boolean getPinkFrightenedMode(){
        return pinkFrightenedMode;
    }
    public void setPinkFrightenedMode(boolean pinkFrightenedMode){
        this.pinkFrightenedMode = pinkFrightenedMode;
    }
    public int getPinkFrightenedLength(){
        return pinkFrightenedLength;
    }
    public void setPinkFrightenedLength(int pinkFrightenedLength){
        this.pinkFrightenedLength = pinkFrightenedLength;
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
        if (getPinkFrightenedMode()){
            if(counter < 8){
                g.drawImage(getGhostImage1(), getX()+4, getY(), getX()+getWidth(), getY()+getHeight(), 0, 0, getWidth(), getHeight(), null);
            }
            else{
                g.drawImage(getGhostImage2(), getX()+4, getY(), getX()+getWidth(), getY()+getHeight(), 0, 0, getWidth(), getHeight(), null);           
            }
        }
        else{  
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