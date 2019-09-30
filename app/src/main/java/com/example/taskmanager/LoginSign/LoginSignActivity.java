package com.example.taskmanager.LoginSign;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Build;
import android.os.Bundle;

import com.example.taskmanager.R;
import com.example.taskmanager.Repository.UserRepository;
import com.example.taskmanager.TaskActivity;
import com.example.taskmanager.model.User;

import java.util.UUID;

public class LoginSignActivity extends AppCompatActivity {

    public static Intent newIntent(Context context) {
        return new Intent(context, LoginSignActivity.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign);

        getSupportActionBar().setBackgroundDrawable(getDrawable(R.drawable.gradient));


        User user = UserRepository.getInstance(this).LoggedUser();

        if (user != null) {
            startTaskList(user);
        }


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.Login_container, LoginFragment.newInstance())
                    .add(R.id.Sign_container, SignUpFragment.newInstance(null, null))
                    .commit();
        } else
            getSupportFragmentManager().beginTransaction().add(R.id.user_container, LoginFragment.newInstance())
                    .commit();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.SignUpFragment_TAG));
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        else
            super.onBackPressed();
    }

    public void startTaskList(User user) {
        startActivity(TaskActivity
                .newIntent(LoginSignActivity.this, user.getUsername(), user.getID().toString()));
        finish();
    }

}
