import java.util.Scanner;

@Deprecated
public class Player {

	private String currentPlayer = "player1";
	private boolean rowChosen = false;
	private boolean peicesChosen = false;
	
	public String getCurrentPlayer() {
		return currentPlayer;
	}

	public void setCurrentPlayer(String currentPlayer) {
		this.currentPlayer = currentPlayer;
	}

	public boolean isRowChosen() {
		return rowChosen;
	}

	public void setRowChosen(boolean rowChosen) {
		this.rowChosen = rowChosen;
	}

	public boolean isPeicesChosen() {
		return peicesChosen;
	}

	public void setPeicesChosen(boolean peicesChosen) {
		this.peicesChosen = peicesChosen;
	}
}
