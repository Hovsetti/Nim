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
	private int _count;
	
	
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
	
	public void addUse() {
		_count++;
	}
	
	public ArrayList<Integer> getRowValues() {
		return _rowValues;
	}
	
	public int getPiecesLeft() {
		return _piecesLeft;
	}
	
	public int getUseCount() {
		return _count;
	}
	
	@Override
	public boolean equals(Object obj) {
		State otherState = (State)obj;
		boolean isEqual = _piecesLeft == otherState.getPiecesLeft() 
						&& _rowValues.equals(otherState.getRowValues());
		return isEqual;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Used: %8d \t Pieces remaining %8d \t", _count, _piecesLeft));
		
		_rowValues.stream().forEach((j) -> {
			sb.append(j + ",");
		});
		return sb.toString();
	}

	@Override
	public int compareTo(State state) {
		int diff = _count - state.getUseCount();
		return diff;
	}
}
