package bakingrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.alfa.bakingreciepes.R;

import java.util.ArrayList;

import bakingrecipes.Data.Example;
import bakingrecipes.idilingResources.SimpleIdlingResources;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
    public static String LAST_ITEM;
    public static int LAST_INDEX;


    @Nullable
    private SimpleIdlingResources mIdlingResource;


    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIdlingResource = new SimpleIdlingResources();
        mIdlingResource.setIdleState(false);

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
        mInterface.getBaking().subscribeOn(Schedulers.io())
                .retry(1)
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<ArrayList<Example>>() {
            public void onCompleted() {
                System.out.println("completed");
            }

            public void onError(Throwable e) {
                System.out.println("failure");
            }

            @Override
            public void onComplete() {

            }


            @Override
            public void onSubscribe(Disposable d) {

            }

            public void onNext(ArrayList<Example> list) {
                mBakingList = list;
                bakingAdapter.loadData(list);
                LAST_INDEX=mBakingList.size()-1;
                LAST_ITEM=mBakingList.get(LAST_INDEX).getName();
                mRecyclerView.setAdapter(bakingAdapter);

                mIdlingResource.setIdleState(true);

            }


        });

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
