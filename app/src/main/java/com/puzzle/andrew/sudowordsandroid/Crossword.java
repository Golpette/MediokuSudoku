package com.puzzle.andrew.sudowordsandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Crossword extends AppCompatActivity {

    ImageButton crosswordButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crossword_menu);

        crosswordButton = (ImageButton)findViewById(R.id.crossword);
    }
}
