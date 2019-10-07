package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

public class UserActivity extends AppCompatActivity {

    public static Intent newIntent(Context context){
        return new Intent(context,UserActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.user_container,UserFragment.newInstance())
                .commit();
    }
}
