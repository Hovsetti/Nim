
public class Move {
	/*
	 * Amount of pieces at the start, in a row before we make our move.
	 */
	private int _startRowAmt;
	
	/*
	 * Amount of pieces we take.
	 */
	private int _takeAmt;
	
	
	public Move(int startRowAmt, int takeAmt) {
		_startRowAmt = startRowAmt;
		_takeAmt = takeAmt;
	}
	
	public int getStartRowAmt() {
		return _startRowAmt;
	}
	
	public int getTakeAmt() {
		return _takeAmt;
	}
	
	public String toString() {
		return String.format("Row Start: %d, Taken: %d", _startRowAmt, _takeAmt);
	}
	
}
