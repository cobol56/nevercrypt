package com.igeltech.nevercrypt.android.settings.container;

import android.content.Intent;
import android.net.Uri;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.filemanager.activities.FileManagerActivity;
import com.igeltech.nevercrypt.android.locations.fragments.CreateContainerFragment;
import com.igeltech.nevercrypt.android.locations.fragments.CreateContainerFragmentBase;
import com.igeltech.nevercrypt.android.locations.fragments.CreateLocationFragment;
import com.igeltech.nevercrypt.android.locations.tasks.CreateContainerTaskFragmentBase;
import com.igeltech.nevercrypt.android.settings.PathPropertyEditor;

import java.io.IOException;


public abstract class PathToContainerPropertyEditorBase extends PathPropertyEditor
{

    public PathToContainerPropertyEditorBase(CreateContainerFragmentBase createLocationFragment)
    {
        super(createLocationFragment,
                R.string.path_to_container,
                0,
                createLocationFragment.getTag());
    }

    @Override
    protected void onTextChanged(final String newValue)
    {
        super.onTextChanged(newValue);
        getHostFragment().getActivity().invalidateOptionsMenu();
    }

    protected CreateContainerFragment getHostFragment()
    {
        return (CreateContainerFragment) getHost();
    }


    @Override
    protected Intent getSelectPathIntent() throws IOException
    {
        boolean addExisting = getHostFragment().getState().getBoolean(CreateLocationFragment.ARG_ADD_EXISTING_LOCATION);
        boolean isEncFs = getHostFragment().isEncFsFormat();
        Intent i = FileManagerActivity.getSelectPathIntent(
                getHost().getContext(),
                null,
                false,
                true,
                isEncFs || addExisting,
                !addExisting,
                true,
                true);
        i.putExtra(FileManagerActivity.EXTRA_ALLOW_SELECT_FROM_CONTENT_PROVIDERS, true);
        return i;
    }

    @Override
    protected void saveText(String text)
    {
        getHostFragment().getState().putParcelable(CreateContainerTaskFragmentBase.ARG_LOCATION, Uri.parse(text));
    }

    @Override
    protected String loadText()
    {
        Uri uri = getHostFragment().getState().getParcelable(CreateContainerTaskFragmentBase.ARG_LOCATION);
        return uri!=null ? uri.toString() : null;
    }
}
