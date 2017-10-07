package com.puzzle.andrew.sudowordsandroid;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class SavedGames  {

    public static List<String> getSavedGames( Context context ){
        /**
         * Method to return list of valid game save files (that is, .dat files)
         */
        // All saved files found in internal storage
        String[] saveFiles = context.fileList();

        // Valid game files
        List<String> gameFiles = new ArrayList<String>();

        for( int l = 0; l < saveFiles.length; l++ ) {
            Log.d("File list: ", saveFiles[l]);
            // All game states saved in .dat files!!
            if( saveFiles[l].contains( ".dat" ) ){
            //if( saveFiles[l].contains( ".zzz" ) ){  // test case if there are no saved games
                gameFiles.add( saveFiles[l] );
                Log.d("Valid save data: ", saveFiles[l]);
            }
        }
        return gameFiles;
    }
}
