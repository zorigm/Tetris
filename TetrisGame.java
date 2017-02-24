import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.Timer;
import javax.swing.JFrame;

/**
 * The  Tetris class is responsible for handling much of the game logic and
 * reading user input.
 * @author Zorig Magnaituvshin
 *
 */
public class TetrisGame extends JFrame {
 
 

 private Random random;
 private GameCycle logicTimer;
 private Piece currentType;
 private Piece nextType;
 private int currentCol;
 private int currentRow;
 private int currentRotation;
 private int dropDown;
 private float gameSpeed;
 private static final long FRAME_TIME = 1000L / 50L; 
 private static final int TYPE_COUNT = Piece.values().length;
 private BoardPanel board;
 private SidePanel side;
 private boolean isPaused;
 private boolean isNewGame;
 private boolean isGameOver;
 private int score;
 //40 seconds for the game
 private final int totalTime=40;
 private Timer t;
  
 /**
  * Creates a new Tetris instance. Sets up the window,
  * and adds a controller listener.
  */
 private TetrisGame() {
  super("Tetris");
  setLayout(new BorderLayout());
  setDefaultCloseOperation(EXIT_ON_CLOSE);
  setResizable(false);
  
  this.board = new BoardPanel(this);
  this.side = new SidePanel(this);
  
  add(board, BorderLayout.CENTER);
  add(side, BorderLayout.EAST);
  
  addKeyListener(new KeyAdapter() {
   
   @Override
   public void keyPressed(KeyEvent e) {
        
    switch(e.getKeyCode()) {
    
    /*
     * Drop - when pressed, check to see that the game is not
     * paused and that there is no drop down, then set the
     * logic timer to run at a speed of 25 cycles per second.
     */
    case KeyEvent.VK_DOWN:
     if(!isPaused && dropDown == 0) {
      logicTimer.setCyclesPerSecond(25.0f);
     }
     break;
     
    /*
     * Move Left - when pressed, check to see that the game is
     * not paused and that the position to the left of the current
     * position is valid. If so, decrement the current column by 1.
     */
    case KeyEvent.VK_LEFT:
     if(!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)) {
      currentCol--;
     }
     break;
     
    /*
     * Move Right - when pressed, check to see that the game is
     * not paused and that the position to the right of the current
     * position is valid. If so, increment the current column by 1.
     */
    case KeyEvent.VK_RIGHT:
     if(!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)) {
      currentCol++;
     }
     break;
     
    /*
     * Rotate Anti-clockwise - When pressed, check to see that the game is not paused
     * and then attempt to rotate the piece anti-clockwise.
     */
    case KeyEvent.VK_Z:
     if(!isPaused) {
      rotatePiece((currentRotation == 0) ? 3 : currentRotation - 1);
     }
     break;
    
    /*
        * Rotate Clockwise - When pressed, check to see that the game is not paused
     * and then attempt to rotate the piece clockwise.  similar to anticlockwise
     * rotation.
     */
    case KeyEvent.VK_X:
     if(!isPaused) {
      rotatePiece((currentRotation == 3) ? 0 : currentRotation + 1);
     }
     break;
     
    /*
     * Pause Game - When pressed, check to see currently playing a game.
     * If so, toggle pause variable and update the logic timer to reflect this
     * change, otherwise the game will execute a huge number of updates and essentially
     * cause an instant game over when  un-pause, if it stay paused for more than a
     * minute or so.
     */
    case KeyEvent.VK_P:
     if(!isGameOver && !isNewGame) {
      isPaused = !isPaused;
      logicTimer.setPaused(isPaused);
     }
     break;
    
    /*
     * Start Game - When pressed, check to see that its in either  game over or new
     * game phase. If so, reset the game.
     */
    case KeyEvent.VK_ENTER:
     
     if(isGameOver || isNewGame) {
      
      
      
       resetGame();

       }
     break;
    }
   }
   
   @Override
   public void keyReleased(KeyEvent e) {
    
    switch(e.getKeyCode()) {
    
    /*
     * Drop - When released, set the speed of the logic timer
     * back to the current game speed is and clear out
     * any cycles still elapsed.
     */
    case KeyEvent.VK_DOWN:
     logicTimer.setCyclesPerSecond(gameSpeed);
     logicTimer.reset();
     break;
    }
    
   }
   
  });
  
  /*
   *   resize the frame to hold the BoardPanel and SidePanel instances,
   * center the window on the screen.
   */
  pack();
  
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  setVisible(true);
 }
 
 /**
  * Starts the game running. Initializes and enters the game loop.
  */
 int currentTime=totalTime;
 private void startGame() {

  this.random = new Random();
  this.isNewGame = true;
  this.gameSpeed = 1.0f;
  
  this.logicTimer = new GameCycle(gameSpeed);
  logicTimer.setPaused(true);
  

ActionListener listener=new 
ActionListener()
{
 public void actionPerformed(ActionEvent event)
 {
  currentTime--;
  side.printTime(currentTime);
  if(currentTime==0)
  {
   
  
   if(getScore()<=2)
   {
   
  isGameOver=true;
   }  
   t.stop();
  
  }
 }
};
  final int DELAY=1000;
   t=new Timer(DELAY,listener);
  
 while(true) {
   //Get the time that the frame started.
   long start = System.nanoTime();
   
   //Update logic timer.
   logicTimer.update();
   
   /*
    * If a cycle has elapsed on the timer, update the game and
    * move  current piece down.
    */
   if(logicTimer.hasElapsedCycle()) {
    updateGame();
   }
  
   //Decrement the drop down.
   if(dropDown > 0) {
    dropDown--;
   }
   
   //Display the window .
   renderGame();
   
   /*
    * Sleep to max the frame rate.
    */
   long delta = (System.nanoTime() - start) / 1000000L;
   if(delta < FRAME_TIME) {
    try {
     Thread.sleep(FRAME_TIME - delta);
    } catch(Exception e) {
     e.printStackTrace();
    }
   }
  }
  
 }
 
 /**
  *@return t it is the object for the timer
  */
 public Timer getTimer()
 {
  return t;
 }
 
 /**
  * Updates the game.
  */
 private void updateGame() {
  /*
   * Check to see if the piece's position can move down to the next row.
   */
  if(board.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)) {
   //Increment the current row .
   currentRow++;
  } else {
   /*
    *  either reached the bottom of the board, or landed on another piece,
    *  add the piece to the board.
    */
   board.addPiece(currentType, currentCol, currentRow, currentRotation);
   
   /*
    * Check to see if adding the new piece resulted in any cleared lines. If so,
    * increase the player's score. Up to 4 lines to clear in a single row;
    * 1 = 1pt.
    */
   int cleared = board.checkLines();
   if(cleared > 0.0) {
    score += 1;
    if(score==2)
    {
     t.stop();
     
     resetGame();
     
    }
   }
   
   /*
    * Increase the speed slightly for the next piece and update the game's timer
    * to reflect the increase.
    */
   gameSpeed += 0.035f;
   logicTimer.setCyclesPerSecond(gameSpeed);
   logicTimer.reset();
   
   /*
    * Set the drop down so the next piece doesn't come quickly
    * in immediately after a piece hits if  not reacted
    * yet. 
    */
   dropDown = 25;
   
   
   
   
   /*
    * Spawn a new piece to control.
    */
   spawnPiece();
  }  
 }
 
 /**
  * have the BoardPanel and SidePanel to repaint.
  */
 private void renderGame() {
  board.repaint();
  side.repaint();
 }
 
 /**
  * resets the game variables to their initial values at the start
  * of a new game.
  */
 private void resetGame() {
  
  this.score = 0;
  this.gameSpeed = 1.0f;
  this.nextType = Piece.values()[random.nextInt(TYPE_COUNT)];
  this.isNewGame = false;
  this.isGameOver = false;  
  board.clear();
  logicTimer.reset();
  logicTimer.setCyclesPerSecond(gameSpeed);
  spawnPiece();
  t.restart();
  currentTime=totalTime;
 }
  
 /**
  * Spawns a new piece and resets our piece's variables to their default
  * values.
  */
 private void spawnPiece() {
  /*
   * cutoff the last piece and reset position and rotation to
   * their default variables, then pick the next piece to use.
   */
  this.currentType = nextType;
  this.currentCol = currentType.getSpawnColumn();
  this.currentRow = currentType.getSpawnRow();
  this.currentRotation = 0;
  this.nextType = Piece.values()[random.nextInt(TYPE_COUNT)];
  
  /*
   * If the spawn point is invalid,  pause the game and check it is lost-
   *  it means that the pieces on the board went too far.
   */
  if(!board.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)) {
   this.isGameOver = true;
   logicTimer.setPaused(true);
  }  
 }

 /**
  * attempts to set the rotation of the current piece to newRotation.
  * @param newRotation The rotation of the new piece.
  */
 private void rotatePiece(int newRotation) {
  /*
   * there are pieces that will need to be moved when rotated to avoid clutter
   * out of the board 
   */
  int newColumn = currentCol;
  int newRow = currentRow;
  
  /*
   * Get the insets for each of the sides. This is used to determine how
   * many empty rows or columns there are on a given side.
   */
  int left = currentType.getLeftInset(newRotation);
  int right = currentType.getRightInset(newRotation);
  int top = currentType.getTopInset(newRotation);
  int bottom = currentType.getBottomInset(newRotation);
  
  /*
   * if the current piece is too far to the left or right, move the piece away from the edges
   * so that the piece doesn't clip out of the map and automatically become invalid.
   */
  if(currentCol < -left) {
   newColumn -= currentCol - left;
  } else if(currentCol + currentType.getDimension() - right >= BoardPanel.COL_COUNT) {
   newColumn -= (currentCol + currentType.getDimension() - right) - BoardPanel.COL_COUNT + 1;
  }
  
  /*
   * if the current piece is too far to the top or bottom, move the piece away from the edges
   * so that the piece doesn't go out  and set to become invalid.
   */
  if(currentRow < -top) {
   newRow -= currentRow - top;
  } else if(currentRow + currentType.getDimension() - bottom >= BoardPanel.ROW_COUNT) {
   newRow -= (currentRow + currentType.getDimension() - bottom) - BoardPanel.ROW_COUNT + 1;
  }
  
  /*
   * check to see if the new position is acceptable; If it is, update the rotation and
   * position of the piece.
   */
  if(board.isValidAndEmpty(currentType, newColumn, newRow, newRotation)) {
   currentRotation = newRotation;
   currentRow = newRow;
   currentCol = newColumn;
  }
 }
 
 /**
  * Checks to see whether or not the game is paused.
  * @return Whether or not the game is paused.
  */
 public boolean isPaused() {
  return isPaused;
 }
 
 /**
  * Checks to see whether or not the game is over.
  * @return Whether or not the game is over.
  */
 public boolean isGameOver() {
  
  return isGameOver;
 }
 
 /**
  * Checks to see whether or not we're on a new game.
  * @return Whether or not this is a new game.
  */
 public boolean isNewGame() {
  return isNewGame;
 }
 
 /**
  * Gets the current score.
  * @return The score.
  */
 public int getScore() {
  return score;
 }
 
 /**
  * Gets the current level.
  * @return The level.
  */

 
 /**
  * Gets the current type of piece we're using.
  * @return The piece type.
  */
 public Piece getPieceType() {
  return currentType;
 }
 
 /**
  * Gets the next type of piece we're using.
  * @return The next piece.
  */
 public Piece getNextPieceType() {
  return nextType;
 }
 
 /**
  * Gets the column of the current piece.
  * @return The column.
  */
 public int getPieceCol() {
  return currentCol;
 }
 
 /**
  * Gets the row of the current piece.
  * @return The row.
  */
 public int getPieceRow() {
  return currentRow;
 }
 
 /**
  * Gets the rotation of the current piece.
  * @return The rotation.
  */
 public int getPieceRotation() {
  return currentRotation;
 }

 /**
  * Entry-point of the game, responsible for creating and starting a new
  * game instance.
  * @param args .
  */
 public static void main(String[] args) {
  TetrisGame tetris = new TetrisGame();
  tetris.startGame();
  
    }

}
