
public class Main {

	public static void main(String[] args){
		Board board = new Board();
		Player player = new Player();
		GameManager manager = new GameManager(player, board);
		manager.runGame();
	}
}
