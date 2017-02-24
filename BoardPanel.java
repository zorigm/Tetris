
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * The BoardPanel class is responsible for displaying the game grid and
 * handling related to the game board.
 */
public class BoardPanel extends JPanel {

 public static final int COL_COUNT = 10;
  
 private static final int VISIBLE_ROW_COUNT = 20;
 
 private static final int HIDDEN_ROW_COUNT = 2;

 public static final int COLOR_MIN = 35;
 
 public static final int COLOR_MAX = 255 - COLOR_MIN;
 
 private static final int BORDER_WIDTH = 5;
 


 public static final int PANEL_WIDTH = COL_COUNT * TILE_SIZE + BORDER_WIDTH * 2;

 public static final int PANEL_HEIGHT = VISIBLE_ROW_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
 
 private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 16);

 private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);
 
 private TetrisGame tetris;
 
 private Piece[][] tiles;
 
 public static final int ROW_COUNT = VISIBLE_ROW_COUNT + HIDDEN_ROW_COUNT;
 
 public static final int TILE_SIZE = 24;
 
 public static final int SHADE_WIDTH = 4;
 
 private static final int CENTER_X = COL_COUNT * TILE_SIZE / 2;
 
 private static final int CENTER_Y = VISIBLE_ROW_COUNT * TILE_SIZE / 2;
  
  
 
 /**
  * Crates a new GameBoard instance.
  * @param tetris The Tetris instance to use.
  */
 public BoardPanel(TetrisGame tetris) {
  this.tetris = tetris;
  this.tiles = new Piece[ROW_COUNT][COL_COUNT];
  
  setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
  setBackground(Color.BLACK);
 }
 
 /**
  * Check to see if the tile is already taken.
  * @param x The x coordinate to check.
  * @param y The y coordinate to check.
  * @return Whether or not the tile is taken.
  */
 private boolean isOccupied(int x, int y) {
  return tiles[y][x] != null;
 }
 
 /**
  * Sets a tile located at the desired column and row.
  * @param x The column.
  * @param y The row.
  * @param type The value to set to the tile to.
  */
 private void setTile(int  x, int y, Piece type) {
  tiles[y][x] = type;
 }
  
 /**
  * Gets a tile by its column and row.
  * @param x The column.
  * @param y The row.
  * @return The tile.
  */
 private Piece getTile(int x, int y) {
  return tiles[y][x];
 }
 
 @Override
 public void paintComponent(Graphics g) {
  super.paintComponent(g);
  
  // simplify the positioning of things.
  g.translate(BORDER_WIDTH, BORDER_WIDTH);
  
  /*
   * Draw the board differently depending on the current game .
   */
  if(tetris.isPaused()) {
   g.setFont(LARGE_FONT);
   g.setColor(Color.WHITE);
   String msg = "PAUSED";
   g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, CENTER_Y);
  } else if(tetris.isNewGame() || tetris.isGameOver()) {
   g.setFont(LARGE_FONT);
   g.setColor(Color.WHITE);
   
  
   g.setFont(SMALL_FONT);
   
  } else {
   
   /*
    * Draw the tiles onto the board.
    */
   for(int x = 0; x < COL_COUNT; x++) {
    for(int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
     Piece tile = getTile(x, y);
     if(tile != null) {
      drawTile(tile, x * TILE_SIZE, (y - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
     }
    }
   }
   
   /*
    * Draw the current piece. 
    */
   Piece type = tetris.getPieceType();
   int pieceCol = tetris.getPieceCol();
   int pieceRow = tetris.getPieceRow();
   int rotation = tetris.getPieceRotation();
   
   //Draw the piece onto the board.
   for(int col = 0; col < type.getDimension(); col++) {
    for(int row = 0; row < type.getDimension(); row++) {
     if(pieceRow + row >= 2 && type.isTile(col, row, rotation)) {
      drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
     }
    }
   }
   
   /*
    * Draw the ghost (semi-transparent piece- where current piece will land). take the current position and move
    * down until hit a row that could collide.
    */
   Color base = type.getBaseColor();
   base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
   for(int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {
    //If no collision is detected, try the next row.
    if(isValidAndEmpty(type, pieceCol, lowest, rotation)) {     
     continue;
    }
    
    //Draw the ghost one row higher than the one the collision took place at.
    lowest--;
    
    //Draw the ghost piece.
    for(int col = 0; col < type.getDimension(); col++) {
     for(int row = 0; row < type.getDimension(); row++) {
      if(lowest + row >= 2 && type.isTile(col, row, rotation)) {
       drawTile(base, base.brighter(), base.darker(), (pieceCol + col) * TILE_SIZE, (lowest + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
      }
     }
    }
    
    break;
   }
   
   /*
    * Draw the background grid above the pieces .
    */
   g.setColor(Color.DARK_GRAY);
   for(int x = 0; x < COL_COUNT; x++) {
    for(int y = 0; y < VISIBLE_ROW_COUNT; y++) {
     g.drawLine(0, y * TILE_SIZE, COL_COUNT * TILE_SIZE, y * TILE_SIZE);
     g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW_COUNT * TILE_SIZE);
    }
   }
  }
  
  
 }
 
 /**
  * Draws a tile onto the board.
  * @param type The type of tile to draw.
  * @param x The column.
  * @param y The row.
  * @param g The graphics object.
  */
 private void drawTile(Piece type, int x, int y, Graphics g) {
  drawTile(type.getBaseColor(), type.getLightColor(), type.getDarkColor(), x, y, g);
 }
 
 /**
  * Draws a tile onto the board.
  * @param base The base color of tile.
  * @param light The light color of the tile.
  * @param dark The dark color of the tile.
  * @param x The column.
  * @param y The row.
  * @param g The graphics object.
  */
 private void drawTile(Color base, Color light, Color dark, int x, int y, Graphics g) {
  
  /*
   * Fill entire tile with base color.
   */
  g.setColor(base);
  g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
  
  /*
   * Fill bottom and right edges of the tile with dark shading color.
   */
  g.setColor(dark);
  g.fillRect(x, y + TILE_SIZE - SHADE_WIDTH, TILE_SIZE, SHADE_WIDTH);
  g.fillRect(x + TILE_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, TILE_SIZE);
  
  /*
   * Fill the top and left edges with the light shading. draw a single line
   * for each row or column rather than a rectangle.
   *  draw a  diagonal where the light and dark shading meet.
   */
  g.setColor(light);
  for(int i = 0; i < SHADE_WIDTH; i++) {
   g.drawLine(x, y + i, x + TILE_SIZE - i - 1, y + i);
   g.drawLine(x + i, y, x + i, y + TILE_SIZE - i - 1);
  }
 }
 
 /**
  * Reset board and clear away tiles.
  */
 public void clear() {
   
  for(int i = 0; i < ROW_COUNT; i++) {
   for(int j = 0; j < COL_COUNT; j++) {
    tiles[i][j] = null;
   }
  }
 }
 
 /**
  * determine whether a piece can be placed at coordinates.
  * @param type the type of piece to use.
  * @param x  coordinate of the piece.
  * @param y  coordinate of the piece.
  * @param rotation  rotation of the piece.
  * @return Whether or not the position is valid.
  */
 public boolean isValidAndEmpty(int x, int y, Piece type, int rotation) {
    
  if(x < -type.getLeftInset(rotation) || x + type.getDimension() - type.getRightInset(rotation) >= COL_COUNT) {
   return false;
  }
  
  if(y < -type.getTopInset(rotation) || y + type.getDimension() - type.getBottomInset(rotation) >= ROW_COUNT) {
   return false;
  }
  
  /*
   * Loop through every tile in the piece and see if it connects with a previous tile. 
   */
  for(int col = 0; col < type.getDimension(); col++) {
   for(int row = 0; row < type.getDimension(); row++) {
    if(type.isTile(col, row, rotation) && isOccupied(x + col, y + row)) {
     return false;
    }
   }
  }
  return true;
 }
 
 /**
  * Adds a piece to the game board. 
  * @param type The type of piece to place.
  * @param x the x coordinate of the piece.
  * @param y the y coordinate of the piece.
  * @param rotation The rotation of the piece.
  */
 public void addPiece(Piece type, int x, int y, int rotation) {
  /*
   * Loop through every tile that is in the piece and add it
   * to the board only if the boolean that represents that
   * tile is set to true.
   */
  for(int col = 0; col < type.getDimension(); col++) {
   for(int row = 0; row < type.getDimension(); row++) {
    if(type.isTile(col, row, rotation)) {
     setTile(col + x, row + y, type);
    }
   }
  }
 }
 
 /**
  * Checks the board to see if any lines have been cleared, and
  * removes them from the game.
  * @return The number of lines that were cleared.
  */
 public int checkLines() {
  int completedLines = 0;
  
  for(int row = 0; row < ROW_COUNT; row++) {
   if(checkLine(row)) {
    completedLines++;
   }
  }
  return completedLines;
 }
   
 /**
  * Checks whether or not row is full.
  * @param line The row to check.
  * @return Whether or not this row is full.
  */
 private boolean checkLine(int line) {
  /*
   * Iterate through every column in this row. If any of them are
   * empty, then the row is not full.
   */
  for(int col = 0; col < COL_COUNT; col++) {
   if(!isOccupied(col, line)) {
    return false;
   }
  }
  
  for(int row = line - 1; row >= 0; row--) {
   for(int col = 0; col < COL_COUNT; col++) {
    setTile(col, row + 1, getTile(col, row));
   }
  }
  return true;
 }

}
