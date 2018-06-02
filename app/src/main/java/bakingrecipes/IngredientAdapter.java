package bakingrecipes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.alfa.bakingreciepes.*;

import java.util.ArrayList;

import bakingrecipes.Data.Ingredient;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Alfa on 5/11/2018.
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {
    private ArrayList<Ingredient> mIngredientList;


    public IngredientAdapter() {
    }

    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int listLayoutId = R.layout.ingredient_item;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(listLayoutId, parent, false);
        return new IngredientAdapter.IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(IngredientAdapter.IngredientViewHolder holder, int position) {
        holder.mQuantity.setText((mIngredientList.get(position).getQuantity())+"  "+mIngredientList.get(position).getMeasure());
        holder.mName.setText(mIngredientList.get(position).getIngredient());



    }


    @Override
    public int getItemCount() {
        return mIngredientList.size();
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder  {
        @BindView(R.id.quantity)
        TextView mQuantity;
        @BindView(R.id.name)
        TextView mName;

        public IngredientViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }



    }

    public void loadData(ArrayList<Ingredient> ingredientsList) {
        this.mIngredientList = ingredientsList;

    }
}
