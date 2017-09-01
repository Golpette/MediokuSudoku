package com.puzzle.andrew.sudowordsandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainMenu extends AppCompatActivity {

    ImageButton crosswordButton, wordsearchButton, sudokuButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        crosswordButton = (ImageButton)findViewById(R.id.crossword);
        /*crosswordButton.setOnClickListener(new View.OnClickListener(){
            // When the button is pressed/clicked, it will run the code below
            public void onClick(){
                // Intent is what you use to start another activity
                Intent intent = new Intent(this, YourActivity.class);
                startActivity(intent);
            }
        });*/
        wordsearchButton = (ImageButton)findViewById(R.id.wordsearch);
        sudokuButton = (ImageButton)findViewById(R.id.sudoku);
    }

    public void openCrossword(View v)
    {
        Intent myIntent = new Intent(this, Crossword.class);
        startActivity(myIntent);
    }

    public void openWordsearch(View v)
    {
        Intent myIntent = new Intent(this, Wordsearch.class);
        startActivity(myIntent);
    }

    public void openSudoku(View v)
    {
        Intent myIntent = new Intent(this, Sudoku.class);
        startActivity(myIntent);
    }
}
