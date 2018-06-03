package miadesign.hu.cakerecipes.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import miadesign.hu.cakerecipes.R;
import miadesign.hu.cakerecipes.data.model.Recipe;
import miadesign.hu.cakerecipes.ui.activity.RecipeListActivity;

public class IngredientListWidget extends AppWidgetProvider {

    public static Recipe recipe = null;

    static void updateAppWidget(Context context,
                                AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient_list);
        if (recipe != null) {
            views.setTextViewText(R.id.widget_recipe_name, recipe.getName());
            Intent listIntent = new Intent(context, IngredientListWidgetViews.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("recipe", recipe);
            listIntent.putExtra("ingredientlist", bundle);
            listIntent.setData(Uri.parse(listIntent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setRemoteAdapter(R.id.widget_ingredient_list, listIntent);

        }

        Intent intent = new Intent(context, RecipeListActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        views.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {

            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
    }


    @Override
    public void onDisabled(Context context) {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context.getApplicationContext(), IngredientListWidget.class));

        final String action = intent.getAction();

        if (action != null && action.equals(WidgetService.UPDATE_INGREDIENTS_LIST)) {

            if (intent.getExtras() != null) {
                recipe = intent.getExtras().getParcelable("recipeData");
            } else {
                Log.e(context.getPackageName(), "Recipe is null");
            }
            onUpdate(context, appWidgetManager, appWidgetIds);
        } else {
            Log.e(context.getPackageName(), "No passed data\n" + action);
        }

        super.onReceive(context, intent);
    }
}