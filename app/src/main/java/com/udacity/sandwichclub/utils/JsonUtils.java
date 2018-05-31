package com.udacity.sandwichclub.utils;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String NAME_KEY = "name";
    private static final String MAIN_NAME_KEY = "mainName";
    private static final String PLACE_OF_ORIGIN_KEY = "placeOfOrigin";
    private static final String DESCRIPTION_KEY = "description";
    private static final String IMAGE_KEY = "image";
    private static final String INGREDIENTS_KEY = "ingredients";
    private static final String ALSO_KNOWN_AS_KEY = "alsoKnownAs";

    /**
     *
     * @param json - The json string to be parsed
     * @return Sandwich - The Sandwich object
     * @Description - parses the json string and returns a sandwich object
     */
    public static Sandwich parseSandwichJson(String json) {
        List<String> alsoKnownAs = new ArrayList<>();
        List<String> ingredients = new ArrayList<>();
        Sandwich sandwich;
        try {
            //root json object
            JSONObject baseObject = new JSONObject(json);
            //name object
            JSONObject nameObject = baseObject.getJSONObject(NAME_KEY);
            String mainName = nameObject.getString(MAIN_NAME_KEY);
            //alsoKnownAs json array
            JSONArray alsoKnownAsArray = nameObject.getJSONArray(ALSO_KNOWN_AS_KEY);
            for (int i = 0; i < alsoKnownAsArray.length(); i++) {
                alsoKnownAs.add(alsoKnownAsArray.getString(i));
            }
            String description = baseObject.getString(DESCRIPTION_KEY);
            //ingredients json array
            JSONArray ingredientsArray = baseObject.getJSONArray(INGREDIENTS_KEY);
            for (int i = 0; i < ingredientsArray.length(); i++) {
                ingredients.add(ingredientsArray.getString(i));
            }
            String imageUrl = baseObject.getString(IMAGE_KEY);
            String placeOfOrigin = baseObject.getString(PLACE_OF_ORIGIN_KEY);
            sandwich = new Sandwich(mainName, alsoKnownAs, placeOfOrigin, description, imageUrl, ingredients);
            return sandwich;
        } catch (JSONException je) {
            je.printStackTrace();
        }
        return null;
    }
}
