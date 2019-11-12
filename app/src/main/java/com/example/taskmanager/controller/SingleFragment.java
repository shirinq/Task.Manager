package com.example.taskmanager.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.taskmanager.R;
import com.example.taskmanager.Repository.TaskRepository;
import com.example.taskmanager.Utils.PictureUtils;
import com.example.taskmanager.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SingleFragment extends Fragment {

    private static final int ADD_REQUEST_CODE = 0;
    private static final int DELETE_REQUEST_CODE = 1;
    private static final int EDIT_REQUEST_CODE = 2;
    private static final int DATE_REQUEST_CODE = 3;
    private static final int TIME_REQUEST_CODE = 4;
    private static final int CAMERA_REQUEST_CODE = 5;
    private static final int PHOTO_REQUEST_CODE = 6;

    private static final String STATE_KEY_BUNDLE = "state";

    private static final String ADD_DIALOG_TAG = "add_dialog";
    private static final String EDIT_DIALOG_TAG = "edit_dialog";
    private static final String DELETE_DIALOG_TAG = "delete_dialog";
    private static final String PHOTO_ITEM_DIALOG_TAG = "photo_dialog";

    /**
     * TASK LIST VIEW
     */
    private FloatingActionButton add;
    private String mState;
    private Adapter adapter;
    private RecyclerView mRecycler;
    private View mView;
    private ImageView taskimage;

    /**
     * GLOBAL OBJECTS
     */
    private Task mTask;
    private Date tempDate;
    private DetailView detailView;


    public SingleFragment() {
        // Required empty public constructor
    }

    public static SingleFragment newInstance(String state) {
        SingleFragment fragment = new SingleFragment();
        fragment.mState = state;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            mState = savedInstanceState.getString("state");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_single, container, false);
        add = mView.findViewById(R.id.add_btn);
        mRecycler = mView.findViewById(R.id.task_recycler);
        if (!TaskRepository.getInstance(getActivity()).getTasks(mState).isEmpty())
            mView.setBackgroundColor(getResources().getColor(R.color.back));
        Recycler();
        Listener();
        return mView;
    }

    private void Recycler() {
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new Adapter();
        mRecycler.setAdapter(adapter);

        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && add.isShown())
                    add.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    add.show();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }


    public class Adapter extends RecyclerView.Adapter<TaskHolder> {
        List<Task> taskList = TaskRepository
                .getInstance(getActivity())
                .getTasks(mState);

        public Adapter() {
        }

        @NonNull
        @Override
        public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.task_item_holder, parent, false);
            return new TaskHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
            holder.bind(taskList.get(position));
        }

        @Override
        public int getItemCount() {
            return taskList.size();
        }

    }

    public void updateAdapter() {
        if (adapter != null) {
            adapter.taskList = TaskRepository
                    .getInstance(getActivity()).getTasks(mState);
            adapter.notifyDataSetChanged();
            if (!TaskRepository.getInstance(getActivity()).getTasks(mState).isEmpty())
                mView.setBackgroundColor(getResources().getColor(R.color.back));
            else
                mView.setBackgroundResource(R.mipmap.empty);
        }
    }


    private class TaskHolder extends RecyclerView.ViewHolder {

        private TextView tview;
        private TextView name;
        private ImageButton delete;
        private ImageButton edit;
        private ImageButton share;
        private ImageView image;
        private Task hTask;
        private File mPhotoFile;

        private TaskHolder(@NonNull final View itemView) {
            super(itemView);
            tview = itemView.findViewById(R.id.view);
            name = itemView.findViewById(R.id.nameTxt);
            delete = itemView.findViewById(R.id.Button_delete);
            edit = itemView.findViewById(R.id.Button_edit);
            share = itemView.findViewById(R.id.Button_share);
            image = itemView.findViewById(R.id.image);

            delete.setOnClickListener(view -> {
                mTask = hTask;
                customDialogFragment fragment =
                        customDialogFragment.newInstance(null, getString(R.string.delete));
                fragment.setTargetFragment(SingleFragment.this, DELETE_REQUEST_CODE);
                fragment.show(getFragmentManager(), DELETE_DIALOG_TAG);
            });

            edit.setOnClickListener(view -> {
                mTask = hTask;
                tempDate = hTask.getDate();
                detailView = new DetailView(getContext(), hTask, mState, SingleFragment.this);
                customDialogFragment fragment = customDialogFragment
                        .newInstance(
                                detailView.detailView(tempDate, false)
                                , null);

                fragment.setTargetFragment(SingleFragment.this, EDIT_REQUEST_CODE);
                fragment.show(getFragmentManager(), EDIT_DIALOG_TAG);
            });

            image.setOnClickListener(view -> {

                if (image.getDrawable() != null) {
                    mTask = hTask;
                    Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getAbsolutePath(), getActivity());
                    taskimage = new ImageView(getActivity());
                    taskimage.setImageBitmap(bitmap);
                    customDialogFragment fragment = customDialogFragment.newInstance(taskimage, null);
                    fragment.setTargetFragment(SingleFragment.this, PHOTO_REQUEST_CODE);
                    fragment.show(getFragmentManager(), PHOTO_ITEM_DIALOG_TAG);
                }
            });

            share.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,hTask.toString());
                intent.setType("text/plain");
                startActivity(intent);
            });
        }

        private void bind(Task task) {
            hTask = task;
            tview.setText(String.valueOf(task.getTitle().charAt(0)));
            name.setText(task.getTitle());

            mPhotoFile = TaskRepository.getInstance(getActivity()).getPhotoFile(task);
            if (mPhotoFile == null || !mPhotoFile.exists()) {
                image.setImageDrawable(null);
            } else {
                Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getAbsolutePath(), getActivity());
                image.setImageBitmap(bitmap);
            }
        }
    }

    public void Listener() {
        add.setOnClickListener(view -> {
            mTask = new Task();
            tempDate = mTask.getDate();
            detailView = new DetailView(getContext(), mTask, mState, SingleFragment.this);
            customDialogFragment fragment =
                    customDialogFragment.newInstance(detailView.detailView(new Date(), true), null);
            fragment.setTargetFragment(SingleFragment.this, ADD_REQUEST_CODE);
            fragment.show(getFragmentManager(), ADD_DIALOG_TAG);
        });

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED) {
            TaskRepository.getInstance(getContext()).deletePhotoFile(mTask);
            return;
        }

        switch (requestCode) {
            case ADD_REQUEST_CODE:
                mTask.setState(mState);
                mTask.setTitle(detailView.getTitle());
                mTask.setDescription(detailView.getDescription());
                TaskRepository.getInstance(getActivity()).insertTask(mTask);
                updateAdapter();
                break;

            case DELETE_REQUEST_CODE:
                TaskRepository.getInstance(getActivity().getApplicationContext()).deleteTask(mTask);
                TaskRepository.getInstance(getContext()).deletePhotoFile(mTask);
                updateAdapter();
                break;

            case EDIT_REQUEST_CODE:
                mTask.setTitle(detailView.getTitle());
                mTask.setDescription(detailView.getDescription());
                mTask.setDate(tempDate);
                mTask.setState(detailView.getState());
                TaskRepository.getInstance(getActivity().getApplicationContext()).updateTask(mTask);
                updateAdapter();
                break;

            case DATE_REQUEST_CODE:
                tempDate = detailView.DateResult(tempDate);
                break;

            case TIME_REQUEST_CODE:
                tempDate = detailView.TimeResult(tempDate);
                break;

            case CAMERA_REQUEST_CODE:
                detailView.setImage();
                break;

            case PHOTO_REQUEST_CODE:
                TaskRepository.getInstance(getContext()).deletePhotoFile(mTask);
                updateAdapter();
                break;
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_KEY_BUNDLE, mState);
    }


}