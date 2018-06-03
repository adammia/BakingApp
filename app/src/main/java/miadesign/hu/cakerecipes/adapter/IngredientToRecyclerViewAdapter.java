package miadesign.hu.cakerecipes.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import miadesign.hu.cakerecipes.R;
import miadesign.hu.cakerecipes.data.model.Ingredient;

import java.util.ArrayList;

public class IngredientToRecyclerViewAdapter extends RecyclerView.Adapter<IngredientToRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Ingredient> ingredientList;

    IngredientToRecyclerViewAdapter(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_ingredients_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientToRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.ingredientName.setText(ingredientList.get(position).getIngredient());
        holder.ingredientQuantity.setText(String.valueOf(ingredientList.get(position).getQuantity()));
        holder.ingredientMeasure.setText(ingredientList.get(position).getMeasure());
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final TextView ingredientName;
        final TextView ingredientQuantity;
        final TextView ingredientMeasure;

        ViewHolder(final View itemView) {
            super(itemView);

            ingredientName = itemView.findViewById(R.id.ingredient_name);
            ingredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
            ingredientMeasure = itemView.findViewById(R.id.ingredient_measure);

        }
    }

}
