package com.puzzle.andrew.sudowordsandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


import com.puzzle.andrew.sudowordsandroid.sudoku.GameState;
import com.puzzle.andrew.sudowordsandroid.sudoku.Sudoku;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    Button easyButton, mediumButton, loadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //Steve: need this plus the android:screenOrientation="portrait" in the xml
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        easyButton = (Button) findViewById(R.id.button_easy);
        easyButton.setOnClickListener(MainMenu.this);

        mediumButton = (Button) findViewById(R.id.button_medium);
        mediumButton.setOnClickListener(MainMenu.this);

        loadButton = (Button) findViewById(R.id.button_medium);
        loadButton.setOnClickListener(MainMenu.this);


    }


    // STEVE: Added this to stop screen rotation; force portrait
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


    //Use this to load game eventually
    public GameState decodeSavedSudoku(String savedGame) {
        int[][] gameToLoad = new int[9][9];
        int [][] start_game = new int [9][9];
        int [][] end_game = new int [9][9];
        for (int i = 0; i < savedGame.length(); i++) {
            if (savedGame.charAt(i) == ' ') {
                gameToLoad[i % 9][i / 8] = 0;
                start_game[i % 9][i / 8] = 0;
                end_game[i % 9][i / 8] = 0;
            } else if ((int) savedGame.charAt(i) > 32 && (int) savedGame.charAt(i) < 42) {
                gameToLoad[i % 9][i / 8] = (int) savedGame.charAt(i) - 32;
                start_game[i % 9][i / 8] = (int) savedGame.charAt(i) - 32;
                end_game[i % 9][i / 8] = (int) savedGame.charAt(i) - 32;
            } else if ((int) savedGame.charAt(i) > 41 && (int) savedGame.charAt(i) < 51) {
                gameToLoad[i % 9][i / 8] = (int) savedGame.charAt(i) - 41;
                start_game[i % 9][i / 8] = 0;
                end_game[i % 9][i / 8] =  (int) savedGame.charAt(i) - 41;
            } else {
                int a = (int)savedGame.charAt(i)-50;
                if(a%8 <= a/8){
                    gameToLoad[i % 9][i / 8] = a % 8;
                }else {
                    gameToLoad[i % 9][i / 8] = a % 8 + 1;
                }
                start_game[i % 9][i / 8] = 0;
                end_game[i % 9][i / 8] = a/8 + 1;
            }
        }
        return new GameState(start_game, gameToLoad, end_game);
    }



    @Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.button_easy:
                // RUN THE CODE TO START THE NEXT ACTIVITY
                Intent easy = new Intent(this, Sudoku.class);
                easy.putExtra("difficulty", "easy");
                startActivity( easy );

                break;

            case R.id.button_medium:
                Intent medium = new Intent(this, Sudoku.class);
                medium.putExtra("difficulty", "medium");
                startActivity( medium );

                break;

            case R.id.button_load:
                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                //int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
                String saved = sharedPref.getString(getString(R.string.code), "first");
                GameState game = decodeSavedSudoku(saved);
                Intent load = new Intent(this, Sudoku.class);
                //load.putExtra();
                startActivity(load);

        }

    }



}
