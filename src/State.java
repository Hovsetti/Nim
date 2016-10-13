import java.util.ArrayList;
import java.util.Collections;

/**
 * Helper class for exporting the state of the board so that it can be used 
 * with the SmartAI.
 * @author David Kramer
 *
 */
public class State implements Comparable<State> {
	
	/*
	 * Board export stuff.
	 */
	private int _piecesLeft;
	private ArrayList<Integer> _rowValues;
	
	
	public State(Board board) {
		export(board);
	}
	
	/**
	 * Exports the state of the board and sorts each of the row values
	 * so that equality can be determined not by the order of each row,
	 * but rather that each row contains the same amount of pieces.
	 * @param board
	 */
	private void export(Board board) {
		Row[] rows = board.getRows();
		
		_rowValues = new ArrayList<>();
		_piecesLeft = board.getPieceCount();
		
		for (int j = 0; j < rows.length; ++j) {
			int count = rows[j].getCount();
			_rowValues.add(count);
		}
		Collections.sort(_rowValues);
	}
	
	public ArrayList<Integer> getRowValues() {
		return _rowValues;
	}
	
	public int getPiecesLeft() {
		return _piecesLeft;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + _piecesLeft;
		result = prime * result + ((_rowValues == null) ? 0 : _rowValues.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		State other = (State) obj;
		boolean isEqual = _piecesLeft == other.getPiecesLeft()
						 && _rowValues.equals(other.getRowValues());
		return isEqual;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Remaining:%5d\t--> [", _piecesLeft));
		
		_rowValues.stream().forEach((j) -> {
			sb.append(j + ",");
		});
		// remove last trailing comma and add close bracket
		int index = sb.lastIndexOf(",");
		sb.replace(index, index + 1, "");
		sb.append("]");
		
		return sb.toString();
	}

	@Override
	public int compareTo(State o) {
		int diff = getPiecesLeft() - o.getPiecesLeft();
		return diff;
	}
}
