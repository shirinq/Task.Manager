package com.example.taskmanager.controller;


import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.taskmanager.LoginSign.LoginSignActivity;
import com.example.taskmanager.R;
import com.example.taskmanager.Repository.UserRepository;
import com.example.taskmanager.model.User;
import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {

    private static final int LOGOUT_CODE = 10;
    private static final String CURRENT_ITEM = "currentItem";
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private FragmentStatePagerAdapter mAdapter;
    private int[] srcString = {R.string.todo, R.string.doing, R.string.done};
    private SingleFragment todo = SingleFragment.newInstance("TODO");
    private SingleFragment doing = SingleFragment.newInstance("DOING");
    private SingleFragment done = SingleFragment.newInstance("DONE");

    private int TabNumber = 3;

    View view;

    public TabFragment() {
        // Required empty public constructor
    }

    public static Fragment newInstance() {
        TabFragment fragment = new TabFragment();
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_task, container, false);
        viewPager = view.findViewById(R.id.pager);
        tabLayout = view.findViewById(R.id.tab);

        tabLayout.setupWithViewPager(viewPager);
        mAdapter = new pagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

        if (savedInstanceState != null)
            viewPager.setCurrentItem(savedInstanceState.getInt(CURRENT_ITEM));


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    todo.updateAdapter();
                else if (position == 1)
                    doing.updateAdapter();
                else
                    done.updateAdapter();
            }
        });

        setHasOptionsMenu(true);

        return view;
    }

    private class pagerAdapter extends FragmentStatePagerAdapter {

        public pagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0)
                return todo;
            if (position == 1)
                return doing;
            return done;
        }

        @Override
        public int getCount() {
            return TabNumber;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getString(srcString[position]);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.activity_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                customDialogFragment fragment = customDialogFragment.newInstance(null, getString(R.string.logOut));
                fragment.setTargetFragment(TabFragment.this, LOGOUT_CODE);
                fragment.show(getFragmentManager(), "Logout");
                return true;
            case R.id.app_bar_search:
                getActivity().startActivity(SearchResult.newIntent(getContext()));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGOUT_CODE && resultCode == Activity.RESULT_OK) {
            UserRepository users = UserRepository.getInstance(getActivity().getApplicationContext());
            User user = users.LoggedUser();
            user.setLogged(false);
            users.updateUser(user);
            startActivity(LoginSignActivity.newIntent(getActivity()));
            getActivity().finish();
        }
    }

}
