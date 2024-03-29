package com.example.taskmanager.controller;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.*;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.taskmanager.R;
import com.example.taskmanager.Repository.TaskRepository;
import com.example.taskmanager.Repository.UserRepository;
import com.example.taskmanager.model.User;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private static final int DELETE_REQUEST_CODE = 1;
    private static final String DELETE_DIALOG_TAG = "delete_dialog";
    public static final int LOGOUT_CODE = 0;

    private RecyclerView mRecycler;
    private View mView;
    private UserAdapter mAdapter;
    private User currentUser;


    private TextView username;
    private TextView password;


    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public static UserFragment newInstance(){
        return new UserFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView= inflater.inflate(R.layout.fragment_use, container, false);
        mRecycler = mView.findViewById(R.id.user_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        if(savedInstanceState==null) {
            mAdapter = new UserAdapter();
            mAdapter.setUserList();
            mRecycler.setAdapter(mAdapter);
        }
        return mView;
    }

    /**
     * VIEW HOLDER
     */
    private class UserHolder extends ViewHolder{

        private TextView hUsername;
        private ImageButton hDelete;
        private TextView uview;
        private View item;
        private ImageButton hDetail;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            hUsername = itemView.findViewById(R.id.usernameTxt);
            uview = itemView.findViewById(R.id.uview);
            hDelete = itemView.findViewById(R.id.Button_user_delete);
            hDetail = itemView.findViewById(R.id.Button_user_detail);
            item = itemView;

            hDelete.setOnClickListener(view -> {
                customDialogFragment fragment =
                        customDialogFragment.newInstance(null, getString(R.string.delete_user));
                fragment.setTargetFragment(UserFragment.this, DELETE_REQUEST_CODE);
                fragment.show(getFragmentManager(), DELETE_DIALOG_TAG);
            });

            item.setOnClickListener(view -> startActivity(TaskActivity.newIntent(getActivity(),
                    "ADMIN",currentUser.getID().toString())));

            hDetail.setOnClickListener(view -> userDetail(currentUser).show());
        }
        private void bind(User user) {
            currentUser = user;
            uview.setText(String.valueOf(user.getUsername().charAt(0)));
            hUsername.setText(user.getUsername());
        }
    }

    /**
     * ADAPTER
     */
    private class UserAdapter extends Adapter<UserHolder>{
        List<User> userList = new ArrayList<>();

        public void setUserList(){
            userList = UserRepository
                    .getInstance(getActivity())
                    .getUsers();
        }

        @NonNull
        @Override
        public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.user_item_holder, parent, false);
            return new UserHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull UserHolder holder, int position) {
            holder.bind(userList.get(position));

        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }

    private void updateAdapter(){
        if(mAdapter != null){
            mAdapter.setUserList();
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * MENU
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_main, menu);
        menu.findItem(R.id.profile).setIcon(R.drawable.ic_action_exit);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                customDialogFragment fragment = customDialogFragment.newInstance(null, getString(R.string.logOut));
                fragment.setTargetFragment(UserFragment.this, LOGOUT_CODE);
                fragment.show(getFragmentManager(), "Logout");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * RESULT
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==DELETE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            UserRepository.getInstance(getActivity()).deleteUser(currentUser);
            TaskRepository.getInstance(getActivity()).deleteTask(currentUser.getID());
            updateAdapter();
        }
        else if(requestCode==LOGOUT_CODE && resultCode == Activity.RESULT_OK){
            getActivity().finish();
        }
    }


    private AlertDialog userDetail(User user){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.user_detail,null, false);
        username = view.findViewById(R.id.username_show);
        password = view.findViewById(R.id.password_show);

        username.setText(user.getUsername());
        password.setText(user.getPassword().toString());

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .setNeutralButton(android.R.string.ok,null)
                .create();
        return alertDialog;

    }
}
