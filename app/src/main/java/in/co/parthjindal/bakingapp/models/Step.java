package in.co.parthjindal.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

public class Step implements Parcelable {

    @Json(name = "thumbnailURL")
    private String mThumbnailURL;
    @Json(name = "id")
    private int id;
    @Json(name = "videoURL")
    private String mVideoURL;
    @Json(name = "shortDescription")
    private String shortDescription;
    @Json(name = "description")
    private String mDescription;

    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    public Step(int id, String shortDescription, String mDescription, String mVideoURL, String mThumbnailURL) {
        this.id = id;
        this.shortDescription = shortDescription;
        this.mDescription = mDescription;
        this.mVideoURL = mVideoURL;
        this.mThumbnailURL = mThumbnailURL;
    }

    public int getId() {
        return id;
    }

    public String getmVideoURL() {
        return mVideoURL;
    }

    public String getmThumbnailURL() {
        return mThumbnailURL;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getmDescription() {
        return mDescription;
    }

    protected Step(Parcel in) {
        id = in.readInt();
        shortDescription = in.readString();
        mDescription = in.readString();
        mVideoURL = in.readString();
        mThumbnailURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(shortDescription);
        parcel.writeString(mDescription);
        parcel.writeString(mVideoURL);
        parcel.writeString(mThumbnailURL);
    }
}
