package edu.unl.cse.Sudoku.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.*;

public class SudokuFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private boolean titleScreenShown;
	private boolean gameScreenShown;
	private boolean winScreenShown;
	private int difficulty;
	private long gameTimer;
	private int selectedRow;
	private int selectedCol;
	private static Block[][] currentGame;
	private boolean gameWon;
	static Stack moves;
	boolean editingNotes;

	// private JPanel rootPanel;

	/**
	 * Main execution body
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		// create the window for the game
		SudokuFrame frame = new SudokuFrame();
		generateGame(1);
		printBlockMatrix(currentGame, 9);
	}

	/**
	 * SudokuFrame constructor
	 * Used to define properties of the game window
	 */
	public SudokuFrame() {
		this.setupUI();
		this.setVisible(true);
		this.setFocusable(true);
		this.setSize(400, 400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Method to set up the visual arrangement of the screen
	 */
	public void setupUI() {
		this.setBackground(Color.gray);
		Color[] colors = { Color.red, Color.blue, Color.green };
		this.getContentPane().setLayout(new GridLayout(9, 9));
		// for (Color c: colors) {
		// JPanel panel = new JPanel();
		// panel.setBackground(c);
		// this.getContentPane().add(panel);
		// }
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				JPanel panel = new JPanel();
				panel.setBackground(colors[(r + c) % 3]);
				this.getContentPane().add(panel);
			}
		}
	}

	/**
	 * Method to generate a valid puzzle based on difficulty
	 * 
	 * @param difficulty
	 * @return validIncompletePuzzle
	 */
	public static void generateGame(int difficulty) {
		// TODO:REMOVE ELEMENTS FROM THE MATRIX
		// TODO:CHANGE EDIABLITY OF REMAINING ELEMENTS
		// manually create a correct base puzzle that is correct and always the
		// same
		Block[] r0 = { new Block(1, true), new Block(2, true),
				new Block(3, true), new Block(4, true), new Block(5, true),
				new Block(6, true), new Block(7, true), new Block(8, true),
				new Block(9, true) };
		Block[] r1 = { new Block(4, true), new Block(5, true),
				new Block(6, true), new Block(7, true), new Block(8, true),
				new Block(9, true), new Block(1, true), new Block(2, true),
				new Block(3, true) };
		Block[] r2 = { new Block(7, true), new Block(8, true),
				new Block(9, true), new Block(1, true), new Block(2, true),
				new Block(3, true), new Block(4, true), new Block(5, true),
				new Block(6, true) };
		Block[] r3 = { new Block(2, true), new Block(3, true),
				new Block(4, true), new Block(5, true), new Block(6, true),
				new Block(7, true), new Block(8, true), new Block(9, true),
				new Block(1, true) };
		Block[] r4 = { new Block(5, true), new Block(6, true),
				new Block(7, true), new Block(8, true), new Block(9, true),
				new Block(1, true), new Block(2, true), new Block(3, true),
				new Block(4, true) };
		Block[] r5 = { new Block(8, true), new Block(9, true),
				new Block(1, true), new Block(2, true), new Block(3, true),
				new Block(4, true), new Block(5, true), new Block(6, true),
				new Block(7, true) };
		Block[] r6 = { new Block(3, true), new Block(4, true),
				new Block(5, true), new Block(6, true), new Block(7, true),
				new Block(8, true), new Block(9, true), new Block(1, true),
				new Block(2, true) };
		Block[] r7 = { new Block(6, true), new Block(7, true),
				new Block(8, true), new Block(9, true), new Block(1, true),
				new Block(2, true), new Block(3, true), new Block(4, true),
				new Block(5, true) };
		Block[] r8 = { new Block(9, true), new Block(1, true),
				new Block(2, true), new Block(3, true), new Block(4, true),
				new Block(5, true), new Block(6, true), new Block(7, true),
				new Block(8, true) };

		Block[][] basePuzzle = { r0, r1, r2, r3, r4, r5, r6, r7, r8 };
		do{
			currentGame = basePuzzle;
			makeSwaps();
		}while(!checkGameValid());
	}

	/**
	 * Method to check whether the current arrangement is valid or not.
	 * 
	 * @return true if there are no errors
	 */
	static boolean checkGameValid() {
		// check if the row and column are valid
		if (checkRowValid() == false ||checkColValid() == false ||checkBoxes() == false) {
			return false;
		}
		System.out.println("valid");
		return true;
	}
	/**
	 * Prints out the value of all of the elements in the matrix.
	 * Does not account for null elements.
	 * Size of game must be larger than s x s.
	 * @param game
	 * @param s
	 */
	public static void printBlockMatrix(Block[][] game, int s) {
		//for every row
		for (int r = 0; r < s; r++) {
			//for every column
			for (int c = 0; c < s; c++) {
				//print the value of the element with a space after it
				System.out.print(game[r][c].getValue() + "  ");
			}
			//after each row move to the next line
			System.out.println();
		}
	}

	/**
	 * Checks the if all of the rows are valid. Does NOT account for null boxes
	 * 
	 * @return if every row in the game is valid
	 */
	public static boolean checkRowValid() {
		// for every row
		for (int r = 0; r < 9; r++) {
			// check every element vs every other element in the row
			for (int c = 0; c < 9; c++) {
				for (int c2 = c + 1; c2 < 9; c2++) {
					// if two elements in a row are equal, return false
					if (currentGame[r][c].getValue() == currentGame[r][c2]
							.getValue()) {
						return false;
					}
				}
			}
		}
		// no invalid rows in the game
		return true;
	}

	/**
	 * Checks the if all of the columns are valid. Does NOT account for null
	 * boxes
	 * 
	 * @return if every column in the game is valid
	 */
	public static boolean checkColValid() {
		// for every column
		for (int c = 0; c < 9; c++) {
			// check every element vs every other element in the column
			for (int r = 0; r < 9; r++) {
				for (int r2 = r + 1; r2 < 9; r2++) {
					// if two elements in a column are equal, return false
					if (currentGame[r][c].getValue() == currentGame[r2][c]
							.getValue()) {
						return false;
					}
				}
			}
		}
		// no invalid columns in the game
		return true;
	}

	/**
	 * Checks an all of the 3x3 boxes that must contain the numbers 1-9 each only once
	 * @return true if all boxes are valid
	 */
	public static boolean checkBoxes() {
		//for each of the 3 rows of 3x3 blocks
		for (int r = 0; r < 3; r++) {
			//for each of the 3 columns of 3xx3 blocks
			for (int c = 0; c < 3; c++) {
				//create a temp block that contains only the elements in the 3x3
				Block[][] temp = {
						{ currentGame[3 * r + 0][3 * c + 0],
						  currentGame[3 * r + 0][3 * c + 1],
						  currentGame[3 * r + 0][3 * c + 2] },
						{ currentGame[3 * r + 1][3 * c + 0],
						  currentGame[3 * r + 1][3 * c + 1],
					   	  currentGame[3 * r + 1][3 * c + 2] },
						{ currentGame[3 * r + 2][3 * c + 0],
						  currentGame[3 * r + 2][3 * c + 1],
						  currentGame[3 * r + 2][3 * c + 2] } };
				//if that box is not valid return false
				if (!checkBox(temp)) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Checks an individual 3x3 box to see if it contains the numbers 1-9 each only once.
	 * Parameter box must be 3x3 or larger.
	 * @param box
	 * @return if the passed matrix is valid
	 */
	public static boolean checkBox(Block[][] box) {
		//for every element in the matrix
		for (int i = 0; i < 9; i++) {
			//check against every other element in the matrix
			for (int j = i + 1; j < 9; j++) {
				//if the values of the two elements are the same, return false.
				if (box[i / 3][i % 3].getValue() == box[j / 3][j % 3]
						.getValue()) {
					return false;
				}
			}
		}
		//every element in the box only appears once.
		return true;
	}
	
	/**
	 * Method takes the base puzzle and randomly decides how many times to swap rows and columns.
	 * Directly modifies the currentGame matrix
	 */
	public static void makeSwaps(){
		Random r = new Random();
		int rand1, rand2, swaps = r.nextInt(2);
		// determine how many times to swap columns
		for (int i = 0; i < swaps; i++) {
			// randomly choose 2 columns to swap, where the cols are not the same
			rand1 = r.nextInt(8);
			do {
				rand2 = r.nextInt(8);
			} while (rand1 == rand2);
			swapColumns(rand1, rand2);
			// randomly choose 2 rows to swap, where the rows are not the same
			rand1 = r.nextInt(8);
			do {
				rand2 = r.nextInt(8);
			} while (rand1 == rand2);
			swapRows(rand1, rand2);
		}
	}
	/**
	 * Swaps two columns in the currentGame matrix.
	 * a and b must be 0-8
	 * @param a
	 * @param b
	 */
	public static void swapColumns(int a, int b){
		Block temp;
		//for every row
		for(int r=0; r<9; r++){
			temp= currentGame[r][a];						//save element from col a
			currentGame[r][a]= currentGame[r][b];			//place element from b into a
			currentGame[r][b] = temp;						//place element from a into b
		}
	}
	
	/**
	 * Swaps two rows in the currentGame matrix
	 * a and b must be 0-8
	 * @param a
	 * @param b
	 */
	public static void swapRows(int a, int b){
		Block[] temp = currentGame[a];						//save row a
		currentGame[a] = currentGame[b];					//replace row a with row b
		currentGame[b] = temp;								//replace row b with row a
	}
}
