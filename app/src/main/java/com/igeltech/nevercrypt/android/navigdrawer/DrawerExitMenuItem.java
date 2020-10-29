package com.igeltech.nevercrypt.android.navigdrawer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;

import com.igeltech.nevercrypt.android.R;
import com.igeltech.nevercrypt.android.locations.closer.fragments.LocationCloserBaseFragment;
import com.igeltech.nevercrypt.android.service.FileOpsService;
import com.igeltech.nevercrypt.locations.Location;
import com.igeltech.nevercrypt.locations.LocationsManager;

import java.util.Iterator;

public class DrawerExitMenuItem extends DrawerMenuItemBase
{
    private static Drawable _icon;

    public DrawerExitMenuItem(DrawerControllerBase drawerController)
    {
        super(drawerController);
    }

    private synchronized static Drawable getIcon(Context context)
    {
        if (_icon == null)
        {
            _icon = context.getResources().getDrawable(R.drawable.ic_exit, context.getTheme());
        }
        return _icon;
    }

    @Override
    public String getTitle()
    {
        return getDrawerController().getMainActivity().getString(R.string.stop_service_and_exit);
    }

    @Override
    public void onClick(View view, int position)
    {
        super.onClick(view, position);
        getDrawerController().
                getMainActivity().
                getSupportFragmentManager().
                beginTransaction().
                add(new ExitFragment(), ExitFragment.TAG).
                commit();
    }

    @Override
    public Drawable getIcon()
    {
        return getIcon(getDrawerController().getMainActivity());
    }

    public static class ExitFragment extends Fragment implements LocationCloserBaseFragment.CloseLocationReceiver
    {
        public static final String TAG = "com.igeltech.nevercrypt.android.ExitFragment";

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            closeNextOrExit();
        }

        @Override
        public void onTargetLocationClosed(Location location, Bundle closeTaskArgs)
        {
            closeNextOrExit();
        }

        @Override
        public void onTargetLocationNotClosed(Location location, Bundle closeTaskArgs)
        {
        }

        private void closeNextOrExit()
        {
            Iterator<Location> it = LocationsManager.getLocationsManager(getActivity()).getLocationsClosingOrder().iterator();
            if (it.hasNext())
                launchCloser(it.next());
            else
                exit();
        }

        private void launchCloser(Location loc)
        {
            Bundle args = new Bundle();
            args.putString(LocationCloserBaseFragment.PARAM_RECEIVER_FRAGMENT_TAG, TAG);
            LocationsManager.storePathsInBundle(args, loc, null);
            Fragment closer = LocationCloserBaseFragment.getDefaultCloserForLocation(loc);
            closer.setArguments(args);
            getFragmentManager().beginTransaction().add(closer, LocationCloserBaseFragment.getCloserTag(loc)).commit();
        }

        private void exit()
        {
            FileOpsService.clearTempFolder(getActivity().getApplicationContext(), true);
            getActivity().finish();
        }
    }
}
