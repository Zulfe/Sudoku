package Controller;

import Model.PuzzleModel;
import Model.WebScraperModel;
import View.PuzzleView;

public class Controller {
	private PuzzleModel theModel;
	private PuzzleView theView;
	
	public Controller(PuzzleModel theModel, PuzzleView theView){
		this.theModel = theModel;
		this.theView = theView;
	}
	
	public void setPuzzle(){
		theModel.set
	}
	
	public void getNYPuzzle(String difficulty){
		new WebScraperModel().scrapeWebSudokuByLevel(difficulty);
	}
	
}
