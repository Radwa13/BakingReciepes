package bakingrecipes.Data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Alfa on 5/9/2018.
 */
public class Example implements Parcelable {
    @Nullable
    @SerializedName("id")
    @Expose
    private final Integer id;
    @SerializedName("name")
    @Expose
    private final String name;
    @SerializedName("ingredients")
    @Expose
    private final ArrayList<Ingredient> ingredients;

    @Nullable
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    @Nullable
    public Integer getServings() {
        return servings;
    }

    public String getImage() {
        return image;
    }

    @SerializedName("steps")
    @Expose

    private final ArrayList<Step> steps;
    @Nullable
    @SerializedName("servings")
    @Expose
    private final Integer servings;
    @SerializedName("image")
    @Expose
    private final String image;

    protected Example(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        ingredients = in.createTypedArrayList(Ingredient.CREATOR);
        steps = in.createTypedArrayList(Step.CREATOR);
        if (in.readByte() == 0) {
            servings = null;
        } else {
            servings = in.readInt();
        }
        image = in.readString();
    }

    public static final Creator<Example> CREATOR = new Creator<Example>() {
        @Override
        public Example createFromParcel(@NonNull Parcel in) {
            return new Example(in);
        }

        @Override
        public Example[] newArray(int size) {
            return new Example[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeTypedList(ingredients);
        dest.writeTypedList(steps);
        if (servings == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(servings);
        }
        dest.writeString(image);
    }
}