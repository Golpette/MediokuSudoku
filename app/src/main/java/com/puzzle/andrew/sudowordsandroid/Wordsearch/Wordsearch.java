package com.puzzle.andrew.sudowordsandroid.wordsearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.puzzle.andrew.sudowordsandroid.R;

/**
 * Created by Andrew on 30/08/2017.
 */
public class Wordsearch extends AppCompatActivity{

    String dictionary1 = "words_cambridge.txt";
    String dictionary2 = "words_cambridge.txt";
    int[][] grid = new int [9][9];
    int x = 8, y = 8;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordsearch_menu);

        //WordSearchGenerator wordsearch = new WordSearchGenerator(6, 4, dictionary1, dictionary2, );
        makeGrid(grid);
    }


      public void makeGrid(int [][] grid2){
        /**
         * Generates starting grid from solution grid
         */

        //Generate Wordsearch here
        //grid = SudokuMethods.makeEasy(grid2);

        android.widget.GridLayout wordsearchGrid = (android.widget.GridLayout) findViewById(R.id.wordsearchGrid);

        for(int i = 0; i < x-2; i++){
            for (int j = 0; j < y-2; j++){
                EditText field = (EditText) wordsearchGrid.getChildAt(i * 9 + j);
                field.setBackgroundResource(R.drawable.border_active);
                if(grid[i][j]!=0) {
                    field.setText("" + grid[i][j]);
                    field.setBackgroundResource(R.drawable.border);
                    field.setKeyListener(null);
                }
            }
        }
    }

}