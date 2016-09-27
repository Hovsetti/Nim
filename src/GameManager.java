import java.util.Random;
import java.util.Scanner;

public class GameManager {

	private final int START_ROW_ONE_SIZE = 3;
	private Piece[] rowOne = new Piece[START_ROW_ONE_SIZE];
	private final int START_ROW_TWO_SIZE = 5;
	private Piece[] rowTwo = new Piece[START_ROW_TWO_SIZE];
	private final int START_ROW_THREE_SIZE = 7;
	private Piece[] rowThree = new Piece[START_ROW_THREE_SIZE];

	private int piecesInRowOne = START_ROW_ONE_SIZE;
	private int piecesInRowTwo = START_ROW_TWO_SIZE;
	private int piecesInRowThree = START_ROW_THREE_SIZE;
	private int totalPieces = piecesInRowOne+piecesInRowTwo+piecesInRowThree;

	private Player player;
	private Scanner scan = new Scanner(System.in);
	private Board board;
	private Random rand = new Random();
	
	private Ai ai = new Ai();

	public GameManager(Player player, Board board){
		populateRow(rowOne);
		populateRow(rowTwo);
		populateRow(rowThree);
		this.player = player;
		this.board = board;
	}

	public void runGame(){
		boolean gameStarted = false;
		while(!gameStarted){
			System.out.print("ENTER 1 FOR PVP, 2 FOR PVAI, 3 FOR AIVAI: ");
			int gameMode = scan.nextInt();
			if(gameMode == 1){
				runPlayerVsPlayer();
				gameStarted = true;
			}else if(gameMode == 2){
				runPlayerVsAi();
				gameStarted = true;
			}else if(gameMode == 3){
				gameStarted = true;
			}else{
				System.out.println("That is not a valid game mode! Try Again!");
			}
		}
	}

	private void runPlayerVsPlayer(){
		while(totalPieces > 1){
			board.drawBoard(rowOne, rowTwo, rowThree);
			queryPlayer();
			if(totalPieces > 1){
				changePlayer();
			}
		}
		System.out.println(player.getCurrentPlayer() + " Wins!");
	}
	
	private void runPlayerVsAi(){
		String currentPlayer = "NO ONE";
		int playOrder = rand.nextInt(2);
		System.out.println(playOrder);
		if(playOrder == 0){
			while(totalPieces > 1){
				board.drawBoard(rowOne, rowTwo, rowThree);
				queryPlayer();
				currentPlayer = "player1";
				if(totalPieces>1){
					board.drawBoard(rowOne, rowTwo, rowThree);
					currentPlayer = "Ai1";
					queryAi();
				}
			}
			board.drawBoard(rowOne, rowTwo, rowThree);
			System.out.println(currentPlayer + " WINS!");
		}else if(playOrder == 1){
			while(totalPieces > 1){
				board.drawBoard(rowOne, rowTwo, rowThree);
				queryAi();
				currentPlayer = "Ai1";
				if(totalPieces>1){
					board.drawBoard(rowOne, rowTwo, rowThree);
					currentPlayer = "player1";
					queryPlayer();
				}
			}
			board.drawBoard(rowOne, rowTwo, rowThree);
			System.out.println(currentPlayer + " WINS!");
		}
	}
	
	private void populateRow(Piece[] pieces){
		for(int j = 0; j < pieces.length; j++){
			pieces[j] = new Piece();
		}
	}

	private void queryPlayer(){
		player.setPeicesChosen(false);
		player.setRowChosen(false);
		while(!player.isRowChosen()){
			System.out.print(player.getCurrentPlayer() + " choose your row: ");
			int row = scan.nextInt();
			if(checkRowValidity(row)){
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
			if(checkRowValidity(row)){
				ai.setRowChosen(true);
				while(!ai.isPeicesChosen() && ai.isRowChosen()){
					int pieces = getAiPieces(row);
					if(checkPieceValidity(row, pieces)){
						ai.setPeicesChosen(true);
						if(ai.isRowChosen()){
							editPieces(row, pieces);
							System.out.println("Ai has taken " + pieces + " from row " + row + "!");
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

	public boolean checkRowValidity(int row){
		boolean isValid = false;
		if(row == 1){
			if(piecesInRowOne > 0){
				isValid = true;
			}else{
				System.out.println("Row 1 has 0 pieces! Pick Another!");
			}
		}else if(row == 2){
			if(piecesInRowTwo > 0){
				isValid = true;
			}else{
				System.out.println("Row 2 has 0 pieces! Pick Another!");
			}
		}else if(row == 3){
			if(piecesInRowThree > 0){
				isValid = true;
			}else{
				System.out.println("Row 3 has 0 pieces! Pick Another!");
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

	private void removePieces(Piece[] row, int pieces, int piecesInRow){
		int placeToStartRemoval = (piecesInRow - pieces);
		for(int j = 0; j < piecesInRow; j++){
			if(j >= placeToStartRemoval){
				row[j].setCharacter('-');
			}
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
