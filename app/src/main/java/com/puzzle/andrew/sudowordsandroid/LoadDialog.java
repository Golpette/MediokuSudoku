package com.puzzle.andrew.sudowordsandroid;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.R.drawable.ic_dialog_alert;


public class LoadDialog extends DialogFragment {

    /* The activity that creates an instance of this dialog fragment must
 * implement this interface in order to receive event callbacks.
 * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onClick (int i ) ;
    }

    // Use this instance of the interface to deliver action events to main activity!
    NoticeDialogListener mListener;


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener   // STEVE: I HAVE NO IDEA WHAT THIS IS BUT WE NEED IT
    @Override
    public void onAttach(Activity activity) {  // TODO: CARE NEEDED: this was deprecated in API 23; might need onAttach(Context context) ...
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        /**
         * Make a list dialog showing all saved games
         */
        List<String> list_w= MainMenu.savedGames;

        // Get rid of .dat extension here for neatness in the list
        List<String> list_x = new ArrayList<String>();
        for( String s : list_w ){
            list_x.add( s.substring( 0, s.lastIndexOf('.') )   );
        }

        // The builder needs an array not a list
        String[] arr = list_x.toArray( new String[ list_w.size() ] );

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialog);


        String loadDialogTitle = "Choose game";
        if( arr.length == 0 ){ loadDialogTitle = "No saved games!"  ;  }

        builder.setTitle( loadDialogTitle )
                //R.string.pick_color
                .setItems( arr, new DialogInterface.OnClickListener() {
                    //R.array.colors_array
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains index of selected item
                        mListener.onClick( which );
                    }
                });

        return builder.create();

    }
}