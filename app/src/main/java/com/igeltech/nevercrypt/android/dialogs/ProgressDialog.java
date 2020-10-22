package com.igeltech.nevercrypt.android.dialogs;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentManager;

import com.igeltech.nevercrypt.android.R;

public class ProgressDialog extends AppCompatDialogFragment
{
	public static final String TAG = "ProgressDialog";
	public static final String ARG_TITLE = "com.igeltech.nevercrypt.android.TITLE";

	public static ProgressDialog showDialog(FragmentManager fm, String title)
	{
		Bundle args = new Bundle();
		args.putString(ARG_TITLE, title);
		ProgressDialog d = new ProgressDialog();
		d.setArguments(args);
		d.show(fm, TAG);
		return d;
	}

	public void setProgress(int progress)
	{
		if(_progressBar!=null)
			_progressBar.setProgress(progress);
	}

	public void setTitle(CharSequence title)
	{
		if(_titleTextView!=null)
			_titleTextView.setText(title);
	}

	public void setText(CharSequence text)
	{
		if(_statusTextView!=null)
			_statusTextView.setText(text);
	}

	public void setOnCancelListener(DialogInterface.OnCancelListener listener)
	{
		_cancelListener = listener;
	}

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.progress_dialog, container);
		_titleTextView = v.findViewById(android.R.id.text1);
		_statusTextView = v.findViewById(android.R.id.text2);
		_progressBar = v.findViewById(android.R.id.progress);
		setTitle(getArguments().getString(ARG_TITLE));
        return v;
    }

	@Override
	public void onCancel(DialogInterface dialog)
	{
		super.onCancel(dialog);
		if(_cancelListener!=null)
			_cancelListener.onCancel(dialog);
	}

	private DialogInterface.OnCancelListener _cancelListener;
	private AppCompatTextView _statusTextView, _titleTextView;
	private ProgressBar _progressBar;


}
