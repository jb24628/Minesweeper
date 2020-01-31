To play Minesweeper either
1. put in a number for rows then columns after the program name if in command line in this form:
	java Minesweeper [ROWS] [COLS]
2. put the seed file's directory and name after the program name in command line in this form:
	java Minesweeper seedfile.txt

Formating a seedfile:
1. On the first line, put the number of rows
2. On the second line, put the number of columns
3. On the third line, put the number of mines in the game
4. On each line afterwards, put the coordinate pairs of the mines.
5. Save as a .txt file and put it in the Class's folder, not the same folder as the .java file.

Example text (Makes a 3x3 game with one mine in the top left corner):
3
3
1
0 0

Note:
Rows and columns cannot exceed 10x10.
Use "h" or "help" to show commands
Use "r" or "reveal" to reveal the content of a cell. Numbers indicate the number of mines adjacent to a cell. 
This must be followed by 2 numbers seperated by a space. The same applies for the "guess" and "mark" commands.
Use "g" or "guess" to mark a cell with a questionmark to come back to later
Use "m" or "mark" to mark cells that you know have mines.
