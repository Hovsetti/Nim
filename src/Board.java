import java.util.ArrayList;

public class Board {

	public void drawBoard(ArrayList<Piece> rowOne, ArrayList<Piece> rowTwo, ArrayList<Piece> rowThree){
		drawRow(rowOne);
		drawRow(rowTwo);
		drawRow(rowThree);
	}
	
	private void drawRow(ArrayList<Piece> row){
		for(int j = 0; j < row.size(); j++){
			System.out.print(row.get(j).getCharacter());
		}
		System.out.println("\n");
	}

}
