package fullerton.csu.justin.metapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.AlarmClock;
import android.support.annotation.NonNull;
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
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

public class ItineraryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    public static final String TAG = "ItineraryActivity";
    public static final int MILLIS_IN_HOUR = 1000 * 60 * 60;
    public static final int MILLIS_IN_SECOND = 1000 * 60;
    public static final int MINUTES_IN_HOUR = 60;
    ItineraryEventRepository eventsRepo;
    private ListView itemsListview;
    private Calendar mCurrentTime;
    private int mCurrentIntentIndex;
    private static final int REQUEST_DISMISS = 2;
    private boolean alarmSet = false;

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
                Intent intent = new Intent(this, EventActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_set_alarms:
                if (!alarmSet) {
                    alarmSet = true;
                    setAlarms();
                }
                return true;
            case R.id.menu_cancel_alarm:
                if (alarmSet) {
                    alarmSet = false;
                    cancelAlarms();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cancelAlarms() {
        if (eventsRepo.getSize() > 0) {
            mCurrentIntentIndex = 0;
                Intent intent = getIntentForDismiss(eventsRepo.getItem(mCurrentIntentIndex));
                startActivityForResult(intent, ++mCurrentIntentIndex);
            }
        }

    @NonNull
    private Intent getIntentForDismiss(EventEntity item) {
        return new Intent(AlarmClock.ACTION_DISMISS_ALARM)
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                .putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_LABEL)
                .putExtra(AlarmClock.EXTRA_MESSAGE, eventsRepo.getItem(mCurrentIntentIndex).getTitle());
    }


    private void setAlarms() {
        if (eventsRepo.getEvents().size() > 0) {
            mCurrentTime = new GregorianCalendar();
            mCurrentIntentIndex = 0;
            EventEntity firstItem = eventsRepo.getItem(mCurrentIntentIndex);
            Intent intent = getIntentForNewAlarm(firstItem);
            mCurrentIntentIndex++;
            startActivityForResult(intent, mCurrentIntentIndex);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (alarmSet && requestCode < eventsRepo.getSize()) {
            Log.d(TAG, "Request code: " + requestCode);
            Intent intent = getIntentForNewAlarm(eventsRepo.getItem(requestCode));
            requestCode++;
            this.startActivityForResult(intent, requestCode);
        }
        else if (!alarmSet && requestCode < eventsRepo.getSize()) {
            Log.d(TAG, "Canceled through " + requestCode + " times.");
            Intent intent = getIntentForDismiss(eventsRepo.getItem(requestCode));
            requestCode++;
            this.startActivityForResult(intent, requestCode);
        } else {
            onResume();
             }
        }


    @NonNull
    private Intent getIntentForNewAlarm(EventEntity item) {
        int alarmHour = getHourForAlarm(item);
        int alarmMinute = getMinutesForAlarm(item);
        Log.d(TAG, "Time set for: " + alarmHour + ":" + alarmMinute);
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_HOUR, alarmHour)
                .putExtra(AlarmClock.EXTRA_MINUTES, alarmMinute)
                .putExtra(AlarmClock.EXTRA_MESSAGE, item.getTitle())
                .putExtra(AlarmClock.EXTRA_SKIP_UI, true)
                .putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_LABEL);
        return intent;
    }

    private int getMinutesForAlarm(EventEntity item) {
        return (item.getTimeOffset() / MILLIS_IN_SECOND - ((item.getTimeOffset() / MILLIS_IN_HOUR) * MINUTES_IN_HOUR)
                + mCurrentTime.get(GregorianCalendar.MINUTE)) % MINUTES_IN_HOUR;
    }

    private int getHourForAlarm(EventEntity item) {
        return ((item.getTimeOffset() / MILLIS_IN_HOUR) + mCurrentTime.get(GregorianCalendar.HOUR_OF_DAY)) % 24;
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
        List<EventEntity> events = eventsRepo.getEvents();
        if (events == null) {
            Toast.makeText(this, "No events found", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Found " + events.size() + " events", Toast.LENGTH_SHORT).show();
        }

        ArrayList<HashMap<String, String>> eventData =
                new ArrayList<>();
        //String elapsedTime = String.format("%02d", );
        for (EventEntity event: events) {
            long minutes = event.getTimeOffset() / (1000 * 60);
            Log.d(TAG, "updateDisplay: Milliseconds= " + event.getTimeOffset());
            Log.d(TAG, "updateDisplay: Minutes= " + minutes);
            long hours = minutes / 60;
            String hourFormat = String.format("%02d", hours);
            String minuteFormat = String.format("%02d", (minutes - (hours * 60)));
            HashMap<String, String> map = new HashMap<>();
            map.put("elapsedTime", hourFormat + ":" + minuteFormat);
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