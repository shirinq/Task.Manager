package com.example.taskmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.taskmanager.Repository.TaskRepository;
import com.example.taskmanager.model.State;
import com.example.taskmanager.model.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    private static final String STATE_KEY_BUNDLE = "state";

    private static final String PICK_DIALOG_TAG = "datePick_dialog";
    private static final String ADD_DIALOG_TAG = "add_dialog";
    private static final String EDIT_DIALOG_TAG = "edit_dialog";
    private static final String DELETE_DIALOG_TAG = "delete_dialog";
    private static final String TIME_PICK_DIALOG_TAG = "timePick_dialog";

    /**
     * TASK LIST VIEW
     */
    private FloatingActionButton add;
    private State mState;
    private Adapter adapter;
    private RecyclerView mRecycler;
    private View mView;

    /**
     * GLOBAL OBJECTS
     */
    private Task mTask;
    private Date tempDate;

    /**
     * Task Detail ViewItem
     */
    private EditText mTitle;
    private EditText mDescription;
    private Button mDate;
    private Button mTime;
    private TimePicker mTimePicker;
    private DatePicker mDatePicker;
    private RadioButton todo;
    private RadioButton done;
    private RadioButton doing;

    public SingleFragment() {
        // Required empty public constructor
    }

    public static SingleFragment newInstance(State state) {
        SingleFragment fragment = new SingleFragment();
        fragment.mState = state;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            mState = (State) savedInstanceState.getSerializable("state");
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
        Log.d("single", "onCreateView: ");
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
        private Task hTask;

        private TaskHolder(@NonNull final View itemView) {
            super(itemView);
            tview = itemView.findViewById(R.id.view);
            name = itemView.findViewById(R.id.nameTxt);
            delete = itemView.findViewById(R.id.Button_delete);
            edit = itemView.findViewById(R.id.Button_edit);
        }

        private void bind(final Task task) {
            hTask = task;
            tview.setText(String.valueOf(task.getTitle().charAt(0)));
            name.setText(task.getTitle());

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTask = hTask;
                    customDialogFragment fragment =
                            customDialogFragment.newInstance(null, getString(R.string.delete));
                    fragment.setTargetFragment(SingleFragment.this, DELETE_REQUEST_CODE);
                    fragment.show(getFragmentManager(), DELETE_DIALOG_TAG);
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mTask = hTask;
                    tempDate = hTask.getDate();
                    customDialogFragment fragment = customDialogFragment
                            .newInstance(
                                    detailView(tempDate, false)
                                    , null);

                    fragment.setTargetFragment(SingleFragment.this, EDIT_REQUEST_CODE);
                    fragment.show(getFragmentManager(), EDIT_DIALOG_TAG);
                }
            });
        }
    }

    public void Listener() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTask = new Task();
                tempDate = mTask.getDate();
                customDialogFragment fragment =
                        customDialogFragment.newInstance(detailView(new Date(), true), null);
                fragment.setTargetFragment(SingleFragment.this, ADD_REQUEST_CODE);
                fragment.show(getFragmentManager(), ADD_DIALOG_TAG);
            }
        });

    }

    private View detailView(Date date, boolean add) {
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity().getApplicationContext());
        View view = layoutInflater.inflate(R.layout.add_task, null, false);

        mTitle = view.findViewById(R.id.Title);
        mDescription = view.findViewById(R.id.Description);
        mDate = view.findViewById(R.id.date_button);
        mTime = view.findViewById(R.id.time_button);
        todo = view.findViewById(R.id.todo);
        doing = view.findViewById(R.id.doing);
        done = view.findViewById(R.id.done);

        mTitle.setText(mTask.getTitle());
        mDescription.setText(mTask.getDescription());

        if (mState == State.TODO) {
            todo.setChecked(true);
            if (add) {
                doing.setVisibility(View.GONE);
                done.setVisibility(View.GONE);
            }
        } else if (mState == State.DOING) {
            doing.setChecked(true);
            if (add) {
                todo.setVisibility(View.GONE);
                done.setVisibility(View.GONE);
            }
        } else {
            done.setChecked(true);
            if (add) {
                doing.setVisibility(View.GONE);
                todo.setVisibility(View.GONE);
            }
        }

        updateUI(date);
        detailListener();


        return view;
    }

    private void detailListener() {
        mDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(tempDate);
                int year = calendar.get(Calendar.YEAR);
                int monthOfYear = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                mDatePicker = new DatePicker(getActivity());
                mDatePicker.init(year, monthOfYear, dayOfMonth, null);
                customDialogFragment fragment =
                        customDialogFragment.newInstance(mDatePicker, null);
                fragment.setTargetFragment(SingleFragment.this, DATE_REQUEST_CODE);
                fragment.show(getFragmentManager(), PICK_DIALOG_TAG);
            }
        });


        mTime.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(tempDate);
                int Hour = calendar.get(Calendar.HOUR);
                int Minute = calendar.get(Calendar.MINUTE);
                mTimePicker = new TimePicker(getActivity());
                mTimePicker.setIs24HourView(true);
                mTimePicker.setHour(Hour);
                mTimePicker.setMinute(Minute);

                customDialogFragment fragment =
                        customDialogFragment.newInstance(mTimePicker, null);
                fragment.setTargetFragment(SingleFragment.this, TIME_REQUEST_CODE);
                fragment.show(getFragmentManager(), TIME_PICK_DIALOG_TAG);

            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_CANCELED)
            return;
        switch (requestCode) {
            case ADD_REQUEST_CODE:
                Task task = new Task(mState, mTitle.getText().toString(), new Date(), mDescription.getText().toString());
                TaskRepository.getInstance(getActivity()).insertTask(task);
                updateAdapter();
                break;

            case DELETE_REQUEST_CODE:
                TaskRepository.getInstance(getActivity().getApplicationContext()).deleteTask(mTask);
                updateAdapter();
                break;

            case EDIT_REQUEST_CODE:
                mTask.setTitle(mTitle.getText().toString());
                mTask.setDescription(mDescription.getText().toString());
                mTask.setDate(tempDate);
                if (todo.isChecked()) {
                    mTask.setState("todo");
                } else if (doing.isChecked()) {
                    mTask.setState("doing");
                } else {
                    mTask.setState("done");
                }
                TaskRepository.getInstance(getActivity().getApplicationContext()).updateTask(mTask);
                updateAdapter();
                break;

            case DATE_REQUEST_CODE:
                int year = mDatePicker.getYear();
                int monthOfYear = mDatePicker.getMonth();
                int dayOfMonth = mDatePicker.getDayOfMonth();
                Calendar date = Calendar.getInstance();
                date.setTime(tempDate);
                date.set(Calendar.YEAR, year);
                date.set(Calendar.MONTH, monthOfYear);
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                tempDate = date.getTime();
                updateUI(tempDate);
                break;

            case TIME_REQUEST_CODE:
                int hour = mTimePicker.getHour();
                int minute = mTimePicker.getMinute();
                Calendar time = Calendar.getInstance();
                time.setTime(tempDate);
                time.set(Calendar.HOUR, hour);
                time.set(Calendar.MINUTE, minute);
                tempDate = time.getTime();
                updateUI(tempDate);
                break;
        }
    }

    private void updateUI(Date date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        mDate.setText(simpleDateFormat.format(date));

        simpleDateFormat = new SimpleDateFormat("HH:mm");
        mTime.setText(simpleDateFormat.format(date));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(STATE_KEY_BUNDLE, mState);
    }
}
