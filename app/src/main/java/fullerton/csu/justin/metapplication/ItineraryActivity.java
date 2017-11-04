package fullerton.csu.justin.metapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ItineraryActivity extends AppCompatActivity {

    public static final String TAG = "ItinerayActivity";
    ItineraryEventsDB db;
    ItineraryEventRepository repo;
    List<ItineraryEvent> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        repo = new ItineraryEventRepository(this);
        events = repo.getEvents();

        testRepo();
    }

    private void testRepo() {
        for (ItineraryEvent event : events) {
            Log.d(TAG, event.getTitle());
            Log.d(TAG, event.getDescription());
        }
    }


//TODO clean up this mess
//    public void testDB() {
//        ArrayList<ItineraryEvent> events = new ArrayList<>();
//
//        events = db.getEvents();
//        db.insertEvent(new ItineraryEvent("Wake up", "Wake the fuck up", 15));
//
//        if (events != null) {
//            for (ItineraryEvent event : events) {
//                        Log.d(TAG, "Title " + event.getTitle());
//                    }
//        } else {
//            Log.d(TAG, "testDB: No events :(");
//        }
//    }


}
