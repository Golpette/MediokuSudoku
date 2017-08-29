package com.puzzle.andrew.sudowordsandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

public class MainMenu extends AppCompatActivity {

    ImageButton crosswordButton, wordsearchButton, sudokuButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        crosswordButton = (ImageButton)findViewById(R.id.crossword);
        wordsearchButton = (ImageButton)findViewById(R.id.wordsearch);
        sudokuButton = (ImageButton)findViewById(R.id.sudoku);
    }
}
