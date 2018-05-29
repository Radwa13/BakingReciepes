package bakingrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.alfa.bakingreciepes.*;

import java.util.ArrayList;

import bakingrecipes.Data.Example;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static bakingrecipes.RecipeActivity.BAKING_KEY;
import static bakingrecipes.RecipeActivity.INGREDIENTS_KEY;
import static bakingrecipes.RecipeActivity.STEPS_KEY;


public class MainActivity extends AppCompatActivity implements BakingAdapter.ListItemClickListner {
    @BindView(R.id.bakingRv)
    RecyclerView mRecyclerView;
    BakingInterface mInterface;
    BakingAdapter bakingAdapter;
    ArrayList<Example> mBakingList;
    private final String STATE_KEY = " com.example.alfa.bakingrecipes.state_kwy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        bakingAdapter = new BakingAdapter(this, this);
        AutoGridLayout gridLayout = new AutoGridLayout(this);
        mRecyclerView.setLayoutManager(gridLayout);
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_KEY)) {
            mBakingList = savedInstanceState.getParcelableArrayList(STATE_KEY);
            bakingAdapter.loadData(mBakingList);
            mRecyclerView.setAdapter(bakingAdapter);


        } else {


            loadBakingData();
        }

    }


    private void loadBakingData() {
        mInterface = RetrofitClient.getClient().create(BakingInterface.class);
        mInterface.getBaking().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .retry(3)
                .subscribe(this::handleResponse, this::handleError);

    }

    private void handleResponse(ArrayList<Example> list) {
        mBakingList = list;
        bakingAdapter.loadData(list);
        mRecyclerView.setAdapter(bakingAdapter);

    }


    private void handleError(Throwable error) {

        Toast.makeText(this, "Error " + error.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_KEY, mBakingList);
    }

    @Override
    public void onClick(int position) {
        Example baking = mBakingList.get(position);
        Intent intent = new Intent(MainActivity.this, RecipeActivity.class);
        intent.putExtra(BAKING_KEY, baking);
        intent.putParcelableArrayListExtra(INGREDIENTS_KEY, baking.getIngredients());
        intent.putParcelableArrayListExtra(STEPS_KEY, baking.getSteps());

        startActivity(intent);
    }
}
