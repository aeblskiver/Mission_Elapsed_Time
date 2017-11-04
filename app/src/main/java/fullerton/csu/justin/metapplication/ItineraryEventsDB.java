package fullerton.csu.justin.metapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Justin on 11/3/2017.
 */

public class ItineraryEventsDB {
    //Database name and version constants
    private static final String DB_NAME = "events.db";
    private static final int    DB_VERSION = 2;
    private static final String TAG = "Events Database";

    // Database and helper objects
    private SQLiteDatabase db;
    private DBHelper dbHelper;


    // Event table constants
    private static final String EVENT_TABLE = "event";

    private static final String EVENT_ID = "_id";
    public static final int EVENT_ID_COL = 0;

    private static final String EVENT_TITLE = "event_title";
    private static final int EVENT_TITLE_COL = 1;

    private static final String EVENT_DESCRIPTION = "event_description";
    private static final int EVENT_DESCRIPTION_COL = 2;

    private static final String EVENT_ELAPSED_TIME = "event_elapsed";
    private static final int EVENT_ELAPSED_TIME_COL = 3;

    private static final String EVENT_IS_DELETED = "event_deleted";
    public static final int EVENT_IS_DELETED_COL = 4;

    // Create and drop table Constants
    private static final String CREATE_EVENT_TABLE =
            "CREATE TABLE " + EVENT_TABLE + " (" +
                    EVENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    EVENT_TITLE + " TEXT NOT NULL, " +
                    EVENT_DESCRIPTION + " TEXT NOT NULL, " +
                    EVENT_ELAPSED_TIME + " INTEGER NOT NULL, " + "" +
                    EVENT_IS_DELETED + " TEXT NOT NULL);";

    private static final String DROP_EVENT_TABLE =
            "DROP TABLE IF EXISTS " + EVENT_TABLE;

    public ItineraryEventsDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    public ArrayList<ItineraryEvent> getEvents() {
        String where =
                EVENT_IS_DELETED + " = 0"; // Get all events that are not deleted
        try {
            this.openReadableDB();
            Cursor cursor = db.query(EVENT_TABLE, null,
                    where, null, null, null, null); //TODO: Order clause
            ArrayList<ItineraryEvent> events = new ArrayList<>();

            while (cursor.moveToNext()) {
                events.add(getEventFromCursor(cursor));
            }
            cursor.close();
            return  events;
        } catch (SQLiteException e) {
            Log.d(TAG, "Exception occurred " + e.getMessage());
            return null;
        } finally {
            this.closeDB();
        }
    }

    //Returns -1 if it fails
    public long insertEvent(ItineraryEvent event) {
        ContentValues cv = new ContentValues();
        cv.put(EVENT_TITLE, event.getTitle());
        cv.put(EVENT_DESCRIPTION, event.getDescription());
        cv.put(EVENT_ELAPSED_TIME, event.getElapsedTime());
        cv.put(EVENT_IS_DELETED, event.getDeleted());
        long rowID = -1;

        try {
            this.openWriteableDB();
            rowID = db.insert(EVENT_TABLE, null, cv);
        } catch (SQLiteException e) {
            Log.d(TAG, "Exception occurred " + e.getMessage());
        } finally {
            this.closeDB();
        }
        return rowID;
    }

    public int updateEvent(ItineraryEvent event) {
        ContentValues cv = new ContentValues();
        cv.put(EVENT_TITLE, event.getTitle());
        cv.put(EVENT_DESCRIPTION, event.getDescription());
        cv.put(EVENT_ELAPSED_TIME, event.getElapsedTime());
        cv.put(EVENT_IS_DELETED, event.getDeleted());

        String where = EVENT_ID + "= ?";
        String[] whereArgs = { String.valueOf(event.getId()) };

        int rowCount = 0;

        try {
            this.openWriteableDB();
             rowCount = db.update(EVENT_TABLE, cv, where, whereArgs);
        } catch (SQLiteException e) {
            Log.d(TAG, "Exception occurred " + e.getMessage());
        } finally {
            this.closeDB();
        }
        return rowCount;
    }

    private static ItineraryEvent getEventFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        } else {
            try {

                return new ItineraryEvent(
                        cursor.getString(EVENT_TITLE_COL),
                        cursor.getString(EVENT_DESCRIPTION_COL),
                        cursor.getInt(EVENT_ELAPSED_TIME_COL));
            } catch (Exception e) {
                Log.d(TAG, "getEventFromCursor: Error " + e.getMessage());
                return null;
            }
        }
    }



    private static class DBHelper extends SQLiteOpenHelper {

        DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_EVENT_TABLE);

            //Insert sample events;
            db.execSQL("INSERT INTO event(event_title, event_description, event_elapsed, event_deleted) VALUES('Title','Descr',0,'0')"); //TODO comment out
            db.execSQL("INSERT INTO event(event_title, event_description, event_elapsed, event_deleted) VALUES('Title2','Descr2',15,'0')"); //TODO comment out

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("Event table", "Upgrading db from " + oldVersion + "to " + newVersion);

            db.execSQL(ItineraryEventsDB.DROP_EVENT_TABLE);
            onCreate(db);
        }
    }
}
