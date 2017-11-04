package fullerton.csu.justin.metapplication;

/**
 * Created by Justin on 11/3/2017.
 */

public class ItineraryEvent {
    private int id;
    private String title;
    private String description;
    private int elapsedTime;
    private String deleted;

    public static final String TRUE = "1";
    private static final String FALSE = "0";

    public ItineraryEvent(String title, String description, int elapsedTime) {
        this.title = title;
        this.description = description;
        this.elapsedTime = elapsedTime;
        this.deleted = FALSE;
    }
    public int getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(int elapsedTime) {
        this.elapsedTime = elapsedTime;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
