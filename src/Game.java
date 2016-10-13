import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {	
		
	/*
	 * Amount of games to run for learning AI
	 */
	private static final int ITERATIONS = 100;
	
	private Player[] _players;
	private Board _board;
	private Random _random;
	private Scanner _console;
	private int _playerIndex;
	
	private int _turnNum;
	
	public Game() {
		init();
	}
	
	/**
	 * Initializes a new game
	 */
	private void init() {
		_board = new Board();
		_random = new Random();
		_console = new Scanner(System.in);
		_playerIndex = 0;
		_turnNum = 1;
		
//		promptGameMode();
	}
	
	/**
	 * Resets the game with a brand new board.
	 */
	private void reset() {
		System.out.println(" RESET()");
		_board = new Board();
		_turnNum = 1;
	}
	
	
	/**
	 * Takes promptGameMode() out of the constructor
	 */
	public void runGame(){
		promptGameMode();
		for(Player p : _players){
			if(p.getName().equals("SmartAI") || p.getName().equals("SmartAI 1") || p.getName().equals("SmartAI 2")){
				((SmartAi) p).printLearnedMoves();
			}
		}
	}
	
	/**
	 * Prompts the user for the type of game that they wish to play.
	 */
	private void promptGameMode() {
		String input = null;
		boolean isValid = false;
		
		int gameMode = 0;
		
		do {
			System.out.print("ENTER 1 FOR PLAYER VS. PLAYER, 2 FOR PLAYER VS. DUMB AI\n"
					+ "3 FOR DUMB AI VS SMART AI, 4 FOR SMART AI VS. SMART AI\n0 TO END THE GAME: ");
			
			input = _console.nextLine();
			
			try {
				gameMode = Integer.parseInt(input);
			
				// check if is valid
				isValid = (gameMode >= 0 && gameMode <= 4);
				
				if (!isValid) {
					System.out.println("Invalid choice! Please try again!");
				} else {
					run(gameMode);	
				}
				
			} catch (NumberFormatException e) {
				/**
				 * Example of minimal exception handling, showing us we fell into catch statement
				 */
				System.out.println("Invalid input! Please try again!");
//				e.printStackTrace();
			}
			
		} while (!isValid);
	}
	
	/**
	 * Prompts the user for the row that they wish to take pieces from.
	 * @return Row
	 */
	public Row promptForRow() {
		String input = null;
		Row row = null;
		boolean isValid = false;
		
		int rowChoice = 0;
		String rowChoices = getRowChoices();
		
		/**
		 * Possible example of grouping logical to return value
		 */
		do {
			System.out.format("Enter a row: %s: ", rowChoices);
			input = _console.nextLine();
			
			try {
				rowChoice = Integer.parseInt(input);
				row = _board.getRow(rowChoice);
				isValid = (row != null);
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				System.out.println("Invalid row choice! Please try again!");
			} 			
		} while (!isValid);
//		return (_board.getRow(rowChoice) != null) ? _board.getRow(rowChoice) : null;
		return row;
	}
	
	/**
	 * Prompts the user for the amount to take from the specified row.
	 * @param row Row to take pieces from
	 * @return the amount that we wish to take from the row
	 */
	public int promptTake(Row row) {
		String input = null;
		boolean isValid = false;
		int takeAmt = 0;
		
		do {
			System.out.format("Enter an amount to take between 1 and %d: ", row.getCount());
			input = _console.nextLine();
			try {
				takeAmt = Integer.parseInt(input);
				if (!row.canTake(takeAmt)) {
					System.out.format("Unable to take [%d]. Please try again!", takeAmt);
				} else {
					isValid = true;
				}
			} catch (NumberFormatException e) {
				System.out.println("Invalid take amount! Please try again!");
			}
		} while (!isValid);
		return takeAmt;
	}
	
	/**
	 * Gets string representation of the NimSum from all rows in the board.
	 * @return binary string representation of the NimSum
	 */
	@Deprecated // no longer using this for getting moves
	public String nimSum() {
		ArrayList<String> binNums = getBinNums();
		
		int strLength = 8;
		StringBuilder nimSim = new StringBuilder(strLength);
		
		for (int j = 0; j < strLength; ++j) {
			int onesCount = 0;
			
			for (String s : binNums) {
				if (s.charAt(j) == '1') {
					++onesCount;
				}
			}
			
			// append '1' if odd else append '0'
			if (onesCount % 2 != 0) {
				nimSim.append('1');
			} else {
				nimSim.append('0');
			}
		}
		return nimSim.toString();
	}
	
	/**
	 * 
	 * @return ArrayList of padded binary string representations of each data set
	 */
	@Deprecated // no longer using this in conjunction with nimSum
	private ArrayList<String> getBinNums() {
		ArrayList<String> binNums = new ArrayList<>();
		
		for (int j = 0; j < _board.getRowCount(); ++j) {
			int value = _board.getRow(j).getCount();
			
			// left pad with leading 0's
			String binNum = String.format("%8s", Integer.toBinaryString(value)).replace(' ', '0');
			binNums.add(binNum);
		}
		return binNums;
	}
	
	/**
	 * 
	 * @return string containing valid rows to choose from.
	 */
	private String getRowChoices() {
		int count = _board.getRowCount();
		String rows = ""; 
		
		for (int j = 0; j < count; ++j) {
			if (!_board.getRow(j).isEmpty()) {
				rows += j + ",";
			}
		}
		// remove trailing comma
		rows = rows.substring(0, rows.length() - 1);
		
		return rows;
	}
	
	/**
	 * Creates an array of players based on the type of game mode.
	 * @param gameMode - Type of game mode to play
	 * @return array containing players
	 */
	private Player[] createPlayers(int gameMode) {
		Player[] players = null;
		
		switch (gameMode) {
		case 1:
			players = new Player[] { new HumanPlayer("Human 1"), new HumanPlayer("Human 2") };
			break;
		case 2:
			players = new Player[] { new HumanPlayer("Human"), new DumbAi("DumbAI") };
			break;
		case 3:
			players = new Player[] { new DumbAi("DumbAI"), new SmartAi("SmartAI") };
			break;
		case 4:
			players = new Player[] { new SmartAi("SmartAI 1"), new SmartAi("SmartAI 2") };
			break;
			default:
				throw new IllegalArgumentException(String.format("Invalid game mode: [%d]", gameMode));
		}
		
		return players;
	}
	
	/**
	 * Checks to see if the current game is over.
	 * @return true if game is over.
	 */
	private boolean isOver() {
		boolean isOver = _board.getPieceCount() < 1;
		return isOver;
	}
	
	/**
	 * Main game loop. Runs in the specified game mode.
	 * @param gameMode - game mode to run in
	 */
	private void run(int gameMode) {
		if (gameMode == 0) {
			quit();
		}
		
		Player curPlayer = null;		
		_players = createPlayers(gameMode);
		
		for (int j = 0; j < ITERATIONS; ++j) {
			curPlayer = getRandPlayer();
			
			while (!isOver()) {
				curPlayer = nextPlayer();
				System.out.format("\n%s's turn: ", curPlayer);
				
				_board.draw();
				curPlayer.takeTurn(this);
				SmartAi.storeCurrentGamestate(this);
				++_turnNum;
			}
			curPlayer = nextPlayer();
			curPlayer.AddWin();
			System.out.format("%s won!\n", curPlayer);
			finalizeGame();
			reset();
		}
		printStats();
	}
	
	/**
	 * Final cleanup after a game has ended.
	 */
	private void finalizeGame() {
		for (Player p : _players) {
			p.finalizeGame();
		}
	}
	
	/**
	 * Prints out the stats for all players.
	 */
	private void printStats() {
		for (Player p : _players) {
			double winRate = ((double)p.getWinCount() / ITERATIONS) * 100;
			System.out.format("\n%s won %.2f%%\n", p, winRate);
		}
	}
	
	/**
	 * 
	 * @return next player in the array.
	 */
	private Player nextPlayer() {
		if (++_playerIndex >= _players.length) {
			_playerIndex = 0;
		}
		Player player = _players[_playerIndex];
		return player;
	}
	
	/**
	 * 
	 * @return random player from the array
	 */
	private Player getRandPlayer() {
		int index = _random.nextInt(_players.length);
		Player player = _players[index];
		return player;
	}
	
	/**
	 * Exits the application.
	 */
	private void quit() {
		System.out.println("Goodbye!");
		System.exit(0);
	}
	
	
	public Board getBoard() {
		return _board;
	}

	public int getTurnNum() {
		return _turnNum;
	}
}
