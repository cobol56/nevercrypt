package com.igeltech.nevercrypt.android.settings.container;

import android.view.View;
import android.view.ViewGroup;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.fragments.CreateLocationFragment;
import com.igeltech.nevercrypt.android.locations.fragments.CreateLocationFragmentBase;
import com.igeltech.nevercrypt.android.settings.PropertyEditorBase;

public class ExistingContainerPropertyEditor extends PropertyEditorBase
{
    public ExistingContainerPropertyEditor(CreateLocationFragmentBase createEDSLocationFragment)
    {
        super(
                createEDSLocationFragment,
                R.layout.settings_create_new_or_existing_container,
                R.string.create_new_container_or_add_existing_container,
                0);
    }

    @Override
    protected View createView(ViewGroup parent)
    {
        View view = super.createView(parent);
        view.findViewById(R.id.create_new_container_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getHostFragment().showCreateNewLocationProperties();
                getHostFragment().getPropertiesView().loadProperties();
            }
        });
        view.findViewById(R.id.add_existing_container_button).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                getHostFragment().showAddExistingLocationProperties();
                getHost().getPropertiesView().loadProperties();
            }
        });
        return view;
    }

    protected CreateLocationFragment getHostFragment()
    {
        return (CreateLocationFragment) getHost();
    }
}
