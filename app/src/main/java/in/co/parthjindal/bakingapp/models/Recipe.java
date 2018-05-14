package in.co.parthjindal.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

public class Recipe implements Parcelable {

    @Json(name = "servings")
    private int servings;
    @Json(name = "id")
    private int id;
    @Json(name = "image")
    private String mImageUrl;
    @Json(name = "name")
    private String name;

    private Recipe(Parcel in) {
        servings = in.readInt();
        id = in.readInt();
        mImageUrl = in.readString();
        name = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public Recipe(int id, String name, int servings, String imageUrl) {
        this.servings = servings;
        this.id = id;
        this.mImageUrl = imageUrl;
        this.name = name;
    }

    public int getServings() {
        return servings;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(servings);
        parcel.writeString(mImageUrl);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
