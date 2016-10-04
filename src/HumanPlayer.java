public class HumanPlayer extends NewPlayer {

	public HumanPlayer(String name) {
		super(name);
	}

	
	public void takeTurn(Game game) {
		Row row = game.promptForRow();
		int takeAmt = game.promptTake(row);
		row.take(takeAmt);
	}

}
