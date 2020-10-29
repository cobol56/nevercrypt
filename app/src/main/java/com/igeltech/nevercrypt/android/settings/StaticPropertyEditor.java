package com.igeltech.nevercrypt.android.settings;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.helpers.CompatHelper;

public class StaticPropertyEditor extends PropertyEditorBase
{
    protected AppCompatTextView _descTextView;

    public StaticPropertyEditor(PropertyEditor.Host host, int titleResId)
    {
        this(host, titleResId, 0);
    }

    public StaticPropertyEditor(PropertyEditor.Host host, int titleResId, int descResId)
    {
        super(host, R.layout.settings_simple_editor, titleResId, descResId);
    }

    @Override
    public View createView(ViewGroup parent)
    {
        View view = super.createView(parent);
        _descTextView = view.findViewById(R.id.desc);
        return view;
    }

    @Override
    public void load()
    {
        String txt = loadText();
        if (txt != null)
        {
            _descTextView.setVisibility(View.VISIBLE);
            _descTextView.setText(txt);
        }
        else
            _descTextView.setVisibility(View.GONE);
    }

    @Override
    public void load(Bundle b)
    {
        if (_descTextView != null)
        {
            if (isInstantSave())
                load();
            else
                _descTextView.setText(b.getString(getBundleKey()));
        }
    }

    @Override
    public void save()
    {
    }

    @Override
    public void save(Bundle b)
    {
        if (!isInstantSave() && _descTextView != null)
            b.putString(getBundleKey(), _descTextView.getText().toString());
    }

    @Override
    public void onClick()
    {
        super.onClick();
        CompatHelper.storeTextInClipboard(getHost().getContext(), _descTextView.getText().toString());
        Toast.makeText(getHost().getContext(), R.string.text_has_been_copied, Toast.LENGTH_SHORT).show();
    }

    protected String loadText()
    {
        return null;
    }
}
