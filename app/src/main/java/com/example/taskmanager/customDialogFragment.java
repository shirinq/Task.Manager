package com.example.taskmanager;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.taskmanager.Repository.TaskRepository;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;

import java.io.Serializable;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class customDialogFragment extends DialogFragment {


    private View mView;
    private String mTitle;

    public customDialogFragment() {
        // Required empty public constructor
    }

    public static customDialogFragment newInstance(View view , String title) {
        customDialogFragment dialogFragment = new customDialogFragment();
        dialogFragment.mView = view;
        dialogFragment.mTitle = title;
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getTargetFragment()
                                .onActivityResult(getTargetRequestCode(), RESULT_OK,new Intent());
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setTitle(mTitle)
                .setView(mView).create();

        return alertDialog;
    }
}
