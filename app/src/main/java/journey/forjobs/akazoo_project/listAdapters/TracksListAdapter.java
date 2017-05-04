package journey.forjobs.akazoo_project.listAdapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.model.Playlist;
import journey.forjobs.akazoo_project.model.Track;

/**
 * Created by johndaratzikis on 28/04/2017.
 */

public class TracksListAdapter extends ArrayAdapter<Track>{

    List<Track> tracks = new ArrayList<Track>();
    Context context;

    public TracksListAdapter(Context context, ArrayList<Track> tracks) {
        super(context, R.layout.vw_list_item_track, tracks);
        this.context = context;
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public View getView(int position,View view,ViewGroup parent) {
        ViewHolder holder;

        if (view != null){
            holder = (ViewHolder) view.getTag();
        }else {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.vw_list_item_track, parent, false);

            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder.trackName.setText(tracks.get(position).getTrackName());
        holder.trackArtist.setText(tracks.get(position).getArtistName());
        holder.trackId.setText(Long.toString(tracks.get(position).getTrackId()));
        Picasso.with(context).load(tracks.get(position).getImageUrl()).into(holder.trackImageView);

        return view;
    }

    static class ViewHolder{
        @InjectView(R.id.track_name)
        TextView trackName;
        @InjectView(R.id.track_artist)
        TextView trackArtist;
        @InjectView(R.id.track_id)
        TextView trackId;
        @InjectView(R.id.iv_image_view)
        ImageView trackImageView;
        public ViewHolder(View view){
            ButterKnife.inject(this, view);
        }
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
