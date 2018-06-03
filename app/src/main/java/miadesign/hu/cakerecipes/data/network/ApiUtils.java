package miadesign.hu.cakerecipes.data.network;

public class ApiUtils {

    private static final String BASE_URL = "http://miadesign.hu/";

    public static RecipeService getRecipeService() {
        return RetrofitClient.getClient(BASE_URL).create(RecipeService.class);
    }
}
