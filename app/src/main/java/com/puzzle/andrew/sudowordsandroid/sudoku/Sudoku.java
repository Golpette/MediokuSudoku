package com.puzzle.andrew.sudowordsandroid.sudoku;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.puzzle.andrew.sudowordsandroid.MainMenu;
import com.puzzle.andrew.sudowordsandroid.R;
import com.puzzle.andrew.sudowordsandroid.SavedGames;

import java.io.File;
import java.io.FileOutputStream;



//TODO:   BUGS TO SORT
// (1) The hint will not be correct if there is an incorrect entry; fix the method / give warning?


/**
 * Created by Andrew on 30/08/2017.
 */
public class Sudoku extends AppCompatActivity implements View.OnClickListener{

    private Button moraleButton, hintButton;

    private boolean hintPressed = false;

    final static int GRID_SIZE = MainMenu.GRID_SIZE;

    // Current state of grid
    int[][] grid = new int [GRID_SIZE][GRID_SIZE];
    // Hold solution
    int[][] grid_correct = new int [GRID_SIZE][GRID_SIZE];
    // Initial puzzle's state
    int[][] grid_initial_state = new int[GRID_SIZE][GRID_SIZE];

    String saveFileName = "", file_loaded;     //so we auto-input current filename for easy over-writing


    protected void onCreate(Bundle savedInstanceState) {

        // Get the start_grid and correct_grid passed
        Bundle extras = getIntent().getExtras();
        grid_correct = (int[][]) extras.getSerializable("grid_solution");
        grid = (int[][]) extras.getSerializable("grid_currentState");
        grid_initial_state = (int[][]) extras.getSerializable("grid_initialState");
        file_loaded = extras.getString( "file_loaded" ); // either the saved name or, if new game, "_temp_file_"


        //Steve: need this plus the android:screenOrientation="portrait" in the xml
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);

        // TODO: Do not use xml layout
        setContentView(R.layout.sudoku_menu);


        // Make Hint and Check buttons and listeners
        moraleButton = (Button) findViewById(R.id.sudokuMoraleButton);
        moraleButton.setOnClickListener(Sudoku.this);
        hintButton = (Button) findViewById(R.id.sudokuHintButton);
        hintButton.setOnClickListener(Sudoku.this);


        // TODO: Programatically generate the sudokuGrid here -----------------------
        //  GridLayout with 81 positioned EditText views

        android.widget.GridLayout sudGrid = (android.widget.GridLayout) findViewById(R.id.sudokuGrid);

        // TODO -----------------------------------------------------------------


        // Set up grid using initial and current states
        for(int i = 0; i < GRID_SIZE; i++){
            for (int j = 0; j < GRID_SIZE; j++){
                EditText field = (EditText) sudGrid.getChildAt(i * GRID_SIZE + j);
                field.setBackgroundResource(R.drawable.border_active);
                // Set current grid state
                if( grid[i][j]!=0 ){  field.setText("" + grid[i][j]);  }
                // Set initial state colour and make non-editable
                if(grid_initial_state[i][j]!=0) {
                    field.setBackgroundResource(R.drawable.border);
                    field.setKeyListener(null);
                }
            }
        }




        //Create a TextWatcher for input to addTextChangedListener() to hide keypad / reset colour /
        // autosave / ...  upoon number entry
        // (this HAS TO GO HERE at end of onCreate() or the autosave will activate as we initialise grid)
        GridLayout layout = (GridLayout)findViewById(R.id.sudokuGrid);
        for (int i = 0; i < layout.getChildCount(); i++) {

            View v = layout.getChildAt(i);
            if (v instanceof EditText) {
                final EditText et = (EditText) v;

                // Only allow digits 1-9 to be entered
                et.setKeyListener(DigitsKeyListener.getInstance("123456789"));

                // Make the TextWatcher
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

                        // Autosave upon every change
                        updateGrid();
                        saveGame();
                        // Reset colours after any change
                        resetGridColours();
                        // and remove hint
                        hintPressed = false;

                        // Make congrats Toast if correct
                        checkIfCorrect(  );

                    }
                };
                et.addTextChangedListener( tw );

            }
        }





        // Stop progressBar
        MainMenu.progressBar.setVisibility(View.GONE);


    }






    // STEVE: added this to prevent screen rotation
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    public void checkIfCorrect(){

        android.widget.GridLayout sudGrid = (android.widget.GridLayout) findViewById(R.id.sudokuGrid);

        // Check if grid is full or correct
        boolean gridCorrect = true;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] != grid_correct[i][j]) {
                    gridCorrect = false;
                }
            }
        }

        if( gridCorrect ){
            // Congratulations Toast
            Toast toast = Toast.makeText(this, "CONGRATULATIONS!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,0);
            // Increase text size in Toast
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(25);
            toast.show();
        }



    }




    public boolean isGridFull(){
        /**
        * Check if grid is full of numbers
        */
        boolean gridFull = true;
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if(grid[i][j]==0){  // 0 is set if no number is entered
                    gridFull = false;
                }
            }
        }
        return gridFull;
    }



    @Override
    public void onClick(View view) {
        /**
         * Define function of Hint and Check buttons here
         */

        updateGrid();

        android.widget.GridLayout sudGrid = (android.widget.GridLayout) findViewById(R.id.sudokuGrid);

        // Check if grid is full (so we can prevent Hint method running)
        boolean gridFull = isGridFull();


        // Deal with buton presses
        switch ( view.getId() ){

            // Hint button -----------------------
            case R.id.sudokuHintButton:
                // If correct, make a Snackbar with congrats message
                checkIfCorrect();

                resetGridColours();

                boolean errors = incorrectEntries();

                if( !errors ) {

                    if (!hintPressed && !gridFull) {
                        hintPressed = true;
                        // actual Hint Button functionality
                        int[] hint_xy = SudokuMethods.getHint_singles_hiddenSingles(grid);
                        EditText field2 = (EditText) sudGrid.getChildAt(hint_xy[0] * GRID_SIZE + hint_xy[1]);   //TODO: double check the [0] and [1] are correct way round
                        field2.setBackgroundColor(getResources().getColor(R.color.sudoku_hint));
                    } else {
                        hintPressed = false;
                    }

                }
                else{
                    Toast toast = Toast.makeText(this, "There are incorrect entries!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP,0,0);
                    // Increase text size in Toast
                    ViewGroup group = (ViewGroup) toast.getView();
                    TextView messageTextView = (TextView) group.getChildAt(0);
                    messageTextView.setTextSize(20);

                    toast.show();
                    hintPressed = false;
                }

                break;



            // Check button -------------------------------------------
            case R.id.sudokuMoraleButton:

                hintPressed = false;
                resetGridColours();

                String[] morale = {"You can do it!", "Einstein never solved a single sudoku", "This world doesn't deserve you",
                        "You're incredible!", "Fill me!", "I believe in you", "You complete me",
                        "Such perseverance and resolve", "Keep going!", "In the future, when robots rule the world, puzzles are all we'll have",
                        "To solve the sudoku, one must become the sudoku", "Your smile is contagious :)",
                        "You are the chosen one", "You're a wizard, Harry!", "This puzzle was generated JUST FOR YOU",
                        "There are 6,670,903,752,021,072,936,960 sudoku puzzles to complete", "You can do sudoku!",
                        "We all need a little help sometimes", "Feel the joy", "You should be proud!"};

                int r = (int)(  Math.random() * (morale.length)   );

                Toast toast = Toast.makeText(this, morale[r], Toast.LENGTH_SHORT );
                toast.setGravity(Gravity.CENTER,0,0);
                // Increase text size in Toast
                ViewGroup group = (ViewGroup) toast.getView();
                TextView messageTextView = (TextView) group.getChildAt(0);
                messageTextView.setTextSize(18);
                toast.show();
                break;


            default:
                break;  //is this essential?

        }
        
    }

//=========================================================
// REMOVE CHECK FEATURE AND JUST ADD A WARNING IN HINT IF THERE ARE INCORRECT ENTRIES
//                if( !checkPressed ) {
//                    checkPressed=true;
//                    for (int i = 0; i < x - 2; i++) {
//                        for (int j = 0; j < y - 2; j++) {
//                            EditText field = (EditText) sudGrid.getChildAt(i * 9 + j);
//                            //field.setBackgroundResource(R.drawable.border_active);
//                            if (grid[i][j] == grid_correct[i][j]) {
//                                field.setBackgroundColor( getResources().getColor(R.color.sudoku_correct) );
//                            }
//                            else if(grid[i][j]!=0 ){  // 0 is set if no number is entered
//                                field.setBackgroundColor( getResources().getColor(R.color.sudoku_wrong) );
//                            }
//                        }
//                    }
//                }
//                else{
//                    checkPressed = false;
//                }
//========================================================





    public boolean incorrectEntries(){
        /**
         * Check if there are incorrect entries; ok to have empty
         */
        boolean errors = false;

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                if (grid[i][j] != grid_correct[i][j]  &&  grid[i][j] !=0 ) {
                    errors = true;
                }
            }
        }

        return errors;
    }





    public void saveGame() {
        /**
         * Save game state to file
         */

        // Create state String of 81*3 digits
        String stateString = "";
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                stateString = stateString + String.valueOf(grid[i][j]);
            }
        }
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                stateString = stateString + String.valueOf( grid_initial_state[i][j] ) ;
            }
        }
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                stateString = stateString + String.valueOf( grid_correct[i][j] ) ;
            }
        }

        ///ONLY RECOGNIZE .dat FILE TYPES!!
        saveFileName = file_loaded+".dat";

        // Write file
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput( saveFileName , Context.MODE_PRIVATE);
            outputStream.write( stateString.getBytes()   );
            outputStream.close();
            Log.d("SUCCESS", "FILE WRITTEN SUCCESSFULLY");
        } catch (Exception e) {
            Log.d("NO FILE WORK", "NO SAVE FILE WRITTEN");
            e.printStackTrace();
        }

    }





    public void updateGrid(){
        /**
         *   Update grid[][] (does not happen upon text entry).
         */
        android.widget.GridLayout sudGrid = (android.widget.GridLayout) findViewById(R.id.sudokuGrid);
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                EditText field = (EditText) sudGrid.getChildAt(i * GRID_SIZE + j);
                if (  !String.valueOf(field.getText()).isEmpty()  ) {
                    grid[i][j] = Integer.parseInt(String.valueOf(field.getText()));
                }
                else{
                    // Need to reset empties to zero
                    grid[i][j] = 0 ;
                }
            }
        }



    }



    public void resetGridColours(){
        /**
         * Reset grid to initial  colours
         */
        android.widget.GridLayout sudGrid = (android.widget.GridLayout) findViewById(R.id.sudokuGrid);
        // Reset colours
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                EditText field = (EditText) sudGrid.getChildAt(i * GRID_SIZE + j);
                if( field.getKeyListener() == null ){
                    field.setBackgroundResource(R.drawable.border);
                }
                else {
                    field.setBackgroundResource(R.drawable.border_active);
                }
            }
        }
    }







    @Override
    public void onBackPressed() {
        /**
         * Create warning message when back button is pressed
         */
        String default_title = "Want to save?";
        String default_msg = "enter save name";

        customOnBackPressed( default_title, default_msg );
    }






    public void customOnBackPressed( String default_title, String default_msg ){
        /**
         * Custom onBackPressed to allow passing of Strings
         */

        // Only warn for unsaved games (i.e. with the unsavedGameName generic name
        if( file_loaded.equals( MainMenu.unsavedGameName  ) ){

            // Use AlertDialog to select file name or exit without saving
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle( default_title );

            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);

            // Initialize with current loaded file name (or "" if new puzzle)
            input.setHint( default_msg );
            //input.setText( default_msg );

            // and select it all for over-writing
            input.setSelectAllOnFocus(true);
            builder.setView(input);

            //Set up buttons
            builder.setPositiveButton(R.string.sudoku_save, new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which ){

                    /// Get name from the input EditText  (only recognize .dat files!)
                    String inputText = input.getText().toString();
                    String fileToSave = inputText+".dat";
                    Context context = getApplicationContext();


                    if(  MainMenu.savedGames.contains( fileToSave )  ||   inputText.equals("")  ){
                        String nextTitle = "Name already used!";
                        if( inputText.equals("")  ){
                            nextTitle = "Save name invalid";
                        }
                        // Recursively call this function if name already taken!
                        customOnBackPressed(  nextTitle , "try another"  );

                    }
                    else{
                        // Remove the extension;  "file_loaded" is what is saved in saveGame()
                        file_loaded = fileToSave.substring( 0, fileToSave.lastIndexOf(".")    );   //saveGame doesn't want the extension
                        saveGame();
                        Toast toast = Toast.makeText( getApplicationContext(), "Saving: "+file_loaded, Toast.LENGTH_SHORT );
                        toast.show();



                        //delete the temp file
                        File fdelete = new File( context.getFilesDir(), MainMenu.unsavedGameName+".dat" );  // this has to be the temp_file
                        if (fdelete.exists()) {
                            if (fdelete.delete()) {
                                //Toast.makeText(context, "Deleting: " + filename, Toast.LENGTH_SHORT).show();
                                Log.d("FILE DELETION", "Deletion of _temp_file_ successful");
                            } else {
                                Log.d("FILE DELETION", "Deletion of _temp_file_ failed");
                            }
                        }


                        // Update the save game list!
                        MainMenu.savedGames = SavedGames.getSavedGames( context );

                        finish();
                    }
                }
            });


            builder.setNegativeButton(R.string.sudoku_dont_save, new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which){
                    //TODO :  delete the temp file name!
                    // no save desired; delete temp file
                    Context context = getApplicationContext();
                    File fdelete = new File( context.getFilesDir(), MainMenu.unsavedGameName+".dat" );
                    if (fdelete.exists()) {
                        if (fdelete.delete()) {
                            //Toast.makeText(context, "Deleting: " + filename, Toast.LENGTH_SHORT).show();
                            Log.d("FILE DELETION", "Deletion of _temp_file_ successful");
                        } else {
                            Log.d("FILE DELETION", "Deletion of _temp_file_ failed");
                        }
                    }

                    finish();
                }
            });


            builder.show();

        }
        else{
            Sudoku.super.onBackPressed();
        }

    }






}