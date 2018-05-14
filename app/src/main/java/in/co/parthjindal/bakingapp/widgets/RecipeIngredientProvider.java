package in.co.parthjindal.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import java.util.ArrayList;
import in.co.parthjindal.bakingapp.R;
import in.co.parthjindal.bakingapp.activities.RecipeStepsActivity;
import in.co.parthjindal.bakingapp.models.Recipe;
import com.google.gson.GsonBuilder;

public class RecipeIngredientProvider extends AppWidgetProvider {

    private static String mRecipeName;
    private static int stepId;
    private static ArrayList<Recipe> mRecipe;
    private static String mIngredients;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, new int[]{appWidgetId}, mIngredients, mRecipeName, mRecipe, stepId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int[] appWidgetId, String ingredients, String recipeName,
                                       ArrayList<Recipe> recipes, int id) {

        stepId = id;
        mRecipeName = recipeName;
        mRecipe = recipes;
        mIngredients = ingredients;
        Intent intent = new Intent(context, RecipeStepsActivity.class);
        intent.putExtra("current_recipe", recipeName);
        intent.putExtra("recipe", new GsonBuilder().create().toJson(mRecipe));
        intent.putExtra("id", stepId);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredient_provider);

        views.setTextViewText(R.id.widget_recipe_name, recipeName);
        views.setTextViewText(R.id.widget_ingredients, ingredients);

        views.setOnClickPendingIntent(R.id.recipe_linear_layout, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
