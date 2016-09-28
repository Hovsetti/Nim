
public class Board {

	public void drawBoard(Piece[] rowOne, Piece[] rowTwo, Piece[] rowThree){
		drawRow(rowOne);
		drawRow(rowTwo);
		drawRow(rowThree);
	}
	
	private void drawRow(Piece[] row){
		for(int j = 0; j < row.length; j++){
			System.out.print(row[j].getCharacter());
		}
		System.out.println("\n");
	}

}
