import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SmartAi extends DumbAi {
	/*
	 * This is static, because whenever we create a new SmartAi,
	 * we want to preserve the moves it has learned.
	 */
	private static HashMap<State, Double> _weightedTurns;
	
	/*
	 * Temporary turns for the current game round.
	 */
	private static HashMap<Integer, State> _tempTurns;
	
	
	
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
		if (_weightedTurns == null) {
			_weightedTurns = new HashMap<>();	
		}
		_tempTurns = new HashMap<>();
	}


	@Override
	/**
	 * Turn taking implementation for smart AI. Checks to see if we
	 * have learned a move, based on the current state of the game. If
	 * we have, we need to use that move, otherwise, we just make a
	 * random move.
	 * @param game - Current game
	 */
	public void takeTurn(Game game) {		
		Board board = game.getBoard();
		Move move = getSmartMove(game);
		
		if (move == null) {
			super.takeTurn(game);
		} else {
			int rowIndex = findRow(board, move);
			Row row = board.getRow(rowIndex);
			row.take(move.getTakeAmt());
		}
	}
	
	/**
	 * Housekeeping after a game has ended that ensures the moves
	 * we have made for the current game are learned.
	 */
	public void finalizeGame() {
		addTempStates();
		_tempTurns.clear();
		
		// DEBUG stuff
		printLearnedMoves();
	}
	
	/**
	 * Stores the current state of the game.
	 * @param game - Current game instance
	 */
	public static void storeCurrentGamestate(Game game) {
		int turnNum = game.getTurnNum();
		Board board = game.getBoard();
		State state = new State(board);
		
		_tempTurns.put(turnNum, state);
		
		for (int j : _tempTurns.keySet()) {
			State s = _tempTurns.get(j);
			System.out.format("%d --> %s\n", j, s);
		}
	}
	
	/**
	 * Adds all the temporary game states of the current game
	 * to the main states hashmap.
	 */
	private void addTempStates() {
		double tempSize = _tempTurns.size();
		
		for (int j = 1; j < tempSize; ++j) {
			double weight = getWeight(j, tempSize);
			State state = _tempTurns.get(j);
			updateState(state, weight);
		}
	}
	
	/**
	 * Updates the specified state with the new weight.
	 * @param state - State to update or addd
	 * @param weight - Weight factor to apply
	 */
	private void updateState(State state, double weight) {
		if (_weightedTurns.containsKey(state)) {
			double startWeight = _weightedTurns.get(state);
			weight = startWeight + weight;
			_weightedTurns.replace(state, weight);
		} else {
			_weightedTurns.put(state, weight);
		}
	}
	
	/**
	 * Gets the correct weight for the specified turn.
	 * @param turnNum - The turn number
	 * @param mapSize - Size of the temporary turns
	 * @return weight value as a double
	 */
	private double getWeight(double turnNum, double mapSize) {
		double weight = 0.0;
		
		if (mapSize % 2 == 0) {
			weight = (turnNum % 2 == 0) ? (turnNum / mapSize) : -(turnNum / mapSize);
		} else {
			weight = (turnNum % 2 == 0) ? -(turnNum / mapSize) : (turnNum / mapSize);
		}
		return weight;
	}

	/**
	 * Gets the best possible move based on what this SmartAI has learned.
	 * @param game - Current game instance
	 * @return Best possible move
	 */
	private Move getSmartMove(Game game) {
		Move move = null;
		double bestWeight = 0.0;
		
		Board board = game.getBoard();
		Row[] rows = board.getRows();
		
		for (Row r : rows) {
			int count = r.getCount();
			
			if (count >= 1) {
				
				for (int j = 1; j <= count; ++j) {
					r.take(j);
					
					State state = new State(board);
					
					if (_weightedTurns.containsKey(state)) {
						double weightVal = _weightedTurns.get(state);
						if (weightVal < bestWeight) {
							move = new Move(count, j);
							bestWeight = weightVal;
						}
					}
					// reset back
					r.setCount(count);
				}
			}
		}
		return move;
	}

	
	/**
	 * Finds the index of the row in the board that we need in order to make
	 * our move.
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
	
	private ArrayList<State> getSortedStates() {
		ArrayList<State> sortedStates = new ArrayList<>();
		for (State s : _weightedTurns.keySet()) {
			sortedStates.add(s);
		}
		Collections.sort(sortedStates);
		return sortedStates;
	}

	
	// For debugging - prints all the learned moves
	public void printLearnedMoves() {
		int i = 1;
		ArrayList<State> sortedStates = getSortedStates();
		for (State s : sortedStates) {
			double weight = _weightedTurns.get(s);
			System.out.format("(%03d) %s \tweight: %10.5f\n", i, s, weight);
			i++;
		}
	}
}
