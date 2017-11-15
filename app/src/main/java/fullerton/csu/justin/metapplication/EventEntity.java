package fullerton.csu.justin.metapplication;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by justin on 11/8/17.
 */

@Entity(tableName = "events")
public class EventEntity {
    public static final String TRUE = "1";
    private static final String FALSE = "0";

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "timeOffSet")
    private int timeOffset;

    @ColumnInfo(name = "deleted")
    private String deleted;

    public EventEntity(String title, String description, int timeOffset) {
        this.title = title;
        this.description = description;
        this.timeOffset = timeOffset;
        this.deleted = FALSE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTimeOffset() {
        return timeOffset;
    }

    public void setTimeOffset(int timeOffset) {
        this.timeOffset = timeOffset;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
