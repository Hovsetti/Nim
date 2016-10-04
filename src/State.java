import java.util.ArrayList;
import java.util.Collections;

/**
 * Helper class for exporting the state of the board so that it can be used 
 * with the SmartAI.
 * @author David Kramer
 *
 */
public class State {
	/*
	 * Board export stuff.
	 */
	private int _piecesLeft;
	private ArrayList<Integer> _rowValues;
	
	
	public State(NewBoard board) {
		export(board);
	}
	
	/**
	 * Exports the state of the board and sorts each of the row values
	 * so that equality can be determined not by the order of each row,
	 * but rather that each row contains the same amount of pieces.
	 * @param board
	 */
	private void export(NewBoard board) {
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
	public boolean equals(Object obj) {
		State otherState = (State)obj;
		boolean isEqual = _piecesLeft == otherState.getPiecesLeft() 
						&& _rowValues.equals(otherState.getRowValues());
		return isEqual;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Pieces remaining %d \t", _piecesLeft));
		
		_rowValues.stream().forEach((j) -> {
			sb.append(j + ",");
		});
		return sb.toString();
	}
}
