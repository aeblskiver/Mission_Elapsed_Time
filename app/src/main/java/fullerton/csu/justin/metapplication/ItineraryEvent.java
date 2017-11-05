package fullerton.csu.justin.metapplication;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Justin on 11/3/2017.
 */

public class ItineraryEvent implements Parcelable {
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

    public ItineraryEvent(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.elapsedTime = in.readInt();
        this.deleted = in.readString();
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

    @Override
    public String toString() {
        return title + " " + description;
    }

    //Parcelable implementation
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public ItineraryEvent createFromParcel(Parcel in) {
            return new ItineraryEvent(in);
        }

        @Override
        public ItineraryEvent[] newArray(int i) {
            return new ItineraryEvent[i];
        }
    };

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeInt(this.id);
        out.writeString(this.title);
        out.writeString(this.description);
        out.writeInt(this.elapsedTime);
        out.writeString(this.deleted);
    }
}
