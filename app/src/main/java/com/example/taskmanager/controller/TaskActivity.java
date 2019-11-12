package com.example.taskmanager.controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.taskmanager.R;
import com.example.taskmanager.Repository.TaskRepository;


public class TaskActivity extends AppCompatActivity {

    private static final String EXTRA_NAME_ID = "Username";
    private static final String EXTRA_ID = "UUID";
    private Toolbar toolbar;

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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle("Welcome " + getIntent().getStringExtra(EXTRA_NAME_ID));

        TaskRepository.setUserId(getIntent().getStringExtra(EXTRA_ID));

        if(savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.frag_container, TabFragment.newInstance())
                    .commit();
        }
    }
}
