
public abstract class NewPlayer {
	protected String _name;
	protected int _winCount;
	
	public NewPlayer() {
		
	}
	
	public NewPlayer(String name) {
		_name = name;
	}
	
	public abstract void takeTurn(Game game);
	
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
