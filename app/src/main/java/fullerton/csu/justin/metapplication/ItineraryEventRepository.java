package fullerton.csu.justin.metapplication;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Justin on 11/3/2017.
 */

public class ItineraryEventRepository {
    private static ItineraryEventRepository instance = null;
    private ArrayList<ItineraryEvent> events;
    ItineraryEventsDB db;

    public static ItineraryEventRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ItineraryEventRepository(context);
        }
        return instance;
    }

    private ItineraryEventRepository(Context context) {
        this.events = new ArrayList<>();
        db = new ItineraryEventsDB(context);
    }

    public void loadEventsFromDB() {
        events = db.getEvents();
    }

    public ArrayList<ItineraryEvent> getEvents() {
        return events;
    }

    public ItineraryEvent getItem(int index) {
        return events.get(index);
    }

    public void addNewEvent(ItineraryEvent event) {
        db.insertEvent(event);
    }

    public void updateEvent(ItineraryEvent event) {
        db.updateEvent(event);
    }

    public void deleteEvent(ItineraryEvent mEvent) {
        mEvent.setDeleted(ItineraryEvent.TRUE);
        db.updateEvent(mEvent);
    }
}
