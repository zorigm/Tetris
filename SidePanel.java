import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

/**
 *  Responsible for displaying various information
 * on the game such as the next piece, the score, and controls.
 */
public class SidePanel extends JPanel {
 
 

 private static final int SMALL_INSET = 20;
 private static final int LARGE_INSET = 40;
 private static final int STATS_INSET = 175;
 private static final int CONTROLS_INSET = 300;
 private static final int TEXT_STRIDE = 25;
 private static final int TILE_SIZE = BoardPanel.TILE_SIZE /2;
 private static final int SHADE_WIDTH = BoardPanel.SHADE_WIDTH /2;
 private static final int TILE_COUNT = 5;
 private static final int SQUARE_CENTER_X = 130;
 private static final int SQUARE_CENTER_Y = 65;
 private static final int SQUARE_SIZE = (TILE_SIZE * TILE_COUNT /2);

 private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 11);
 private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 13);
 private static final Color DRAW_COLOR = new Color(128, 192, 128);
 
 private TetrisGame tetris;
 private String currentTime="40";
 
 private Graphics g;
 
  /**
  * @param tetris The Tetris instance.
  */
 public SidePanel(TetrisGame tetris) {
  this.tetris = tetris;
 
  setPreferredSize(new Dimension(200, BoardPanel.PANEL_HEIGHT));
  setBackground(Color.BLACK);
 }
 
 public void printTime(int currentTime)
 {
  this.currentTime=String.valueOf(currentTime);
  g.drawString(String.valueOf(currentTime), LARGE_INSET, 10);
  
 }
 
 /**
  * Draws a tile onto the preview window.
  * @param type The type of tile to draw.
  * @param x The x coordinate of the tile.
  * @param y The y coordinate of the tile.
  * @param g The graphics object.
  */
 private void drawTile(Piece type, int x, int y, Graphics g) {
  /*
   * Fill the entire tile with the base color.
   */
  g.setColor(type.getBaseColor());
  g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
  
  /*
   * Fill  bottom and right edges of the tile with the dark shade color.
   */
  g.setColor(type.getDarkColor());
  g.fillRect(x, y + TILE_SIZE - SHADE_WIDTH, TILE_SIZE, SHADE_WIDTH);
  g.fillRect(x + TILE_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, TILE_SIZE);
  
  /*
   * Fill top and left edges with the light shading. draw a single line
   * for each row or column. draw a 
   * diagonal where the light and dark shading connect.
   */
  g.setColor(type.getLightColor());
  for(int i = 0; i < SHADE_WIDTH; i++) {
   g.drawLine(x, y + i, x + TILE_SIZE - i - 1, y + i);
   g.drawLine(x + i, y, x + i, y + TILE_SIZE - i - 1);
  }
 }
 
 @Override
 public void paintComponent(Graphics g) {
  this.g=g;
  super.paintComponent(g);
  
  g.setColor(DRAW_COLOR);
  
  int offset;
  
  /*
   * Draw  "Scoreboard" area.
   */
  g.setFont(LARGE_FONT);

  g.drawString("Player", SMALL_INSET, offset = STATS_INSET);
  //g.drawString("Enter: start game",SMALL_INSET, offset = TEXT_STRIDE);
  g.setFont(SMALL_FONT);
  
  g.drawString("Score: " + tetris.getScore(), LARGE_INSET, offset += TEXT_STRIDE);
  
  /*
   * Draw  "Controls" area.
   */
  
  g.setFont(LARGE_FONT);
  g.drawString(currentTime, LARGE_INSET, offset += TEXT_STRIDE);
  g.drawString("Score 2 pts in 40 secs.", SMALL_INSET,offset+=TEXT_STRIDE);
  g.drawString("Press enter if failed!", SMALL_INSET,offset+=TEXT_STRIDE);

  g.drawString("Controls:", SMALL_INSET, offset = CONTROLS_INSET);
  g.setFont(SMALL_FONT);
  g.drawString("Enter: start game",LARGE_INSET, offset += TEXT_STRIDE);
  g.drawString("Left Key: Move Left", LARGE_INSET, offset += TEXT_STRIDE);
  g.drawString("Right key: Move Right", LARGE_INSET, offset += TEXT_STRIDE);
  g.drawString("Z: Turn counterClockwise", LARGE_INSET, offset += TEXT_STRIDE);
  g.drawString("X: Turn Clockwise", LARGE_INSET, offset += TEXT_STRIDE);
  g.drawString("Down Key: Drop", LARGE_INSET, offset += TEXT_STRIDE);
  g.drawString("P: Pause Game", LARGE_INSET, offset += TEXT_STRIDE);
 
  /*
   * Draw the next piece -preview.
   */
  g.setFont(LARGE_FONT);
  g.drawString("Next Piece", SMALL_INSET, 70);
  g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 2);
  
  /*
   * Draw a preview of the next piece that will be spawned.
   * similiar to the drawing code on the board, smaller and centered,
   */
  Piece type = tetris.getNextPieceType();
  if(!tetris.isGameOver() && type != null) {
   /*
    * Get the size properties of the current piece.
    */
   int cols = type.getCols();
   int rows = type.getRows();
   int dimension = type.getDimension();
  
   /*
    * Calculate the top left corner (origin) of the piece.
    */
   int startX = (SQUARE_CENTER_X - (cols * TILE_SIZE / 2));
   int startY = (SQUARE_CENTER_Y - (rows * TILE_SIZE / 2));
  
   /*
    * Get the insets for the preview. The default
    * rotation is used for the preview, so we just use 0.
    */
   int top = type.getTopInset(0);
   int left = type.getLeftInset(0);
  
   /*
    * Loop through the piece and draw its tiles onto the preview.
    */
   for(int row = 0; row < dimension; row++) {
    for(int col = 0; col < dimension; col++) {
     if(type.isTile(col, row, 0)) {
      drawTile(type, startX + ((col - left) * TILE_SIZE), startY + ((row - top) * TILE_SIZE), g);
     }
    }
   }
  }
 }

}
