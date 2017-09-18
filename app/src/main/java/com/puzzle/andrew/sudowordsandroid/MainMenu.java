package com.puzzle.andrew.sudowordsandroid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;


import com.puzzle.andrew.sudowordsandroid.sudoku.Sudoku;
import com.puzzle.andrew.sudowordsandroid.sudoku.SudokuMethods;

import java.util.ArrayList;
import java.util.Random;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    Button easyButton, mediumButton;
    public static ProgressBar progressBar; // Want this accessible from other activity - is this the right way??


    // STEVE: TEST MAKING SUDOKU HERE IN ASYNC TASK ==========================================
    private String difficulty;

    ArrayList<Integer> row, checks, correct;
    ArrayList<ArrayList<Integer>> cols, boxes;

    // Current state of grid
    int[][] grid = new int [9][9];
    // Hold solution
    int[][] grid_correct = new int [9][9];
    int[][] start_grid = new int[9][9];

    int x = 11, y = 11;
    Random rand;
    boolean complete = false;
    //=========================================================================================




        @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Steve: need this plus the android:screenOrientation="portrait" in the xml
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressBar =(ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        easyButton = (Button) findViewById(R.id.button_easy);
        easyButton.setOnClickListener(MainMenu.this);

        mediumButton = (Button) findViewById(R.id.button_medium);
        mediumButton.setOnClickListener(MainMenu.this);
    }




    // STEVE: Added this to stop screen rotation; force portrait
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }




    @Override
    public void onClick(View view) {

        // CAREFUL: MAYBE NEED TO RESET ALL ARRAYS AND LISTS HERE??????????????

        progressBar.setVisibility(View.VISIBLE);


        correct = new ArrayList<Integer>();
        row = new ArrayList<Integer>();
        checks = new ArrayList<Integer>();
        boxes = new ArrayList<ArrayList<Integer>>();
        cols = new ArrayList<ArrayList<Integer>>();
        rand = new Random();
        complete = false;


        switch(view.getId()) {

            case R.id.button_easy:
                // Make progressBar visible
                progressBar.setVisibility(View.VISIBLE);

                difficulty = "easy";
                new PuzzleGeneration().execute();

                break;


            case R.id.button_medium:
                // Make progressBar visible
                progressBar.setVisibility(View.VISIBLE);

                difficulty = "medium";
                new PuzzleGeneration().execute();

                break;
        }

    }










    private class PuzzleGeneration extends AsyncTask<Object, Object, Void> {

        @Override
        protected Void doInBackground(Object... objects) {

            // Make full grid
            grid_correct = generateSudoku(grid);
            //Make the puzzle!
            start_grid = makeGrid(grid, difficulty);

            Intent medium = new Intent(MainMenu.this, Sudoku.class);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("grid_correct", grid_correct);
            mBundle.putSerializable("start_grid", start_grid);
            medium.putExtras(mBundle);
            startActivity( medium );

            return null;
        }

        @Override
        protected void onPreExecute() {
         }

        @Override
        protected void onProgressUpdate(Object... values) {}
    }
















    // Generate the full (solution) grid
    public int[][] generateSudoku(int[][] grid){
        /**
         * Generates full solution grid
         */
        boxes.clear();
        cols.clear();
        row.clear();
        checks.clear();
        correct.clear();


        if(!complete){
            for (int i = 0; i < x-2; i++){
                int bound = 9;

                ArrayList<Integer> tempRow = new ArrayList<Integer>();
                ArrayList<Integer> tempBox = new ArrayList<Integer>();
                for (int j = 0; j < y-2; j++){
                    if(j == 0){
                        for(int k = 1; k < 10; k++){
                            row.add(k);
                            if(i == 0){
                                ArrayList<Integer> box = new ArrayList<Integer>();
                                boxes.add(box);
                                ArrayList<Integer> temps = new ArrayList<Integer>();
                                cols.add(temps);
                            }
                        }
                    }
                    tempRow.clear();
                    tempBox.clear();
                    if(!cols.isEmpty()){
                        for(Integer a : cols.get(j)){
                            if(row.contains(a) && bound > 0){
                                tempRow.add(a);
                                row.remove(a);
                                bound--;
                            }
                        }
                    }
                    if(!boxes.isEmpty()){
                        for(Integer b : boxes.get((i/3)*3+(j/3))){
                            if(row.contains(b) && bound > 0){
                                tempBox.add(b);
                                row.remove(b);
                                bound--;
                            }
                        }
                    }
                    if(row.isEmpty()){
                        if(i != 8 || j != 8){
                            generateSudoku(grid);
                        }else{
                            complete = true;
                            break;
                        }
                    }
                    if(bound == 0){
                        complete = true;
                        break;
                        //generateSudoku();
                    }else{
                        if(!cols.isEmpty()){
                            int temp = (rand.nextInt(bound));
                            int insertion = row.get(temp);
                            //numbers[i][j].setText(""+insertion);
                            correct.add(insertion);
                            checks.add(row.get(temp));
                            cols.get(j).add(row.get(temp));
                            boxes.get(((i/3)*3)+(j/3)).add(row.get(temp));
                            row.remove(row.get(temp));
                            bound--;
                            for(Integer a: tempRow){
                                if(!row.contains(a)){
                                    row.add(a);
                                    bound++;
                                }
                            }
                            for(Integer b: tempBox){
                                if(!row.contains(b)){
                                    row.add(b);
                                    bound++;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(!correct.isEmpty()){
            for(int i = 0; i < x-2; i++) {
                for (int j = 0; j < y - 2; j++) {
                    grid[i][j] = correct.get(j * (x - 2) + i);
                }
            }
        }



        // Set correct solution
        for(int i = 0; i < x-2; i++) {
            for (int j = 0; j < y - 2; j++) {
                grid_correct[i][j] = grid[i][j];
            }
        }

        return grid_correct;

    }




    public int[][] makeGrid(int [][] grid2, String diff){
        /**
         * Generates starting grid from solution grid
         */
        int[][] start_grid = new int[9][9];

        if( diff.equals("easy" ) ){
            start_grid = SudokuMethods.makeEasy(grid2);
        }
        else if( diff.equals("medium") ){
            start_grid = SudokuMethods.makeMedium2(grid2);
        }

        return start_grid;


    }

















}
