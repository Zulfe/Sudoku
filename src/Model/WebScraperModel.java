package Model;

import java.io.IOException;
import java.util.Base64;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import Exception.InvalidDifficultyException;

public class WebScraperModel {
	
	public WebScraperModel(){
		
	}
	
	public int[] scrapeWebSudokuByLevel(String difficulty){
		int[] scrapedPuzzle = new int[81];
		
		try {
			if(!(difficulty.equals("easy") || difficulty.equals("medium") || difficulty.equals("hard")))
				throw new InvalidDifficultyException("Difficulty must be easy, medium, or hard.");
			
			Document document = Jsoup.connect("http://www.nytimes.com/crosswords/game/sudoku/easy").get();
			
			Elements encryptedPuzzle = document.select("#preload-payload");
			String isolatedBase64 = encryptedPuzzle.html().substring(18, encryptedPuzzle.html().length() - 2);
			byte[] decodedByteArray = Base64.getDecoder().decode(isolatedBase64);
			String decodedOutput = new String(decodedByteArray);
			
			String puzzleArrayAsString = decodedOutput.substring(decodedOutput.indexOf("[", decodedOutput.indexOf(difficulty)),
					decodedOutput.indexOf("]", decodedOutput.indexOf(difficulty) + 62) + 1);
			
			int scrapedPuzzleIndexCounter = 0;
			for(int i = 1; i < puzzleArrayAsString.length(); i++){
				if(puzzleArrayAsString.charAt(i) == 'n'){
					scrapedPuzzle[scrapedPuzzleIndexCounter] = 0;
					scrapedPuzzleIndexCounter++;
				} else if(puzzleArrayAsString.charAt(i) >= 49 && puzzleArrayAsString.charAt(i) <= 57){
					scrapedPuzzle[scrapedPuzzleIndexCounter] = (int) puzzleArrayAsString.charAt(i) - 48;
					scrapedPuzzleIndexCounter++;
				} else
					continue;
			}
			
		} catch (IOException | InvalidDifficultyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return scrapedPuzzle;
	}
}
