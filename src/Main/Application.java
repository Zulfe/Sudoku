package Main;

import Model.PuzzleModel;
import Model.WebScraperModel;

public class Application {
	public static void main(String[] args){
		WebScraperModel wsm = new WebScraperModel();
		int[] oneDimPuzzle = wsm.scrapeWebSudokuByLevel("medium");
		PuzzleModel puzzle = new PuzzleModel(oneDimPuzzle);
		puzzle.printPuzzle();
	}
}
