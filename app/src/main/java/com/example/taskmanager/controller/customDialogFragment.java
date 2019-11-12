package com.example.taskmanager.controller;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.View;

import com.example.taskmanager.R;
import com.example.taskmanager.Repository.TaskRepository;
import com.example.taskmanager.model.User;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class customDialogFragment extends DialogFragment {


    private View mView;
    private String mTitle;
    private AlertDialog.Builder dialog;

    public customDialogFragment() {
        // Required empty public constructor
    }

    public static customDialogFragment newInstance(View view, String title) {
        customDialogFragment dialogFragment = new customDialogFragment();
        dialogFragment.mView = view;
        dialogFragment.mTitle = title;
        return dialogFragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new AlertDialog.Builder(getActivity())
                .setTitle(mTitle)
                .setView(mView);

        DialogInterface.OnClickListener onClickListener = (dialogInterface, i) ->
                getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_OK, new Intent());

        if (getTargetRequestCode() != 6)
            dialog.setPositiveButton(android.R.string.ok, onClickListener)
                  .setNegativeButton(android.R.string.cancel, (dialogInterface, i) ->
                    getTargetFragment().onActivityResult(getTargetRequestCode(), RESULT_CANCELED, new Intent()));
        else
            dialog.setPositiveButton(R.string.delete_photo, onClickListener);

        return dialog.create();
    }
}
