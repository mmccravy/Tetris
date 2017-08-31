/**
 * Handles everything involved with timing
 */
public class Time{

	private float milliNumPerRound;	
	private long lastUpdate;	
	private int pastNumCycles;		
	private float remainingTime;		
	private boolean isPaused;
	
	/**
	 * Calculates the current time in milliseconds using high resolution clock
	 * @return -- current time in milliseconds
	 */
	private static final long getCurrentTime(){
		return (System.nanoTime() / 1000000L);
	}
	/**
	 * Sets the number of cycles per second.
	 * @param cyclesPerSecond -- cycles per second
	 */
	public void setCyclesPerSecond(float cyclesPerSecond) {
		this.milliNumPerRound = (1.0f / cyclesPerSecond) * 1000;
	}
	/**
	 * Creates a new time and sets cycles
	 * @param cyclesPerSecond -- number of cycles per second.
	 */
	public Time(float cyclesPerSecond) {
		setCyclesPerSecond(cyclesPerSecond);
		reset();
	}
	/**
	 * Pauses the timer. Some methods don't operate during pause.
	 * @param paused -- should this be paused
	 */
	public void setPaused(boolean toPause){
		this.isPaused = toPause;
	}
	/**
	 * Checks if the clock is paused
	 * @return -- if is paused
	 */
	public boolean isPaused(){
		return isPaused;
	}
	/**
	 * Checks to see if a cycle has elapsed and decrements accordingly
	 * @return -- if a cycle has finished
	 */
	public boolean hasCycleFinished(){
		if(pastNumCycles > 0)
		{
			this.pastNumCycles--;
			return true;
		}
		return false;
	}
	/**
	 * Checks to see if a cycle has elapsed, but doesn't decrement
	 * @return -- if a cycle has finished
	 */
	public boolean peekElapsedCycle() {
		return (pastNumCycles > 0);
	}
	/**
	 * Resets the timing stats
	 */
	public void reset(){
		this.isPaused = false;
		this.pastNumCycles = 0;
		this.remainingTime = 0.0f;
		this.lastUpdate = getCurrentTime();	
	}
	/**
	 * Updates the timing stats.  Certain stats only get processed while the game is running
	 */
	public void update(){
		long current = getCurrentTime();
		float delta = (float)(current - lastUpdate) + remainingTime;

		if(!isPaused)
		{
			this.pastNumCycles += (int)Math.floor(delta / milliNumPerRound);
			this.remainingTime = delta % milliNumPerRound;
		}

		this.lastUpdate = current;
	}
}