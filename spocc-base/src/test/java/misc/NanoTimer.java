package misc;


public final class NanoTimer {
	
	private long start, end;
	
	public void start() {
		start = System.nanoTime();
	}
	
	public void stop() {
		end = System.nanoTime();
	}
	
	public long elapsedNanos() {
		return end - start;
	}
}
