package com.puzzle.andrew.sudowordsandroid.sudoku;

/**
 * Created by Andrew on 17/09/2017.
 */

public class GameState {


    int [][] start_game;
    int [][] mid_game;
    int [][] end_game;

    public GameState(int [][] start_game, int [][] mid_game, int [][] end_game){
     //   start_game = new int[9][9];
      //  mid_game = new int[9][9];
      //  end_game = new int[9][9];
        this.start_game = start_game;
        this.mid_game = mid_game;
        this.end_game = end_game;
    }



}
