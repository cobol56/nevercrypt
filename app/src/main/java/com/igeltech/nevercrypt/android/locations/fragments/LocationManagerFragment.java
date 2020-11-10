package com.igeltech.nevercrypt.android.locations.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.igeltech.nevercrypt.android.R;

public class LocationManagerFragment extends Fragment
{
    public static final String EXTRA_LOCATION_TYPE = "com.igeltech.nevercrypt.android.LOCATION_TYPE";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState == null)
            getChildFragmentManager().
                    beginTransaction().
                    add(R.id.location_list_container, new ContainerListFragment(), LocationListBaseFragment.TAG).
                    commit();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_locationmanager, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        ExtendedFloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(view1 -> Navigation.findNavController(view).navigate(LocationManagerFragmentDirections.actionLocationListFragmentToCreateLocationActivity()));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater)
    {
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        NavDirections directions = null;
        switch (item.getItemId())
        {
            case R.id.main_menu_stop_and_exit:
                getActivity().
                        getSupportFragmentManager().
                        beginTransaction().
                        add(new ExitFragment(), ExitFragment.TAG).
                        commit();
                break;
            case R.id.main_menu_settings:
                directions = LocationManagerFragmentDirections.actionLocationManagerFragmentToProgramSettingsActivity();
                break;
            case R.id.main_menu_about:
                directions = LocationManagerFragmentDirections.actionLocationListFragmentToAboutFragment();
                break;
        }
        if (directions != null)
            Navigation.findNavController(getActivity(), R.id.nav_host_locationmanager).navigate(directions);
        return super.onOptionsItemSelected(item);
    }
}





