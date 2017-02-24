//This class computes the gamecycle-more of the function
public class GameCycle {
 
 private int elapsedCycles;
 private float excessCycles;
 private boolean isPaused; 
 private float millisPerCycle;
 private long lastUpdate;
 
 /**
  * Create a new cycle and set it cycles-per-second.
  * @param cyclesPerSecond The number of cycles that elapse per second.
  */
 public GameCycle(float cyclesPerSecond) {
  setCyclesPerSecond(cyclesPerSecond);
  reset();
 }
 
  /**
  * Checks to see if the clock is currently paused.
  * @return Whether or not this cycle is paused.
  */
 public boolean isPaused() {
  return isPaused;
 }
 
 /**
  * Checks to see if a cycle has elapsed. If so,
  * the number of elapsed cycles will be decremented by one.
  * @return Whether or not a cycle has elapsed.
  
  */
 public boolean hasElapsedCycle() {
  if(elapsedCycles > 0) {
   this.elapsedCycles--;
   return true;
  }
  return false;
 }
 
 /**
  * Checks to see if a cycle has elapsed ., the number of cycles will not be decremented
  * if the number of elapsed cycles is greater than 0.
  * @return Whether or not a cycle has elapsed.
  */
 public boolean peekElapsedCycle() {
  return (elapsedCycles > 0);
 }
 
 /**
  * Calculates current time in milliseconds  
  * @return The current time in milliseconds.
  */
 private static final long getCurrentTime() {
  return (System.nanoTime() / 1000000L);
 }

}
 
 /**
  * Sets the number of cycles that elapse per second.
  * @param cyclesPerSecond The number of cycles per second.
  */
 public void setCyclesPerSecond(float cyclesPerSecond) {
  this.millisPerCycle = (1.0f / cyclesPerSecond) * 1000;
 }
 
 /**
  * Resets the stats. Elapsed cycles and cycle excess will be reset
  * to 0, the last update time will be reset to the current time..
  */
 public void reset() {
  this.elapsedCycles = 0;
  this.excessCycles = 0.0f;
  this.lastUpdate = getCurrentTime();
  this.isPaused = false;
 }
 
 /**
  * Updates the stats. The number of elapsed cycles, as well as the
  * cycle excess will be calculated only if it is not paused. This
  * method should be called every frame even when paused to prevent any problems.
  */
 public void update() {
  long currUpdate = getCurrentTime();
  float delta = (float)(currUpdate - lastUpdate) + excessCycles;
  
  if(!isPaused) {
   this.elapsedCycles += (int)Math.floor(delta / millisPerCycle);
   this.excessCycles = delta % millisPerCycle;
  }
  
  //Set the last update time for the next update cycle.
  this.lastUpdate = currUpdate;
 }
 
 /**
  * Pauses or unpause the cycle. 
  * @param paused Whether or not to pause this function.
  */
 public void setPaused(boolean paused) {
  this.isPaused = paused;
 }
 

}


