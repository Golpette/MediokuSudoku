package com.puzzle.andrew.sudowordsandroid;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class LoadDialog extends DialogFragment {


    /* The activity that creates an instance of this dialog fragment must
 * implement this interface in order to receive event callbacks.
 * Each method passes the DialogFragment in case the host needs to query it. */
    public interface NoticeDialogListener {
        public void onClick (int i ) ;
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;


    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener   // STEVE: I HAVE NO IDEA WHAT THIS IS BUT WE NEED IT
    @Override
    public void onAttach(Activity activity) {
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

        List<String> listw= MainMenu.savedGames;

        // builder needs and array not a list
        String[] arr = listw.toArray( new String[ listw.size() ] );

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose game")
                //R.string.pick_color
                .setItems( arr, new DialogInterface.OnClickListener() {
                    //R.array.colors_array
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        mListener.onClick( which );
                    }
                });
        return builder.create();

    }





}