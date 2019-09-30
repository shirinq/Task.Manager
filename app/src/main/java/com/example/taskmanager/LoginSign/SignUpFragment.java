package com.example.taskmanager.LoginSign;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taskmanager.R;
import com.example.taskmanager.Repository.UserRepository;
import com.example.taskmanager.model.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {
    static final String PASSWORD = "password";
    static final String USERNAME = "username";
    private EditText username;
    private EditText password;
    private Button signUp;
    private View mView;


    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance(String user , String pass){
        SignUpFragment fragment = new SignUpFragment();
        Bundle Args = new Bundle();
        Args.putString(PASSWORD,pass);
        Args.putString(USERNAME,user);
        fragment.setArguments(Args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_sign_up, container, false);
        username = mView.findViewById(R.id.userName_sign);
        password = mView.findViewById(R.id.Password_sign);
        signUp = mView.findViewById(R.id.Sign_up);
        password.setText(getArguments().getString(PASSWORD));
        username.setText(getArguments().getString(USERNAME));
        signUpListener();


        return mView;
    }

    private void signUpListener(){
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(getActivity().getApplicationContext()
                    ,R.string.Empty_fields,Toast.LENGTH_LONG).show();
                    return;
                }
                Integer pass = Integer.parseInt(password.getText().toString());
                User newUser = new User(username.getText().toString(),pass);
                UserRepository repository = UserRepository.getInstance(getActivity().getApplicationContext());

                if(repository.UserIsExist(newUser))
                    Toast.makeText(getActivity().getApplicationContext(),"Try another username",Toast.LENGTH_LONG).show();
                else {

                    if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    {
                        repository.insertUser(newUser);
                        LoginFragment fragment = (LoginFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.Login_container);
                        fragment.setText(username.getText().toString(),password.getText().toString());

                        username.getText().clear();
                        password.getText().clear();

                        Toast.makeText(getActivity().getApplicationContext(),
                                "Congratulations! :)",Toast.LENGTH_SHORT)
                                .show();

                        return;
                    }
                    repository.insertUser(newUser);
                    Intent intent = new Intent();
                    intent.putExtra(USERNAME,username.getText().toString());
                    intent.putExtra(PASSWORD,password.getText().toString());
                    getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .remove(SignUpFragment.this)
                            .commit();

                }
            }
        });
    }
}
