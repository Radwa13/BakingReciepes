package bakingrecipes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alfa.bakingreciepes.R;

import java.util.ArrayList;

import bakingrecipes.Data.Step;
import butterknife.BindView;
import butterknife.ButterKnife;

import static bakingrecipes.DetailFragment.STATE_PLAY_WHEN_READY;
import static bakingrecipes.DetailFragment.STATE_RESUME_POSITION;
import static bakingrecipes.RecipeActivity.STEPS_KEY;


/**
 * Created by Alfa on 5/11/2018.
 */

public class StepsFragment extends Fragment implements StepsAdapter.ListItemClickListener {
    @Nullable
    private StepsAdapter stepsAdapter;
    @Nullable
    @BindView(R.id.stepRv)
    RecyclerView stepRecyclerView;
    private boolean isTablet;
    @Nullable
    private ArrayList<Step> mSteps;

    public interface StepItemClickListener {
        void onClick(int position);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {


        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.step_fragment, container, false);

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
            isTablet = getArguments().getBoolean("isTablet");
            mSteps = getArguments().getParcelableArrayList(STEPS_KEY);
            setSteps(mSteps);
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        stepRecyclerView.addItemDecoration(itemDecorator);
        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }


    private void setSteps(ArrayList<Step> steps) {
        mSteps = steps;
        stepsAdapter = new StepsAdapter(getActivity(), this);
        stepsAdapter.loadData(mSteps);

        stepRecyclerView.setAdapter(stepsAdapter);

    }

    @Override
    public void onClick(int position) {
        if (!isTablet) {
            Intent intent = new Intent(getContext(), DetailActivity.class);
            intent.putParcelableArrayListExtra("val", mSteps);
            intent.putExtra("pos", position);
            startActivity(intent);
        } else {

            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setSteps(mSteps, position);
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(R.id.master_list_detail, detailFragment)

                    .commit();

        }


    }
}