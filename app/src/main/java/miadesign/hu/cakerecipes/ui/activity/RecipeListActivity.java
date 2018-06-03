package miadesign.hu.cakerecipes.ui.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import miadesign.hu.cakerecipes.R;
import miadesign.hu.cakerecipes.data.idlingresource.EspressoIdlingResource;
import miadesign.hu.cakerecipes.adapter.RecipeToRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListActivity extends AppCompatActivity {

    @BindView(R.id.recipe_load_progress)
    ProgressBar progressBar;

    @BindView(R.id.recipes_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.no_internet_connection_text)
    TextView noConnection;

    RecipeToRecyclerView recipeToRecyclerView;

    @Nullable
    private EspressoIdlingResource espressoIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        ButterKnife.bind(this);

        setRecipes();
    }

    private void setRecipes() {

        if (checkInternetConnection()) {

            progressBar.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);

            noConnection.setVisibility(View.GONE);

            // Set RecyclerView layout,columns
            setRecyclerView();

            espressoIdlingResource = getEspressoIdlingResource();
            espressoIdlingResource.setIdleState(false);

            // Create Recipe list handle
            recipeToRecyclerView = new RecipeToRecyclerView(this, recyclerView, progressBar, espressoIdlingResource);

            // Load recipes to RecyclerView
            recipeToRecyclerView.loadRecipes();

        } else {

            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);

            noConnection.setVisibility(View.VISIBLE);
        }

    }

    // Set RecyclerView grid layout
    private void setRecyclerView() {
        int spanCount = 1;
        final int orientation = this.getResources().getConfiguration().orientation;
        if (Configuration.ORIENTATION_LANDSCAPE == orientation) {
            spanCount = 2;
        }

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(gridLayoutManager);

    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @VisibleForTesting
    @NonNull
    public EspressoIdlingResource getEspressoIdlingResource() {
        if (espressoIdlingResource == null) {
            espressoIdlingResource = new EspressoIdlingResource();
        }
        return espressoIdlingResource;
    }

    @Override
    protected void onResume() {
        super.onResume();

        setRecipes();
    }
}

