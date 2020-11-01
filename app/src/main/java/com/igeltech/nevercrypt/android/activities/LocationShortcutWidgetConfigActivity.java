package com.igeltech.nevercrypt.android.activities;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.fragments.PropertiesFragmentBase;
import com.igeltech.nevercrypt.android.settings.PathPropertyEditor;
import com.igeltech.nevercrypt.android.settings.PropertyEditor;
import com.igeltech.nevercrypt.android.settings.TextPropertyEditor;
import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.android.widgets.LocationShortcutWidget;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;
import com.igeltech.nevercrypt.locations.Openable;
import com.igeltech.nevercrypt.settings.Settings;

import java.io.IOException;

public class LocationShortcutWidgetConfigActivity extends SettingsBaseActivity
{
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setResult(RESULT_CANCELED);
    }

    @Override
    protected Fragment getSettingsFragment()
    {
        return new MainFragment();
    }

    public static class MainFragment extends PropertiesFragmentBase
    {
        private static final String ARG_TITLE = "title";
        private static final String ARG_URI = "uri";
        private final Bundle _state = new Bundle();

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        protected void createProperties()
        {
            _propertiesView.addProperty(new TextPropertyEditor(this, R.string.enter_widget_title, 0, getTag())
            {
                @Override
                protected String loadText()
                {
                    return _state.getString(ARG_TITLE);
                }

                @Override
                protected void saveText(String text) throws Exception
                {
                    _state.putString(ARG_TITLE, text);
                }
            });
            _propertiesView.addProperty(getPathPE());
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
        {
            inflater.inflate(R.menu.widget_config_menu, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem menuItem)
        {
            if (menuItem.getItemId() == R.id.confirm)
            {
                createWidget();
                return true;
            }
            return super.onOptionsItemSelected(menuItem);
        }

        protected PropertyEditor getPathPE()
        {
            return new TargetPathPropertyEditor();
        }

        private void createWidget()
        {
            try
            {
                _propertiesView.saveProperties();
                String title = _state.getString(ARG_TITLE);
                String path = _state.getString(ARG_URI);
                if (title == null || title.trim().isEmpty() || path == null || path.trim().isEmpty())
                    return;
                initWidgetFields(title, path);
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, getWidgetId());
                getActivity().setResult(RESULT_OK, resultValue);
                getActivity().finish();
            }
            catch (Exception e)
            {
                Logger.showAndLog(getActivity(), e);
            }
        }

        private void initWidgetFields(String title, String path) throws Exception
        {
            int widgetId = getWidgetId();
            Location target = LocationsManager.getLocationsManager(getActivity()).getDefaultLocationFromPath(path);
            Settings.LocationShortcutWidgetInfo info = new Settings.LocationShortcutWidgetInfo();
            info.widgetTitle = title;
            info.locationUriString = target.getLocationUri().toString();
            UserSettings.getSettings(getContext()).setLocationShortcutWidgetInfo(widgetId, info);
            LocationShortcutWidget.setWidgetLayout(getContext(), AppWidgetManager.getInstance(getContext()), widgetId, info, (!(target instanceof Openable)) || ((Openable) target).isOpen());
        }

        private int getWidgetId()
        {
            return getActivity().getIntent().getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        public class TargetPathPropertyEditor extends PathPropertyEditor
        {
            public TargetPathPropertyEditor()
            {
                super(MainFragment.this, R.string.target_path, 0, getTag());
            }

            @Override
            protected String loadText()
            {
                return _state.getString(ARG_URI);
            }

            @Override
            protected void saveText(String text) throws Exception
            {
                _state.putString(ARG_URI, text);
            }

            @Override
            protected Intent getSelectPathIntent() throws IOException
            {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");
                return intent;
            }
        }
    }
}
