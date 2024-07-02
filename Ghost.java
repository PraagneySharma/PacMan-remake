/**
 * This class is the superclass for all the ghosts in the game. It contains the methods and attributes that are common to all the ghosts.
 */
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.swing.text.Position;

import javafx.scene.control.ContentDisplay;


public class Ghost {
    private int x, y, speed, width, height, levelDelayFrames, ghostImage, defaultGhostImage, releaseTime;
    private String CURRENT_DIRECTION;
    
    public static final int gate = 4;
    
    public int counter = 0;
    
    public boolean exitingGate = false; //if the player is exiting the gate

    
    RedGhost redGhost; //creating instances of the other ghosts
    CyanGhost cyanGhost; 
    PinkGhost pinkGhost;
    OrangeGhost orangeGhost;
    
    private static Image ghostImage1; 
    private static Image ghostImage2;
    
    public Ghost(int ghostStartingX, int ghostStartingY, int speed, int releaseTime){   
        x = ghostStartingX; //setting the x position of the player
        y = ghostStartingY;
        
        this.speed = speed;
        this.releaseTime = releaseTime; 
        levelDelayFrames = GamePanel.levelDelayFrames; //setting the level delay frames
        
        CURRENT_DIRECTION = "LEFT"; //setting the current direction of the player to left
        
        width = GamePanel.TILEWIDTH;  //24
        height = GamePanel.TILEHEIGHT; //24
        
        ghostImage1 = Util.loadImage("GhostAnimations/fearedGhost/ghost000.png"); //loading the images for the ghost
        ghostImage2 = Util.loadImage("GhostAnimations/fearedGhost/ghost001.png");
    }

    // this code is inspired by this website: https://www.redblobgames.com/pathfinding/a-star/introduction.html
    // this is where I learned how to implement it 
    // I had to make many adjustments to the code to make it work for my game
    // Unfortunately, it is really close to working, but there was a small error
    public String getBestDirection(Pacman pacman, int[][] grid, ArrayList<Wall> WallsList) {
        // int[][] directions = { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } };
        
        int pacman_x = pacman.getX() / GamePanel.TILEWIDTH;
        int pacman_y = pacman.getY() / GamePanel.TILEHEIGHT;
    
        int start_x = x / GamePanel.TILEWIDTH;
        int start_y = y / GamePanel.TILEHEIGHT;
    
        // Point start = new Point(start_x, start_y);
        // Point goal = new Point(pacman_x, pacman_y);

        //moving in a certain direction will get the ghost closer to the player
        if (start_x < pacman_x && !collideWall(WallsList, x, y, "RIGHT")) {
            return "RIGHT";
        }
        else if (start_x > pacman_x && !collideWall(WallsList, x, y, "LEFT")) {
            return "LEFT";
        }
        else if (start_y < pacman_y && !collideWall(WallsList, x, y, "DOWN")) {
            return "DOWN";
        }
        else if (start_y > pacman_y && !collideWall(WallsList, x, y, "UP")) {
            return "UP";
        }

    
        // Set<Point> visited = new HashSet<>();
        // LinkedList<Point> queue = new LinkedList<>();
        // queue.push(start);
        
        // HashMap<Point, Point> parent = new HashMap<>();
        // parent.put(start, null);
    
        // while (!queue.isEmpty()) {
        //     Point current = queue.poll();
    
        //     if (current.equals(goal)) {
        //         break;
        //     }
    
        //     visited.add(current);
        //     for (int[] dir : directions) {
        //         Point next = new Point(current.x + dir[0], current.y + dir[1]);
    
        //         if (visited.contains(next) || next.x < 0 || next.x >= grid.length || next.y < 3 || next.y >= grid[0].length || grid[next.x][next.y] == 1) {
        //             continue;
        //         }
    
        //         if (!parent.containsKey(next)) { // if the parent does not contain the next point, add it to the queue
        //             queue.add(next);
        //             parent.put(next, current);
        //         }
        //     }
        // }
    
        // if (!parent.containsKey(goal)) {
        //     return "LEFT";
        // }
    
        // Point nextStep = goal;
        // while (parent.get(nextStep) != start) {
        //     nextStep = parent.get(nextStep);
        // }
    
        // int dx = nextStep.x - start.x;
        // int dy = nextStep.y - start.y;
    
        // if (dx == 1) return "RIGHT";
        // if (dx == -1) return "LEFT";
        // if (dy == 1) return "DOWN";
        // if (dy == -1) return "UP";
        return getAvailableDirections(WallsList).get(Util.randint(0, getAvailableDirections(WallsList).size()-1));
    }

    public boolean isCentered(){
        if (x % width == 0 && y % height == 0){ // if the player is centered on a tile
            return true;
        }
        return false;
    }
    public boolean isAtIntersection(ArrayList<Wall> WallsList){ 
        String[] directions = {"UP", "DOWN", "LEFT", "RIGHT"};
        int availableDirections = 0;
        if (isCentered() == false){ // check if the player is centered on a tile
            return false;
        }
        for (String direction : directions){
            if (!collideWall(WallsList, x, y, direction)){ // check if the ghost has any available directions to move in
                availableDirections++;
            }
        }
        return (availableDirections > 2); //if there are more than 2 available directions, the player is at an intersection
    }
    public void speedUp(ArrayList<Coin> coinsList){ // speed up the player as the game progresses
        if (coinsList.size() < 20 && GamePanel.level == 0){
            speed = GamePanel.redghostSpeed[1];
        }
        else if (coinsList.size() < 40 && GamePanel.level == 1){
            speed = GamePanel.redghostSpeed[2];
        }
        else if (coinsList.size() < 60 && GamePanel.level == 2){
            speed = GamePanel.redghostSpeed[3];
        }
        else if(coinsList.size() < 80 && GamePanel.level == 3){
            speed = GamePanel.redghostSpeed[4];
        }
        else if(coinsList.size() < 100 && GamePanel.level == 4){
            speed = GamePanel.redghostSpeed[5];
        }
    }

    public ArrayList<String> getAvailableDirections(ArrayList<Wall> WallsList){ // get the available directions the player can move in
        String[] directions = {"UP", "DOWN", "LEFT", "RIGHT"};
        ArrayList<String> availableDirections = new ArrayList<String>();
        for (String direction : directions){
            if (!collideWall(WallsList, x, y, direction)){ // checks if the player can move in any direction 
                availableDirections.add(direction);
            }
        }
        return availableDirections;
    }
    
    public boolean collideWall(ArrayList<Wall> WallsList, int ghostX, int ghostY, String CURRENT_DIRECTION){ // this function takes in the x and y position of the player and the direction the player is moving in
       // adding/substracting speed based off of the direction the player is moving in
        if (CURRENT_DIRECTION == "LEFT"){                                                                    // it checks if the player would collide with a wall with that specific direction
            ghostX-=speed;
        }
        if (CURRENT_DIRECTION == "UP"){ 
            ghostY-=speed;
        }
        if (CURRENT_DIRECTION == "RIGHT"){ 
            ghostX+=speed;
        }
        if (CURRENT_DIRECTION == "DOWN"){
            ghostY+=speed;
        }

        Rectangle futureGhostRect = new Rectangle(ghostX, ghostY, width, height); //creating a rectangle for the future position of the player to see if that would collide with a wall
        for(int i = 0; i < WallsList.size(); i++){
            Rectangle tileRect = WallsList.get(i).getRect();
            if(futureGhostRect.intersects(tileRect)){
                return true; //if the future position of the ghost collides with a wall, return true
            } 
        } 
        return false;    
    }
    public boolean collideWithGate(int[][] grid){ //checks if the player collides with the gate
        if (grid[(getY()-GamePanel.TILEHEIGHT)/GamePanel.TILEHEIGHT][getX()/GamePanel.TILEWIDTH]==gate){
            return true;
        }
        return false;
    }
    
    public void collideWithPacman(Pacman pacman, RedGhost redGhost, CyanGhost cyanGhost, PinkGhost pinkGhost, OrangeGhost orangeGhost){ //checks if the player collides with pacman   
        //if the player collides with pacman and the ghost is not in frightened mode, pacman loses a life and the ghosts are reset
        if (cyanGhost.getRect().intersects(pacman.getRect()) && cyanGhost.getCyanFrightenedMode()==false || redGhost.getRect().intersects(pacman.getRect()) && redGhost.getRedFrightenedMode()==false || pinkGhost.getRect().intersects(pacman.getRect()) && pinkGhost.getPinkFrightenedMode()==false || orangeGhost.getRect().intersects(pacman.getRect())&& orangeGhost.getOrangeFrightenedMode()==false){
            pacman.setLives(pacman.getLives()-1);
            pacman.reset();
            redGhost.reset();
            cyanGhost.reset();
            pinkGhost.reset();
            orangeGhost.reset();
            redGhost.setRedFrightenedMode(false);
            cyanGhost.setCyanFrightenedMode(false);
            pinkGhost.setPinkFrightenedMode(false);
            orangeGhost.setOrangeFrightenedMode(false);
            
            
        }
          
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
    public void setLevelDelayFrames(int levelDelayFrames){ //set the level delay frames
        this.levelDelayFrames = levelDelayFrames;
    }
    public int getLevelDelayFrames(){ //get the level delay frames
        return levelDelayFrames;
    }
    public void setReleaseTime(int releaseTime){ //set the release time
        this.releaseTime = releaseTime;
    }
    public int getReleaseTime(){ //get the release time
        return releaseTime;
    }
    public void setExitingGate(boolean exitingGate){ //set if the player is exiting the gate
        this.exitingGate = exitingGate;
    }
    public boolean getExitingGate(){ //get if the player is exiting the gate
        return exitingGate;
    }
    public Image getGhostImage1(){
        return ghostImage1;
    }
    public Image getGhostImage2(){
        return ghostImage2;
    }

    public int updateGhostImage(ArrayList<Wall> WallsList){ //finds the neccessary image to display for the player
        defaultGhostImage = 0;
        if (levelDelayFrames>0){
            return defaultGhostImage;
        }
        if (CURRENT_DIRECTION == "LEFT"){
            ghostImage = 2; 
        }
        if (CURRENT_DIRECTION == "RIGHT"){ 
            ghostImage = 4;
        }
        if (CURRENT_DIRECTION == "UP"){
            ghostImage = 6;
        }
        if (CURRENT_DIRECTION == "DOWN"){
            ghostImage = 8;
        }
        return ghostImage;
    }
    
    public Rectangle getRect(){
        return new Rectangle(x, y, width, height);
    }
    public static void main(String[] arguments) {
        new Main();
    }
    
}