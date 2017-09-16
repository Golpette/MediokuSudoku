package com.puzzle.andrew.sudowordsandroid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;


import com.puzzle.andrew.sudowordsandroid.sudoku.Sudoku;

public class MainMenu extends AppCompatActivity implements View.OnClickListener {

    Button easyButton, mediumButton;
    public static ProgressBar progressBar; // Want this accessible from other activity - is this the right way??


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
    }


    // STEVE: Added this to stop screen rotation; force portrait
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }




    @Override
    public void onClick(View view) {

        switch(view.getId()) {

            case R.id.button_easy:
                // Make progressBar visible
                progressBar.setVisibility(View.VISIBLE);
                // RUN THE CODE TO START THE NEXT ACTIVITY
                Intent easy = new Intent(this, Sudoku.class);
                easy.putExtra("difficulty", "easy");
                startActivity( easy );

                break;

            case R.id.button_medium:
                // Make progressBar visible
                progressBar.setVisibility(View.VISIBLE);
                Intent medium = new Intent(this, Sudoku.class);
                medium.putExtra("difficulty", "medium");
                startActivity( medium );

                break;
        }

    }



}
