package com.puzzle.andrew.sudowordsandroid.sudoku;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;

import com.puzzle.andrew.sudowordsandroid.R;

import java.util.ArrayList;
import java.util.Random;


//TODO:   BUGS TO SORT
// (1) The hint will not be correct if there is an incorrect entry; fix the method / give warning?
// (2) Landscape not supported; deal with this
// (3) If grid is full, crashes if Hint is pressed -- maybe just do congrats message and close?


/**
 * Created by Andrew on 30/08/2017.
 */
public class Sudoku extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = Sudoku.class.getSimpleName();
    private static final String TAG2 = Sudoku.class.getSimpleName();

    ImageButton sudokuButton;

    private Button checkButton;
    private Button hintButton;

    android.widget.GridLayout clickableGrid;

    private boolean checkPressed = false;
    private boolean hintPressed = false;


    ArrayList<Integer> row, checks;
    ArrayList<Integer> correct;
    ArrayList<ArrayList<Integer>> cols;
    ArrayList<ArrayList<Integer>> boxes;

    // current state of grid
    int[][] grid = new int [9][9];
    // hold solution
    int[][] grid_correct = new int [9][9];


    int x = 11, y = 11;
    Random rand;
    boolean complete = false;

    protected void onCreate(Bundle savedInstanceState) {

        correct = new ArrayList<Integer>();
        row = new ArrayList<Integer>();
        checks = new ArrayList<Integer>();
        boxes = new ArrayList<ArrayList<Integer>>();
        cols = new ArrayList<ArrayList<Integer>>();
        rand = new Random();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku_menu);

        checkButton = (Button) findViewById(R.id.sudokuCheckButton);
        checkButton.setOnClickListener(Sudoku.this);

        hintButton = (Button) findViewById(R.id.sudokuHintButton);
        hintButton.setOnClickListener(Sudoku.this);

        sudokuButton = (ImageButton)findViewById(R.id.crossword);
        generateSudoku(grid);
        makeGrid(grid);
    }









    public void generateSudoku(int[][] grid){
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

                    //field.setText(""+ correct.get(j*(x-2) + i));
                    grid[i][j] = correct.get(j * (x - 2) + i);
                    //grid[i][j] = grid2[i][j];
                }
            }
        }



        // Set correct solution
        for(int i = 0; i < x-2; i++) {
            for (int j = 0; j < y - 2; j++) {
                //field.setText(""+ correct.get(j*(x-2) + i));
                grid_correct[i][j] = grid[i][j];
                //grid[i][j] = grid2[i][j];
            }
        }



    }




    public void makeGrid(int [][] grid2){
        /**
         * Generates starting grid from solution grid
         */

        grid = SudokuMethods.makeEasy(grid2);
        //grid = SudokuMethods.makeMedium(grid);

        android.widget.GridLayout sudGrid = (android.widget.GridLayout) findViewById(R.id.sudokuGrid);

        for(int i = 0; i < x-2; i++){
            for (int j = 0; j < y-2; j++){
                EditText field = (EditText) sudGrid.getChildAt(i * 9 + j);
                field.setBackgroundResource(R.drawable.border_active);
                if(grid[i][j]!=0) {
                    field.setText("" + grid[i][j]);
                    field.setBackgroundResource(R.drawable.border);
                    field.setKeyListener(null);
                }
            }
        }
    }





    @Override
    public void onClick(View view) {
        /**
         * Define function of Hint and Check buttons here
         */

        android.widget.GridLayout sudGrid = (android.widget.GridLayout) findViewById(R.id.sudokuGrid);


        switch ( view.getId() ){


            // Hint button -----------------------
            case R.id.sudokuHintButton:
                Log.d(TAG,"Hint button pressed");

                // Every time hint is pressed, get rid of "check" features (copy and pasted from below)
                checkPressed = false;

                // Reset colours
                for (int i = 0; i < x - 2; i++) {
                    for (int j = 0; j < y - 2; j++) {
                        EditText field = (EditText) sudGrid.getChildAt(i * 9 + j);
                        // Need to update the grid[][] array -  this does not happen upon text entry!
                        // Is there a better way to do this, not just upon button press?
                        if (!String.valueOf(field.getText()).isEmpty()) {
                            grid[i][j] = Integer.parseInt(String.valueOf(field.getText()));
                        }

                        if( field.getKeyListener() == null ){
                            field.setBackgroundResource(R.drawable.border);
                        }
                        else {
                            field.setBackgroundResource(R.drawable.border_active);
                        }
                    }
                }

                if( !hintPressed ) {
                    hintPressed = true;
                    // actual Hint Button functionality
                    int[] hint_xy = SudokuMethods.getHint_singles_hiddenSingles(grid);
                    EditText field2 = (EditText) sudGrid.getChildAt(hint_xy[0] * 9 + hint_xy[1]);   //TODO: double check the [0] and [1] are correct way round
                    field2.setBackgroundColor(getResources().getColor(R.color.sudoku_hint));
                }
                else{
                    hintPressed = false;
                }

                break;



            // Check button -------------------------------------------
            case R.id.sudokuCheckButton:
                Log.d(TAG2,"Check button pressed");

                // Always reset colours when Check is pressed
                for (int i = 0; i < x - 2; i++) {
                    for (int j = 0; j < y - 2; j++) {
                        EditText field = (EditText) sudGrid.getChildAt(i * 9 + j);
                        // Need to update the grid[][] array -  this does not happen upon text entry!
                        // Is there a better way to do this, not just upon button press?
                        if (!String.valueOf(field.getText()).isEmpty()) {
                            grid[i][j] = Integer.parseInt(String.valueOf(field.getText()));
                        }
                        if( field.getKeyListener() == null ){
                            field.setBackgroundResource(R.drawable.border);
                        }
                        else {
                            field.setBackgroundResource(R.drawable.border_active);
                        }
                    }
                }


                if( !checkPressed ) {
                    checkPressed=true;
                    for (int i = 0; i < x - 2; i++) {
                        for (int j = 0; j < y - 2; j++) {

                            EditText field = (EditText) sudGrid.getChildAt(i * 9 + j);

                            // Need to update the grid[][] array -  this does not happen upon text entry!
                            // Is there a better way to do this, not just upon button press?
                            if (!String.valueOf(field.getText()).isEmpty()) {
                                grid[i][j] = Integer.parseInt(String.valueOf(field.getText()));
                            }
                            //field.setBackgroundResource(R.drawable.border_active);
                            if (grid[i][j] == grid_correct[i][j]) {
                                field.setBackgroundColor( getResources().getColor(R.color.sudoku_correct) );
                            }
                            else if(grid[i][j]!=0){  // 0 is set if no number is entered
                                field.setBackgroundColor( getResources().getColor(R.color.sudoku_wrong) );
                            }
                        }
                    }
                }
                else{
                    checkPressed = false;
                }

                break;


            default:
                break;  //is this essential?

        }
        
    }










}