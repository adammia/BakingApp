package miadesign.hu.cakerecipes.ui.activity;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import miadesign.hu.cakerecipes.R;
import miadesign.hu.cakerecipes.data.model.Recipe;
import miadesign.hu.cakerecipes.adapter.StepToRecyclerViewAdapter;
import miadesign.hu.cakerecipes.ui.fragment.RecipeDetailFragment;
import miadesign.hu.cakerecipes.ui.fragment.StepDetailFragment;
import miadesign.hu.cakerecipes.widget.WidgetService;

import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity implements StepToRecyclerViewAdapter.ChangeStep {

    public static Boolean twoFragment;
    private FragmentManager fragmentManager;
    private StepDetailFragment stepDetailFragment;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        fragmentManager = getSupportFragmentManager();

        // Check if screen is large enough for two frame
        if (findViewById(R.id.step_detail) != null) {

            // Get Recipe
            if (getIntent().getExtras() != null) {
                recipe = getIntent().getExtras().getParcelable(this.getString(R.string.pass_recipe_in_intent));
            }

            // Get saved current step
            if (savedInstanceState == null) {
                //currentStep = savedInstanceState.getInt(getString(R.string.ingredient_and_steps_current_step));


                // Create Step fragment
                stepDetailFragment = new StepDetailFragment();

                Bundle bundle = new Bundle();
                bundle.putParcelable(getString(R.string.pass_step_data_to_fragment_bundle), recipe.getSteps().get(0));

                stepDetailFragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .add(R.id.step_detail, stepDetailFragment)
                        .commit();
            }
            twoFragment = true;
        } else {
            twoFragment = false;
        }

        // Handle screen orientation
        if (savedInstanceState == null) {
            if (getIntent().getExtras() != null) {

                recipe = getIntent().getExtras().getParcelable(this.getString(R.string.pass_recipe_in_intent));

                RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();

                Bundle bundle = new Bundle();

                bundle.putParcelable(getString(R.string.pass_recipe_in_intent), recipe);

                recipeDetailFragment.setArguments(bundle);

                fragmentManager.beginTransaction()
                        .add(R.id.ingredient_step_list_container, recipeDetailFragment)
                        .commit();
            } else {
                Toast.makeText(this, R.string.no_passed_data, Toast.LENGTH_SHORT).show();
            }
        } else {

            if (getIntent().getExtras() != null) {
                recipe = savedInstanceState.getParcelable(getString(R.string.instance_state_save_pass_recipe));
            }
        }

        // Change title to selected recipe name
        setTitle(recipe.getName());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_to_widget_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_to_widget:
                WidgetService.startActionUpdateWidget(this, recipe);
                Toast.makeText(
                        this,
                        recipe.getName() + "\ningredients added to the widget.",
                        Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(getString(R.string.instance_state_save_pass_recipe), recipe);

        super.onSaveInstanceState(outState);
    }

    // Handle the fragment change in stepDetailFragment from steps list
    @Override
    public void whenTowFragment(int position) {

        fragmentManager = getSupportFragmentManager();

        stepDetailFragment = new StepDetailFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.pass_step_data_to_fragment_bundle), recipe.getSteps().get(position));

        stepDetailFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(R.id.step_detail, stepDetailFragment)
                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}