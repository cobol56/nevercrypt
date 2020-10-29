package com.igeltech.nevercrypt.android.dialogs;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.settings.activities.OpeningOptionsActivity;
import com.igeltech.nevercrypt.crypto.SecureBuffer;
import com.igeltech.nevercrypt.locations.LocationsManager;
import com.igeltech.nevercrypt.locations.Openable;
import com.trello.rxlifecycle3.components.support.RxDialogFragment;

public abstract class PasswordDialogBase extends RxDialogFragment
{
    public static final String TAG = "com.igeltech.nevercrypt.android.dialogs.PasswordDialog";
    public static final String ARG_LABEL = "com.igeltech.nevercrypt.android.LABEL";
    public static final String ARG_VERIFY_PASSWORD = "com.igeltech.nevercrypt.android.VERIFY_PASSWORD";
    public static final String ARG_HAS_PASSWORD = "com.igeltech.nevercrypt.android.HAS_PASSWORD";
    public static final String ARG_RECEIVER_FRAGMENT_TAG = "com.igeltech.nevercrypt.android.RECEIVER_FRAGMENT_TAG";
    protected static final int REQUEST_OPTIONS = 1;
    protected AppCompatTextView _labelTextView;
    protected AppCompatEditText _passwordEditText, _repeatPasswordEditText;
    protected Openable _location;
    protected Bundle _options;
    protected SecureBuffer _passwordResult, _repeatPasswordSB;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        _location = (Openable) LocationsManager.
                getLocationsManager(getActivity()).
                getFromBundle(getArguments(), null);
        _options = savedInstanceState == null ? getArguments() : savedInstanceState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.password_dialog, container);
        _labelTextView = v.findViewById(R.id.label);
        if (_labelTextView != null)
        {
            String label = loadLabel();
            if (label != null)
            {
                _labelTextView.setText(label);
                _labelTextView.setVisibility(View.VISIBLE);
            }
            else
                _labelTextView.setVisibility(View.GONE);
        }
        _passwordEditText = v.findViewById(R.id.password_et);
        _repeatPasswordEditText = v.findViewById(R.id.repeat_password_et);
        if (_passwordEditText != null)
        {
            if (hasPassword())
            {
                _passwordEditText.setVisibility(View.VISIBLE);
            }
            else
            {
                _passwordResult = null;
                _passwordEditText.setVisibility(View.GONE);
            }
        }
        else
            _passwordResult = null;
        if (_repeatPasswordEditText != null)
        {
            TextInputLayout _layout = v.findViewById(R.id.repeat_password_til);
            if (hasPassword() && isPasswordVerificationRequired())
            {
                _repeatPasswordEditText.setVisibility(View.VISIBLE);
                _layout.setVisibility(View.VISIBLE);
            }
            else
            {
                _repeatPasswordSB = null;
                _repeatPasswordEditText.setVisibility(View.GONE);
                _layout.setVisibility(View.GONE);
            }
        }
        else
            _repeatPasswordSB = null;
        View passwordLayout = v.findViewById(R.id.password_layout);
        if (passwordLayout != null)
        {
            if (hasPassword())
            {
                passwordLayout.setVisibility(View.VISIBLE);
                _passwordEditText.requestFocus();
                /*lifecycle().
                        filter(event -> event == FragmentEvent.RESUME).
                        subscribe(event -> {
                            final InputMethodManager imm = (InputMethodManager) _passwordEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            if(imm != null)
                                imm.showSoftInput(_passwordEditText, InputMethodManager.SHOW_FORCED);
                            //_passwordEditText.requestFocus();
                        });*/
            }
            else
                passwordLayout.setVisibility(hasPassword() ? View.VISIBLE : View.GONE);
        }
        AppCompatButton b = v.findViewById(android.R.id.button1);
        if (b != null)
            b.setOnClickListener(view -> confirm());
        AppCompatImageButton ib = v.findViewById(R.id.settings);
        if (ib != null)
        {
            if (_location == null)
                ib.setVisibility(View.GONE);
            ib.setOnClickListener(v1 -> openOptions());
        }
        return v;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (_passwordResult != null)
        {
            _passwordResult.close();
            _passwordResult = null;
        }
        if (_repeatPasswordSB != null)
        {
            _repeatPasswordSB.close();
            _repeatPasswordSB = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == REQUEST_OPTIONS)
        {
            if (resultCode == AppCompatActivity.RESULT_OK)
                _options = data.getExtras();
        }
        else
        {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public char[] getPassword()
    {
        if (hasPassword() && _passwordEditText != null)
        {
            Editable pwd = _passwordEditText.getText();
            char[] res = new char[pwd.length()];
            pwd.getChars(0, res.length, res, 0);
            return res;
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        if (_options != null)
            outState.putAll(_options);
    }

    public boolean hasPassword()
    {
        Bundle args = getArguments();
        return args != null && args.getBoolean(ARG_HAS_PASSWORD, _location != null && _location.hasPassword());
    }

    public Bundle getOptions()
    {
        return _options;
    }

    protected String loadLabel()
    {
        Bundle args = getArguments();
        return args != null ? args.getString(ARG_LABEL) : null;
    }

    protected boolean isPasswordVerificationRequired()
    {
        Bundle args = getArguments();
        return args != null && args.getBoolean(ARG_VERIFY_PASSWORD, false);
    }

    protected void openOptions()
    {
        Intent i = new Intent(getActivity(), OpeningOptionsActivity.class);
        if (_location != null)
            LocationsManager.storePathsInIntent(i, _location, null);
        i.putExtras(_options);
        startActivityForResult(i, REQUEST_OPTIONS);
    }

    protected PasswordReceiver getResultReceiver()
    {
        Bundle args = getArguments();
        String recTag = args != null ? args.getString(ARG_RECEIVER_FRAGMENT_TAG) : null;
        return recTag != null ? (PasswordReceiver) getFragmentManager().findFragmentByTag(recTag) : null;
    }

    protected boolean checkInput()
    {
        if (hasPassword() && isPasswordVerificationRequired())
        {
            if (!checkPasswordsMatch())
            {
                Toast.makeText(getActivity(), R.string.password_does_not_match, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    protected void confirm()
    {
        if (!checkInput())
            return;
        onPasswordEntered();
        dismiss();
    }

    protected boolean checkPasswordsMatch()
    {
        return _passwordEditText.getText().toString().equals(_repeatPasswordEditText.getText().toString());
    }

    protected void onPasswordEntered()
    {
        PasswordReceiver r = getResultReceiver();
        if (r != null)
            r.onPasswordEntered((PasswordDialog) this);
        else
        {
            FragmentActivity act = getActivity();
            if (act instanceof PasswordReceiver)
                ((PasswordReceiver) act).onPasswordEntered((PasswordDialog) this);
        }
    }

    @Override
    public void onCancel(DialogInterface dialog)
    {
        onPasswordNotEntered();
    }

    protected void onPasswordNotEntered()
    {
        PasswordReceiver r = getResultReceiver();
        if (r != null)
            r.onPasswordNotEntered((PasswordDialog) this);
        else
        {
            FragmentActivity act = getActivity();
            if (act instanceof PasswordReceiver)
                ((PasswordReceiver) act).onPasswordNotEntered((PasswordDialog) this);
        }
    }

    public interface PasswordReceiver
    {
        void onPasswordEntered(PasswordDialog dlg);

        void onPasswordNotEntered(PasswordDialog dlg);
    }
}
