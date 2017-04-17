package journey.forjobs.akazoo_project.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

public class TracksContentProvider extends ContentProvider {

    // database
    private DBTableHelper database;

    // used for the UriMacher
    private static final int TRACKS = 10;
    private static final int TRACK_ID = 20;

    private static final String AUTHORITY = "journey.forjobs.akazoo_project.contentprovider_tracks";

    private static final String BASE_PATH = "tracks";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/playlists";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/playlist";

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, TRACKS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TRACK_ID);
    }

    @Override
    public boolean onCreate() {
        database = new DBTableHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor = null;

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return Uri.parse(BASE_PATH + "/" + null);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int rowsDeleted = 0;
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int rowsUpdated = 0;
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {

    }

}

