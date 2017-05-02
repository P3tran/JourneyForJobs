package journey.forjobs.akazoo_project.listAdapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.rest.pojos.GetPlaylistsResponse;
import retrofit2.Callback;

/**
 * Created by johndaratzikis on 02/05/2017.
 */

public class PlaylistListAdapter extends ArrayAdapter<Playlist> {

    List<Playlist> playlists = new ArrayList<Playlist>();
    Context context;

    public PlaylistListAdapter(Context context, ArrayList<Playlist> playlists) {
        super(context, R.layout.vw_list_item_playlist, playlists);
        this.context = context;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        PlaylistListAdapter.ViewHolder holder;

        if (view != null){
            holder = (PlaylistListAdapter.ViewHolder) view.getTag();
        }else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.vw_list_item_playlist, parent, false);

            holder = new PlaylistListAdapter.ViewHolder(view);
            view.setTag(holder);
        }
        holder.playlistName.setText(playlists.get(position).getName());
        holder.playlistItemCount.setText(String.valueOf(playlists.get(position).getItemCount()));


        return view;
    }

    static class ViewHolder{
        @InjectView(R.id.playlist_name)
        TextView playlistName;
        @InjectView(R.id.playlist_item_count)
        TextView playlistItemCount;
        public ViewHolder(View view){
            ButterKnife.inject(this, view);
        }
    }

    public List<Playlist> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.playlists = playlists;
    }
}
