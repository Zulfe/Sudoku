package Model;

import java.awt.Point;
import java.util.Arrays;

public class PuzzleModel {
	public int[][] puzzle;
	public int[][] puzzleBackup = new int[9][9];
	public boolean[][][] candidates;
	public boolean refinementsPossible = true;
	public boolean secondaryRefinementsPossible = true;
	
	public PuzzleModel(int[] givenPuzzle){
		int[][] convertedPuzzle = convertToTwoDimensions(givenPuzzle);
		puzzle = convertedPuzzle.clone();
		puzzleBackup = convertedPuzzle.clone();
		candidates = new boolean[9][9][9];
		
		buildCandidatesMatrix();
		
		while(refinementsPossible){
			refinementsPossible = false;
			if(isBooleanConstrainPresent()){
				booleanConstrain();
				refinementsPossible = true;
			}
			if(isHiddenRowSinglesPresent()){
				hiddenRowSingles();
				refinementsPossible = true;
			}
			if(isHiddenColumnSinglesPresent()){
				hiddenColumnSingles();
				refinementsPossible = true;
			}
			if(isHiddenBlockSinglesPresent()){
				hiddenBlockSingles();
				refinementsPossible = true;
			}
		}
//		while(secondaryRefinementsPossible){
//			secondaryRefinementsPossible = true;
//			if(isBooleanConstrainPresent()){
//				booleanConstrain();
//				secondaryRefinementsPossible = true;
//			}
//			if(isHiddenRowSinglesPresent()){
//				hiddenRowSingles();
//				secondaryRefinementsPossible = true;
//			}
//			if(isHiddenColumnSinglesPresent()){
//				hiddenColumnSingles();
//				secondaryRefinementsPossible = true;
//			}
//			if(isHiddenBlockSinglesPresent()){
//				hiddenBlockSingles();
//				secondaryRefinementsPossible = true;
//			}
//			if(isNakedPairsPresent()){
//				nakedPairsReduction();
//				secondaryRefinementsPossible = true;
//			}
//		}
		//printCandidatesArray();
	}
	
	public PuzzleModel(int[][] givenPuzzle){
		puzzle = givenPuzzle.clone();
		puzzleBackup = givenPuzzle.clone();
		candidates = new boolean[9][9][9];
	}
	
	public int[][] getOriginalPuzzle(){
		return puzzleBackup;
	}
	
	public int[][] getPuzzleState(){
		return puzzle;
	}
	
	public int[][] convertToTwoDimensions(int[] oneDimPuzzle){
		int[][] twoDimPuzzle = new int[9][9];
		for(int i = 0; i < 9; i++)
			for(int k = 0; k < 9; k++)
				twoDimPuzzle[i][k] = oneDimPuzzle[(i * 9) + k];
		return twoDimPuzzle;
	}
	
	public boolean isSolved(){
		for(int[] row : puzzle)
			for(int value : row)
				if(value == 0)
					return false;
		return true;
	}
	
	private void buildCandidatesMatrix(){
		boolean[] presentValues = new boolean[9];
		for(int row = 0; row < 9; row++){
			for(int column = 0; column < 9; column++){
				
				if(puzzle[row][column] != 0){
					Arrays.fill(presentValues, true);
					candidates[row][column] = presentValues.clone();
					continue;
				}
				
				Arrays.fill(presentValues, false);
				
				presentValues = findPresentValuesInRow(row, presentValues);
				presentValues = findPresentValuesInColumn(column, presentValues);
				presentValues = findPresentValuesInBlock((row / 3) + 1, (column / 3) + 1, presentValues);
				
				candidates[row][column] = presentValues.clone();
			}
		}
	}
	
	private void updateLocalCandidates(int row, int column, int foundValue){
		Arrays.fill(candidates[row][column], true);
		System.out.println("Attempting to update candidates local to (" + row + ", " + column + ") with found value " + foundValue);
		for(int col = 0; col < 9; col++)
			candidates[row][col][foundValue - 1] = true;
		System.out.println("ROW SUCCESSFULLY UPDATED!");
		for(int r = 0; r < 9; r++)
			candidates[r][column][foundValue - 1] = true;
		System.out.println("COLUMN SUCCESSFULLY UPDATED!");
		for(int r = (((row / 3) + 1) * 3 - 2) - 1; r < (((row / 3) + 1) * 3); r++)
			for(int col = ((column / 3 + 1) * 3 - 2) - 1; col < ((column / 3 + 1) * 3); col++)
				candidates[r][col][foundValue - 1] = true;
		System.out.println("BLOCK SUCCESSFULLY UPDATED!");
	}

	public boolean isBooleanConstrainPresent(){
		int presentValuesSum = 0;
		for(int row = 0; row < 9; row++){
			for(int column = 0; column < 9; column++){
				presentValuesSum = 0;
				for(int i = 0; i < candidates[row][column].length; i++)
					presentValuesSum += candidates[row][column][i] ? 1 : 0;
				if(presentValuesSum == 8)
					return true;
			}
		}
		return false;
	}
	
	public void booleanConstrain(){
		int presentValuesSum = 0;
		for(int row = 0; row < 9; row++){
			for(int column = 0; column < 9; column++){
				presentValuesSum = 0;
				for(int i = 0; i < candidates[row][column].length; i++)
					presentValuesSum += candidates[row][column][i] ? 1 : 0;
				if(presentValuesSum == 8){
					puzzle[row][column] = getValueFromBooleanArray(candidates[row][column]) + 1;
					updateLocalCandidates(row, column, getValueFromBooleanArray(candidates[row][column]) + 1);
				}
			}
		}
	}
	
	public int getValueFromBooleanArray(boolean[] possibleValues){
		for(int i = 0; i < possibleValues.length; i++)
			if(!possibleValues[i])
				return i;
		return -1;
	}
	
	public boolean[] findPresentValuesInRow(int row, boolean[] presentValues){
		for(int column = 0; column < 9; column++)
			if(puzzle[row][column] != 0)
				presentValues[puzzle[row][column] - 1] = true;
		return presentValues;
	}
	
	public boolean[] findPresentValuesInColumn(int column, boolean[] presentValues){
		for(int row = 0; row < 9; row++)
			if(puzzle[row][column] != 0)
				presentValues[puzzle[row][column] - 1] = true;
		return presentValues;
	}
	
	public boolean[] findPresentValuesInBlock(int block_x, int block_y, boolean[] presentValues){
		for(int row = (block_x * 3 - 2) - 1; row < (block_x * 3); row++)
			for(int column = (block_y * 3 - 2) - 1; column < (block_y * 3); column++)
				if(puzzle[row][column] != 0)
					presentValues[puzzle[row][column] - 1] = true;
		return presentValues;
	}

	
	
	public boolean isHiddenRowSinglesPresent(){
		int totalCandidates;
		for(int row = 0; row < puzzle.length; row++){
			for(int candidateIndex = 0; candidateIndex < candidates[row][row].length; candidateIndex++){
				totalCandidates = 0;
				for(int column = 0; column < puzzle[row].length; column++){
					if(!candidates[row][column][candidateIndex])
						totalCandidates++;
				}
				if(totalCandidates == 1)
					return true;
			}
		}
		return false;
	}
	
	public void hiddenRowSingles(){
		int totalCandidates;
		int lastKnownCandidateIndex;
		for(int row = 0; row < puzzle.length; row++){
			for(int candidateIndex = 0; candidateIndex < candidates[row][row].length; candidateIndex++){
				totalCandidates = 0;
				lastKnownCandidateIndex = 0;
				for(int column = 0; column < puzzle[row].length; column++){
					System.out.printf("RHidSing: (%d, %d, %d) \n", row, column, candidateIndex);
					if(!candidates[row][column][candidateIndex]){
						totalCandidates++;
						lastKnownCandidateIndex = column;
					}
				}
				if(totalCandidates == 1){
					System.out.printf("CHidSing: HidSin FOUND AT (%d, %d) OF VALUE %d \n",
							   row, lastKnownCandidateIndex, candidateIndex + 1);
					puzzle[row][lastKnownCandidateIndex] = candidateIndex + 1;
					updateLocalCandidates(row, lastKnownCandidateIndex, candidateIndex + 1);
				}
			}
		}
	}
	
	
	public boolean isHiddenColumnSinglesPresent(){
		int totalCandidates;
		for(int column = 0; column < puzzle[0].length; column++){
			for(int candidateIndex = 0; candidateIndex < candidates[0][0].length; candidateIndex++){
				totalCandidates = 0;
				for(int row = 0; row < puzzle.length; row++){
					if(!candidates[row][column][candidateIndex])
						totalCandidates++;
				}
				if(totalCandidates == 1)
					return true;
			}
		}
		return false;
	}
	
	public void hiddenColumnSingles(){
		int totalCandidates;
		int lastKnownCandidateIndex;
		for(int column = 0; column < puzzle[0].length; column++){
			for(int candidateIndex = 0; candidateIndex < candidates[0][0].length; candidateIndex++){
				totalCandidates = 0;
				lastKnownCandidateIndex = 0;
				for(int row = 0; row < puzzle.length; row++){
					System.out.printf("CHidSing: CHECKING (%d, %d, %d) \n", row, column, candidateIndex);
					if(!candidates[row][column][candidateIndex]){
						System.out.printf("CHidSing: ENCOUNTERED FALSE AT (%d, %d, %d) \n", row, column, candidateIndex);
						totalCandidates++;
						lastKnownCandidateIndex = row;
					}
				}
				if(totalCandidates == 1){
					System.out.printf("CHidSing: HidSin FOUND AT (%d, %d) OF VALUE %d \n",
									   lastKnownCandidateIndex, column, candidateIndex + 1);
					puzzle[lastKnownCandidateIndex][column] = candidateIndex + 1;
					updateLocalCandidates(lastKnownCandidateIndex, column, candidateIndex + 1);
				}
			}
		}
	}
	
	public boolean isHiddenBlockSinglesPresent(){
		int totalCandidates;
		for(int candidateIndex = 0; candidateIndex < candidates[0][0].length; candidateIndex++){
			for(int block_x = 1; block_x < 4; block_x++){
				for(int block_y = 1; block_y < 4; block_y++){
					totalCandidates = 0;
					for(int row = (block_x * 3 - 2) - 1; row < (block_x * 3); row++)
						for(int column = (block_y * 3 - 2) - 1; column < (block_y * 3); column++)
							if(!candidates[row][column][candidateIndex]){
								totalCandidates++;
							}
					if(totalCandidates == 1)
						return true;
					
				}
			}
		}
		return false;
	}
	
	public void hiddenBlockSingles(){
		int totalCandidates;
		Point lastKnownCandidateIndex = new Point(0, 0);
		for(int candidateIndex = 0; candidateIndex < candidates[0][0].length; candidateIndex++){
			for(int block_x = 1; block_x < 4; block_x++){
				for(int block_y = 1; block_y < 4; block_y++){
					totalCandidates = 0;
					for(int row = (block_x * 3 - 2) - 1; row < (block_x * 3); row++)
						for(int column = (block_y * 3 - 2) - 1; column < (block_y * 3); column++)
							if(!candidates[row][column][candidateIndex]){
								totalCandidates++;
								lastKnownCandidateIndex = new Point(row, column);
							}
					if(totalCandidates == 1){
						System.out.printf("BHidSing: HidSin FOUND AT (%d, %d) OF VALUE %d \n",
										   (int) lastKnownCandidateIndex.getX(), (int) lastKnownCandidateIndex.getY(), candidateIndex + 1);
						puzzle[(int) lastKnownCandidateIndex.getX()][(int) lastKnownCandidateIndex.getY()] = candidateIndex + 1;
						updateLocalCandidates((int) lastKnownCandidateIndex.getX(), (int) lastKnownCandidateIndex.getY(), candidateIndex + 1);
					}
				}
			}
		}
			
	}

	public boolean isNakedPairsPresent(){
		for(int row = 0; row < puzzle.length; row++)
			for(int column = 0; column < puzzle[0].length; column++){
				if(!containsTwoFalses(candidates[row][column]))
					continue;
				/*
				 * Column-wise
				 */
				for(int rowSpan = 0; rowSpan < puzzle.length; rowSpan++){
					if(row != rowSpan && 
					   Arrays.equals(candidates[row][column], candidates[rowSpan][column])){
						for(int rowEliminate = 0; rowEliminate < puzzle.length; rowEliminate++){
							if(rowEliminate == row || rowEliminate == rowSpan)
								continue;
							else{
								for(int candidateFix = 0; candidateFix < candidates[0][0].length; candidateFix++)
									if(!candidates[row][column][candidateFix]){
										return true;
									}
							}
						}
					}
				}
				/*
				 * Row-wise
				 */
				for(int columnSpan = 0; columnSpan < puzzle[0].length; columnSpan++){
					if(column != columnSpan &&
					   Arrays.equals(candidates[row][column], candidates[row][columnSpan])){
						for(int columnEliminate = 0; columnEliminate < puzzle[0].length; columnEliminate++){
							if(columnEliminate == column || columnEliminate == columnSpan)
								continue;
							else{
								for(int candidateFix = 0; candidateFix < candidates[0][0].length; candidateFix++)
									if(!candidates[row][column][candidateFix]){
										return true;
									}
							}
						}
					}
				}
			}
		return false;
	}
	
	public void nakedPairsReduction(){
		for(int row = 0; row < puzzle.length; row++)
			for(int column = 0; column < puzzle[0].length; column++){
				if(!containsTwoFalses(candidates[row][column]))
					continue;
				/*
				 * Column-wise
				 */
				for(int rowSpan = 0; rowSpan < puzzle.length; rowSpan++){
					if(row != rowSpan && 
					   Arrays.equals(candidates[row][column], candidates[rowSpan][column])){
						System.out.printf("NPR: COLUMN NAKED PAIR FOUND FOR (%d, %d) AND (%d, %d)\n", row, column, rowSpan, column);
						for(int rowEliminate = 0; rowEliminate < puzzle.length; rowEliminate++){
							if(rowEliminate == row || rowEliminate == rowSpan)
								continue;
							else{
								for(int candidateFix = 0; candidateFix < candidates[0][0].length; candidateFix++)
									if(!candidates[row][column][candidateFix]){
										candidates[rowEliminate][column][candidateFix] = true;
										System.out.printf("NPR: SET (%d, %d, %d) TO TRUE!\n", rowEliminate, column, candidateFix + 1);
									}
							}
						}
					}
				}
				/*
				 * Row-wise
				 */
				for(int columnSpan = 0; columnSpan < puzzle[0].length; columnSpan++){
					if(column != columnSpan &&
					   Arrays.equals(candidates[row][column], candidates[row][columnSpan])){
						System.out.printf("NPR: COLUMN NAKED PAIR FOUND FOR (%d, %d) AND (%d, %d)\n", row, column, row, columnSpan);
						for(int columnEliminate = 0; columnEliminate < puzzle[0].length; columnEliminate++){
							if(columnEliminate == column || columnEliminate == columnSpan)
								continue;
							else{
								for(int candidateFix = 0; candidateFix < candidates[0][0].length; candidateFix++)
									if(!candidates[row][column][candidateFix]){
										candidates[row][columnEliminate][candidateFix] = true;
										System.out.printf("NPR: SET (%d, %d, %d) TO TRUE!\n", row, columnEliminate, candidateFix + 1);
									}
							}
						}
					}
				}
			}
	}
	
	public boolean containsTwoFalses(boolean[] booleanArray){
		int totalFalses = 0;
		for(boolean state : booleanArray)
			if(!state)
				totalFalses++;
		if(totalFalses == 2)
			return true;
		return false;
	}
	
	public void skyscraperReduction(){
		for(int column = 0; column < puzzle[0].length - 1; column++){
			for(int row = 1; row < puzzle.length - 1; row++){
				for(int candidateIndex = 0; candidateIndex < candidates[row][column].length; candidateIndex++){
					if(!candidates[row][column][candidateIndex]){
						System.out.printf("SSR: FOUND OPEN CANDIDATE AT (%d, %d):%d\n", row, column, candidateIndex);
						for(int rowMatch = puzzle.length - 1; rowMatch > row + 1; rowMatch--){
							if(!candidates[rowMatch][column][candidateIndex]){
								System.out.printf("SSR: FOUND MATCHING CANDIDATE CELL AT (%d, %d):%d\n", rowMatch, column, candidateIndex);
								for(int colMatch = column + 1; colMatch < puzzle[0].length; colMatch++){
									if(!candidates[row][colMatch][candidateIndex] &&
									   !candidates[rowMatch][colMatch][candidateIndex] &&
									   candidates[row - 1][colMatch][candidateIndex])
										break;
									if(!candidates[row - 1][colMatch][candidateIndex] &&
									   !candidates[rowMatch][colMatch][candidateIndex]){
										System.out.printf("SSR: FOUND RIGHT-SIDE PAIR AT H(%d, %d):%d AND L(%d, %d):%d\n", row - 1, colMatch, candidateIndex, rowMatch, colMatch, candidateIndex);
										for(int visibilityColumn = column + 1; visibilityColumn < puzzle[0].length; visibilityColumn++){
											if(isCellVisibleByCell(row, visibilityColumn, row - 1, colMatch)){
												if(!candidates[row][visibilityColumn][candidateIndex]){
													System.out.printf("CELL (%d, %d) VISIBLE BY BOTH, FILLING IN VALUE OF %d\n", row, visibilityColumn, candidateIndex + 1);
													candidates[row][visibilityColumn][candidateIndex] = true;
												}
											}
										}
										break;
									}
								}
							}
						}
					}
				}
			}
		}
	}
	
	
	/*
	 * Checks if the target cell is visible by the host cell, or in the same row, column, or block.
	 */
	public boolean isCellVisibleByCell(int targetCellRow, int targetCellColumn, int hostCellRow, int hostCellColumn){
		if(targetCellRow == hostCellRow)
			return true;
		if(targetCellColumn == hostCellColumn)
			return true;
		if( ((targetCellRow / 3) + 1) == ((hostCellRow / 3) + 1) &&
			((targetCellColumn / 3) + 1) == ((hostCellColumn / 3)+ 1))
			return true;
		return false;
	}
	
	/*
	 * Methods for the displaying of puzzles.
	 */
	public void printPuzzle(){
		for(int i = 0; i < 9; i++){
			System.out.println("");
			for(int k = 0; k < 9; k++)
				System.out.print(puzzle[i][k] + "  ");
		}
	}
	
	public void printCandidatesArray(){
		for(int row = 0; row < puzzle.length; row++)
			for(int column = 0; column < puzzle[row].length; column++){
				System.out.print("(" + row + "," + column + ") ");
				for(boolean state : candidates[row][column]){
					if(state) 
						System.out.print("t,");
					else 
						System.out.print("f,");
				}
				System.out.print("\n");
			}
		System.out.println("\n");
					
	}
}
