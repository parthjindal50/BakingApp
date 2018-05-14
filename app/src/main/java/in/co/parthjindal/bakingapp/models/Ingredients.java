package in.co.parthjindal.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.squareup.moshi.Json;

public class Ingredients implements Parcelable {

    @Json(name = "ingredient")
    private String mIngredient;
    @Json(name = "quantity")
    private Double mQuantity;
    @Json(name = "measure")
    private String mMeasure;

    private Ingredients(Parcel in) {
        mQuantity = in.readDouble();
        mMeasure = in.readString();
        mIngredient = in.readString();
    }

    public Ingredients(Double mQuantity, String mMeasure, String mIngredient) {
        this.mQuantity = mQuantity;
        this.mMeasure = mMeasure;
        this.mIngredient = mIngredient;
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }

        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }
    };

    public Double getmQuantity() {
        return mQuantity;
    }

    public String getmMeasure() {
        return mMeasure;
    }

    public String getmIngredient() {
        return mIngredient;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(mQuantity);
        parcel.writeString(mMeasure);
        parcel.writeString(mIngredient);
    }
}
