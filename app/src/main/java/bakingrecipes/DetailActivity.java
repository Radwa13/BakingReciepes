package bakingrecipes;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.alfa.bakingreciepes.*;

import java.util.ArrayList;

import bakingrecipes.Data.Step;

public class DetailActivity extends AppCompatActivity implements DetailFragment.CloseVideo {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {

            // Retrieve list index values that were sent through an intent; use them to display the desired Android-Me body part image
            // Use setListindex(int index) to set the list index for all BodyPartFragments

            // Create a new head BodyPartFragment
            DetailFragment detailFragment = new DetailFragment();
            ArrayList<Step> stepArrayList = getIntent().getParcelableArrayListExtra("val");
            int position = getIntent().getIntExtra("pos", 0);
            detailFragment.setSteps(stepArrayList, position);

            // Get the correct index to access in the array of head images from the intent
            // Set the default value to 0


            // Add the fragment to its container using a FragmentManager and a Transaction
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.frame, detailFragment)
                    .commit();

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            getActionBar().hide();
        }
        super.onConfigurationChanged(newConfig);
    }


    @Override
    public void onBack() {

        finish();
    }
}
