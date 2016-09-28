import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class GameManager {

	private final int START_ROW_ONE_SIZE = 3;
	private ArrayList<Piece> rowOne = new ArrayList<Piece>();
	private final int START_ROW_TWO_SIZE = 5;
	private ArrayList<Piece> rowTwo = new ArrayList<Piece>();
	private final int START_ROW_THREE_SIZE = 7;
	private ArrayList<Piece> rowThree = new ArrayList<Piece>();

	private int piecesInRowOne = START_ROW_ONE_SIZE;
	private int piecesInRowTwo = START_ROW_TWO_SIZE;
	private int piecesInRowThree = START_ROW_THREE_SIZE;
	private int totalPieces = piecesInRowOne+piecesInRowTwo+piecesInRowThree;

	private Player player;
	private Scanner scan = new Scanner(System.in);
	private Board board;
	private Random rand = new Random();

	private Ai ai = new Ai();
	private SmartAi smartAi = new SmartAi();
	private HashMap<ArrayList<Integer>, Double> weightedTurns = new HashMap<ArrayList<Integer>, Double>();
	private HashMap<Integer, ArrayList<Integer>> gameInProgress = new HashMap<Integer, ArrayList<Integer>>();
	private int smartAiWins = 0;
	private int aiWins = 0;

	public GameManager(Player player, Board board){
		populateRows(rowOne, rowTwo, rowThree);
		this.player = player;
		this.board = board;
		ArrayList<Integer> firstBoard = new ArrayList<Integer>();
		firstBoard.add(3);
		firstBoard.add(5);
		firstBoard.add(7);
		weightedTurns.put(firstBoard, 0.0);
	}

	public void runGame(){
		boolean gameStarted = false;
		int gamesPlayed = 0;
		int gamesToPlay = 1000000;
		while(!gameStarted){
			System.out.print("ENTER 1 FOR PVP, 2 FOR PVAI, 3 FOR AIVAI: ");
			int gameMode = scan.nextInt();
			if(gameMode == 1){
				while(gamesPlayed < gamesToPlay){
					runPlayerVsPlayer();
					gamesPlayed++;
					resetBoard();
				}
				gameStarted = true;
			}else if(gameMode == 2){
				while(gamesPlayed < gamesToPlay){
					runPlayerVsAi();
					gamesPlayed++;
					resetBoard();
				}
				gameStarted = true;
			}else if(gameMode == 3){
				while(gamesPlayed < gamesToPlay){
					runAiVsSmartAi();
					resetBoard();
					gamesPlayed++;
					addGamestatesToMap();
					gameInProgress.clear();
				}
				gameStarted = true;
				System.out.println("Smart Ai Won: " + ((double)smartAiWins/gamesToPlay)*100 + "%");
				System.out.println("Normal Ai Won: " + ((double)aiWins/gamesToPlay)*100 + "%");
			}else{
				System.out.println("That is not a valid game mode! Try Again!");
			}
		}
	}

	private void runPlayerVsPlayer(){
		while(totalPieces > 0){
			board.drawBoard(rowOne, rowTwo, rowThree);
			queryPlayer();
			changePlayer();
		}
		System.out.println(player.getCurrentPlayer() + " Wins!");
	}

	private void runPlayerVsAi(){
		String currentPlayer = "NO ONE";
		int playOrder = rand.nextInt(2);
		if(playOrder == 0){
			while(totalPieces > 0){
				board.drawBoard(rowOne, rowTwo, rowThree);
				queryPlayer();
				currentPlayer = "Ai";
				if(totalPieces>0){
					board.drawBoard(rowOne, rowTwo, rowThree);
					currentPlayer = "player";
					queryAi();
				}
			}
			board.drawBoard(rowOne, rowTwo, rowThree);
			System.out.println(currentPlayer + " WINS!");
		}else if(playOrder == 1){
			while(totalPieces > 0){
				board.drawBoard(rowOne, rowTwo, rowThree);
				queryAi();
				currentPlayer = "player";
				if(totalPieces>0){
					board.drawBoard(rowOne, rowTwo, rowThree);
					currentPlayer = "ai";
					queryPlayer();
				}
			}
			board.drawBoard(rowOne, rowTwo, rowThree);
			System.out.println(currentPlayer + " WINS!");
		}
	}

	private void runAiVsSmartAi(){
		int currentTurn = 1;
		String currentPlayer = "NO ONE";
		int playOrder = rand.nextInt(2);
		if(playOrder == 0){
			while(totalPieces > 0){
				querySmartAi();
				storeCurrentGamestate(currentTurn);
				currentTurn++;
				currentPlayer = "Normal Ai";
				if(totalPieces>0){
					currentPlayer = "Smart Ai";
					queryAi();
					storeCurrentGamestate(currentTurn);
					currentTurn++;
				}
			}
			System.out.println(currentPlayer + " WINS!");
			if(currentPlayer == "Smart Ai"){
				smartAiWins++;
			}else{
				aiWins++;
			}
		}else if(playOrder == 1){
			while(totalPieces > 0){
				queryAi();
				storeCurrentGamestate(currentTurn);
				currentTurn++;
				currentPlayer = "Smart Ai";
				if(totalPieces>0){
					currentPlayer = "Normal Ai";
					querySmartAi();
					storeCurrentGamestate(currentTurn);
					currentTurn++;
				}
			}
			System.out.println(currentPlayer + " WINS!");
			if(currentPlayer == "Smart Ai"){
				smartAiWins++;
			}else{
				aiWins++;
			}
		}
	}

	private void storeCurrentGamestate(int currentTurn){
		ArrayList<Integer> rowStates = new ArrayList<Integer>();
		rowStates.add(piecesInRowOne);
		rowStates.add(piecesInRowTwo);
		rowStates.add(piecesInRowThree);
		gameInProgress.put(currentTurn, rowStates);
	}

	private void addGamestatesToMap(){
		double tempMapSize = gameInProgress.size();
		for(int j = 1; j < tempMapSize; j++){
			double weight;
			if(tempMapSize%2== 0){
				if(j%2==0){
					weight = -(j/tempMapSize);
				}else{
					weight = j/tempMapSize;
				}
			}else{
				if(j%2==0){
					weight = j/tempMapSize;
				}else{
					weight = -(j/tempMapSize);
				}
			}
			if(weightedTurns.containsKey(gameInProgress.get(j))){
				weightedTurns.replace(gameInProgress.get(j), (double)weightedTurns.get(gameInProgress.get(j))+weight);
			}else{
				weightedTurns.put(gameInProgress.get(j), weight);
			}
		}
	}

	private void populateRows(ArrayList<Piece> rowOne, ArrayList<Piece> rowTwo, ArrayList<Piece> rowThree){
		for(int j = 0; j < START_ROW_ONE_SIZE; j++){
			rowOne.add(new Piece());
		}
		for(int j = 0; j < START_ROW_TWO_SIZE; j++){
			rowTwo.add(new Piece());
		}
		for(int j = 0; j < START_ROW_THREE_SIZE; j++){
			rowThree.add(new Piece());
		}
	}

	private void queryPlayer(){
		player.setPeicesChosen(false);
		player.setRowChosen(false);
		while(!player.isRowChosen()){
			System.out.print(player.getCurrentPlayer() + " choose your row: ");
			int row = scan.nextInt();
			if(checkRowValidity(row, true)){
				player.setRowChosen(true);
				while(!player.isPeicesChosen() && player.isRowChosen()){
					System.out.print(player.getCurrentPlayer() + " (Enter 0 to restart your turn) How many pieces do you want to take: ");
					int pieces = scan.nextInt();
					if(checkPieceValidity(row, pieces)){
						player.setPeicesChosen(true);
						if(player.isRowChosen()){
							editPieces(row, pieces);
						}
					}
				}
			}
		}
	}

	private void queryAi(){
		ai.setPeicesChosen(false);
		ai.setRowChosen(false);
		while(!ai.isRowChosen()){
			int row = rand.nextInt(3)+1;
			if(checkRowValidity(row, false)){
				ai.setRowChosen(true);
				while(!ai.isPeicesChosen() && ai.isRowChosen()){
					int pieces = getAiPieces(row);
					if(checkPieceValidity(row, pieces)){
						ai.setPeicesChosen(true);
						if(ai.isRowChosen()){
							editPieces(row, pieces);
							//System.out.println("Ai has taken " + pieces + " from row " + row + "!");
						}
					}
				}
			}
		}
	}

	private int getAiPieces(int row){
		int pieces = 0;
		if(row == 1){
			pieces = rand.nextInt(piecesInRowOne)+1;
		}else if(row == 2){
			pieces = rand.nextInt(piecesInRowTwo)+1;
		}else if(row == 3){
			pieces = rand.nextInt(piecesInRowThree)+1;
		}
		return pieces;
	}

	private void querySmartAi(){
		smartAi.setPeicesChosen(false);
		smartAi.setRowChosen(false);
		ArrayList<Integer> bestGameState = new ArrayList<Integer>();
		double bestWeight = 0;
		for(int j = 1; j<=piecesInRowOne; j++){
			int potentialPieces = piecesInRowOne-j;
			ArrayList<Integer> potentialGameState = new ArrayList<Integer>();
			potentialGameState.add(potentialPieces);
			potentialGameState.add(piecesInRowTwo);
			potentialGameState.add(piecesInRowThree);
			if(weightedTurns.containsKey(potentialGameState)){
				if(weightedTurns.get(potentialGameState) > bestWeight){
					bestGameState = potentialGameState;
					bestWeight = weightedTurns.get(potentialGameState);
				}
			}
		}
		for(int j = 1; j<=piecesInRowTwo; j++){
			int potentialPieces = piecesInRowTwo-j;
			ArrayList<Integer> potentialGameState = new ArrayList<Integer>();
			potentialGameState.add(piecesInRowOne);
			potentialGameState.add(potentialPieces);
			potentialGameState.add(piecesInRowThree);
			if(weightedTurns.containsKey(potentialGameState)){
				if(weightedTurns.get(potentialGameState) > bestWeight){
					bestGameState = potentialGameState;
					bestWeight = weightedTurns.get(potentialGameState);
				}
			}
		}
		for(int j = 1; j<=piecesInRowThree; j++){
			int potentialPieces = piecesInRowThree-j;
			ArrayList<Integer> potentialGameState = new ArrayList<Integer>();
			potentialGameState.add(piecesInRowOne);
			potentialGameState.add(piecesInRowTwo);
			potentialGameState.add(potentialPieces);
			if(weightedTurns.containsKey(potentialGameState)){
				if(weightedTurns.get(potentialGameState) > bestWeight){
					bestGameState = potentialGameState;
					bestWeight = weightedTurns.get(potentialGameState);
				}
			}
		}
		if(bestWeight<=0){
			randomMove();
		}else{
			if(piecesInRowOne>bestGameState.get(0)){
				int difference = piecesInRowOne-bestGameState.get(0);
				editPieces(1, difference);
			}else if(piecesInRowTwo>bestGameState.get(1)){
				int difference = piecesInRowTwo-bestGameState.get(1);
				editPieces(2, difference);
			}else if(piecesInRowThree>bestGameState.get(2)){
				int difference = piecesInRowThree-bestGameState.get(2);
				editPieces(3, difference);
			}
		}
	}

	private void randomMove(){
		while(!smartAi.isRowChosen()){
			int row = rand.nextInt(3)+1;
			if(checkRowValidity(row, false)){
				smartAi.setRowChosen(true);
				while(!smartAi.isPeicesChosen() && smartAi.isRowChosen()){
					int pieces = getAiPieces(row);
					if(checkPieceValidity(row, pieces)){
						smartAi.setPeicesChosen(true);
						if(smartAi.isRowChosen()){
							editPieces(row, pieces);
							//System.out.println("Smart Ai has taken " + pieces + " from row " + row + "!");
						}
					}
				}
			}
		}
	}

	public boolean checkRowValidity(int row, boolean printMessages){
		boolean isValid = false;
		if(row == 1){
			if(piecesInRowOne > 0){
				isValid = true;
			}else{
				if(printMessages){
					System.out.println("Row 1 has 0 pieces! Pick Another!");
				}
			}
		}else if(row == 2){
			if(piecesInRowTwo > 0){
				isValid = true;
			}else{
				if(printMessages){
					System.out.println("Row 2 has 0 pieces! Pick Another!");
				}
			}
		}else if(row == 3){
			if(piecesInRowThree > 0){
				isValid = true;
			}else{
				if(printMessages){
					System.out.println("Row 3 has 0 pieces! Pick Another!");
				}
			}
		}else{
			System.out.println("Not a valid row! Pick Another!");
		}
		return isValid;
	}

	public boolean checkPieceValidity(int row, int pieces){
		boolean isValid = false;
		if(pieces == 0){
			System.out.println("restarting your turn!");
			player.setRowChosen(false);
		}else if(row == 1){
			if(piecesInRowOne >= pieces){
				isValid = true;
			}else{
				System.out.println("Row 1 doesn't have that many pieces try again!");
			}
		}else if(row == 2){
			if(piecesInRowTwo >= pieces){
				isValid = true;
			}else{
				System.out.println("Row 2 doesn't have that many pieces try again!");
			}
		}else if(row == 3){
			if(piecesInRowThree >= pieces){
				isValid = true;
			}else{
				System.out.println("Row 3 doesn't have that many pieces try again!");
			}
		}
		return isValid;
	}

	public void editPieces(int row, int pieces){
		if(row == 1){
			removePieces(rowOne, pieces, piecesInRowOne);
			piecesInRowOne -= pieces;
		}else if(row == 2){
			removePieces(rowTwo, pieces, piecesInRowTwo);
			piecesInRowTwo -= pieces;
		}else if(row == 3){
			removePieces(rowThree, pieces, piecesInRowThree);
			piecesInRowThree -= pieces;
		}
		totalPieces -= pieces;
	}

	private void removePieces(ArrayList<Piece> row, int pieces, int piecesInRow){
		int placeToStartRemoval = (piecesInRow - pieces);
		for(int j = 0; j < piecesInRow; j++){
			if(j >= placeToStartRemoval){
				row.get(j).setCharacter('-');
			}
		}
	}

	private void resetBoard(){
		piecesInRowOne = START_ROW_ONE_SIZE;
		piecesInRowTwo = START_ROW_TWO_SIZE;
		piecesInRowThree = START_ROW_THREE_SIZE;
		totalPieces = piecesInRowOne + piecesInRowTwo + piecesInRowThree;
		resetPieces(rowOne);
		resetPieces(rowTwo);
		resetPieces(rowThree);
	}

	private void resetPieces(ArrayList<Piece> row){
		for(int j = 0; j < row.size(); j++){
			row.get(j).setCharacter('o');
		}
	}

	private void changePlayer(){
		if(player.getCurrentPlayer() == "player1"){
			player.setCurrentPlayer("player2");
		}else{
			player.setCurrentPlayer("player1");
		}
	}

	public void setPiecesInRowOne(int piecesInRowOne) {
		this.piecesInRowOne = piecesInRowOne;
	}

	public void setPiecesInRowTwo(int piecesInRowTwo) {
		this.piecesInRowTwo = piecesInRowTwo;
	}

	public void setPiecesInRowThree(int piecesInRowThree) {
		this.piecesInRowThree = piecesInRowThree;
	}

}
