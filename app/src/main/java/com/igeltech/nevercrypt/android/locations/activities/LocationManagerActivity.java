package com.igeltech.nevercrypt.android.locations.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.helpers.AppInitHelper;
import com.igeltech.nevercrypt.android.helpers.CompatHelper;
import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.locations.LocationsManager;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.util.concurrent.CancellationException;

public class LocationManagerActivity extends RxAppCompatActivity
{
    public static final String TAG = "LocationManagerActivity";

    private final BroadcastReceiver _closeAllReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            finish();
        }
    };

    AppBarConfiguration appBarConfiguration;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locationmanager);
        // Widget setup
        NavController navController = Navigation.findNavController(this, R.id.nav_host_locationmanager);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        // Security
        if (UserSettings.getSettings(this).isFlagSecureEnabled())
            CompatHelper.setWindowFlagSecure(this);
        // Register broadcasts
        registerReceiver(_closeAllReceiver, new IntentFilter(LocationsManager.BROADCAST_CLOSE_ALL));
        // Check master password, if any
        AppInitHelper.
                createObservable(this).
                compose(bindToLifecycle()).
                subscribe(() -> {
                }, err -> {
                    if (!(err instanceof CancellationException))
                        Logger.showAndLog(getApplicationContext(), err);
                });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.main_menu, menu);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_locationmanager);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    protected void onDestroy()
    {
        unregisterReceiver(_closeAllReceiver);
        super.onDestroy();
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        Logger.debug("FileManagerActivity has started");
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Logger.debug("LocationListActivity has stopped");
    }
}
