package miadesign.hu.cakerecipes.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import miadesign.hu.cakerecipes.data.model.Ingredient;

import java.util.ArrayList;

public class IngredientToRecyclerView {

    private RecyclerView recyclerView;

    private ProgressBar progressBar;

    public IngredientToRecyclerView(RecyclerView recyclerView, ProgressBar progressBar) {
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;
    }

    public void loadIngredients(ArrayList<Ingredient> ingredientList) {

        IngredientToRecyclerViewAdapter recyclerAdapter = new IngredientToRecyclerViewAdapter(ingredientList);

        recyclerView.setAdapter(recyclerAdapter);

        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }
}
