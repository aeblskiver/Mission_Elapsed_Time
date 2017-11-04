package fullerton.csu.justin.metapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

        //testRepo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_itinerary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_new:
                Toast.makeText(this,"New item",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_set_alarms:
                Toast.makeText(this,"Alarm set",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_cancel_alarm:
                Toast.makeText(this,"Canceled Alarms",Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

