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
    private ArrayList<ItineraryEvent> events;
    ItineraryEventsDB db;

    public ItineraryEventRepository(Context context) {
        this.events = new ArrayList<>();
        db = new ItineraryEventsDB(context);
    }

    public void loadEventsFromDB() {
        events = db.getEvents();
    }

    public ArrayList<ItineraryEvent> getEvents() {
        return events;
    }

}
