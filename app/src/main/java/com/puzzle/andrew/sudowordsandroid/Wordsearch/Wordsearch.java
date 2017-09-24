package com.puzzle.andrew.sudowordsandroid.wordsearch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import com.puzzle.andrew.sudowordsandroid.R;

/**
 * Created by Andrew on 30/08/2017.
 */
public class Wordsearch extends AppCompatActivity{

    ImageButton wordsearchButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wordsearch_menu);

        //wordsearchButton = (ImageButton)findViewById(R.id.crossword);
    }
}