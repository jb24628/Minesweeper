package csci1302;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Minesweeper {
	private static int noRounds;
	private static String[][] grid; //initializes what the user sees
	private static int numRows, numCols; //Demensions of the board
	private static int[][] map, cords = new int[10][2]; //initializes the behind the scenes view
	private static int cellsLeft, numMinesLeft, mines;

	public static void disp(String str) { //allows me to not have to type out System.out.println every single time
		System.out.println(str);
	}

	public static void displ(String str) {
		System.out.print(str);
	}

	public static void cantCreate(File seedFile) { //displays message for incorrecly formated files
		disp("Cannot create a game with " + seedFile + ", because it is not formated correctly.\n");
		quit();
	}

	public static void pregame(File seedFile) throws FileNotFoundException { //for file input
		try {
			Scanner kb1 = new Scanner(seedFile);

			if (kb1.hasNextInt()) {
				disp("0");
				numRows = kb1.nextInt();

				if (kb1.hasNextInt()) {
					disp("2");
					numCols = kb1.nextInt();

					if(kb1.hasNextInt()) {
						disp("3");
						mines = kb1.nextInt();
						numMinesLeft = mines;
					}
				}

			}
			else {
				cantCreate(seedFile); //displays error message
			}

			if(!(numRows > 0 && numRows <= 10 && numCols > 0 && numCols <= 10) || mines > numRows * numCols) {
				cantCreate(seedFile); //displays error message
			}

			int count = 0;
			for (int i = 0; i < mines; i++) { //gets coordinates
				int r, c;
				if (kb1.hasNextInt()) {
					r = kb1.nextInt(); //gets row
					count++;

					if (kb1.hasNextInt()) {
						c = kb1.nextInt(); //gets col
						count++;

						if ((r >= 0 && r < numRows) && (c >= 0 && c < numCols)) { //makes sure the coordinate is valid
							cords[i][0] = r;
							cords[i][1] = c;
							count++;
						}
					}
				}
			}
			if (count != 3*mines) {
				cantCreate(seedFile);
			}
		}
		catch (FileNotFoundException ex) {
			disp("Error: File not found/invalid file.");
			quit();
		}
	}

	public static void pregame(int rows, int cols) { //generates random coordinates of mines when there's no file
		if (rows <= 0 || rows > 10 || cols <= 0 || cols > 10) {
			disp("Cannot create a game with that many rows and columns\n");
			quit();
		}

		mines = (int)Math.ceil(rows*cols*.10); //creates a maximum of 10 mines by filling about 10% of the board
		numMinesLeft = mines;

		for (int i = 0; i < 10; i++) { //creates "blank" array without coordinates
			cords[i][0] = -1;
			cords[i][1] = -1;
		}

		for (int i = 0; i< mines; i++) {
			int r = (int)(Math.random()*rows);
			int c = (int)(Math.random()*cols);
			cords[i][0] = r;
			cords[i][1] = c;
		}
	}

	public static void rounds() { //displays the number of rounds
		disp("\nRounds completed: " + noRounds + "\n");
	}

	public static void dispGame(String[][] grid) { //shows the game to the user
		rounds();

		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j<numCols; j++) {
				if (j == 0) {
					displ(i + " |");
				}
				if (grid[i][j].equals("-1")) { //for if the cell is "blank"
					displ("   |");
				}
				else  { //for if the cell is marked 
					displ(" " + grid[i][j] + " |");
				}
				if (j == (numCols-1)) { //this starts a new line when each row is done
					disp("");
				}
			}

			if (i == (numRows-1)) { //for the end of the row
				displ("    ");
				for (int k = 0; k < numCols; k++) {
					displ(k + "   ");
					if (k == (numCols-1)) {
						disp("\n");
					}
				}
			}
		}
	}

	public static String[][] makeBlankGrid() { //Makes the grid that the user sees
		String[][] grid = new String[numRows][numCols];

		for (int i = 0; i<numRows; i++) {
			for (int j = 0; j<numCols; j++) {
				grid[i][j] = "-1";
			}
		}
		
		return grid;
	}

	public static int countMines(int row, int col) { //counts the number of mines in a cell
		int numMines = 0;

		//There is not if/else if statement for a 1x1 game because there is no need to count it because we know that a mine is in the only cell
		if (numRows == 2 && numCols == 1) { //counts mines in adjacent cell of 2x1 game
			int otherRow = Math.abs(1-row);
			if (map[otherRow][0] == -1) {
				numMines++;
			}
		}
		else if (numRows == 1 && numCols == 2) { //counts mines in adjacent cell of 1x2 game
			int otherCol = Math.abs(1-col);
			if (map[0][otherCol] == -1) {
				numMines++;
			}
		}
		//Everything below checks cells in games with 2 rows, and 2 cols and large
		else if (row == 0 && col == 0) { //checks top left corner
			if (map[1][0] == -1) {
				numMines++;
			}
			if (map[1][1] == -1) {
				numMines++;
			}
			if (map[0][1] == -1) {
				numMines++;
			}
		}

		else if (row == 0 && col == numCols-1) { //checks top right corner
			if (map[0][numCols-2] == -1) {
				numMines++;
			}
			if (map[1][numCols-2] == -1) {
				numMines++;
			}
			if (map[0][numCols-1] == -1) {
				numMines++;
			}
		}

		else if (row == numRows-1 && col == 0) { //checks bottom left corner
			if (map[numRows-2][0] == -1) {
				numMines++;
			}
			if (map[numRows-2][1] == -1) {
				numMines++;
			}
			if (map[numRows-1][1] == -1) {
				numMines++;
			}
		}

		else if (row == numRows-1 && col == numCols-1) { //checks bottom right corner
			if (map[numRows-2][numCols-1] == -1) {
				numMines++;
			}
			if (map[numRows-2][numCols-2] == -1) {
				numMines++;
			}
			if (map[numRows-1][numCols-2] == -1) {
				numMines++;
			}
		}

		else if (row == 0) { //checks first row except the corners
			if (map[0][col-1] == -1) {
				numMines++;
			}
			if (map[0][col+1] == -1) {
				numMines++;
			}
			for (int i = 0; i < 3; i++) {
				if (map[1][col-1+i] == -1) { //goes through the adjacent cells bellow
					numMines++;
				}
			}
		}

		else if (row == numRows-1) { //checks bottom row except the corners
			if (map[numRows-1][col-1] == -1) {
				numMines++;
			}
			if (map[numRows-1][col+1] == -1) {
				numMines++;
			}
			for (int i = 0; i < 3; i++) {
				if (map[numRows-2][col-1+i] == -1) { //goes through the adjacent cells bellow
					numMines++;
				}
			}
		}

		else if (col == 0) { //checks most left column except the corners
			if (map[row-1][0] == -1) {
				numMines++;
			}
			if (map[row+1][0] == -1) {
				numMines++;
			}
			for (int i = 0; i < 3; i++) {
				if (map[row-1+i][1] == -1) { //goes through the adjacent cells bellow
					numMines++;
				}
			}
		}

		else if (col == numCols-1) { //checks most right column except the corners
			if (map[row-1][numCols-1] == -1) {
				numMines++;
			}
			if (map[row+1][numCols-1] == -1) {
				numMines++;
			}
			for (int i = 0; i < 3; i++) {
				if (map[row-1+i][numCols-2] == -1) { //goes through the adjacent cells bellow
					numMines++;
				}
			}
		}

		else { //for all the interior cells
			for (int i = 0; i < 3; i++) { //checks the cells in the previoous row
				if (map[row-1][col-1+i] == -1) {
					numMines++;
				}
			}
			if (map[row][col-1] == -1) { //checks to the left
				numMines++;
			}
			if (map[row][col+1] == -1) { //checks to the right
				numMines++;
			}
			for (int i = 0; i < 3; i++) { //checks cells in next row
				if (map[row+1][col-1+i] == -1) {
					numMines++;
				}
			}
		}
		return numMines;
	}

	public static void makeMap() { //Makes "answer key" that the user can't see
		map = new int[numRows][numCols];

		int r1, c1;
		for (int i = 0; i<mines; i++) {
			r1 = cords[i][0]; //getting row value of cordinate to add to map
			c1 = cords[i][1]; //getting col value of cordinate to add to map
			map[r1][c1] = -1; //-1 indicates a mine
		}

		int numMinesAdj; //for the number of mines that are adjacent to the cell
		for (int i = 0; i<numRows; i++) {
			for (int j = 0; j<numCols; j++) {
				if (map[i][j] != -1) { //ensures that we are not counting mines adjacent to mines
					numMinesAdj = countMines(i,j);
					map[i][j] = numMinesAdj; //This fills the "answer key" with numbers that the player can't see unless they reveal a coordinate
				}
			}
		}
	}



	public static String[] rmvWhtSpc(String str) { //breaks what was typed in the command line into parts
		String[] command = {"","",""}; //place holder
		int spot = 0; //to keep track of which index of command to add to

		for (int i = 0; i < str.length(); i++) {
			String sub = str.substring(i, i+1);
			if (sub.equals("0") || sub.equals("1") || sub.equals("2") || sub.equals("3") || sub.equals("4") || 
					sub.equals("5") || sub.equals("6") || sub.equals("7") || sub.equals("8") || sub.equals("9")) 
			{
				spot++;
				command[spot] = sub;
			}
			else if (spot == 0 && !sub.equals(" ")) { //adds letters of command to the array index for the keyword
				command[0] = command[0] + sub;
			}
		}
		return command;
	}

	public static void quit() { //ends game
		System.exit(0);
	}

	public static void startUp() { //start up message
		disp("          _\r\n" + 
				"    /\\/\\ (_)_ __   ___  _____      _____  ___ _ __   ___ _ __\r\n" + 
				"   /    \\| | '_ \\ / _ \\/ __\\ \\ /\\ / / _ \\/ _ \\ '_ \\ / _ \\ '__|\r\n" + 
				"  / /\\/\\ \\ | | | |  __/\\__ \\\\ V  V /  __/  __/ |_) |  __/ |\r\n" + 
				"  \\/    \\/_|_| |_|\\___||___/ \\_/\\_/ \\___|\\___| .__/ \\___|_|\r\n" + 
				"                                       ALPHA |_| EDITION\n");
	}

	public static void gameOver() { //message for losing
		disp("    Oh no... You revealed a mine!\r\n" + 
				"    __ _  __ _ _ __ ___   ___    _____   _____ _ __\r\n" + 
				"   / _` |/ _` | '_ ` _ \\ / _ \\  / _ \\ \\ / / _ \\ '__|\r\n" + 
				"  | (_| | (_| | | | | | |  __/ | (_) \\ V /  __/ |\r\n" + 
				"   \\__, |\\__,_|_| |_| |_|\\___|  \\___/ \\_/ \\___|_|\r\n" + 
				"   |___/");
		quit();
	}

	public static void youWin() { //message for wininng
		int score = numRows * numCols - mines - noRounds;
		disp("\nCONGRADULATIONS!\nYOU HAVE WON!\nSCORE: " + score);
		quit();
	}

	public static void help() { //I need somebody, not just anybody
		disp("\n  Commands Available...\r\n" + 
				"   -   Reveal: r/reveal row col\r\n" + 
				"   -   Mark: m/mark   row col\r\n" + 
				"   -   Guess: g/guess  row col\r\n" + 
				"   -   Help: h/help\r\n" + 
				"   -   Quit: q/quit\r\n\n" +
				"   -   Note: If you put in cordinates that exceed the bounds of the grid, you will recieve an error. And the number of bombs left will still go down if you incorrectly mark it.");
	}

	public static void mark(int r, int c) { //lets the users note where bombs are

		if (map[r][c] == -1) { //ensures that the the game won't end early from a player falsely claiming that cells contain mines
			if (!grid[r][c].equals("F")) { //to ensure that the game doesn't end early if you already chose that cell
				numMinesLeft--;
			}
		}
		grid[r][c] = "F";
	}

	public static void unmark(int r, int c) { //lets user change their mind on if a cell has a mine
		if (map[r][c] == -1) { //to correct the number of mines if a player unmarks a cell but it actually does have a mine
			numMinesLeft++;
		}
		grid[r][c] = "-1";
	}

	public static void guess(int r, int c) { //allows player to leave a question mark for cells they are unsure of
		grid[r][c] = "?";
	}

	public static void reveal(int r, int c) { //lets player know how many adjacent cells have mines....or kills them if they hit one
		if (map[r][c] == -1) {
			gameOver();
		}
		else {
			grid[r][c] = "" + map[r][c]; //casts the number from the "answer key" to what the user can see
		}
	}

	public static void execute(String str, int r, int c) { //runs the commands
		if (str.equals("mark") || str.equals("m")) {
			if (!grid[r][c].equals("F")) {
				mark(r,c);
				cellsLeft--;
			}
			else {
				unmark(r,c);
				cellsLeft++;
			}
		}
		else if (str.equals("help") || str.equals("h")) {
			help();
		}
		else if (str.contentEquals("guess") || str.contentEquals("g")) {
			guess(r,c);
		}
		else if (str.contentEquals("reveal") || str.contentEquals("r")) {
			reveal(r,c);
			cellsLeft--;
		}
		else if (str.contentEquals("quit") || str.contentEquals("q")) {
			disp("Bye! Have a beautiful time!");
			quit();
		}
		else {
			disp("\nCommand not recognized!");
		}
	}

	public static void run() { // the part of the code that is looped or the game
		Scanner kb = new Scanner(System.in);
		dispGame(grid);

		displ("minesweeper-alpha$ ");
		String input = kb.nextLine();

		try { //Makes the game not exit when a number is entered as the command
			String[] commands = rmvWhtSpc(input);

			try { //Makes the game not exit when a letter is entered for coordinates
				int commandCordR = Integer.parseInt(commands[1]);//strToInt(commands[1]);
				int commandCordC = Integer.parseInt(commands[2]);//strToInt(commands[2]);

				execute(commands[0],commandCordR, commandCordC);
				if (cellsLeft == 0 || numMinesLeft == 0) {
					dispGame(grid);
					youWin();
				}
			}
			catch (NumberFormatException z) { //for when a letter is entered as the coordinates
				disp("Command not recognized!");
			}
		}
		catch (ArrayIndexOutOfBoundsException x) { // for when a number is entered as a command
			disp("Command not recognized!");
		}
		noRounds++;
	}

	public static void bounds(String[] args) { //this tells the game to either make a random game or use a seedfile
		switch (args.length) {

		case 2: //random game
			try {
				numRows = Integer.parseInt(args[0]);
				numCols = Integer.parseInt(args[1]);
				pregame(numRows, numCols);
			}
			catch (NumberFormatException x) {
				disp("Please make sure that if you are trying to make a random game, that the two arguments are numbers.");
			} 

		case 1: //file
			String fileName = args[0];
			File file = new File(fileName);
			if (file.isFile()) {
				try {
					pregame(file);
				}
				catch (FileNotFoundException fnf) {
					disp("Error: File not found.");
					quit();
				}
			}
		}
	}

	public static void main(String[] args) {
		bounds(args);

		makeMap();
		grid = makeBlankGrid();
		startUp();
		disp("Total number of mines: " + mines);

		boolean done = false;

		if (numRows < 1 || numRows > 10 || numCols < 1 || numCols > 10) {
			disp("\nCannot create a mine field with that many rows and/or columns!\nIf you're usinga seedfile, make sure it is formatted correctly.");
			quit();
		}

		cellsLeft = numRows * numCols;

		while (!done) { //loops game
			run();
		}
	}
}