
public class Row {
	private int _count;
	
	public Row(int count) {
		_count = count;
	}
	
	/**
	 * Takes the specified amount of pieces from this row.
	 * @param takeAmt
	 */
	public void take(int takeAmt) {
		if (!canTake(takeAmt)) {
			throw new IllegalArgumentException(String.format("You must take between %d and %d! "
					+ "You tried to take [%d]", 1, _count, takeAmt));
		}
		_count -= takeAmt;
	}
	
	/**
	 * Draws all the pieces in this row to the screen.
	 */
	public void draw() {
		for (int j = 0; j < _count; ++j) {
			System.out.print("* ");
		}
		System.out.println();
	}
	
	/**
	 * Checks to see if this row is empty.
	 * @return true if this row is empty.
	 */
	public boolean isEmpty() {
		boolean isEmpty = (_count == 0);
		return isEmpty;
	}
	
	/**
	 * 
	 * @return count of pieces in this row
	 */
	public int getCount() {
		return _count;
	}
	
	public void setCount(int count) {
		_count = count;
	}
	
	/**
	 * Checks to see if we can remove the specified amount
	 * from this row.
	 * @param takeAmt - amount to take from this row
	 * @return true if we can take the specified amount
	 */
	public boolean canTake(int takeAmt) {
		boolean canTake = (takeAmt >= 1) && (takeAmt <= _count);
		return canTake;
	}
}
