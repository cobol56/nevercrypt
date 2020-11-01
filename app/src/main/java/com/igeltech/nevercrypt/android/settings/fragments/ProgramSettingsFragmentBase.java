package com.igeltech.nevercrypt.android.settings.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.igeltech.nevercrypt.android.CryptoApplication;
import com.igeltech.nevercrypt.android.Logger;
import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.dialogs.MasterPasswordDialog;
import com.igeltech.nevercrypt.android.dialogs.PasswordDialog;
import com.igeltech.nevercrypt.android.fragments.PropertiesFragmentBase;
import com.igeltech.nevercrypt.android.settings.ButtonPropertyEditor;
import com.igeltech.nevercrypt.android.settings.CategoryPropertyEditor;
import com.igeltech.nevercrypt.android.settings.ChoiceDialogPropertyEditor;
import com.igeltech.nevercrypt.android.settings.IntPropertyEditor;
import com.igeltech.nevercrypt.android.settings.MultilineTextPropertyEditor;
import com.igeltech.nevercrypt.android.settings.SwitchPropertyEditor;
import com.igeltech.nevercrypt.android.settings.UserSettings;
import com.igeltech.nevercrypt.android.settings.program.ExtFileManagerPropertyEditor;
import com.igeltech.nevercrypt.crypto.SecureBuffer;
import com.igeltech.nevercrypt.settings.Settings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.igeltech.nevercrypt.android.settings.UserSettingsCommon.DISABLE_DEBUG_LOG;
import static com.igeltech.nevercrypt.android.settings.UserSettingsCommon.DISABLE_MODIFIED_FILES_BACKUP;
import static com.igeltech.nevercrypt.android.settings.UserSettingsCommon.DISABLE_WIDE_SCREEN_LAYOUTS;
import static com.igeltech.nevercrypt.android.settings.UserSettingsCommon.DONT_USE_CONTENT_PROVIDER;
import static com.igeltech.nevercrypt.android.settings.UserSettingsCommon.EXTENSIONS_MIME;
import static com.igeltech.nevercrypt.android.settings.UserSettingsCommon.FORCE_TEMP_FILES;
import static com.igeltech.nevercrypt.android.settings.UserSettingsCommon.IS_FLAG_SECURE_ENABLED;
import static com.igeltech.nevercrypt.android.settings.UserSettingsCommon.MAX_FILE_SIZE_TO_OPEN;
import static com.igeltech.nevercrypt.android.settings.UserSettingsCommon.NEVER_SAVE_HISTORY;
import static com.igeltech.nevercrypt.android.settings.UserSettingsCommon.SHOW_PREVIEWS;
import static com.igeltech.nevercrypt.android.settings.UserSettingsCommon.USE_INTERNAL_IMAGE_VIEWER;
import static com.igeltech.nevercrypt.android.settings.UserSettingsCommon.WIPE_TEMP_FILES;

public abstract class ProgramSettingsFragmentBase extends PropertiesFragmentBase implements MasterPasswordDialog.PasswordReceiver
{
    protected UserSettings _settings;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        _settings = UserSettings.getSettings(getActivity());
        super.onCreate(savedInstanceState);
    }

    public UserSettings getSettings()
    {
        return _settings;
    }

    public SharedPreferences.Editor editSettings()
    {
        return _settings.getSharedPreferences().edit();
    }

    //master password is set
    @Override
    public void onPasswordEntered(PasswordDialog dlg)
    {
        char[] data = dlg.getPassword();
        if (data != null && data.length == 0)
            data = null;
        CryptoApplication.setMasterPassword(data == null ? null : new SecureBuffer(data));
        try
        {
            _settings.saveSettingsProtectionKey();
        }
        catch (Settings.InvalidSettingsPassword ignored)
        {
        }
    }

    //master password is not set
    @Override
    public void onPasswordNotEntered(PasswordDialog dlg)
    {
    }

    @Override
    protected void createProperties()
    {
        getPropertiesView().setInstantSave(true);
        createCategories();
        _propertiesView.setPropertiesState(false);
        _propertiesView.setPropertyState(R.string.main_settings, true);
    }

    protected void createCategories()
    {
        final List<Integer> commonPropertiesList = new ArrayList<>();
        getPropertiesView().addProperty(new CategoryPropertyEditor(this, R.string.main_settings, 0)
        {
            @Override
            public void load()
            {
                enableProperties(commonPropertiesList, isExpanded());
            }
        });
        createCommonProperties(commonPropertiesList);
    }

    protected void createCommonProperties(List<Integer> commonPropertiesIds)
    {
        commonPropertiesIds.add(getPropertiesView().addProperty(new ButtonPropertyEditor(this, R.string.master_password, 0, R.string.enter_master_password)
        {
            @Override
            protected void onButtonClick()
            {
                Bundle args = new Bundle();
                args.putBoolean(MasterPasswordDialog.ARG_VERIFY_PASSWORD, true);
                args.putString(MasterPasswordDialog.ARG_RECEIVER_FRAGMENT_TAG, getTag());
                args.putString(MasterPasswordDialog.ARG_LABEL, getString(R.string.enter_new_password));
                MasterPasswordDialog mpd = new MasterPasswordDialog();
                mpd.setArguments(args);
                mpd.show(getFragmentManager(), MasterPasswordDialog.TAG);
            }
        }));
        commonPropertiesIds.add(getPropertiesView().addProperty(new SwitchPropertyEditor(this, R.string.show_previews, 0)
        {
            @Override
            protected boolean loadValue()
            {
                return _settings.showPreviews();
            }

            @Override
            protected void saveValue(boolean value)
            {
                editSettings().putBoolean(SHOW_PREVIEWS, value).commit();
            }
        }));
        commonPropertiesIds.add(getPropertiesView().addProperty(new SwitchPropertyEditor(this, R.string.disable_wide_screen_layouts, R.string.disable_wide_screen_layouts_desc)
        {
            @Override
            protected boolean loadValue()
            {
                return _settings.disableLargeSceenLayouts();
            }

            @Override
            protected void saveValue(boolean value)
            {
                editSettings().putBoolean(DISABLE_WIDE_SCREEN_LAYOUTS, value).commit();
            }
        }));
        commonPropertiesIds.add(getPropertiesView().addProperty(new SwitchPropertyEditor(this, R.string.never_save_history, R.string.never_save_history_desc)
        {
            @Override
            protected boolean loadValue()
            {
                return _settings.neverSaveHistory();
            }

            @Override
            protected void saveValue(boolean value)
            {
                editSettings().putBoolean(NEVER_SAVE_HISTORY, value).commit();
            }
        }));
        commonPropertiesIds.add(getPropertiesView().addProperty(new ChoiceDialogPropertyEditor(this, R.string.internal_image_viewer_mode, R.string.internal_image_viewer_mode_desc, getTag())
        {
            @Override
            protected int loadValue()
            {
                return _settings.getInternalImageViewerMode();
            }

            @Override
            protected void saveValue(int value)
            {
                if (value >= 0)
                    editSettings().putInt(USE_INTERNAL_IMAGE_VIEWER, value).commit();
                else
                    editSettings().remove(USE_INTERNAL_IMAGE_VIEWER).commit();
            }

            @Override
            protected List<String> getEntries()
            {
                String[] modes = getResources().getStringArray(R.array.image_viewer_use_mode);
                return Arrays.asList(modes);
            }
        }));
        commonPropertiesIds.add(getPropertiesView().addProperty(new ExtFileManagerPropertyEditor(this)));
        commonPropertiesIds.add(getPropertiesView().addProperty(new IntPropertyEditor(this, R.string.max_temporary_file_size, R.string.max_temporary_file_size_desc, getTag())
        {
            @Override
            protected int getDialogViewResId()
            {
                return R.layout.settings_edit_num_lim4;
            }

            @Override
            protected int loadValue()
            {
                return _settings.getMaxTempFileSize();
            }

            @Override
            protected void saveValue(int value)
            {
                if (value >= 0)
                    editSettings().putInt(MAX_FILE_SIZE_TO_OPEN, value).commit();
                else
                    editSettings().remove(MAX_FILE_SIZE_TO_OPEN).commit();
            }
        }));
        commonPropertiesIds.add(getPropertiesView().addProperty(new SwitchPropertyEditor(this, R.string.overwrite_temp_files_with_random_data, R.string.overwrite_temp_files_with_random_data_desc)
        {
            @Override
            protected boolean loadValue()
            {
                return _settings.wipeTempFiles();
            }

            @Override
            protected void saveValue(boolean value)
            {
                editSettings().putBoolean(WIPE_TEMP_FILES, value).commit();
            }
        }));
        commonPropertiesIds.add(getPropertiesView().addProperty(new MultilineTextPropertyEditor(this, R.string.extension_mime_override, R.string.extension_mime_override_desc, getTag())
        {
            @Override
            protected String loadText()
            {
                return _settings.getExtensionsMimeMapString();
            }

            @Override
            protected void saveText(String text)
            {
                if (text != null)
                    editSettings().putString(EXTENSIONS_MIME, text).commit();
                else
                    editSettings().remove(EXTENSIONS_MIME).commit();
            }
        }));
        commonPropertiesIds.add(getPropertiesView().addProperty(new SwitchPropertyEditor(this, R.string.debug_log, 0)
        {
            @Override
            protected boolean loadValue()
            {
                return !_settings.disableDebugLog();
            }

            @Override
            protected void saveValue(final boolean value)
            {
                editSettings().putBoolean(DISABLE_DEBUG_LOG, !value).commit();
                if (!value)
                {
                    Logger.closeLogger();
                    Logger.disableLog(true);
                }
                else
                    try
                    {
                        Logger.disableLog(false);
                        Logger.initLogger();
                    }
                    catch (IOException e)
                    {
                        Logger.showErrorMessage(getContext(), e);
                    }
            }
        }));
        commonPropertiesIds.add(getPropertiesView().addProperty(new SwitchPropertyEditor(this, R.string.disable_modified_files_backup, 0)
        {
            @Override
            protected boolean loadValue()
            {
                return _settings.disableModifiedFilesBackup();
            }

            @Override
            protected void saveValue(final boolean value)
            {
                editSettings().putBoolean(DISABLE_MODIFIED_FILES_BACKUP, value).commit();
            }
        }));
        commonPropertiesIds.add(getPropertiesView().addProperty(new SwitchPropertyEditor(this, R.string.hide_app_screen_from_other_apps, 0)
        {
            @Override
            protected boolean loadValue()
            {
                return _settings.isFlagSecureEnabled();
            }

            @Override
            protected void saveValue(final boolean value)
            {
                editSettings().putBoolean(IS_FLAG_SECURE_ENABLED, value).commit();
            }
        }));
        commonPropertiesIds.add(getPropertiesView().addProperty(new SwitchPropertyEditor(this, R.string.always_force_close_containers, R.string.always_force_close_containers_desc)
        {
            @Override
            protected void saveValue(boolean value)
            {
                editSettings().putBoolean(UserSettings.FORCE_UNMOUNT, value).commit();
            }

            @Override
            protected boolean loadValue()
            {
                return _settings.alwaysForceClose();
            }
        }));
        commonPropertiesIds.add(getPropertiesView().addProperty(new SwitchPropertyEditor(this, R.string.dont_use_content_provider, R.string.dont_use_content_provider_desc)
        {
            @Override
            protected boolean loadValue()
            {
                boolean value = _settings.dontUseContentProvider();
                getPropertiesView().setPropertyState(R.string.force_temp_files, getPropertiesView().isPropertyEnabled(getId()) && !value);
                return value;
            }

            @Override
            protected void saveValue(final boolean value)
            {
                editSettings().putBoolean(DONT_USE_CONTENT_PROVIDER, value).commit();
                getPropertiesView().setPropertyState(R.string.force_temp_files, !value);
            }
        }));
        commonPropertiesIds.add(getPropertiesView().addProperty(new SwitchPropertyEditor(this, R.string.force_temp_files, R.string.force_temp_files_desc)
        {
            @Override
            protected boolean loadValue()
            {
                return _settings.forceTempFiles();
            }

            @Override
            protected void saveValue(final boolean value)
            {
                editSettings().putBoolean(FORCE_TEMP_FILES, value).commit();
            }
        }));
    }

    protected void enableProperties(Iterable<Integer> propIds, boolean enable)
    {
        getPropertiesView().setPropertiesState(propIds, enable);
        getPropertiesView().loadProperties();
    }
}
