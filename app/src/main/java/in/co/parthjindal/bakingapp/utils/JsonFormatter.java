package in.co.parthjindal.bakingapp.utils;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import in.co.parthjindal.bakingapp.models.Ingredients;
import in.co.parthjindal.bakingapp.models.Recipe;
import in.co.parthjindal.bakingapp.models.Step;
import in.co.parthjindal.bakingapp.widgets.RecipeWidgetUpdateService;


public class JsonFormatter {

    private static SharedPreferences mPreferences;
    private static final String TAG = JsonFormatter.class.getSimpleName();
    private static final String THEJSON = "theJson";

    public static List<Recipe> fetchRecipeData(Context context, SharedPreferences preferences) {

        String jsonResponse = null;
        try {
            jsonResponse = loadJSONFromAsset(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPreferences = preferences;
        mPreferences.edit().putString(THEJSON, jsonResponse).apply();

        return extractDataFromJson();
    }

    public static ArrayList<Ingredients> extractIngredientsFromJson(int mId, SharedPreferences preferences) {
        if (mPreferences == null && preferences != null)
            mPreferences = preferences;

        String json = mPreferences.getString(THEJSON, "");
        int id;
        double quantity;
        ArrayList<Ingredients> mIngredients = new ArrayList<>();
        try {
            JSONArray baseJsonArray = new JSONArray(json);
            for (int i = 0; i < baseJsonArray.length(); i++) {
                JSONObject currentRecipe = baseJsonArray.getJSONObject(i);
                id = currentRecipe.getInt("id");
                if (mId == id) {
                    JSONArray ingredientsArray = currentRecipe.getJSONArray("ingredients");
                    for (int j = 0; j < ingredientsArray.length(); j++) {
                        JSONObject currentIngredient = ingredientsArray.getJSONObject(j);
                        quantity = currentIngredient.getDouble("quantity");
                        String measure = currentIngredient.getString("measure");
                        String ingredient = currentIngredient.getString("ingredient");
                        mIngredients.add(new Ingredients(quantity, measure, ingredient));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mIngredients;
    }

    public static ArrayList<Recipe> extractDataFromJson() {
        String json = mPreferences.getString(THEJSON, "");
        int id;
        String name;
        int servings;
        String imageUrl;
        ArrayList<Recipe> mRecipe = new ArrayList<>();
        try {
            JSONArray baseJsonArray = new JSONArray(json);
            for (int i = 0; i < baseJsonArray.length(); i++) {
                JSONObject currentRecipe = baseJsonArray.getJSONObject(i);
                id = currentRecipe.getInt("id");
                name = currentRecipe.getString("name");
                servings = currentRecipe.getInt("servings");
                imageUrl = currentRecipe.getString("image");
                mRecipe.add(new Recipe(id, name, servings, imageUrl));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mRecipe;
    }

    public static void setWedgetData(Context context, int id, AppWidgetManager appWidgetManager) {
        List<Recipe> recipes = extractDataFromJson();
        Bundle bundle = new Bundle();
        for (Recipe recipe : recipes)
            if (recipe.getId() == id)
                RecipeWidgetUpdateService.startActionUpdateRecipe(context, id, recipe.getName(), (ArrayList<Recipe>) recipes);
    }

    public static ArrayList<Step> extractStepsFromJson(int mId) {
        String json = mPreferences.getString(THEJSON, "");
        int id;
        ArrayList<Step> mSteps = new ArrayList<>();
        try {
            JSONArray baseJsonArray = new JSONArray(json);
            for (int i = 0; i < baseJsonArray.length(); i++) {
                JSONObject currentRecipe = baseJsonArray.getJSONObject(i);
                id = currentRecipe.getInt("id");
                if (mId == id) {
                    JSONArray stepsArray = currentRecipe.getJSONArray("steps");
                    for (int j = 0; j < stepsArray.length(); j++) {
                        JSONObject currentStep = stepsArray.getJSONObject(j);
                        int stepId = currentStep.getInt("id");
                        Log.d(TAG, "StepId: " + stepId);
                        String sDescription = currentStep.getString("shortDescription");
                        String description = currentStep.getString("description");
                        String videoUrl = currentStep.getString("videoURL");
                        String thumbnail = currentStep.getString("thumbnailURL");
                        mSteps.add(new Step(stepId, sDescription, description, videoUrl, thumbnail));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mSteps;
    }

    public static String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("baking.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    public static ArrayList<Ingredients> extractIngredientsFromJson(int mId) {
        return extractIngredientsFromJson(mId, null);
    }
}