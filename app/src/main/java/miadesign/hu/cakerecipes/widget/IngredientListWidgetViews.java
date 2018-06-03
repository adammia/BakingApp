package miadesign.hu.cakerecipes.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import miadesign.hu.cakerecipes.R;
import miadesign.hu.cakerecipes.data.model.Recipe;

public class IngredientListWidgetViews extends RemoteViewsService {

    Recipe recipe;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        Bundle bundle = intent.getBundleExtra("ingredientlist");
        recipe = bundle.getParcelable("recipe");
        return new ListRemoteViewsFactory(this.getApplicationContext());
    }

    public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

        Context context;

        ListRemoteViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return recipe.getIngredients().size();
        }

        @Override
        public RemoteViews getViewAt(int position) {

            RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);

            row.setTextViewText(R.id.widget_ingredient_name, recipe.getIngredients().get(position).getIngredient());
            row.setTextViewText(R.id.ingredient_quantity, String.valueOf(recipe.getIngredients().get(position).getQuantity()));
            row.setTextViewText(R.id.ingredient_measure, recipe.getIngredients().get(position).getMeasure());

            return row;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

    }


}
