/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cs4200_Connect;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *
 * @author John
 */
public class Game {
    
    // Prompt for time allowed to make a decision (25s max)
    // Stop search if time-limit is reached
    // Prompt for who is going to move first
    // Detect bad-moves and re-prompt accordingly
    // Alpha-beta pruning to decide the computers move (You input the opponents move)
    /* *Note that, you will need to make some
changes to the alpha-beta pruning introduced in the book. These changes include: an
evaluation function so that you can evaluate non-terminal states and a cut-off test to replace
the terminal test so that your program will return the best solution found so far given a specific
period of time.
    
                      Format
    /*
        1 2 3 4 5 6 7 8     Player vs. Opponent
      A - - - - - - - -        1. e5 d5
      B - - O - - - - -        2. e4 e3
      C - - O - - - - -        3. f4 d4
      D - X O O O X - -        4. e6 e7
      E - - O X X X O -        5. g4 h4
      F - - X X - - - -        6. d6 d3
      G - - - X - - - -        7. d2 c3
      H - - - O - - - -        8. f3 b3 wins
    */
    //         System.out.println((int)System.currentTimeMillis()/1000 & 0xFF);
    
    protected Board board;
    protected char currentPlayer;
    protected int numOfMoves;
    protected boolean gameIsSolved;
    protected boolean imFirst;

    public char getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(char currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
    

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
    
    Game() {
        board = new Board();
        gameIsSolved = false;
    }

    // Initial set-up
    public void initializeGame() {
        System.out.print("Four-in-a-line game\n");
        System.out.println("You will be represented by an 'X', your opponent by an 'O'");
        System.out.print("Who will make the first move, X or O? ");
        Scanner keyboard = new Scanner(System.in);
        String choice = keyboard.next();
        String firstPlayer = findFirstPlayer(choice);
        System.out.println("The first player is: " + firstPlayer);
        currentPlayer = firstPlayer.charAt(0);
        if (currentPlayer == 'X') {
            imFirst = true;
            board.setImFirst(true);
        }
        System.out.println("-----------------------");
        //board.initializeBoard();
        board.printBoard();
        playGame(currentPlayer);
    }
    
    // Returns the first player (X or O)
    public static String findFirstPlayer(String choice) {
        Scanner keyboard = new Scanner(System.in);
        if (choice.equalsIgnoreCase("x") || choice.equalsIgnoreCase("o")) {
            return choice.toUpperCase();
        } else {
            while (!choice.matches("[xoXO]")) {
                System.out.print("Enter a valid first player: ");
                choice = keyboard.next();
            }
        }
        return choice.toUpperCase();
    }
    
    // The loop of taking turns playing the game
    public void playGame(char player) {
        
        while (!gameIsSolved) {
            Scanner keyboard = new Scanner(System.in);
            if (player == 'O') {
                System.out.print("Enter the player's move: ");
                String move = keyboard.next();
                move = checkMove(move);
                board = board.playerMove(move);
                player = 'X';
            } else {
                //System.out.print("Enter a move: ");
                if (numOfMoves == 0) {
                    System.out.println("AI is making a random move.. ");
                } else {
                    System.out.println("AI is moving.. ");
                }
                //String move = keyboard.next();
                //move = checkMove(move);
                //board = board.aiMove(move);
                board = board.aiMove();
                player = 'O';
            }
            
            numOfMoves++;
        }
    }
    
    public String checkMove(String move) {
        Scanner keyboard = new Scanner(System.in);
        while (move.length() != 2 || !move.matches("[a-h][1-8]")) {
            System.out.print("Enter a valid move: ");
            move = keyboard.next();
        }
        while (board.checkIfTaken(move)) {
            System.out.println("Move is taken.  Enter a valid move: ");
            move = keyboard.next();
        }
        return move;
    }
    
  
    
    public static void main(String[] args) {
        Game game = new Game();
        game.initializeGame();
        
        
        
        
        
        
        
        
        
        //System.out.println(game.currentPlayer);
        //b.printBoard();
        //System.out.println((int)System.currentTimeMillis());
        //System.out.println((int)System.currentTimeMillis() + 60*1000);
    }
    
}
