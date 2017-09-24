package com.puzzle.andrew.sudowordsandroid.sudoku;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;

import com.puzzle.andrew.sudowordsandroid.MainMenu;
import com.puzzle.andrew.sudowordsandroid.R;

import java.util.ArrayList;
import java.util.Random;


//TODO:   BUGS TO SORT
// (1) The hint will not be correct if there is an incorrect entry; fix the method / give warning?


/**
 * Created by Andrew on 30/08/2017.
 */
public class Sudoku extends AppCompatActivity implements View.OnClickListener{

    private Button checkButton;
    private Button hintButton;
    private Button saveButton;

    private boolean LOADED;
    private boolean checkPressed = false;
    private boolean hintPressed = false;

    SharedPreferences sharedPref;

    GameState savedGame;
    // Current state of grid
    int[][] grid = new int [9][9];
    int [][] start_grid = new int [9][9];
    // Hold solution
    int[][] grid_correct = new int [9][9];
    int x = 11, y = 11;

    ArrayList<String> saveValues = new ArrayList<>();
    int noGamesSaved;

    protected void onCreate(Bundle savedInstanceState) {


        // Get the start_grid and correct_grid passed
        Bundle extras = getIntent().getExtras();
//        if(extras.getString("difficulty") != null){
//            if(extras.getString("difficulty").equals("loaded")) {
//                LOADED = true;
//            }
//        }
        grid_correct = (int[][]) extras.getSerializable("grid_correct");
        grid = (int[][]) extras.getSerializable("start_grid");
        if(extras.getSerializable("gameLoaded")!= null) {
            LOADED = (boolean) extras.getSerializable("gameLoaded");
        }
        start_grid = grid;

        //Steve: need this plus the android:screenOrientation="portrait" in the xml
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sudoku_menu);

        // Make Hint and Check buttons and listeners
        checkButton = (Button) findViewById(R.id.sudokuCheckButton);
        checkButton.setOnClickListener(Sudoku.this);
        hintButton = (Button) findViewById(R.id.sudokuHintButton);
        hintButton.setOnClickListener(Sudoku.this);
        saveButton = (Button) findViewById(R.id.sudokuSaveButton);
        saveButton.setOnClickListener(Sudoku.this);

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


        if(LOADED){
            sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
            /*if(noGamesSaved == 0){
                System.out.println("There are no saved games");
            }else{*/
            String saved = sharedPref.getString(getString(R.string.code), "");
            savedGame = decodeSavedSudoku(saved);
            start_grid = savedGame.getStartGame();
            grid = savedGame.getMidGame();
            grid_correct = savedGame.getEndGame();
            drawGrid(grid);
        }else{
            drawGrid(grid);
        }
    }


    // STEVE: added this to prevent screen rotation
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void drawGrid(int [][] grid){

        android.widget.GridLayout sudGrid = (android.widget.GridLayout) findViewById(R.id.sudokuGrid);
        for(int i = 0; i < x-2; i++){
            for (int j = 0; j < y-2; j++){
                EditText field = (EditText) sudGrid.getChildAt(i * 9 + j);
                field.setBackgroundResource(R.drawable.border_active);
                if(start_grid[i][j]!=0) {
                    field.setText("" + start_grid[i][j]);
                    field.setBackgroundResource(R.drawable.border);
                    field.setKeyListener(null);
                }else if(grid[i][j]!=0){
                    field.setText("" + grid[i][j]);
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
                else{
                    // Need to reset empties to zero
                    grid[i][j] = 0 ;
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
                    checkPressed=true;

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

                    for (int i = 0; i < x - 2; i++) {
                        for (int j = 0; j < y - 2; j++) {
                            EditText field = (EditText) sudGrid.getChildAt(i * 9 + j);
                            //field.setBackgroundResource(R.drawable.border_active);
                            if (grid[i][j] == grid_correct[i][j]) {
                                field.setBackgroundColor( getResources().getColor(R.color.sudoku_correct) );
                            }
                            else if(grid[i][j]!=0 ){  // 0 is set if no number is entered
                                field.setBackgroundColor( getResources().getColor(R.color.sudoku_wrong) );
                            }
                        }
                    }
                }
                else{
                    checkPressed = false;
                }

                break;


            case R.id.sudokuSaveButton:
                //This is still not working
                /*saveValues.add(createNewSaveString(noGamesSaved+1));
                String code = generateCodedSudoku(start_grid, grid_correct, grid);
                sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(saveValues.get(noGamesSaved), code);
                //need to only increment this if an actual different game has been loaded
                //could use booleans
                if(!LOADED) {
                    noGamesSaved++;
                }else{
                    saveValues.remove(noGamesSaved);
                }
                editor.commit();
                */

                String code = generateCodedSudoku(start_grid, grid_correct, grid);
                sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.code), code);
                editor.commit();

                break;

            default:
                break;  //is this essential?
        }
    }

    public String createNewSaveString(int latest){
        return Integer.toString(latest+1);
    }

    //save game to String
    public String generateCodedSudoku(int start_grid [][], int [][] grid_correct, int [][]grid){
        String savedGame = "";
        for(int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                if(grid[i][j] == 0){
                    savedGame += " ";
                }else if(start_grid[i][j] != 0){
                    char started = (char)(start_grid[i][j]+32);
                    savedGame += started;
                }else if(grid[i][j] == grid_correct[i][j]){
                    char correct = (char)(grid_correct[i][j]+41);
                    savedGame += correct;
                }else{
                    char incorrect;
                    if(grid[i][j] > grid_correct[i][j]){
                        incorrect= (char)(grid[i][j]-1 + (grid_correct[i][j]-1)*8 + 50);
                    }else{
                        incorrect= (char)(grid[i][j] + (grid_correct[i][j]-1)*8 + 50);
                    }

                    savedGame += incorrect;
                }
            }
        }
        return savedGame;
    }

    //Use this to load game eventually
    public GameState decodeSavedSudoku(String savedGame) {
        int[][] gameToLoad = new int[9][9];
        int [][] start_game = new int [9][9];
        int [][] end_game = new int [9][9];
        for (int i = 0; i < savedGame.length(); i++) {
            int row = i/9;
            int col = i%9;
            int characterValue = (int) savedGame.charAt(i);
            if (savedGame.charAt(i) == ' ') {
                gameToLoad[row][col] = 0;
                start_game[row][col] = 0;
                end_game[row][col] = 0;
            } else if (characterValue > 32 && characterValue < 42) {
                gameToLoad[row][col] = characterValue - 32;
                start_game[row][col] = characterValue - 32;
                end_game[row][col] = characterValue - 32;
            } else if (characterValue > 41 && characterValue < 51) {
                gameToLoad[row][col] = characterValue - 41;
                start_game[row][col] = 0;
                end_game[row][col] =  characterValue - 41;
            } else {
                int a = characterValue-50;
                int value = 1 + ( a + (a-1 )/9)%9;
                gameToLoad[row][col] = value;
                start_game[row][col] = 0;
                end_game[row][col] = a/8 + 1;
            }
        }
        return new GameState(start_game, gameToLoad, end_game);
    }


    @Override
    public void onBackPressed() {
        /**
         * Create warning message when back button is pressed
         */

        new AlertDialog.Builder(this)
                .setTitle(R.string.sudoku_backPress_title)
                .setMessage(R.string.sudoku_backPress_message)
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Sudoku.super.onBackPressed();
                    }
                }).create().show();


    }
}