package com.example.taskmanager.LoginSign;


import android.app.Activity;
import android.content.Intent;
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
import com.google.android.material.textfield.TextInputLayout;
/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    private static final int SIGN_CODE = 1;
    private TextInputLayout username;
    private TextInputLayout password;
    private Button login;
    private Button signUp;
    private View mView;
    private UserRepository uRepository;


    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(){
        return new LoginFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView =  inflater.inflate(R.layout.fragment_login, container, false);
        username = mView.findViewById(R.id.userName);
        password = mView.findViewById(R.id.Password);
        login = mView.findViewById(R.id.Login);
        signUp = mView.findViewById(R.id.Sign_up);

        uRepository = UserRepository.getInstance(getActivity().getApplicationContext());


        Listener();

        return mView;
    }

    public void Listener(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(username.getText().toString().equals("ADMIN")
                        && password.getText().toString().equals("0")){
                    ((LoginSignActivity)getActivity()).startUserList();
                    return;
                }

                else if(username.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(getActivity().getApplicationContext(),R.string.Empty_fields,Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                int pass = Integer.parseInt(password.getText().toString());
                User user = new User(username.getText().toString(),pass);
                boolean validUser= uRepository.UserValidation(user);

                if(validUser){
                    user = uRepository.getUser(user);
                    user.setLogged(true);
                    uRepository.updateUser(user);
                    ((LoginSignActivity)getActivity()).startTaskList(user);
                }else
                    Toast.makeText(getActivity().getApplicationContext(),R.string.wrong_inputs,Toast.LENGTH_LONG)
                    .show();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SignUpFragment signUp = SignUpFragment
                        .newInstance(username.getText().toString(),password.getText().toString());
                signUp.setTargetFragment(LoginFragment.this, SIGN_CODE);

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.user_container,signUp,getString(R.string.SignUpFragment_TAG))
                        .commit();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK && data != null){
            mView.setVisibility(View.VISIBLE);
            username.setText(data.getStringExtra(SignUpFragment.USERNAME));
            password.setText(data.getStringExtra(SignUpFragment.PASSWORD));
        }
    }

    public void setText(String user , String pass){
        username.setText(user);
        password.setText(pass);
    }
}
