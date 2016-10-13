
public abstract class Player {
	protected String _name;
	protected int _winCount;
	
	public Player() {
		
	}
	
	public Player(String name) {
		_name = name;
	}
	
	public abstract void takeTurn(Game game);
	
	public void finalizeGame() {}
	
	public void AddWin() {
		_winCount++;
	}

	public int getWinCount() {
		return _winCount;
	}
	
	public String getName() {
		return _name;
	}
	
	public String toString() {
		return _name;
	}
}
