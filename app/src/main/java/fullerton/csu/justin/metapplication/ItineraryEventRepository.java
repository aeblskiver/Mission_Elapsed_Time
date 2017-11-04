package fullerton.csu.justin.metapplication;

import android.content.Context;
import android.os.AsyncTask;

import java.util.ArrayList;
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
        loadEventsFromDB();
    }

    public void loadEventsFromDB() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                events = db.getEvents();
            }
        };

        Timer timer = new Timer(true);
        timer.schedule(task,0);
    }

    public ArrayList<ItineraryEvent> getEvents() {
        return events;
    }
}
