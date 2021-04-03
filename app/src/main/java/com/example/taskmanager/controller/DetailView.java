package com.example.taskmanager.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.taskmanager.R;
import com.example.taskmanager.Repository.TaskRepository;
import com.example.taskmanager.Utils.PictureUtils;
import com.example.taskmanager.model.Task;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DetailView {

    private static final String PICK_DIALOG_TAG = "datePick_dialog";
    private static final String TIME_PICK_DIALOG_TAG = "timePick_dialog";
    private static final String PHOTO_ITEM_DIALOG_TAG = "photo_dialog";

    private static final int DATE_REQUEST_CODE = 3;
    private static final int TIME_REQUEST_CODE = 4;
    private static final int CAMERA_REQUEST_CODE = 5;

    private static final String AUTHORITY_FILE_PROVIDER = "com.example.taskmanager.fileProvider";


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
    private ImageView mCamera;


    private Context mContext;
    private Task mTask;
    private String mState;
    private Date TaskDate;
    private Fragment mFragment;

    public String getTitle() {
        return mTitle.getText().toString();
    }

    public String getDescription() {
        return mDescription.getText().toString();
    }

    public String getState() {

        if (todo.isChecked()) return "todo";

        else if (doing.isChecked()) return "doing";

        else return "done";
    }

    public void setImage(){
        File mPhotoFile = TaskRepository.getInstance(mContext).getTempPhotoFile(mTask);
        if (mPhotoFile != null && mPhotoFile.exists()) {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getAbsolutePath(), (Activity) mContext);
            mCamera.setImageBitmap(bitmap);
        }
        else
            mCamera.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_action_camera));
    }

    public DetailView(Context context, Task task, String state, Fragment fragment){
        this.mContext = context;
        this.mTask = task;
        this.mState = state;
        this.mFragment = fragment;
    }


    public View detailView(Date date, boolean add) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext.getApplicationContext());
        View view = layoutInflater.inflate(R.layout.add_task, null, false);

        mTitle = view.findViewById(R.id.Title);
        mDescription = view.findViewById(R.id.Description);
        mDate = view.findViewById(R.id.date_button);
        mTime = view.findViewById(R.id.time_button);
        todo = view.findViewById(R.id.todo);
        doing = view.findViewById(R.id.doing);
        done = view.findViewById(R.id.done);
        mCamera = view.findViewById(R.id.camera);

        mTitle.setText(mTask.getTitle());
        mDescription.setText(mTask.getDescription());

        TaskDate = date;

        if (mState.equals(mContext.getString(R.string.todo))) {
            todo.setChecked(true);
            if (add) {
                doing.setVisibility(View.GONE);
                done.setVisibility(View.GONE);
            }
        } else if (mState.equals(mContext.getString(R.string.doing))) {
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

        setImage();
        updateUI(date);
        detailListener();


        return view;
    }

    private void detailListener() {
        mDate.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(TaskDate);
            int year = calendar.get(Calendar.YEAR);
            int monthOfYear = calendar.get(Calendar.MONTH);
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            mDatePicker = new DatePicker(mContext);
            mDatePicker.init(year, monthOfYear, dayOfMonth, null);
            customDialogFragment fragment =
                    customDialogFragment.newInstance(mDatePicker, null);
            fragment.setTargetFragment(mFragment, DATE_REQUEST_CODE);
            fragment.show(mFragment.getFragmentManager(), PICK_DIALOG_TAG);
        });


        mTime.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(TaskDate);
            int Hour = calendar.get(Calendar.HOUR);
            int Minute = calendar.get(Calendar.MINUTE);
            mTimePicker = new TimePicker(mContext);
            mTimePicker.setIs24HourView(true);
            mTimePicker.setCurrentHour(Hour);
            mTimePicker.setCurrentMinute(Minute);

            customDialogFragment fragment =
                    customDialogFragment.newInstance(mTimePicker, null);
            fragment.setTargetFragment(mFragment, TIME_REQUEST_CODE);
            fragment.show(mFragment.getFragmentManager(), TIME_PICK_DIALOG_TAG);

        });

        mCamera.setOnClickListener(view -> TakePhoto());
    }

    public Date TimeResult(Date tempDate) {
        int hour = mTimePicker.getCurrentHour();
        int minute = mTimePicker.getCurrentMinute();
        Calendar time = Calendar.getInstance();
        time.setTime(tempDate);
        time.set(Calendar.HOUR, hour);
        time.set(Calendar.MINUTE, minute);
        updateUI(time.getTime());
        return time.getTime();
    }

    public Date DateResult(Date tempDate) {
        int year = mDatePicker.getYear();
        int monthOfYear = mDatePicker.getMonth();
        int dayOfMonth = mDatePicker.getDayOfMonth();
        Calendar date = Calendar.getInstance();
        date.setTime(tempDate);
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, monthOfYear);
        date.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateUI(date.getTime());
        return date.getTime();
    }


    private boolean checkCameraHardware() {
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    private void TakePhoto(){
        if(!checkCameraHardware()) {
            Toast.makeText(mContext, "Camera doesn't exist", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri mPhotoUri = FileProvider.getUriForFile(mContext, AUTHORITY_FILE_PROVIDER,
                TaskRepository.getInstance(mContext).getTempPhotoFile(mTask));

        List<ResolveInfo> cameraActivities = mContext.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : cameraActivities) {
            mContext.grantUriPermission(resolveInfo.activityInfo.packageName,
                    mPhotoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
        mFragment.startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    private void updateUI(Date date) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        mDate.setText(simpleDateFormat.format(date));

        simpleDateFormat = new SimpleDateFormat("HH:mm");
        mTime.setText(simpleDateFormat.format(date));
    }
}
