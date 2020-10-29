package com.igeltech.nevercrypt.android.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;

import com.igeltech.nevercrypt.android.fragments.TaskFragment;
import com.igeltech.nevercrypt.android.settings.views.PropertiesView;

public interface PropertyEditor
{
    String ARG_PROPERTY_ID = "com.igeltech.nevercrypt.android.PROPERTY_ID";
    String ARG_HOST_FRAGMENT_TAG = TaskFragment.ARG_HOST_FRAGMENT;

    interface Host
    {
        Context getContext();

        void startActivityForResult(Intent intent, int requestCode);

        PropertiesView getPropertiesView();

        FragmentManager getFragmentManager();
        //void updateView();
    }

    int getId();

    void setId(int id);

    void load();

    void save() throws Exception;

    void save(Bundle b);

    void load(Bundle b);

    View getView(ViewGroup parent);

    Host getHost();

    boolean onActivityResult(int requestCode, int resultCode, Intent data);

    void onClick();

    int getStartPosition();

    void setStartPosition(int pos);
}
