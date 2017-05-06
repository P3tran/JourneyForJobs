package journey.forjobs.akazoo_project.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Arrays;
import java.util.HashSet;

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
            + "/tracks";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/track";

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

        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        // check if the caller has requested a column which does not exists
        checkColumns(projection);

        // Set the table
        queryBuilder.setTables(DBTableHelper.TABLE_TRACKS);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case TRACKS:
                break;
            case TRACK_ID:
                // adding the ID to the original query
                queryBuilder.appendWhere(DBTableHelper.COLUMN_TRACKS_ID + "="
                        + uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case TRACKS:
                id = sqlDB.insert(DBTableHelper.TABLE_TRACKS, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case TRACKS:
                rowsDeleted = sqlDB.delete(DBTableHelper.TABLE_TRACKS, selection,
                        selectionArgs);
                break;
            case TRACK_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(
                            DBTableHelper.TABLE_TRACKS,
                            DBTableHelper.COLUMN_TRACKS_ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(
                            DBTableHelper.TABLE_TRACKS,
                            DBTableHelper.COLUMN_TRACKS_ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case TRACKS:
                rowsUpdated = sqlDB.update(DBTableHelper.TABLE_TRACKS,
                        values,
                        selection,
                        selectionArgs);
                break;
            case TRACK_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(DBTableHelper.TABLE_TRACKS,
                            values,
                            DBTableHelper.COLUMN_TRACKS_ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(DBTableHelper.TABLE_TRACKS,
                            values,
                            DBTableHelper.COLUMN_TRACKS_ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {

        String[] available = { DBTableHelper.COLUMN_TRACKS_NAME,
                DBTableHelper.COLUMN_ARTIST_NAME,
                DBTableHelper.COLUMN_TRACKS_ID,
                DBTableHelper.COLUMN_TRACKS_TRACK_ID,
                DBTableHelper.COLUMN_TRACKS_PHOTO_URL};
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }

    }

}

