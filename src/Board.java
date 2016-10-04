import java.util.ArrayList;
import java.util.Collections;

public class Board {
	private Row[] _rows;
	
	
	public Board() {
		_rows = createRows(3, 3, 2);	// creates default layout of { 3, 5, 7 }
	}
	
	/**
	 * Creates an array of rows.
	 * @param count - how many rows we want
	 * @param startValue - value we want to start counting at
	 * @param incrementAmt - the offset increment amount
	 * @return array containing rows
	 */
	public Row[] createRows(int count, int startValue, int incrementAmt) {
		Row[] rows = new Row[count];
		rows[0] = new Row(startValue);
		
		for (int j = 1; j < count; ++j) {
			int k = (j - 1);
			int value = rows[k].getCount() + incrementAmt;
			rows[j] = new Row(value);
		}
		return rows;
	}
	
	/**
	 * Gets the row at the specified row index
	 * @param rowIndex - index of row we want
	 * @return Row at specified index
	 */
	public Row getRow(int rowIndex) {
		Row row = null;
		
		if (rowIndex < 0 || rowIndex > _rows.length) {
			throw new IndexOutOfBoundsException(String.format("Invalid row index [%d]", rowIndex));
		}
		row = _rows[rowIndex];
		return row;
	}
	
	/**
	 * Draws all the rows to the screen.
	 */
	public void draw() {
		System.out.println();
		for (int j = 0; j < _rows.length; ++j) {
			System.out.format("[%d]\t", j);
			
			Row row = _rows[j];
			row.draw();
		}
	}
	
	/**
	 * 
	 * @return int array containing indexes for rows which are not empty. 
	 */
	public int[] getFilledRows() {
		ArrayList<Integer> filled = new ArrayList<>();
		
		for (int j = 0; j < _rows.length; ++j) {
			Row row = _rows[j];
			if (!row.isEmpty()) {
				filled.add(j);
			}
		}
		// indexes of non-empty rows
		int[] indexes = filled.stream().mapToInt(i -> i).toArray();
		return indexes;
	}
	
	/**
	 * 
	 * @return Arraylist containing the row count values of every row.
	 */
	private ArrayList<Integer> getCountList() {
		ArrayList<Integer> countList = new ArrayList<>();
		
		for (int j = 0; j < _rows.length; ++j) {
			Row row = _rows[j];
			int count = row.getCount();
			countList.add(count);
		}
		return countList;
	}
	
	/**
	 * 
	 * @return the total count of pieces from all of our rows
	 */
	public int getPieceCount() {
		int count = 0;
		
		for (Row row : _rows) {
			count += row.getCount();
		}
		return count;
	}
	
	/**
	 * 
	 * @return count of the total amount of rows
	 */
	public int getRowCount() {
		int count = _rows.length;
		return count;
	}
	
	/**
	 * 
	 * @return array of all our rows
	 */
	public Row[] getRows() {
		return _rows;
	}
	
	@Override
	public boolean equals(Object obj) {
		Board other = (Board)obj;

		// compare the row values of each, in sorted order to check to see if they're the same
		ArrayList<Integer> myRows = getCountList();
		ArrayList<Integer> otherRows = other.getCountList();
		Collections.sort(myRows);
		Collections.sort(otherRows);
	
		boolean isEqual = myRows.equals(otherRows);
		
		return isEqual;
	}
}
