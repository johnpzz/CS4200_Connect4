/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4200_Connect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author John
 */
public class Board {
    private char[][] aiBoard;
    private char[][] gameBoard;      // 8 by 8
    private int evaluationNumber;
    private boolean amIFirst;    //used for printing player vs opponent or opponent vs player
    private int totalTurns;
    private String Xmove;   //ex: c3
    private String Omove;
    private List<Board> successorBoards;

    public List<Board> getSuccessorBoards() {
        return successorBoards;
    }
    private String formattedChoices;
    private char player;

    Board() {
        aiBoard = new char[8][8];
        gameBoard = new char[8][8];
        successorBoards = new ArrayList<>();
        Xmove = "";
        Omove = "";
        formattedChoices = "";
        
        initializeBoard();
    }
    

    public char[][] getBoard() {
        return gameBoard;
    }
    

    public boolean isImFirst() {
        return amIFirst;
    }

    public void setImFirst(boolean imFirst) {
        this.amIFirst = imFirst;
    }
    
    // Prints the current board state
    public void printBoard() {
        System.out.println("Current Game Board:");
        System.out.print("  1 2 3 4 5 6 7 8");
        if (amIFirst == true) {
            System.out.print("\t\tPlayer vs. Opponent\n");
        } else {
            System.out.print("\t\tOpponent vs. Player\n");
        }
        
        System.out.println("Total moves: " + totalTurns);
        
        int aInASCII = 65;
        
        
        
        for (int i = 0; i < gameBoard.length; i++) {
            System.out.print((char) aInASCII + " ");
            for (int j = 0; j < gameBoard[i].length; j++) {
                System.out.print(gameBoard[i][j] + " ");
            }
            
            // Must print the moves some how 
            int num = totalTurns % 2;
            int timesFormatted = 0;
            if (timesFormatted == i && player == 'X') {
                formattedChoices += "\t" + Xmove;
                timesFormatted++;
                System.out.print(formattedChoices);
            } else if (timesFormatted == i && player == 'O') {
                formattedChoices += "\t" + Omove;
                timesFormatted++;
                System.out.print(formattedChoices);
            }

            // Must print the moves here

            aInASCII++;
            System.out.println();
        }
        //formattedChoices += "\n";
        
    }
    
    public void initializeBoard() {
        
        // Initially an empty board with nothing but -'s
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                gameBoard[i][j] = '-';
            }
        } 
    }
    
    
    /*
    public int findXPairs() {
        
    }
    
    public int findOPairs() {
        
    }
    */
    
    // Returns an evaluation number depending on the current state of the board (WIP)
    // evaluation is called on a board (we want to call this on a boards successor list)
    public int evaluation(char currentPlayer) {

        int score = 0;
        int horizontals = findHorizontals();
        int verticals = findVerticals();

        if (horizontals == 1 || verticals == 1) {
            if (currentPlayer == 'X') {
                score += 10;
            } else {
                score -= 10;
            }
        }

        if (horizontals == 2 || verticals == 2) {
            if (currentPlayer == 'X') {
                score += 20;
            } else {
                score -= 20;
            }
        }

        if (horizontals == 3 || verticals == 3) {
            if (currentPlayer == 'X') {
                score += 100;
            } else {
                score -= 100;
            }
        }
        return score;
    }
    
    
    //                 (passed in board, starts at depth 1, isMax?)
    public int minimax(Board board, int depth, boolean isMaxPlayer) {
        System.out.println("Depth: " + depth);
        
        // Stop minimax when depth reaches 1000, we can also stop based on time.
        if (depth >= 1000) {
            gameBoard = board.cloneBoard();
            //board.printBoard();
            // I don't think I ever use this evaluationNumber or even set it (?)
            return board.evaluationNumber;
        }
        char currentPlayer = 'X';
        if (!isMaxPlayer) 
            currentPlayer = 'O';
       
        
        
        // Grab/generate the successor boards list for the board
        board.successorBoards = findSuccessors(currentPlayer);
        List<Board> successorList = board.getSuccessorBoards();
        
        if (successorList.size() == 0) {
            return board.evaluation(currentPlayer);
        }

        int max = Integer.MIN_VALUE;
        int bestBoardIndex = 0;
        if (isMaxPlayer) {
            for (int i = 0; i < successorList.size(); i++) {
                int number = successorList.get(i).evaluation('O');
                // number should be decrementing now..
                if (number >= max) {
                    max = number;
                    bestBoardIndex = i;
                }
            }
            return minimax(successorList.get(bestBoardIndex), depth+1, false);

        } else {
            int low = Integer.MAX_VALUE;
            for (int i = 0; i < successorList.size(); i++) {
                int number = successorList.get(i).evaluation('X');
                if (number <= low) {
                    low = number;
                    bestBoardIndex = i;
                }
            }
            return minimax(successorList.get(bestBoardIndex), depth+1, true);
        }
    }
    
    
    public int findVerticals() {
        int num = 0;
        
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                
                if (gameBoard[i][j] != '-') {
                    
                    if (i-1 >= 0 && gameBoard[i-1][j] == gameBoard[i][j]) {
                        num++;
                    }
                    if (i+1 <= 7 && gameBoard[i+1][j] == gameBoard[i][j]) {
                        num++;
                    }
                    
                }
 
            }
        }
        return num;
    }
    
    
    public int findHorizontals() {
        int num = 0;
        
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                
                if (gameBoard[i][j] != '-') {
                    if (j-1 >= 0 && gameBoard[i][j-1] == gameBoard[i][j]) {
                        num++;
                    }
                    if (j+1 <= 7 && gameBoard[i][j+1] == gameBoard[i][j]) {
                        num++;
                    }
                    
                }
 
            }
        }
        return num;
    }
    
    public Board playerMove(String move) {
        Board nextBoard = new Board();
        nextBoard.gameBoard = cloneBoard();
        nextBoard.amIFirst = amIFirst;
        nextBoard.player = 'O';
        nextBoard.totalTurns = totalTurns;
        nextBoard.totalTurns++;
        
        int row = (int) move.charAt(0) - 97;    //  97 == ASCII a
        int column = Integer.valueOf(String.valueOf(move.charAt(1))) - 1;
        System.out.println(move + " is: [" + row + "," + column + "] in the gameboard array");
        nextBoard.gameBoard[row][column] = 'O';
        //nextBoard.successorBoards = findSuccessors('O');

        nextBoard.printBoard();
        return nextBoard;
    }
    
    public char[][] cloneBoard() {
        char[][] newBoard = new char[8][8];
        
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[0].length; j++) {
                newBoard[i][j] = gameBoard[i][j];
            }
        }
        return newBoard;
    }
    
    /*  The old DIY connect4 aiMove (ur move)
    public Board aiMove(String move) {
        Board nextBoard = new Board();
        nextBoard.gameBoard = cloneBoard();
        nextBoard.amIFirst = amIFirst;
        nextBoard.totalTurns = totalTurns;
        nextBoard.totalTurns++;


        
        int row = (int) move.charAt(0) - 97;    //  97 == ASCII a
        int column = Integer.valueOf(String.valueOf(move.charAt(1))) - 1;
        System.out.println(move + " is: [" + row + "," + column + "] in the gameboard array");
        nextBoard.gameBoard[row][column] = 'X';
        nextBoard.printBoard();
        return nextBoard;
    }
*/
    
    public Board aiMove() {
        Board nextBoard = new Board();
        nextBoard.gameBoard = cloneBoard();
        nextBoard.amIFirst = amIFirst;
        nextBoard.totalTurns = totalTurns;
        nextBoard.player = 'X';
        
        //if no moves and you're first, do a completely random move
        if (nextBoard.totalTurns == 0) {
            int randRow = new Random().nextInt(8);
            int randColumn = new Random().nextInt(8);
            nextBoard.gameBoard[randRow][randColumn] = 'X';
        } else {
            
            //Run the minimax/alpha-beta pruning algorithm on the board to make a move
            nextBoard.successorBoards = findSuccessors('X');
            nextBoard.minimax(nextBoard, 1, true);
            
            
            //Used to print the possible successor boards
            for (Board b : nextBoard.successorBoards) {
                //b.printBoard();
            }
            //
            
        }


        
        //int row = (int) move.charAt(0) - 97;    //  97 == ASCII a
        //int column = Integer.valueOf(String.valueOf(move.charAt(1))) - 1;
        //System.out.println(move + " is: [" + row + "," + column + "] in the gameboard array");
        //nextBoard.gameBoard[row][column] = 'X';
        nextBoard.printBoard();
        
        nextBoard.totalTurns++;
        return nextBoard;
    }
    
    public boolean checkIfTaken(String move) {
        int row = (int) move.charAt(0) - 97;
        int column = Integer.valueOf(String.valueOf(move.charAt(1))) - 1;
        if (gameBoard[row][column] != '-') {
            return true;
        } else {
            return false;
        }
    }
    
    public List<Board> findSuccessors(char currentPlayer) {
        List<Board> successorList = new ArrayList<Board>();
        
        // Loop through board and find used spots
        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                // If we find a taken move spot, check around it
                if (gameBoard[i][j] != '-') {
                    if (i - 1 >= 0) {
                        if (gameBoard[i-1][j] == '-') {
                            // available open spot next to a current move
                            Board potentialBoard = new Board();
                            potentialBoard.gameBoard = cloneBoard();
                            potentialBoard.gameBoard[i-1][j] = currentPlayer;
                            successorList.add(potentialBoard);
                        }
                    }
                    if (i + 1 <= 7) {
                        if (gameBoard[i+1][j] == '-') {
                            // available open spot next to a current move
                            Board potentialBoard = new Board();
                            potentialBoard.gameBoard = cloneBoard();
                            potentialBoard.gameBoard[i+1][j] = currentPlayer;
                            successorList.add(potentialBoard);
                        } 
                    }
                    if (j-1 >= 0) {
                        if (gameBoard[i][j-1] == '-') {
                            // available open spot next to a current move
                            Board potentialBoard = new Board();
                            potentialBoard.gameBoard = cloneBoard();
                            potentialBoard.gameBoard[i][j-1] = currentPlayer;
                            successorList.add(potentialBoard);
                        } 
                    }
                    if (j+1 <= 7) {
                        if (gameBoard[i][j+1] == '-') {
                            // available open spot next to a current move
                            Board potentialBoard = new Board();
                            potentialBoard.gameBoard = cloneBoard();
                            potentialBoard.gameBoard[i][j+1] = currentPlayer;
                            successorList.add(potentialBoard);
                        } 
                    }

                }
            }
        }
    
        return successorList;
    }
 
    
}
