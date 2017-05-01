package journey.forjobs.akazoo_project.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Created by Petros Efthymiou on 22/7/2016.
 */
public class DBTableHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Akazoo.db";

    public static final int DATABASE_VERSION = 1;

    public DBTableHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Table Playlists
    public static final String TABLE_PLAYLISTS = "table_playlists";
    public static final String COLUMN_PLAYLISTS_ID = "_id";
    public static final String COLUMN_PLAYLISTS_NAME = "playlist_name";
    public static final String COLUMN_PLAYLISTS_TRACK_COUNT = "playlist_track_count";
    public static final String COLUMN_PLAYLISTS_PLAYLIST_ID = "playlist_id";

    // SQL statement to create table users
    private static final String CREATE_TABLE_PLAYLISTS = "create table " + TABLE_PLAYLISTS + "(" + COLUMN_PLAYLISTS_ID + " integer primary key autoincrement, " + COLUMN_PLAYLISTS_NAME + " text, " + COLUMN_PLAYLISTS_TRACK_COUNT
            + " integer, " + COLUMN_PLAYLISTS_PLAYLIST_ID + " text" + ");";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PLAYLISTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e(DBTableHelper.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLISTS);
        onCreate(db);
    }
}
