package miadesign.hu.cakerecipes.data.network;

import miadesign.hu.cakerecipes.data.model.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RecipeService {

    @GET("CakeRecipes/baking.json")
    Call<ArrayList<Recipe>> getRecipes();

}
