package com.puzzle.andrew.sudowordsandroid.wordsearch;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.puzzle.andrew.sudowordsandroid.R;

import java.io.IOException;
import java.util.Random;

/**
 * Created by Andrew on 30/08/2017.
 */
public class Wordsearch extends AppCompatActivity{
    //Context context = getBaseContext();
    //Context context = getApplicationContext();
    Context context = Wordsearch.this;
    String dictionary1 = "words_cambridge.txt";
    String dictionary2 = "words_cambridge.txt";
    Random rand = new Random();
    String randomFill = "AAAAAAAAABBCCDDDDEEEEEEEEEEEFFGGGHHIIIIIIIIIJKLLLLMMNNNNNNOOOOOOOOPPQRRRRRRSSSSTTTTTTUUUUVVWWXYYZ";
    String[][] grid = new String [9][9];
    int x = 8, y = 8;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordsearch_menu);

        try {
            WordSearchGenerator wordsearch = new WordSearchGenerator(context, 6, 2, dictionary1, dictionary2, "English", "English", "None");
            grid = wordsearch.grid;
        }catch(IOException ex){

        }

        makeGrid(grid);
    }


      public void makeGrid(String[][] grid){
        /**
         * Generates starting grid from solution grid
         */

        //Generate Wordsearch here
        //grid = SudokuMethods.makeEasy(grid2);

        android.widget.GridLayout wordsearchGrid = (android.widget.GridLayout) findViewById(R.id.wordsearchGrid);

        for(int i = 0; i < x-2; i++){
            for (int j = 0; j < y-2; j++){
                EditText field = (EditText) wordsearchGrid.getChildAt(j * 6 + i);
                field.setBackgroundResource(R.drawable.border_active);
                field.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                if(!grid[i+1][j+1].equals("_")) {
                    field.setText("" + grid[i+1][j+1].toUpperCase());
                    field.setBackgroundResource(R.drawable.border);
                    field.setKeyListener(null);
                }else{
                    field.setText(Character.toString(randomFill.charAt(rand.nextInt(randomFill.length()))));
                    //field.setBackgroundColor(Color.LTGRAY);
                    field.setBackgroundResource(R.drawable.border2);
                }
            }
        }
    }

}