package journey.forjobs.akazoo_project.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import journey.forjobs.akazoo_project.R;

public class TracksActivity extends AkazooActivity {

    private MyMessageReceiver mMessageReceiver = new MyMessageReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            super.onReceive(context, intent);
        }
    };

    @Override
    protected MyMessageReceiver getmMessageReceiver() {
        return mMessageReceiver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracks);
    }

}
