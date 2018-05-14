package in.co.parthjindal.bakingapp.widgets;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import in.co.parthjindal.bakingapp.R;
import in.co.parthjindal.bakingapp.models.Ingredients;
import com.google.gson.Gson;

public class WidgetProvider extends AppWidgetProvider {
    SharedPreferences sharedPreferences;

    @Override
    public void onEnabled(Context context) {
    }

    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        List<Ingredients> ingredients ;
        sharedPreferences = context.getSharedPreferences("preferences",
                Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String result = sharedPreferences.getString("ingredients",null);
        Ingredients[] arrayIngredient = gson.fromJson(result, Ingredients[].class);
        ingredients = Arrays.asList(arrayIngredient);
        ingredients = new ArrayList<>(ingredients);
        String recipeName = sharedPreferences.getString("recipe_name",null);

        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipeName, ingredients);
        }
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId, String recipeName, List<Ingredients> ingredientList) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_ingredient_provider);
        views.setTextViewText(R.id.widget_recipe_name, recipeName);


        StringBuilder ingredientString = new StringBuilder();
        Double quantity;
        String ingredientName;
        String measure;

        for (int i = 0; i < ingredientList.size(); i++) {
            ingredientName = ingredientList.get(i).getmIngredient();
            quantity = ingredientList.get(i).getmQuantity();
            measure = ingredientList.get(i).getmMeasure();
            ingredientString.append("\u25CF ");
            ingredientString.append(ingredientName);
            ingredientString.append(" (");
            ingredientString.append(quantity);
            ingredientString.append(" ");
            ingredientString.append(measure);
            ingredientString.append(")");
            ingredientString.append("\n");
        }
        String ingredient = ingredientString.toString();
        views.setTextViewText(R.id.widget_ingredients, ingredient);
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
