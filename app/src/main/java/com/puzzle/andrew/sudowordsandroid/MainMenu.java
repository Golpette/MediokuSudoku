package com.puzzle.andrew.sudowordsandroid;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.puzzle.andrew.sudowordsandroid.sudoku.Sudoku;
import com.puzzle.andrew.sudowordsandroid.sudoku.SudokuMethods;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainMenu extends AppCompatActivity implements View.OnClickListener, LoadDialog.NoticeDialogListener {

    Button easyButton, mediumButton, loadButton;

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

    // This list is accessed by the onCreateDialog() method in LoadDialog
    public static List<String> savedGames;

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

        loadButton = (Button) findViewById(R.id.button_saved_games);
        loadButton.setOnClickListener(MainMenu.this);

        // Initialise the list of saved games
        Context context = getApplicationContext();
        savedGames = SavedGames.getSavedGames( context );  // need to re-call this if we start deleting files

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

        correct = new ArrayList<Integer>();
        row = new ArrayList<Integer>();
        checks = new ArrayList<Integer>();
        boxes = new ArrayList<ArrayList<Integer>>();
        cols = new ArrayList<ArrayList<Integer>>();
        rand = new Random();
        // reset solver
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


            case R.id.button_saved_games:
                // Raise the dialog but further puzzle loading has to go into this dialogs onClick() method
                showNoticeDialog();

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

            Intent puzzle = new Intent(MainMenu.this, Sudoku.class);
            Bundle mBundle = new Bundle();
            mBundle.putSerializable("grid_solution", grid_correct );
            mBundle.putSerializable("grid_initialState", start_grid );
            mBundle.putSerializable("grid_currentState", start_grid );       // is this OK? When generating a grid our start grid is our current!
            mBundle.putString("file_loaded", "");  // TODO KEEP THIS IN MIND
            puzzle.putExtras(mBundle);
            startActivity( puzzle );

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








    public void showNoticeDialog() {
        /**
         *  Create an instance of the dialog fragment and show it
         */
        DialogFragment dialog = new LoadDialog();
        dialog.show(  getFragmentManager(), "NoticeDialogFragment");
    }





    @Override
    public void onClick( int which ){
        /**
         *  THE onClick() METHOD FOR THE LIST DIALOG. HERE IS WHERE WE CHOOSE WHAT GAME IS TO BE LOADED!
         *  WE HAVE TO MAKE THE INTENT AND SET-UP THE GAME IN THIS METHOD SINCE THE PROGRAM CONTINUES TO RUN AFTER WE SHOW A DIALOG.
         */

        // File we want to load from LoadDialog
        String gameName = savedGames.get( which );
        // Remove extension
        String gameName_noExt = gameName.substring(  0, gameName.lastIndexOf('.')   );

        // Make alert dialogue for load / delete
        load_delete_choice( gameName_noExt );
        // TODO this is a shit cheat and not user friendly.
        // TODO I want to hold any file in list to bring up a multichoicelist so we can delete multiple files

    }








    public void load_delete_choice( String filename2 ){
        /**
         * Custom onBackPressed to allow passing of Strings
         */

         final String filename = filename2;

        // Use AlertDialog to select file name or exit without saving
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle( "Load or delete?" );


        //Set up buttons. Positive button loads game
        builder.setPositiveButton( "Load", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which ){

                Context context = getApplicationContext();

                Toast.makeText(context, "Loading: " + filename, Toast.LENGTH_SHORT).show();

                // Load game
                // Set initial, current and solution states in mBundle
                String textFromFile = "";
                // Gets the file from the primary **internal** storage space of the current application.
                File testFile = new File( context.getFilesDir(), filename+".dat" );
                if (testFile != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    // Reads the data from the file
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new FileReader(testFile));
                        String line;

                        while ((line = reader.readLine()) != null) {
                            textFromFile += line.toString();
                        }
                        reader.close();
                        Log.d("ReadWriteFile", textFromFile);
                    } catch (Exception e) {
                        //Log.e("eeeeeeReadWriteFile", textFromFile);
                    }
                }

                if( textFromFile.length()!=(81*3) ){
                    Log.d( "INVALID FILE FORMAT", filename  );
                }
                else{
                    // set current state (first 81 digits)
                    for( int i=0; i<81; i++ ){
                        grid[ i/9 ][ i%9 ] = Integer.parseInt(  String.valueOf( textFromFile.charAt(i) )  );
                    }
                    // set initial state
                    for( int i=81; i<162; i++ ){
                        start_grid[ (i-81)/9 ][ (i-81)%9 ] = Integer.parseInt(  String.valueOf( textFromFile.charAt(i) )  );
                    }
                    // set initial state
                    for( int i=162; i<81*3; i++ ){
                        grid_correct[ (i-162)/9 ][ (i-162)%9 ] = Integer.parseInt(  String.valueOf( textFromFile.charAt(i) )  );
                    }
                }

                // Generate the game now!
                Intent puzzle = new Intent(MainMenu.this, Sudoku.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("grid_solution", grid_correct );
                mBundle.putSerializable("grid_initialState", start_grid );
                mBundle.putSerializable("grid_currentState", grid );
                mBundle.putString("file_loaded", filename );
                puzzle.putExtras(mBundle);
                startActivity( puzzle );

            }
        });

        // Negative button deletes game
        builder.setNegativeButton( "Delete" , new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //TODO :  delete the file!

                Context context = getApplicationContext();

                File fdelete = new File( context.getFilesDir(), filename+".dat" );

                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        Toast.makeText(context, "Deleting: " + filename, Toast.LENGTH_SHORT).show();
                        Log.d("FILE DELETION", "Deletion successful");
                    } else {
                        Log.d("FILE DELETION", "Deletion failed");
                    }
                }

                // Update the save game list!
                savedGames = SavedGames.getSavedGames( context );
            }
        });


        builder.show();

    }













}
