package in.co.parthjindal.bakingapp.widgets;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import in.co.parthjindal.bakingapp.utils.JsonFormatter;
import in.co.parthjindal.bakingapp.models.Ingredients;
import in.co.parthjindal.bakingapp.models.Recipe;

public class RecipeWidgetUpdateService extends IntentService {

    private ArrayList<Ingredients> mIngredients;
    private static ArrayList<Recipe> mRecipe;
    private static int id;
    private static String mRecipeName;
    public static final String UPDATE_RECIPE_INGREDIENTS = "in.co.parthjindal.bakingapp.action.update_recipe";

    public RecipeWidgetUpdateService() {
        super("RecipeWidgetUpdateService");
    }

    private void handleActionUpdateRecipe() {
        StringBuilder ingredientString = new StringBuilder();
        Double quantity;
        String ingredientName;
        String measure;
        mIngredients = JsonFormatter.extractIngredientsFromJson(id);
        for (int i = 0; i < mIngredients.size(); i++) {
            ingredientName = mIngredients.get(i).getmIngredient();
            quantity = mIngredients.get(i).getmQuantity();
            measure = mIngredients.get(i).getmMeasure();
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
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeIngredientProvider.class));
        RecipeIngredientProvider.updateAppWidget(this, appWidgetManager, appWidgetIds, ingredient, mRecipeName, mRecipe, id);
    }

    public static void startActionUpdateRecipe(Context context, int recipeId, String recipeName, ArrayList<Recipe> recipes) {
        mRecipe = recipes;
        id = recipeId;
        mRecipeName = recipeName;
        Intent intent = new Intent(context, RecipeWidgetUpdateService.class);
        intent.setAction(UPDATE_RECIPE_INGREDIENTS);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (UPDATE_RECIPE_INGREDIENTS.equals(action)) {
                handleActionUpdateRecipe();
            }
        }
    }
}
