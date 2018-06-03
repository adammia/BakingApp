package miadesign.hu.cakerecipes.widget;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import miadesign.hu.cakerecipes.data.model.Recipe;

public class WidgetService extends IntentService {

    public static final String UPDATE_INGREDIENTS_LIST = "miadesign.hu.cakerecipes.datatoui.action.update_widget";

    public WidgetService() {
        super("WidgetService");
    }

    public static void startActionUpdateWidget(Context context, Recipe recipe) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(UPDATE_INGREDIENTS_LIST);
        intent.putExtra("recipeData", recipe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (UPDATE_INGREDIENTS_LIST.equals(action)) {
                if (intent.getExtras() != null) {
                    Recipe recipe = intent.getExtras().getParcelable("recipeData");
                    handleActionUpdateWidgetList(recipe);
                } else {
                    Log.e(getPackageName(), "Failed to load widget data");
                }
            }
        }
    }

    private void handleActionUpdateWidgetList(Recipe recipe) {
        Intent intent = new Intent(UPDATE_INGREDIENTS_LIST);
        intent.putExtra("recipeData", recipe);
        intent.setAction(UPDATE_INGREDIENTS_LIST);
        sendBroadcast(intent);

    }


}
