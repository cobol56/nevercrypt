package com.igeltech.nevercrypt.android.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.R;

import java.io.IOException;

public abstract class PathPropertyEditor extends PropertyEditorBase
{
    private static final int SELECT_PATH_REQ_CODE = 1;
    protected AppCompatTextView _selectedValueTextView;

    public PathPropertyEditor(PropertyEditor.Host host, int titleResId, int descResId, String hostFragmentTag)
    {
        super(host, R.layout.settings_path_editor, titleResId, descResId);
    }

    @Override
    public View createView(ViewGroup parent)
    {
        View view = super.createView(parent);
        _selectedValueTextView = view.findViewById(android.R.id.text1);
        AppCompatButton b = view.findViewById(android.R.id.button2);
        b.setOnClickListener(v -> {
            try
            {
                requestActivity(getSelectPathIntent(), SELECT_PATH_REQ_CODE);
            }
            catch (Exception e)
            {
                Logger.showAndLog(getHost().getContext(), e);
            }
        });
        return view;
    }

    @Override
    public void load()
    {
        _selectedValueTextView.setText(loadText());
    }

    @Override
    public void load(Bundle b)
    {
        if (_selectedValueTextView != null)
        {
            if (isInstantSave())
                load();
            else
                _selectedValueTextView.setText(b.getString(getBundleKey()));
        }
    }

    @Override
    public void save() throws Exception
    {
        saveText(_selectedValueTextView.getText().toString());
    }

    @Override
    public void save(Bundle b)
    {
        if (!isInstantSave() && _selectedValueTextView != null)
            b.putString(getBundleKey(), _selectedValueTextView.getText().toString());
    }

    protected abstract String loadText();

    protected abstract void saveText(String text) throws Exception;

    @Override
    protected void onPropertyRequestResult(int propertyRequestCode, int resultCode, Intent data)
    {
        if (propertyRequestCode == SELECT_PATH_REQ_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null)
        {
            // Get persistable permissions, required for Storage Access Framework
            _host.getContext().getContentResolver().takePersistableUriPermission(data.getData(), Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            onPathSelected(data);
        }
    }

    protected void onPathSelected(Intent result)
    {
        Uri uri = result.getData();
        _selectedValueTextView.setText(uri == null ? "" : uri.toString());
        if (!_host.getPropertiesView().isLoadingProperties() && _host.getPropertiesView().isInstantSave())
            try
            {
                save();
            }
            catch (Exception e)
            {
            }
    }

    protected abstract Intent getSelectPathIntent() throws IOException;
}
