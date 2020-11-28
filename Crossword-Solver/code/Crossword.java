import java.io.*;
import java.util.*;

 public class Crossword{
    private char [][] theBoard;
    private char [][] partialSolution;
    private int boardSize;
    private DictInterface D;
    private final char [] alphabet = {'a','b','c','d','e','f','g','h','i','g','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
    
    /** 
	 * Solve crossword puzzle using recusive backtracking. 
     * Return true if able to find a solution, otherwise unable 
     * to find an acceptable solution so return false. Row and
     * col represent the starting indexes of the board. 
	 * @param col
	 * @param row
	 * @return 
	 */
    private boolean solve(int row, int col){
        //
        // CHECK COMPLETION STATUS
        //  
        boolean isFull= true; 
        for (int i = 0; i < boardSize;i++) { 
            for (int j = 0; j < boardSize; j++) { 
                if (theBoard[i][j] == '+') { 
                    row = i; 
                    col = j; 
                    isFull = false; 
                    break; 
                } 
            } 
            if (!isFull) break;    
        } 
        if (isFull) return true; 
        //
        // BACKTRACK
        //  
        for (char letter : alphabet){
            if (isValid(row, col, letter)){
                //
                // APPLY CHOICE 
                //
                theBoard[row][col] = letter;
                //
                // RECURSE ON CHOICE 
                //
                if (solve(row,col)) { 
                    return true;  
                } else { 
                //
                // UNDO CHOICE
                //
                    theBoard[row][col] = '+'; 
                } 
            }
        }
        return false;
    }

    /** 
	 * Given a specific index on the board and a new letter
     * check if letter fits criteria for validity, if not reject
     * it. Row and col represent the current indexes of the board,
     * nLetter is our 'new choice' that we either accept or reject
	 * @param col
	 * @param row
     * @param nLetter
	 * @return 
	 */
    private boolean isValid(int row, int col, char nLetter){
        StringBuilder curRow = new StringBuilder(boardSize);
        StringBuilder curCol = new StringBuilder(boardSize);
        //
        // UPDATE PARTIAL SOULTION
        //
        for (int i =0; i < boardSize;i++){
            partialSolution[i][col]= theBoard[i][col];
            partialSolution[row][i]= theBoard[row][i];
        }
        partialSolution[row][col] = nLetter;
        for (int i=0;i<boardSize;i++){
            if (partialSolution[i][col] != '+' ){
                if ( i > 0 ){
                    if (partialSolution[i-1][col] != '+') {
                        curCol.append(partialSolution[i][col]);
                    }
                } else {
                    curCol.append(partialSolution[i][col]);
                }
            }
            if (partialSolution[row][i] != '+'){
                if ( i > 0 ){
                    if (partialSolution[row][i - 1] != '+') {
                        curRow.append(partialSolution[row][i]);
                    }
                } else {
                    curRow.append(partialSolution[row][i]);
                }
            }
        }
        boolean blockR =false;
        boolean blockC =false;
        int sColIndex = curCol.lastIndexOf("-");
        int sRowIndex = curRow.lastIndexOf("-");
        int resR = 0;
        int resC = 0;
        // '-' is at END of string 
        if (sColIndex == curCol.length()-1){
            blockC = true;
            resC = D.searchPrefix(curCol, 0, sColIndex - 1);
        // '-' is at START of string 
        } else if (sColIndex == 0){ 
            blockC = true;
            resC = D.searchPrefix(curCol, 1, curCol.length()-1);
        // '-' is NOT in string 
        } else if (sColIndex == -1){ 
            resC = D.searchPrefix(curCol, 0, curCol.length()-1);
        } else if (sColIndex != 0){ 
        // '-' is at MIDDLE of string 
            if (sColIndex > boardSize/2 ){
                resC = D.searchPrefix(curCol, 0, sColIndex - 1);
            } else {
                resC = D.searchPrefix(curCol, sColIndex+1, curCol.length()-1);
            }
        } 

        // '-' is at END of string 
        if (sRowIndex == curRow.length()-1){
            blockR = true;
            resR = D.searchPrefix(curRow, 0, sRowIndex - 1);
        // '-' is at START of string 
        } else if (sRowIndex == 0){ 
            blockR = true;
            resR = D.searchPrefix(curRow, 1, curRow.length()-1);
        // '-' is NOT in string 
        } else if (sRowIndex == -1){ 
            resR = D.searchPrefix(curRow, 0, curRow.length()-1);
        } else if (sRowIndex != 0){ 
        // '-' is at MIDDLE of string 
            if (sRowIndex > boardSize/2 ){
                resR = D.searchPrefix(curRow, 0, sRowIndex - 1);
            } else {
                resR = D.searchPrefix(curRow, sRowIndex + 1, curRow.length()-1);
            }
        }
        /***********FOR DEBUGGING***********/
        // System.out.println( "\n" + "PARTIAL SOLUTION");
        // printBoard(partialSolution);
        // System.out.println( "CUR ROW: " + row + " CUR ROW STRING " + curRow +  " sRowIndex: " + Integer.toString(sRowIndex) + 
        // "\n" + "CUR COL: " + col +  " CUR COL STRING " + curCol+  " sColIndex: " + Integer.toString(sColIndex));
        //
        // CHECK VALIDITY
        //
        if (blockR ){
            if ( resR != 2 && resR != 3 ){
                return false;
            }
        }
        if(col < boardSize-1)  {
            if ( resR != 1 && resR != 3 ){
                return false;
            }
        } else {
            if ( resR != 2 && resR != 3 ){
                return false;
            }
        }
        if (blockC ){
            if ( resC != 2 && resC != 3 ){
                return false;
            }    
        }
        if(row < boardSize -1 ) {
            if ( resC != 1 && resC != 3 ){
                return false;
            }
        } else {
            if ( resC != 2 && resC != 3 ){
                return false;
            }
        } 
        return true;
    }
    /** 
	 * Given a specific board, print its values. The parameter, board, 
     * represents the array we are processing. 
	 * @param board
	 */ 
    public void printBoard(char[][] board){
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }
    /** 
	 * Main method.
	 * @param args
	 * @return 
	 */ 
    public static void main(String [] args) throws IOException
    {
        if (args.length != 2) {
            System.out.println("ERROR: REQUIRES 2 ARGUEMENTS" + "\n"  + "1. DICTIONARY FILE" + "\n"  + "2. BOARD FILE");
            System.exit(0);
        }
        new Crossword(args[0], args[1]);
    }  
     /** 
	 * Main method. f1 is the dictionary file, and f2 is the board file.
     * @param f1
     * @param f2
	 * @return 
	 */ 
    public Crossword(String f1, String f2) throws IOException{
        Scanner fReader;
        File inFile = null;
        //
        // LOAD DICTIONARY
        // 
        Scanner fileScan = new Scanner(new FileInputStream(f1));
        String st;
        D = new MyDictionary();
        while (fileScan.hasNext())
        {
            st = fileScan.nextLine();
            D.add(st);
        }
        fileScan.close();
        //
        // LOAD BOARD 
        //
        inFile = new File(f2);
        fReader = new Scanner(inFile);
        boardSize = Integer.parseInt(fReader.nextLine());
        theBoard = new char[boardSize][boardSize];  
        partialSolution=new char[boardSize][boardSize];  
        for (int i = 0; i < boardSize; i++)
        {
            String rowString = fReader.nextLine();
            for (int j = 0; j < boardSize; j++)
            {
                theBoard[i][j] = rowString.charAt(j);
            }
        }

        fReader.close();
        System.out.println("\n" + "THE BOARD" + "\n");
        printBoard(theBoard);
        //
        // MAIN LOOP
        //
        if ( solve(0, 0)  == false ){
            System.out.println("\n" + "NO SOLUTION EXISTS" + "\n");
        } else {
            System.out.println("\n" + "BOARD RESULTS" + "\n");
            printBoard(theBoard);
        }
    }
}