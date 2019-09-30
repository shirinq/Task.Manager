package com.example.taskmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.taskmanager.Repository.TaskRepository;

import java.util.UUID;

public class TaskActivity extends AppCompatActivity {

    private static final String EXTRA_NAME_ID = "Username";
    private static final String EXTRA_ID = "UUID";

    public static Intent newIntent(Context context, String name , String uuid) {
        Intent intent = new Intent(context, TaskActivity.class);
        intent.putExtra(EXTRA_NAME_ID, name);
        intent.putExtra(EXTRA_ID, uuid);

        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Welcome " + getIntent().getStringExtra(EXTRA_NAME_ID));
        TaskRepository.setUserId(getIntent().getStringExtra(EXTRA_ID));

        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.frag_container, TabFragment.newInstance())
                    .commit();
        }
    }
}
