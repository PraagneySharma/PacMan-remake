import java.awt.*;
import java.io.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;

// Main game logic
class GamePanel extends JPanel implements KeyListener, ActionListener, MouseListener{
    public static final int TILEWIDTH = 24, TILEHEIGHT = 24;
    public static final int COLUMNS = 19, ROWS = 25;
    public static final int WIDTH = TILEWIDTH * COLUMNS, HEIGHT = TILEHEIGHT * ROWS;
    Timer timer;
    boolean []keys;
    Font comicFnt=null;
     
    Pacman pacman;

    CyanGhost cyanGhost;
    RedGhost redGhost;
    PinkGhost pinkGhost;
    OrangeGhost orangeGhost;
    Ghost ghost;
    

    ArrayList<Wall> WallsList = new ArrayList<Wall>();
    ArrayList<Coin> CoinsList = new ArrayList<Coin>();
    ArrayList<Pellet> PelletList = new ArrayList<Pellet>();    

    public static int level = 0; // level counter
    private int score = 0; // score counter
    private int scoreMultiplyerTime = 0;
    private int scoreMultiplyer = 1;
    private boolean addScoreMultiplyerTime = false;
    public static int levelDelayFrames = 50; // frame delay between levels
    
    public static int[] playerSpeed = {2, 3, 4, 6, 8, 12}; // player speed
    public static int[] redghostSpeed = {1, 2, 3, 4, 6, 8, 12}; // ghost speed
    public static int[] ghostSpeed = {1, 2, 3, 4, 4, 8}; // ghost speed

    public static int[] redghostRelease = {0, 0, 0, 0, 0, 0}; // ghost release time
    public static int[] pinkghostRelease = {100, 50, 20, 10, 1}; // ghost release time
    public static int[] cyanghostRelease = {300, 150, 60, 30, 3}; // ghost release time
    public static int[] orangeghostRelease = {400, 200, 80, 40, 4}; // ghost release time

    public static int[] frightenedLength = {360, 300, 240, 180, 120}; // frightened mode length

    public static int pacmanStartingX = 216, pacmanStartingY = 360; // pacman starting position

    public static int redGhostStartingX = 216, redGhostStartingY = 264; // red ghost starting position
    public static int cyanGhostStartingX = 192, cyanGhostStartingY = 312; // cyan ghost starting position
    public static int pinkGhostStartingX = 216, pinkGhostStartingY = 312; // pink ghost starting position
    public static int orangeGhostStartingX = 240, orangeGhostStartingY = 312; // orange ghost starting position
    

    static String screen = "intro";

    Font gameFnt = null; // font used for LARGE words
    Font miniFnt = null; // fonts used for small words
    
    Image back;
    Image intro, backround;
    Image gameover;
    Image lifeImage;

    public static int[][] defaultGrid = { // default grid for the game
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 },
        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
        { 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1 },
        { 1, 3, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 3, 1 },
        { 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1 },
        { 1, 2, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 1 },
        { 1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 1 },
        { 1, 1, 1, 1, 2, 1, 1, 1, 0, 1, 0, 1, 1, 1, 2, 1, 1, 1, 1 },
        { 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0 },
        { 5, 1, 1, 1, 2, 1, 0, 1, 1, 4, 1, 1, 0, 1, 2, 1, 1, 1, 5 },
        { 0, 0, 0, 0, 2, 0, 0, 1, 0, 0, 0, 1, 0, 0, 2, 0, 0, 0, 0 },
        { 5, 1, 1, 1, 2, 1, 0, 1, 1, 1, 1, 1, 0, 1, 2, 1, 1, 1, 5 },
        { 0, 0, 0, 1, 2, 1, 0, 0, 0, 0, 0, 0, 0, 1, 2, 1, 0, 0, 0 },
        { 1, 1, 1, 1, 2, 1, 0, 1, 1, 1, 1, 1, 0, 1, 2, 1, 1, 1, 1 },
        { 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1 },
        { 1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1 },
        { 1, 3, 2, 1, 2, 2, 2, 2, 2, 0, 2, 2, 2, 2, 2, 1, 2, 3, 1 },
        { 1, 1, 2, 1, 2, 1, 2, 1, 1, 1, 1, 1, 2, 1, 2, 1, 2, 1, 1 },
        { 1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 1, 2, 2, 2, 2, 1 },
        { 1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1 },
        { 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1 },
        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 }
    };
      
    public GamePanel(){
        
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        gameFnt = Util.loadFont("pacfont/ARCADECLASSIC.ttf", 26); // loading fonts from file
        miniFnt = Util.loadFont("pacfont/ARCADECLASSIC.ttf", 10);
        lifeImage = Util.loadImage("PacmanAnimations/pacman/player001.png"); // image for how many lives pacman has
        intro = Util.loadImage("intro.png");
        gameover = Util.loadImage("gameover.png");
        

        keys = new boolean[1000];

        timer = new Timer(15, this);
        timer.start();

        setFocusable(true);
        requestFocus();
        addKeyListener(this);
        addMouseListener(this);
    }
    

    @Override
    public void actionPerformed(ActionEvent e){
        if(screen == "game"){
            pacman.move(keys, WallsList, PelletList, redGhost, pinkGhost, cyanGhost, orangeGhost);
            ghost.collideWithPacman(pacman, redGhost, cyanGhost, pinkGhost, orangeGhost);
            redGhost.move(keys, WallsList, CoinsList, defaultGrid, pacman);
            pinkGhost.move(keys, WallsList, defaultGrid, pacman);
            cyanGhost.move(keys, WallsList, defaultGrid, pacman);
            orangeGhost.move(keys, WallsList, defaultGrid, pacman);
            // setHighScore();
            for (int i = CoinsList.size() - 1; i >= 0; i--) { // loop through all coins and check if pacman has collected them
                Coin c = CoinsList.get(i);
                if (c.collectCoin((CoinsList), pacman)){ // if pacman has collected a coin, remove it from the list
                    score += 10; // add 10 points to the score
                }
            }
            if (score%10000 == 0){ // every 10000 points pacman gets an extra life
                if (pacman.getLives()<3){
                    pacman.setLives(pacman.getLives()+1);
                }
            }
            // if pacman has eaten ghost, start timer
            if (addScoreMultiplyerTime){
                scoreMultiplyerTime++;
            }
            // if a ghost has been eaten, start a timer for the score multiplyer
            if (orangeGhost.getAddScore() || redGhost.getAddScore()|| cyanGhost.getAddScore()|| pinkGhost.getAddScore()){
                addScoreMultiplyerTime = true;
            }
            // if the timer has reached the frightened mode length, reset the score multiplyer and timer
            if (scoreMultiplyerTime>frightenedLength[level]){
                scoreMultiplyerTime = 0;
                scoreMultiplyer = 1;
            }
            // every time you eat a ghost in the frightened mode in the limited time frame, the ghosts value double each time
            if (orangeGhost.getAddScore()){
                score+=200*scoreMultiplyer;
                if (scoreMultiplyerTime<frightenedLength[level]){
                    scoreMultiplyer++;
                }
                orangeGhost.setAddScore(false);
            }
            if (redGhost.getAddScore()){
                score+=200*scoreMultiplyer;
                if (scoreMultiplyerTime<frightenedLength[level]){
                    scoreMultiplyer++;
                }
                redGhost.setAddScore(false);
            }
            if (cyanGhost.getAddScore()){
                score+=200*scoreMultiplyer;
                if (scoreMultiplyerTime<frightenedLength[level]){
                    scoreMultiplyer++;
                }
                cyanGhost.setAddScore(false);
            }
            if (pinkGhost.getAddScore()){
                score+=200*scoreMultiplyer;
                if (scoreMultiplyerTime<frightenedLength[level]){
                    scoreMultiplyer++;
                }
                pinkGhost.setAddScore(false);
            }
            for(int i = PelletList.size() - 1; i >= 0; i--){
                Pellet p = PelletList.get(i);
                p.collectPellet(PelletList, pacman); // check if pacman has collected a pellet
            }
            if (CoinsList.size() == 0){ // if all coins have been collected, start a new level
                newLvl();
            }
            if (pacman.getLives() == 0){ // if pacman has no lives left, game over
                pacman.setLives(3); // reset pacman lives
                screen = "gameOver"; 
            }
            
        }
        repaint();
    }
    
    @Override
    public void keyPressed(KeyEvent e){
        keys[e.getKeyCode()] = true;
    }
    
    @Override
    public void keyReleased(KeyEvent e){
        keys[e.getKeyCode()] = false;
    }
    
    @Override
    public void keyTyped(KeyEvent e){}
    
    @Override
    public void mouseClicked(MouseEvent e){}
    
    @Override
    public void mouseEntered(MouseEvent e){}
    
    @Override
    public void mouseExited(MouseEvent e){}
    
    @Override
    public void mousePressed(MouseEvent e){
        if (screen == "intro"){
            startnewGame();
        }
        
        if (screen == "gameOver"){
            screen = "intro";
        }
    }
    
    private void startnewGame(){
        // starting a new game, sets everything to default
        ghost = new Ghost(0,0,0,0);
        pacman = new Pacman(KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT, KeyEvent.VK_UP, KeyEvent.VK_DOWN, playerSpeed[level], pacmanStartingX, pacmanStartingY, levelDelayFrames); // Pass the required arguments to the Pacman constructor
        cyanGhost = new CyanGhost(cyanGhostStartingX, cyanGhostStartingY, ghostSpeed[level], cyanghostRelease[level]);
        redGhost = new RedGhost(redGhostStartingX, redGhostStartingY, ghostSpeed[level], redghostRelease[level]);
        pinkGhost = new PinkGhost(pinkGhostStartingX, pinkGhostStartingY, ghostSpeed[level], pinkghostRelease[level]);
        orangeGhost = new OrangeGhost(orangeGhostStartingX, orangeGhostStartingY, ghostSpeed[level], orangeghostRelease[level]);

        CoinsList = new ArrayList<Coin>();
        PelletList = new ArrayList<Pellet>();

        Wall.addWallToGrid(defaultGrid, WallsList);
        Coin.addCoinToGrid(defaultGrid, CoinsList);
        Pellet.addPelletToGrid(defaultGrid, PelletList);
        screen = "game";
    }

    private void newLvl() {
        level++;
        if (level == 5){ // if the player has completed all levels, you win 
            screen = "gameOver";
            level = 0;
            return;
        }
        startnewGame();
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
    }

    
    @Override
    public void paint(Graphics g){
        
        if(screen == "game"){
            g.setFont(gameFnt);
            //fill screen with black
            g.setColor(Color.black);
            g.fillRect(0,0,WIDTH,HEIGHT);
            // Draw pacman, walls and coins
           
            for(Wall t: WallsList){
                t.draw(g, defaultGrid);
            }
            for(Coin c: CoinsList){
                c.draw(g, defaultGrid);
            }
            for(Pellet p: PelletList){
                p.draw(g, defaultGrid);
            }
            pacman.draw(g, WallsList, ghost);
            redGhost.draw(g, WallsList, pacman);
            pinkGhost.draw(g, WallsList, pacman);
            orangeGhost.draw(g, WallsList, pacman);
            cyanGhost.draw(g, WallsList, pacman);


            g.drawString("score " + score, 20, 40);
            
            for (int i = 0 ; i<pacman.getLives() ; i++){ // draw lives, based on how many lives pacman has
                g.drawImage(lifeImage, 20 + i*30, 60, null);
            }
        }
        
        if(screen == "intro"){
            //fill screen with yellow
            g.setColor(new Color(255, 223, 0));
            g.fillRect(0,0,WIDTH,HEIGHT);
            g.drawImage(intro, 0, 100, null);

        }
        
        if (screen == "gameOver"){
            //fill screen with red
            g.setColor(Color.black);
            g.fillRect(0,0,WIDTH,HEIGHT);
            g.drawImage(gameover, 100, 300, null);
            
        }    
    }
    
    public static void main(String[] arguments) {
        new Main();  
    }

}
