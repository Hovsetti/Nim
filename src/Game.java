import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Game {	
	
	/*
	 * Amount of games to run for learning AI
	 */
	private static final int ITERATIONS = 100;
	
	private NewPlayer[] _players;
	private NewBoard _board;
	private Random _random;
	private Scanner _console;
	
	public Game() {
		init();
	}
	
	/**
	 * Initializes a new game
	 */
	private void init() {
		_board = new NewBoard();
		_random = new Random();
		_console = new Scanner(System.in);
		
		promptGameMode();
	}
	
	/**
	 * Resets the game with a brand new board.
	 */
	private void reset() {
		System.out.println("RESET()");
		_board = new NewBoard();
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
				System.out.println("Invalid input! Please try again!");
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
		
		do {
			System.out.format("Enter a row: %s: ", rowChoices);
			input = _console.nextLine();
			
			try {
				rowChoice = Integer.parseInt(input);
				row = _board.getRow(rowChoice);
				isValid = (row != null);
			} catch (NumberFormatException | IndexOutOfBoundsException e) {
				System.out.println("Invalid row choice! Please try again!");
			} 
			
		} while (!isValid);
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
	private NewPlayer[] createPlayers(int gameMode) {
		NewPlayer[] players = null;
		
		switch (gameMode) {
		case 1:
			players = new NewPlayer[] { new HumanPlayer("Human 1"), new HumanPlayer("Human 2") };
			break;
		case 2:
			players = new NewPlayer[] { new HumanPlayer("Human"), new NewDumbAi() };
			break;
		case 3:
			players = new NewPlayer[] { new NewDumbAi(), new NewSmartAi() };
			break;
		case 4:
			players = new NewPlayer[] { new NewSmartAi(), new NewSmartAi() };
			break;
			default:
				throw new IllegalArgumentException(String.format("Invalid game mode: [%d]", gameMode));
		}
		
		return players;
	}
	
	private boolean isOver() {
		boolean isOver = _board.getPieceCount() < 1;
		return isOver;
	}
	
	private void run(int gameMode) {
		if (gameMode == 0) {
			quit();
		}
		
		NewPlayer curPlayer = null;		
		_players = createPlayers(gameMode);
		
		for (int j = 0; j < ITERATIONS; ++j) {
			int playerIndex = _random.nextInt(_players.length);
			
			while (!isOver()) {
				curPlayer = _players[playerIndex];
				System.out.format("\n%s's turn: ", curPlayer);
				
				_board.draw();
				
				curPlayer.takeTurn(this);
				
				// get next player index
				playerIndex = nextPlayerIndex(playerIndex);
			}
			playerIndex = nextPlayerIndex(playerIndex);
			curPlayer = _players[playerIndex];
			curPlayer.AddWin();
			System.out.println("GAME OVER");
			reset();
		}
		printStats();
	}
	
	private void printStats() {
		for (NewPlayer p : _players) {
			double winRate = (double)p.getWinCount() / ITERATIONS;
			System.out.format("%s won %f\n", p, winRate);
		}
	}
	
	private int nextPlayerIndex(int playerIndex) {
		int index = ((playerIndex + 1) >= _players.length) ? 0 : (playerIndex + 1);
		return index;
	}
	
	private void quit() {
		System.out.println("Goodbye!");
		System.exit(0);
	}
	
	public NewBoard getBoard() {
		return _board;
	}
}
