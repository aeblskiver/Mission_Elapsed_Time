package fullerton.csu.justin.metapplication;

import android.arch.persistence.room.Room;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justin on 11/3/2017.
 */

public class ItineraryEventRepository {
    private static ItineraryEventRepository instance = null;
    private List<EventEntity> events;
    AppDatabase db;

    public static ItineraryEventRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ItineraryEventRepository(context);
        }
        return instance;
    }

    private ItineraryEventRepository(Context context) {
        this.events = new ArrayList<>();
        db = Room.databaseBuilder(context, AppDatabase.class, "events").build();
    }

    public int getSize() {
        return events.size();
    }

    public void loadEventsFromDB() {
        events = db.eventDao().loadAllEvents();
    }

    public List<EventEntity> getEvents() {
        return events;
    }

    public EventEntity getItem(int index) {
        return events.get(index);
    }

    public void addNewEvent(EventEntity event) {
        db.eventDao().insertEvent(event);
    }

    public void updateEvent(EventEntity event) {
        db.eventDao().updateEvent(event);
    }

    public void deleteEvent(EventEntity mEvent) {
        mEvent.setDeleted(EventEntity.TRUE);
        db.eventDao().updateEvent(mEvent);
    }
}
