package fullerton.csu.justin.metapplication;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by justin on 11/8/17.
 */
@Database(entities = {EventEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    abstract public EventDao eventDao();
}
