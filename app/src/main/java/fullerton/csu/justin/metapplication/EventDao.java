package fullerton.csu.justin.metapplication;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by justin on 11/8/17.
 */

@Dao
public interface EventDao {
    @Query("SELECT * FROM events WHERE deleted=0 ORDER BY timeOffSet")
    public List<EventEntity> loadAllEvents();

    @Query("SELECT * FROM events WHERE id = :search")
    public EventEntity getEvent(int search);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertEvent(EventEntity event);

    @Update
    public void updateEvent(EventEntity event);

    @Delete
    public void deleteEvent(EventEntity event);
}
