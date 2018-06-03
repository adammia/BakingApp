package miadesign.hu.cakerecipes.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import miadesign.hu.cakerecipes.R;
import miadesign.hu.cakerecipes.data.idlingresource.EspressoIdlingResource;
import miadesign.hu.cakerecipes.data.model.Recipe;
import miadesign.hu.cakerecipes.ui.activity.RecipeDetailActivity;

import java.util.ArrayList;

public class RecipeToRecyclerViewAdapter extends RecyclerView.Adapter<RecipeToRecyclerViewAdapter.ViewHolder> {

    private final Context context;

    private final ArrayList<Recipe> recipesList;

    private EspressoIdlingResource espressoIdlingResource;

    RecipeToRecyclerViewAdapter(Context context, ArrayList<Recipe> recipesList, EspressoIdlingResource espressoIdlingResource) {
        this.context = context;
        this.recipesList = recipesList;
        this.espressoIdlingResource = espressoIdlingResource;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_recipe_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeToRecyclerViewAdapter.ViewHolder holder, int position) {

        RequestOptions options = new RequestOptions().frame(2500);
        Glide.with(context).asBitmap()
                .load(recipesList.get(position).getImage())
                .apply(options)
                .into(holder.recipeImage);

        holder.recipeName.setText(recipesList.get(position).getName());
        holder.servingsNumber.setText(String.valueOf(recipesList.get(position).getServings()));

        if (recipesList.get(position).getName().equals("Brownies")) {
            if (espressoIdlingResource != null) {
                espressoIdlingResource.setIdleState(true);
            }
        }
    }

    @Override
    public int getItemCount() {

        return recipesList.size();
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final CardView cardView;

        final ImageView recipeImage;

        final AppCompatTextView recipeName;
        final AppCompatTextView servingsNumber;

        ViewHolder(final View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.recipe_card_view);

            recipeImage = itemView.findViewById(R.id.recipe_image);

            recipeName = itemView.findViewById(R.id.recipe_name);
            servingsNumber = itemView.findViewById(R.id.servings_number);

            cardView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (checkInternetConnection()) {
                        Intent intent = new Intent(v.getContext(), RecipeDetailActivity.class);
                        Recipe recipe = recipesList.get(getAdapterPosition());
                        intent.putExtra(v.getContext().getString(R.string.pass_recipe_in_intent), recipe);
                        v.getContext().startActivity(intent);
                    } else {
                        Toast.makeText(v.getContext(), v.getContext().getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

}

