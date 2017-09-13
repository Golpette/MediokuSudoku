package com.puzzle.andrew.sudowordsandroid.sudoku;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.puzzle.andrew.sudowordsandroid.R;

import java.util.ArrayList;
import java.util.Random;


//TODO:   BUGS TO SORT
// (1) The hint will not be correct if there is an incorrect entry; fix the method / give warning?


/**
 * Created by Andrew on 30/08/2017.
 */
public class Sudoku extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = Sudoku.class.getSimpleName();
    private static final String TAG2 = Sudoku.class.getSimpleName();

    ImageButton sudokuButton;

    private Button checkButton;
    private Button hintButton;

    private boolean checkPressed = false;
    private boolean hintPressed = false;

    ArrayList<Integer> row, checks;
    ArrayList<Integer> correct;
    ArrayList<ArrayList<Integer>> cols;
    ArrayList<ArrayList<Integer>> boxes;

    // Current state of grid
    int[][] grid = new int [9][9];
    // Hold solution
    int[][] grid_correct = new int [9][9];

    int x = 11, y = 11;
    Random rand;
    boolean complete = false;




    protected void onCreate(Bundle savedInstanceState) {

        //Steve: need this plus the android:screenOrientation="portrait" in the xml
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        correct = new ArrayList<Integer>();
        row = new ArrayList<Integer>();
        boxes = new ArrayList<ArrayList<Integer>>();
        cols = new ArrayList<ArrayList<Integer>>();
        rand = new Random();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku_menu);

        // Make Hint and Check buttons and listeners
        checkButton = (Button) findViewById(R.id.sudokuCheckButton);
        checkButton.setOnClickListener(Sudoku.this);
        hintButton = (Button) findViewById(R.id.sudokuHintButton);
        hintButton.setOnClickListener(Sudoku.this);

        //Create a TextWatcher for input to addTextChangedListener() to hide keypad on number entry
        GridLayout layout = (GridLayout)findViewById(R.id.sudokuGrid);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View v = layout.getChildAt(i);
            if (v instanceof EditText) {
                final EditText et = (EditText) v;
                TextWatcher tw = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(et.getApplicationWindowToken(), 0);
                    }
                };
                et.addTextChangedListener( tw );

            }
        }

        // ???
        sudokuButton = (ImageButton)findViewById(R.id.crossword);

        //Make the puzzle!
        long startTime = System.currentTimeMillis();
        generateSudoku(grid);
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        System.out.println("Time elapsed to generate sudoku: " + elapsedTime +"ms");

        makeGrid(grid);
    }


    // STEVE: added this to prevent screen rotation
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }



    // Generate the full (solution) grid
    public void generateSudoku(int[][] grid){
        /**
         * Generates full solution grid
         */




        boxes.clear();
        cols.clear();
        row.clear();
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
                    }else{
                        if(!cols.isEmpty()){
                            int temp = (rand.nextInt(bound));
                            int insertion = row.get(temp);
                            correct.add(insertion);
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
                    grid_correct[i][j] = correct.get(j * (x - 2) + i);
                }
            }
        }
    }




    public void makeGrid(int [][] grid2){
        /**
         * Generates starting grid from solution grid
         */

        //grid = SudokuMethods.makeEasy(grid2);
        //grid = SudokuMethods.makeMedium(grid2); // DO NOT USE THIS
        grid = SudokuMethods.makeMedium2(grid2); /// Steve: quick fix to make medium puzzles more efficient
        //grid = SudokuMethods.makeMedium3(grid2); /// Steve: removing numbers 1 at a time


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

        // Update grid[][] every time a button is pressed, does not happen upon text entry.
        // Is there a better way to do this?
        android.widget.GridLayout sudGrid = (android.widget.GridLayout) findViewById(R.id.sudokuGrid);
        for (int i = 0; i < x - 2; i++) {
            for (int j = 0; j < y - 2; j++) {
                EditText field = (EditText) sudGrid.getChildAt(i * 9 + j);
                if (  !String.valueOf(field.getText()).isEmpty()  ) {
                    grid[i][j] = Integer.parseInt(String.valueOf(field.getText()));
                }
            }
        }


        boolean gridFull = true;
        boolean gridCorrect = true;

        // Check if grid is full or correct
        for (int i = 0; i < x - 2; i++) {
            for (int j = 0; j < y - 2; j++) {
                EditText field = (EditText) sudGrid.getChildAt(i * 9 + j);
                //field.setBackgroundResource(R.drawable.border_active);
                if (grid[i][j] != grid_correct[i][j]) {
                    gridCorrect = false;
                }
                if(grid[i][j]==0){  // 0 is set if no number is entered
                    gridFull = false;
                }
            }
        }



        switch ( view.getId() ){


            // Hint button -----------------------
            case R.id.sudokuHintButton:
                // Every time hint is pressed, get rid of "check" features (copy and pasted from below)
                checkPressed = false;

                // Reset colours
                for (int i = 0; i < x - 2; i++) {
                    for (int j = 0; j < y - 2; j++) {
                        EditText field = (EditText) sudGrid.getChildAt(i * 9 + j);
                        if( field.getKeyListener() == null ){
                            field.setBackgroundResource(R.drawable.border);
                        }
                        else {
                            field.setBackgroundResource(R.drawable.border_active);
                        }
                    }
                }

                if( !hintPressed && !gridFull ) {
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
                hintPressed = false;

                // Always reset colours when Check is pressed
                for (int i = 0; i < x - 2; i++) {
                    for (int j = 0; j < y - 2; j++) {
                        EditText field = (EditText) sudGrid.getChildAt(i * 9 + j);
                        if( field.getKeyListener() == null ){
                            field.setBackgroundResource(R.drawable.border);
                        }
                        else {
                            field.setBackgroundResource(R.drawable.border_active);
                        }
                    }
                }

                if( !checkPressed ) {
                    // Display congrats message upon completion
                    if( gridCorrect){
                        // Make snackbar
                        Snackbar mSnackbar = Snackbar.make(view, R.string.sudoku_congrats, Snackbar.LENGTH_LONG);
                        // Get snackbar view
                        View mView = mSnackbar.getView();
                        // Get textview inside snackbar view
                        TextView mTextView = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
                        // Center the message
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        else
                            mTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                        mSnackbar.show();
                    }

                    checkPressed=true;
                    for (int i = 0; i < x - 2; i++) {
                        for (int j = 0; j < y - 2; j++) {
                            EditText field = (EditText) sudGrid.getChildAt(i * 9 + j);
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