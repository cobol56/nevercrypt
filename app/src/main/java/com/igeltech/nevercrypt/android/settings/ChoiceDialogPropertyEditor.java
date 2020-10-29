package com.igeltech.nevercrypt.android.settings;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.settings.dialogs.ChoiceDialog;

import java.util.List;

public abstract class ChoiceDialogPropertyEditor extends PropertyEditorBase
{
    private final String _hostFragmentTag;
    protected int _selectedEntry = -1;
    protected AppCompatTextView _selectedItems;
    private List<String> _entries;
    private AppCompatButton _selectButton;

    public ChoiceDialogPropertyEditor(Host host, int titleResId, int descResId, String hostFragmentTag)
    {
        super(host, R.layout.settings_choice_dialog_editor, titleResId, descResId);
        _entries = getEntries();
        _hostFragmentTag = hostFragmentTag;
    }

    public ChoiceDialogPropertyEditor(Host host, int propertyId, String title, String desc, String hostFragmentTag)
    {
        super(host, propertyId, R.layout.settings_choice_dialog_editor, title, desc);
        _entries = getEntries();
        _hostFragmentTag = hostFragmentTag;
    }

    @Override
    public View createView(ViewGroup parent)
    {
        View view = super.createView(parent);
        _selectedItems = view.findViewById(android.R.id.text1);
        _selectButton = view.findViewById(android.R.id.button1);
        _selectButton.setOnClickListener(view1 -> startChoiceDialog());
        return view;
    }

    public int getSelectedEntry()
    {
        return _selectedEntry;
    }

    public void setSelectedEntry(int val)
    {
        _selectedEntry = val;
        updateSelectionText();
        if (_host.getPropertiesView().isInstantSave())
            save();
    }

    @Override
    public void load()
    {
        _entries = getEntries();
        _selectButton.setVisibility(_entries.size() < 2 ? View.GONE : View.VISIBLE);
        _selectedEntry = loadValue();
        updateSelectionText();
    }

    @Override
    public void load(Bundle b)
    {
        if (_selectButton != null)
        {
            if (isInstantSave())
                load();
            else
            {
                _entries = getEntries();
                _selectButton.setVisibility(_entries.size() < 2 ? View.GONE : View.VISIBLE);
                _selectedEntry = b.getInt(getBundleKey());
                updateSelectionText();
            }
        }
    }

    @Override
    public void save()
    {
        saveValue(_selectedEntry);
    }

    @Override
    public void save(Bundle b)
    {
        if (!isInstantSave() && _selectButton != null)
            b.putInt(getBundleKey(), _selectedEntry);
    }

    protected abstract int loadValue();

    protected abstract void saveValue(int value);

    protected abstract List<String> getEntries();

    private void updateSelectionText()
    {
        if (_selectedEntry >= 0 && _selectedEntry < _entries.size())
            _selectedItems.setText(_entries.get(_selectedEntry));
        else
            _selectedItems.setText("");
    }

    private void startChoiceDialog()
    {
        ChoiceDialog.showDialog(_host.getFragmentManager(), getId(), _title != null ? _title : _host.getContext().getString(_titleResId), _entries, _hostFragmentTag);
    }
}
