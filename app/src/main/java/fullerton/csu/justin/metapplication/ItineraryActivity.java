package fullerton.csu.justin.metapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItineraryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String TAG = "ItinerayActivity";
    ItineraryEventRepository eventsRepo;
    ArrayList<ItineraryEvent> events;
    private ListView itemsListview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itinerary);

        itemsListview = (ListView) findViewById(R.id.itineraryListView);
        itemsListview.setOnItemClickListener(this);

        //Set title in action bar
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.activity_itin_title);

        // Create events repository
        eventsRepo = ItineraryEventRepository.getInstance(getApplicationContext());
        new ReadFromDatabase().execute();
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
                Intent intent = new Intent(this, EventActivity.class);
                startActivity(intent);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra("index", i);
        startActivity(intent);
    }


    // Asynchronous code for accessing database and updating display
    class ReadFromDatabase extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            eventsRepo.loadEventsFromDB();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "Database entries loaded");
            updateDisplay();
        }
    }

    private void updateDisplay() {
        events = eventsRepo.getEvents();
        if (events == null) {
            Toast.makeText(this, "No events found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Found " + events.size() + " events", Toast.LENGTH_SHORT).show();
        }

        ArrayList<HashMap<String, String>> eventData =
                new ArrayList<>();
        for (ItineraryEvent event: events) {
            HashMap<String, String> map = new HashMap<>();
            map.put("elapsedTime", event.getElapsedTime());
            map.put("title", event.getTitle());
            eventData.add(map);
        }

        //Create resource, from, and to variables
        int resource = R.layout.listview_item;
        String[] from = {"elapsedTime", "title"};
        int[] to = {R.id.listviewItemElapsedTime, R.id.listviewItemTitle};

        //Create and set adapter
        SimpleAdapter adapter = new SimpleAdapter(this, eventData, resource, from, to);
        itemsListview.setAdapter(adapter);
    }
}