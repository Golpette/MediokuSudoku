package com.puzzle.andrew.sudowordsandroid.crossword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.puzzle.andrew.sudowordsandroid.R;

public class Crossword extends AppCompatActivity {

    ImageButton crosswordButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crossword_menu);

        //crosswordButton = (ImageButton)findViewById(R.id.crossword);
    }
}
