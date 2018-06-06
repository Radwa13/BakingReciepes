package bakingrecipes;

import android.app.PictureInPictureParams;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Rational;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.alfa.bakingreciepes.*;

import java.util.ArrayList;

import bakingrecipes.Data.Step;

public class DetailActivity extends AppCompatActivity implements DetailFragment.CloseVideo {
    private boolean isTablet;
    private int orientation;

    public interface UserPressHome {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            orientation = getResources().getConfiguration().orientation;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet) {
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getSupportActionBar().hide();
            }
            setContentView(R.layout.activity_detail);
        if (findViewById(R.id.layoutForTablet) != null) {
            isTablet = true;
        } else {
            isTablet = false;}
            if (savedInstanceState == null) {

                DetailFragment detailFragment = new DetailFragment();
                ArrayList<Step> stepArrayList = getIntent().getParcelableArrayListExtra("val");
                int position = getIntent().getIntExtra("pos", 0);
                detailFragment.setSteps(stepArrayList, position);


                FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.frame, detailFragment)
                        .commit();

            }
        }
        @Override
        public void onConfigurationChanged (Configuration newConfig){
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !isTablet) {
                getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
                getActionBar().hide();
            } else if (isTablet) {
                finish();
            }
            super.onConfigurationChanged(newConfig);
        }


        @Override
        public void onBack () {
            if (!isTablet && orientation == Configuration.ORIENTATION_LANDSCAPE)
                finish();
        }
        @Override
        protected void onUserLeaveHint ()
        {
            super.onUserLeaveHint();
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                //Trigger PiP mode
                try {
                    Rational rational = new Rational(16,
                            9);

                    PictureInPictureParams mParams =
                            new PictureInPictureParams.Builder()
                                    .setAspectRatio(rational)
                                    .build();

                    enterPictureInPictureMode(mParams);
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }
            }

        }


        //method to change actionBar title
        public void setActionBarTitle (String title){
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle(title);
        }
    }
