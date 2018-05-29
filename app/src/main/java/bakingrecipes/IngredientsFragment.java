package bakingrecipes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alfa.bakingreciepes.*;

import java.util.ArrayList;

import bakingrecipes.Data.Example;
import bakingrecipes.Data.Ingredient;
import butterknife.BindView;
import butterknife.ButterKnife;

import static bakingrecipes.RecipeActivity.INGREDIENTS_KEY;


/**
 * Created by Alfa on 5/11/2018.
 */

public class IngredientsFragment extends Fragment {
    BakingInterface mInterface;
    IngredientAdapter ingredientAdapter;
    ArrayList<Example> mBakingList;

    @BindView(R.id.ingredientsRv)
    RecyclerView ingredientRecyclerView;
    private boolean isTablet;
    private ArrayList<Ingredient> mIngredient;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.ingredient_fragment, container, false);

        // Get a reference to the ImageView in the fragment layout
        ButterKnife.bind(this, rootView);
        // If a list of image ids exists, set the image resource to the correct item in that list
        // Otherwise, create a Log statement that indicates that the list was not found
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            mIngredient = getArguments().getParcelableArrayList(INGREDIENTS_KEY);
            setIngredients(mIngredient);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        ingredientRecyclerView.addItemDecoration(itemDecorator);
        ingredientRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


    public void setIngredients(ArrayList<Ingredient> ingredients) {
        mIngredient = ingredients;
        ingredientAdapter = new IngredientAdapter(getActivity());
        ingredientAdapter.loadData(mIngredient);
        ingredientRecyclerView.setAdapter(ingredientAdapter);


    }


}

