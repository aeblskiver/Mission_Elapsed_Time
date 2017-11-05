package fullerton.csu.justin.metapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class EventActivity extends AppCompatActivity {

    public static final String TAG = "EventActivity";

    EditText editTextTitle;
    EditText editTextDescription;
    TimePicker timePicker;

    ItineraryEventRepository eventsRepo;
    ItineraryEvent event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        //Initialize widgets
        editTextTitle = (EditText) findViewById(R.id.editTextTitle);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true);



        //Get intent and extras
        Intent intent = getIntent();
        //int index = intent.getIntExtra("index");
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
                Toast.makeText(this,"Saving", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_discard_changes:
                Toast.makeText(this,"Discarding", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_delete:
                Toast.makeText(this,"Deleting", Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Decide whether it's a new one or edited one
    //We can pass an intent with extras from ItinActivity
    //If extra is null? Create a new one. Right?
    //If extra is not null, grab the necessary info
    //Extra will be index of the item in the list.
}
