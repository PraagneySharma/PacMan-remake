/**
 * Comments from this file are the same from Cyan ghost class
 */
import java.awt.*;
import java.util.ArrayList;

public class RedGhost extends Ghost{
    public static final int gate = 4;
    public int counter = 0;
    public int redFrightenedLength;
    public boolean redFrightenedMode = false;
    public boolean addScore = false;

    private static Image []ghostImages;
    
    
    public RedGhost(int ghostStartingX, int ghostStartingY, int speed, int releaseTime) { 
        super(ghostStartingX, ghostStartingY, speed, releaseTime);

        if(ghostImages == null){
            ghostImages = new Image[10];
            for(int i=0; i<10; i++){
                 ghostImages[i] = Util.loadImage(String.format("GhostAnimations/red/ghost%03d.png", i));
            }
        }
        redFrightenedLength = GamePanel.frightenedLength[GamePanel.level];

    }

    public void move(boolean []keys, ArrayList<Wall> WallsList, ArrayList<Coin> CoinsList, int[][] grid, Pacman pacman){
        setLevelDelayFrames(getLevelDelayFrames()-1);
        if (getLevelDelayFrames()>0){
            return;
        }
        if (redFrightenedMode){
            frightenedCollide(pacman);
        }
        if (!redFrightenedMode){
            speedUp(CoinsList);
        }
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
            if (redFrightenedMode){
                setCurrentDirection(getAvailableDirections(WallsList).get(Util.randint(0, getAvailableDirections(WallsList).size()-1)));
            }
            else{
                setCurrentDirection(getBestDirection(pacman, grid, WallsList));
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
        setX(GamePanel.redGhostStartingX);
        setY(GamePanel.redGhostStartingY);
        setExitingGate(false);
        setCurrentDirection("LEFT");
        setLevelDelayFrames(GamePanel.levelDelayFrames);
    }
    public void frightenedCollide(Pacman pacman){
        redFrightenedLength--;
        if (redFrightenedLength==0){
            setRedFrightenedLength(GamePanel.frightenedLength[GamePanel.level]);
            redFrightenedMode = false;
        }
        if (getRect().intersects(pacman.getRect())){
            addScore = true;
            reset();
            redFrightenedMode = false;
        }
    }
    public boolean getRedFrightenedMode(){
        return redFrightenedMode;
    }
    public void setRedFrightenedMode(boolean redFrightenedMode){
        this.redFrightenedMode = redFrightenedMode;
    }
    public int getRedFrightenedLength(){
        return redFrightenedLength;
    }
    public void setRedFrightenedLength(int redFrightenedLength){
        this.redFrightenedLength = redFrightenedLength;
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
        if (getRedFrightenedMode()){
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