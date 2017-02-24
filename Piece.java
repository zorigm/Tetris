
import java.awt.Color;

/**
 * The enum describes the properties of the various pieces that can be used in the game.
 * inset-the tile in placed in the set.
 */
public enum Piece {


  TypeO(new Color(BoardPanel.COLOR_MAX, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN), 2, 2, 2, new boolean[][] {
  {
   true, true,
   true, true,
  },
  {
   true, true,
   true, true,
  },
  { 
   true, true,
   true, true,
  },
  {
   true, true,
   true, true,
  }
 }),
 

 TypeS(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN), 3, 3, 2, new boolean[][] {
  {
   false, true, true,
   true, true, false,
   false, false, false,
  },
  {
   false, true, false,
   false, true, true,
   false, false, true,
  },
  {
   false, false, false,
   false, true, true,
   true, true, false,
  },
  {
   true, false, false,
   true, true, false,
   false, true, false,
  }
 }),
 

   
 TypeT(new Color(128, BoardPanel.COLOR_MIN, 128), 3, 3, 2, new boolean[][] {
  {
   false, true, false,
   true, true, true,
   false, false, false,
  },
  {
   false, true, false,
   false, true, true,
   false, true, false,
  },
  {
   false, false, false,
   true, true, true,
   false, true, false,
  },
  {
   false, true, false,
   true, true, false,
   false, true, false,
  }
 }),
 
   
 TypeZ(new Color(BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN, BoardPanel.COLOR_MIN), 3, 3, 2, new boolean[][] {
  {
   true, true, false,
   false, true, true,
   false, false, false,
  },
  {
   false, false, true,
   false, true, true,
   false, true, false,
  },
  {
   false, false, false,
   true, true, false,
   false, true, true,
  },
  {
   false, true, false,
   true, true, false,
   true, false, false,
  }
 });
  
 TypeI(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MAX), 4, 4, 1, new boolean[][] {
  {
   false, false, false, false,
   true, true, true, true,
   false, false, false, false,
   false, false, false, false,
  },
  {
   false, false, true, false,
   false, false, true, false,
   false, false, true, false,
   false, false, true, false,
  },
  {
   false, false, false, false,
   false, false, false, false,
   true, true, true, true,
   false, false, false, false,
  },
  {
   false, true, false, false,
   false, true, false, false,
   false, true, false, false,
   false, true, false, false,
  }
 }),
 

   
 TypeJ(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX), 3, 3, 2, new boolean[][] {
  {
   true, false, false,
   true, true, true,
   false, false, false,
  },
  {
   false, true, true,
   false, true, false,
   false, true, false,
  },
  {
   false, false, false,
   true, true, true,
   false, false, true,
  },
  {
   false, true, false,
   false, true, false,
   true, true, false,
  }
 }),
 

   
 TypeL(new Color(BoardPanel.COLOR_MAX, 127, BoardPanel.COLOR_MIN), 3, 3, 2, new boolean[][] {
  {
   false, false, true,
   true, true, true,
   false, false, false,
  },
  {
   false, true, false,
   false, true, false,
   false, true, true,
  },
  {
   false, false, false,
   true, true, true,
   true, false, false,
  },
  {
   true, true, false,
   false, true, false,
   false, true, false,
  }
 });
  

 private int spawnRow;
 private int dimension;
 private int rows;
 private int cols;
 private boolean[][] tiles;
 private Color baseColor;
 private Color lightColor;
 private Color darkColor;
 private int spawnCol;
 
 /**
  * Creates a new TileType.
  * @param color The base color of the tile.
  * @param dimension The dimensions of the tiles array.
  * @param cols The number of columns.
  * @param rows The number of rows.
  * @param tiles The tiles.
  */
 private Piece(Color color, int dimension, int cols, int rows, boolean[][] tiles) {
  this.baseColor = color;
  this.lightColor = color.brighter();
  this.darkColor = color.darker();
  this.dimension = dimension;
  this.tiles = tiles;
  this.cols = cols;
  this.rows = rows;
  
  this.spawnCol = 5 - (dimension >> 1);
  this.spawnRow = getTopInset(0);
 }
 

 public int getSpawnColumn() {
  return spawnCol;
 }
 

 public int getSpawnRow() {
  return spawnRow;
 }
 

 public int getRows() {
  return rows;
 }
 

 public int getCols() {
  return cols;
 }
 
 public Color getBaseColor() {
  return baseColor;
 }
 

 public Color getLightColor() {
  return lightColor;
 }
 

 public Color getDarkColor() {
  return darkColor;
 }
 

 public int getDimension() {
  return dimension;
 }
 
 
  public int getTopInset(int rotation) {

  for(int y = 0; y < dimension; y++) {
   for(int x = 0; x < dimension; x++) {
    if(isTile(x, y, rotation)) {
     return y;
    }
   }
  }
  return -1;
 }
 
 /**
  * The bottom inset is represented by the number of empty rows on the bottom
  * side of the array for the given rotation.
  * @param rotation The rotation.
  * @return The bottom inset.
  */
 public int getBottomInset(int rotation) {

  for(int y = dimension - 1; y >= 0; y--) {
   for(int x = 0; x < dimension; x++) {
    if(isTile(x, y, rotation)) {
     return dimension - y;
    }
   }
  }
  return -1;
 }
 
 /**
  * The left inset is represented by the number of empty rows on the top
  * side of the array for the given rotation.
  * @param rotation The rotation.
  * @return The top inset.
  */
 public int getLeftInset(int rotation) {

  for(int x = 0; x < dimension; x++) {
   for(int y = 0; y < dimension; y++) {
    if(isTile(x, y, rotation)) {
     return x;
    }
   }
  }
  return -1;
 }
 
 /**
  * The right inset is represented by the number of empty columns on the left
  * side of the array for the given rotation.
  * @param rotation The rotation.
  * @return The right inset.
  */
 public int getRightInset(int rotation) {
   
  for(int x = dimension - 1; x >= 0; x--) {
   for(int y = 0; y < dimension; y++) {
    if(isTile(x, y, rotation)) {
     return dimension - x;
    }
   }
  }
  return -1;
 }
 
 /**
  * Checks to see if the given coordinates and rotation contain a tile.
  * @param x The x coordinate of the tile.
  * @param y The y coordinate of the tile.
  * @param rotation The rotation to check in.
  * @return Whether or not a tile resides there.
  */
 public boolean isTile(int x, int y, int rotation) {
  return tiles[rotation][y * dimension + x];
 }
 
}

