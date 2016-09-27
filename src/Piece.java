
public class Piece {
	
	private char character = 'o';
	private boolean isTaken = false;
	
	public Piece(){
	}

	public boolean isTaken() {
		return isTaken;
	}

	public void setTaken(boolean isTaken) {
		this.isTaken = isTaken;
	}

	public char getCharacter() {
		return character;
	}

	public void setCharacter(char character) {
		this.character = character;
	}
	
}
