import java.util.Random;

public class DumbAi extends Player {
	protected Random _random;
	
	public DumbAi() {
		_name = "Dumb AI";
		init();
	}

	public DumbAi(String name) {
		super(name);
		init();
	}
	
	public void takeTurn(Game game) {
		getMove(game);
	}
	
	protected void init() {
		_random = new Random();
	}
	
	protected Move getMove(Game game) {
		Board board = game.getBoard();
		
		int[] rows = board.getFilledRows();
		int randRow = rows[_random.nextInt(rows.length)];
		
		Row row = board.getRow(randRow);
		int takeAmt = _random.nextInt(row.getCount()) + 1;
		
		System.out.format("%s taking %d from row %d\n", getName(), takeAmt, randRow);
		
		Move move = new Move(row.getCount(), takeAmt);
		
		row.take(takeAmt);
		return move;
	}

}
