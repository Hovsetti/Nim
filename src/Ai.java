
@Deprecated
public class Ai {
	
	private String currentAi;
	private boolean rowChosen = false;
	private boolean peicesChosen = false;
	
	public String getCurrentPlayer() {
		return currentAi;
	}

	public void setCurrentPlayer(String currentAi) {
		this.currentAi = currentAi;
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
