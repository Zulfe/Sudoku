package View;

public class PuzzleView {
	
	SudokuTable sudoku;
	
	public PuzzleView(){
		this.sudoku = new SudokuTable();
		initialize();
	}
	
	public void initialize(){
		
	}
	
	public void setPuzzleState(int[][] puzzleState){
		for(int i = 0; i < puzzleState.length; i++)
			for(int j = 0; j < puzzleState[i].length; j++)
				sudoku.setValueAt(puzzleState[i][j], i, j);
	}
	
	public void printPuzzleState(){
		for(int i = 0; i < 9; i++)
			for(int j = 0; j < 9; j++)
				System.out.println(sudoku.getValueAt(i, j));
	}
	
}
