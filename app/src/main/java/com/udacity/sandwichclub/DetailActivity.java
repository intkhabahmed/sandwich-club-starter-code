package com.udacity.sandwichclub;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.databinding.ActivityDetailBinding;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private ActivityDetailBinding activityDetailBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setting the detail activity layout for data binding
        activityDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if(!isConnected) {
                Snackbar.make(activityDetailBinding.rootLayout, R.string.no_network, Snackbar.LENGTH_LONG).show();
            }
        }

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        int position = DEFAULT_POSITION;
        if(intent != null && intent.hasExtra(EXTRA_POSITION)) {
            position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        }
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }
        //populates the sandwich data in the UI
        populateUI(sandwich);
        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        activityDetailBinding.mainNameTv.setText(sandwich.getMainName());
        List<String> alsoKnownAs = sandwich.getAlsoKnownAs();
        for(int i=0;i<alsoKnownAs.size();i++) {
            activityDetailBinding.alsoKnownTv.append(alsoKnownAs.get(i));
            if(i<alsoKnownAs.size()-1) {
                activityDetailBinding.alsoKnownTv.append(", ");
            }
        }
        List<String> ingredients = sandwich.getIngredients();
        for(int i=0;i<ingredients.size();i++) {
            activityDetailBinding.ingredientsTv.append(ingredients.get(i));
            if(i<ingredients.size()-1) {
                activityDetailBinding.ingredientsTv.append(", ");
            }
        }
        activityDetailBinding.descriptionTv.setText(sandwich.getDescription());
        activityDetailBinding.originTv.setText(sandwich.getPlaceOfOrigin());
        Picasso.with(this)
                .load(sandwich.getImage())
                .placeholder(R.drawable.ic_image_black_24dp)
                .error(R.drawable.ic_error_black_24dp)
                .into(activityDetailBinding.imageIv);
    }
}
