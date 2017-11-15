package fullerton.csu.justin.metapplication;

import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

public class EventActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    public static final String TAG = "EventActivity";
    public static final int MILLIS_IN_MINUTES = 1000 * 60;
    public static final int MINUTES_IN_HOUR = 60;

    EditText editTextTitle;
    EditText editTextDescription;
    TimePicker timePicker;

    ItineraryEventRepository eventsRepo;
    private EventEntity mEvent;
    int index;
    String elapsedTime;
    private boolean isNew;
    private String newTitle;
    private String newDescription;
    private String newHour;
    private String newMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        //Initialize widgets
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);

        //Set listeners
        editTextTitle.setOnEditorActionListener(this);
        editTextDescription.setOnEditorActionListener(this);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int hourOfDay, int minute) {
                newHour = String.format("%02d", hourOfDay);
                newMinute = String.format("%02d", minute);

            }
        });

        //Get events repo
        eventsRepo = ItineraryEventRepository.getInstance(getApplicationContext());

        //Get intent and extras
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            index = intent.getIntExtra("index", -1);
            //update display
            mEvent = eventsRepo.getItem(index);
            updateDisplay();
            isNew = false;
        } else {
            //create new event
            isNew = true;
            timePicker.setMinute(0);
            timePicker.setHour(0);
        }
    }

    private void updateDisplay() {
        mEvent = eventsRepo.getItem(index);
        editTextTitle.setText(mEvent.getTitle());
        editTextDescription.setText(mEvent.getDescription());
        updateTimePicker(mEvent.getTimeOffset());
    }

    private void updateTimePicker(int elapsedTime) {
        long minutes = elapsedTime / MILLIS_IN_MINUTES;
        long hours = minutes / MINUTES_IN_HOUR;
        timePicker.setHour((int) hours);
        timePicker.setMinute((int) (minutes  - (hours * MINUTES_IN_HOUR)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                saveEventInfo();
                return true;
            case R.id.menu_discard_changes:
                Toast.makeText(this,"Discarding", Toast.LENGTH_SHORT).show();
                //Go back to main activity without saving anything
                startIntentToMainActivity();
                return true;
            case R.id.menu_delete:
                Toast.makeText(this,"Deleting", Toast.LENGTH_SHORT).show();
                deleteEvent();
                startIntentToMainActivity();
                //Delete event (after confirmation) and go back to main activity
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteEvent() {
        new Thread(new Runnable() {
            public void run() {
                eventsRepo.deleteEvent(mEvent);
            }
        }).start();
    }

    private void saveEventInfo() {
        if (isNew) {
            saveNewEvent();
        } else
        {
            saveUpdatedEvent();
        }
        startIntentToMainActivity();
    }

    private void startIntentToMainActivity() {
        Intent intent = new Intent(this, ItineraryActivity.class);
        startActivity(intent);
    }

    private void saveUpdatedEvent() {
        Log.d(TAG, "saveEventInfo: " + mEvent.getId());
        if (newTitle != null) mEvent.setTitle(newTitle);
        if (newDescription != null) mEvent.setDescription(newDescription);
        if (newHour != null || newMinute != null) mEvent.setTimeOffset(getTimeOffsetInMillis());
        new Thread(new Runnable() {
            @Override
            public void run() {
                eventsRepo.updateEvent(mEvent);
            }
        }).start();
    }

    private int getTimeOffsetInMillis() {
        return (int) TimeUnit.HOURS.toMillis(timePicker.getHour())
                    + (int) TimeUnit.MINUTES.toMillis(timePicker.getMinute());
    }

    private void saveNewEvent() {
        new Thread(new Runnable() {
            public void run() {
                eventsRepo.addNewEvent(new EventEntity(
                        newTitle,
                        newDescription,
                        getTimeOffsetInMillis()
                ));
            }
        }).start();
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

        if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
        {
            setEventTextView(textView);
        }
        return true;
    }

    private void setEventTextView(TextView textView) {
        switch (textView.getId()) {
            case R.id.editTextTitle:
                newTitle = textView.getText().toString();
                Log.d(TAG, "set Title: " + newTitle);
                break;
            case R.id.editTextDescription:
                newDescription = textView.getText().toString();
                Log.d(TAG, "set Descr: " + newDescription);
                break;
        }
    }

    //Decide whether it's a new one or edited one
    //We can pass an intent with extras from ItinActivity
    //If extra is null? Create a new one. Right?
    //If extra is not null, grab the necessary info
    //Extra will be index of the item in the list.
}
