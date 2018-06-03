package miadesign.hu.cakerecipes.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import miadesign.hu.cakerecipes.data.idlingresource.EspressoIdlingResource;
import miadesign.hu.cakerecipes.data.model.Recipe;
import miadesign.hu.cakerecipes.data.network.ApiUtils;
import miadesign.hu.cakerecipes.data.network.RecipeService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeToRecyclerView {

    private String LOG_TAG;

    private Context context;

    private RecyclerView recyclerView;

    private ProgressBar progressBar;

    private ArrayList<Recipe> recipesList;

    private EspressoIdlingResource espressoIdlingResource;

    public RecipeToRecyclerView(Context context, RecyclerView recyclerView, ProgressBar progressBar, EspressoIdlingResource idlingResource) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.progressBar = progressBar;
        this.LOG_TAG = context.getPackageName();
        this.espressoIdlingResource = idlingResource;
    }

    public void loadRecipes() {

        RecipeService recipeService = ApiUtils.getRecipeService();

        // Get recipes
        recipeService.getRecipes().enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Recipe>> call, @NonNull Response<ArrayList<Recipe>> response) {

                if (response.isSuccessful()) {

                    if (response.body() != null) {
                        recipesList = new ArrayList<>();

                        recipesList.addAll(response.body());

                        if (recipesList.size() != 0) {

                            // Load recipes to RecyclerView
                            RecipeToRecyclerViewAdapter recyclerAdapter = new RecipeToRecyclerViewAdapter(context, recipesList, espressoIdlingResource);
                            recyclerView.setAdapter(recyclerAdapter);

                            progressBar.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }

                } else {
                    Log.d(LOG_TAG, "Status code : " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Recipe>> call, @NonNull Throwable t) {
                Toast.makeText(context, "Error loading data from API", Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Error loading from API");
            }
        });
    }
}

