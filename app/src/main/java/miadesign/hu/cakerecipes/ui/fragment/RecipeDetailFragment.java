package miadesign.hu.cakerecipes.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import miadesign.hu.cakerecipes.R;
import miadesign.hu.cakerecipes.data.model.Recipe;
import miadesign.hu.cakerecipes.adapter.IngredientToRecyclerView;
import miadesign.hu.cakerecipes.adapter.StepToRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment {

    @BindView(R.id.select_ingredients_list)
    AppCompatButton selectIngredientsList;
    @BindView(R.id.select_steps_list)
    AppCompatButton selectStepsList;
    @BindView(R.id.ingredients_and_steps_list)
    RecyclerView ingredientAndStepsList;
    @BindView(R.id.ingredients_steps_load_progress_bar)
    ProgressBar ingredientAndStepsProgressBar;
    private Recipe recipe;
    private View rootView;

    public RecipeDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Create view
        rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        ButterKnife.bind(this, rootView);

        if (getArguments() != null) {

            recipe = getArguments().getParcelable(rootView.getContext().getString(R.string.pass_recipe_in_intent));

            // Set RecyclerView
            ingredientAndStepsList.setHasFixedSize(true);

            ingredientAndStepsList.setLayoutManager(new LinearLayoutManager(rootView.getContext()));

            // Set steps list as default
            setStepsList();

            // Change to ingredients list
            selectIngredientsList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ingredientAndStepsList.setVisibility(View.GONE);
                    ingredientAndStepsProgressBar.setVisibility(View.VISIBLE);

                    setIngredientsList();
                }
            });

            // Change to steps list
            selectStepsList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ingredientAndStepsList.setVisibility(View.GONE);
                    ingredientAndStepsProgressBar.setVisibility(View.VISIBLE);

                    setStepsList();
                }
            });
        } else {
            Toast.makeText(rootView.getContext(), getString(R.string.no_passed_data), Toast.LENGTH_SHORT).show();
        }
        return rootView;
    }

    private void setIngredientsList() {

        IngredientToRecyclerView ingredientToRecyclerView = new IngredientToRecyclerView(ingredientAndStepsList, ingredientAndStepsProgressBar);

        ingredientToRecyclerView.loadIngredients(recipe.getIngredients());

    }

    private void setStepsList() {

        StepToRecyclerView stepToRecyclerView = new StepToRecyclerView(rootView.getContext(), ingredientAndStepsList, ingredientAndStepsProgressBar);

        stepToRecyclerView.loadSteps(recipe.getSteps(), recipe.getName());

    }
}
