package bakingrecipes;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.alfa.bakingreciepes.*;
import com.google.gson.Gson;


import java.util.ArrayList;

import bakingrecipes.Data.Example;
import bakingrecipes.Data.Ingredient;
import bakingrecipes.Data.Step;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//import static com.example.alfa.bakingrecipes.BakingProvider.ACTION_INGREDIENT_ADDED;


public class RecipeActivity extends AppCompatActivity implements StepsAdapter.ListItemClickListner, StepsFragment.StepItemClickListener {
    private boolean isTablet;
    private Example mBaking;
    private ArrayList<Ingredient> mIngredientList;
    private ArrayList<Step> mStepList;
    @Nullable
    @BindView(R.id.pin)
    Button pin;
    public static final String BAKING_KEY = "com.example.alfa.bakingrecipes.baking.key";
    public static final String INGREDIENTS_KEY = "com.example.alfa.bakingrecipes.ingredients.key";
    public static final String STEPS_KEY = "com.example.alfa.bakingrecipes.steps.key";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().hasExtra("fromWidget")) {
            Gson gson = new Gson();
            String json = SharedPreferencesMethods.loadSavedPreferencesString(this, BAKING_KEY);
            mBaking = gson.fromJson(json, Example.class);
            mIngredientList = mBaking.getIngredients();
            mStepList = mBaking.getSteps();
        } else if (getIntent() != null && !getIntent().hasExtra("fromWidget")) {

            mBaking = getIntent().getParcelableExtra(BAKING_KEY);
            mIngredientList = getIntent().getParcelableArrayListExtra(INGREDIENTS_KEY);
            mStepList = getIntent().getParcelableArrayListExtra(STEPS_KEY);
        }
        Fragment fragmentIngredients = getSupportFragmentManager().findFragmentById(R.id.master_list_ingredients);
        Bundle bundle2 = new Bundle();
        bundle2.putParcelableArrayList(INGREDIENTS_KEY, mIngredientList);
        fragmentIngredients.setArguments(bundle2);

        Fragment fragmentSteps = getSupportFragmentManager().findFragmentById(R.id.master_list_steps);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEPS_KEY, mStepList);
        if (findViewById(R.id.layoutForTablet) != null) {
            isTablet = true;

            if (savedInstanceState == null||getSupportFragmentManager().getFragments().size()==0) {
                bundle = new Bundle();

                DetailFragment detailFragment = new DetailFragment();
                bundle.putBoolean("isTablet", isTablet);
                detailFragment.setSteps(mStepList, 0);
                detailFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction().add(R.id.master_list_detail, detailFragment)

                        .commit();
//
            }

        }
        bundle = new Bundle();

        bundle.putBoolean("isTablet", isTablet);
        bundle.putParcelableArrayList(STEPS_KEY, mStepList);

        fragmentSteps.setArguments(bundle);
        getSupportActionBar().setTitle(mBaking.getName());

    }

    @OnClick(R.id.pin)
    public void pin(View v) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext()
        );
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidget.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.appwidget_listView);

        Gson gson = new Gson();
        String json = gson.toJson(mBaking);
        SharedPreferencesMethods.savePreferencesString(this, BAKING_KEY, json);
        //Now update all widgets
        IngredientsWidget.updateIngredientsWidgets(this, appWidgetManager, appWidgetIds, mBaking.getName());

    }

    //    @Override
    public void onClick(int position) {

        if (findViewById(R.id.layoutForTablet) != null) {
            isTablet = true;
            Bundle bundle = new Bundle();
            bundle.putBoolean("isTablet", isTablet);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setSteps(mStepList, position);
            detailFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().add(R.id.master_list_detail, detailFragment)

                    .commit();
//


        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("isTablet", isTablet);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}

