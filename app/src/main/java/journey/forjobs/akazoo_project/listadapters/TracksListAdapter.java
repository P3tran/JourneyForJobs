package journey.forjobs.akazoo_project.listadapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import journey.forjobs.akazoo_project.R;
import journey.forjobs.akazoo_project.model.Track;

/**
 * Created by petros on 29/4/2017.
 */

public class TracksListAdapter extends ArrayAdapter<Track> {

    List<Track> tracks = new ArrayList<Track>();
    Context context;

    public TracksListAdapter(Context context, List<Track> tracks) {
        super(context, R.layout.vw_list_item_track, tracks);
        this.tracks = tracks;
        this.context = context;
    }

    static class ViewHolder {
        @InjectView(R.id.track_name)
        TextView trackName;
        @InjectView(R.id.track_artist)
        TextView trackArtist;
        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
       if (view != null) {
            holder = (ViewHolder) view.getTag();
       } else {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.vw_list_item_track, parent, false);

            holder = new ViewHolder(view);
            view.setTag(holder);
       }
       holder.trackName.setText(tracks.get(position).getTrackName());
        holder.trackArtist.setText(tracks.get(position).getArtistName());
        return view;
    }



    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
