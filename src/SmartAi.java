import java.util.HashMap;

public class SmartAi extends DumbAi {
	/*
	 * This is static, because whenever we create a new SmartAi,
	 * we want to preserve the moves it has learned.
	 */
	private static HashMap<State, Move> weightedTurns;
	
	public SmartAi() {
		_name = "Smart AI";
		init();
	}

	public SmartAi(String name) {
		super(name);
		init();
	}
	
	protected void init() {
		super.init();
		if (weightedTurns == null) {
			weightedTurns = new HashMap<>();	
		}
	}


	@Override
	/**
	 * Turn taking implementation for smart AI. Checks to see if we
	 * have learned a move, based on the current state of the game. If
	 * we have, we need to use that move, otherwise, we just make a
	 * random move. At the end, we check to see if the move we just
	 * made put us in a good state.
	 * @param game - Current game
	 */
	public void takeTurn(Game game) {		
		Board curBoard = game.getBoard();
		State curState = new State(curBoard);
		Move move = null;
		
		if (checkExisting(curState) != null) {
			// ensure we have a proper key
			curState = checkExisting(curState);
			move = weightedTurns.get(curState);
			
			int rowIndex = findRow(curBoard, move);
			Row row = curBoard.getRow(rowIndex);
			
			row.take(move.getTakeAmt());
		} else {
			move = getMove(game);
		}
		checkMove(game, move, curState);
	}
	
	/**
	 * Checks to see if the move put us in a good state. If it did
	 * and it doesn't exist in the learned moves yet, it is added.
	 * @param game - Current game
	 * @param move - Move to check
	 * @param state - State to check
	 */
	private void checkMove(Game game, Move move, State state) {
		int nimSum = Integer.parseInt(game.nimSum());
		
		if (nimSum == 0) {
			if (checkExisting(state) == null) {
				weightedTurns.put(state, move);
			}
		}
	}
	
	/**
	 * Finds the index of the row that we need in order to make our move.
	 * @param board Board to get rows from
	 * @param move Move we need to match the starting take amount
	 * @return index of row
	 */
	private int findRow(Board board, Move move) {
		int rowIndex = -1;
		
		Row[] rows = board.getRows();
		
		for (int j = 0; j < rows.length; ++j) {
			Row row = rows[j];
			
			if (row.getCount() == move.getStartRowAmt()) {
				rowIndex = j;
				break;
			}
		}
		return rowIndex;
	}
	
	/**
	 * Checks to see if there exists a state in the weighted turns
	 * that is equal to the specified state. 
	 * @param state - State to check
	 * @return State as a key from the hashmap. 
	 */
	private State checkExisting(State state) {
		State returnVal = null;
		
		for(State s : weightedTurns.keySet()) {
			 if (s.equals(state)) {
				 returnVal = s;
			 }
		}
		return returnVal;
	}
	
	// For debugging - prints all the learned moves
	private void printLearnedMoves() {
		for (State s : weightedTurns.keySet()) {
			Move value = weightedTurns.get(s);
			System.out.format("%s --> %s\n", s, value);
		}
	}
}
