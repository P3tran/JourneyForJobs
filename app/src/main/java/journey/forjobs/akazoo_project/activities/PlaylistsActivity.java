package journey.forjobs.akazoo_project.activities;

import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.CursorLoader;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.application.AkazooApplication;
import journey.forjobs.akazoo_project.controllers.AkazooController;
import journey.forjobs.akazoo_project.database.DBTableHelper;
import journey.forjobs.akazoo_project.database.PlaylistContentProvider;
import journey.forjobs.akazoo_project.database.TracksContentProvider;
import journey.forjobs.akazoo_project.fragments.PlaylistsFragment;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.listAdapters.PlaylistListAdapter;
import journey.forjobs.akazoo_project.listAdapters.TracksListAdapter;
import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.model.Track;
import journey.forjobs.akazoo_project.rest.RestClient;
import journey.forjobs.akazoo_project.rest.pojos.GetPlaylistsResponse;
import journey.forjobs.akazoo_project.utils.Const;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.view.ViewGroup.LayoutParams;

public class PlaylistsActivity extends AkazooActivity {


    private boolean fetchStatus = false;

    private MyMessageReceiver mMessageReceiver = new MyMessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);

            if(intent.getStringExtra(Const.CONTROLLER_SUCCESSFULL_CALLBACK_MESSAGE) == Const.REST_PLAYLISTS_SUCCESS){
                Fragment newFragment = new PlaylistsFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_playlists_container,newFragment).commit();
            }

            if(intent.getAction() == Const.SERVICE_BIND && fetchStatus == true){
                getAkazooController().fetchPlaylists();
            }

        }
    };

    @Override
    protected MyMessageReceiver getmMessageReceiver() {
        return mMessageReceiver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlists);
        ButterKnife.inject(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(getmMessageReceiver(),
                new IntentFilter(Const.SERVICE_BIND));

        Cursor mCursor;

        String[] mProjection = {
                DBTableHelper.COLUMN_PLAYLISTS_PLAYLIST_ID,
                DBTableHelper.COLUMN_PLAYLISTS_NAME,
                DBTableHelper.COLUMN_PLAYLISTS_TRACK_COUNT
        };

        mCursor = getContentResolver().query(
                PlaylistContentProvider.CONTENT_URI,  // The content URI of the words table - You need to use TracksContentProvider.CONTENT_URI to test yours
                mProjection,                       // The columns to return for each row
                null,
                null,
                null);


        if (mCursor.moveToFirst() == false){
            Log.d("Cursor", "Cursor is null");
            fetchStatus = true;
        }else{
            Fragment newFragment = new PlaylistsFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.fragment_playlists_container,newFragment).commit();
            Log.d("Cursor", "Cursor is not null");
            //fetchStatus = false;
        }



    }

    protected void showSnackbar(String message){
        Snackbar mSnackBar = Snackbar.make(findViewById(R.id.root), message, Snackbar.LENGTH_LONG);
        mSnackBar.show();
    }



}
