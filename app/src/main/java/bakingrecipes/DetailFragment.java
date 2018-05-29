package bakingrecipes;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alfa.bakingreciepes.*;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import bakingrecipes.Data.Step;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Alfa on 5/15/2018.
 */

public class DetailFragment extends Fragment {
    @BindView(R.id.short_desc)
    protected TextView short_desc;
    @BindView(R.id.desc)
    protected TextView desc;
    @BindView(R.id.exo_next)
    protected ImageView next;
    @BindView(R.id.exo_prev)
    protected ImageView previous;
    @BindView(R.id.simpleExoPlayerView)
    protected SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.main_media_frame)
    protected FrameLayout mFrameLayout;
    private ArrayList<Step> mStepList;
    private int mPosition;
    Context mContext;
    SimpleExoPlayer mPlayer;
    private boolean landScape = false;

    private final String STEP_LIST_KEY = "stepListKey";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private DetailFragment.CloseVideo mClickHandler;

    private MediaSource mVideoSource;
    private boolean mExoPlayerFullscreen = false;
    private FrameLayout mFullScreenButton;
    private ImageView mFullScreenIcon;
    private Dialog mFullScreenDialog;
private boolean isTablet;
    private int mResumeWindow;
    private long mResumePosition;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        int resId = R.layout.detail_fragment;
        View v = inflater.inflate(resId, container, false);
        ButterKnife.bind(this, v);
        if (savedInstanceState != null) {
            mStepList = savedInstanceState.getParcelableArrayList(STEP_LIST_KEY);
        }
        isTablet=getArguments().getBoolean("isTablet");
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE&&!mStepList.get(mPosition).getVideoURL().equals("")&&!isTablet) {
            landScape = true;
            initFullscreenDialog();
            openFullscreenDialog();
    //
      mClickHandler = (CloseVideo) getActivity();
        }
        short_desc.setText(mStepList.get(mPosition).getShortDescription());
        desc.setText(mStepList.get(mPosition).getDescription());
        showHideExoPlayer(mPosition);
        return v;

    }

    public interface CloseVideo {
        void onBack();
    }

    public void setSteps(ArrayList<Step> stepList, int position) {
        mStepList = stepList;
        mPosition = position;
    }


    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (mExoPlayerFullscreen && !landScape) {
                    closeFullscreenDialog();
                    super.onBackPressed();

                } else if (mExoPlayerFullscreen && landScape) {
                    exit();
                }
            }
        };
    }

    private void closeFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);

        ((FrameLayout) getActivity().findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
        mExoPlayerFullscreen = false;
        mFullScreenDialog.dismiss();
        mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_expand));
    }

    private void exit() {
        mClickHandler.onBack();
    }


    private void initFullscreenButton() {

        PlaybackControlView controlView = mExoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mExoPlayerFullscreen)
                    openFullscreenDialog();
                else
                    closeFullscreenDialog();
            }
        });
    }


    private void openFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        if (!landScape)
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_skrink));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();
    }


    private void initializePlayer(Uri uri) {

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
        String userAgent = Util.getUserAgent(mContext, "ExoPlayer");
        MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(mContext, userAgent), new DefaultExtractorsFactory(), null, null);
        mPlayer.prepare(mediaSource);
        mPlayer.setPlayWhenReady(true);
    }

    @OnClick(R.id.exo_prev)
    void loadPrevStep(View view) {
        if (mPosition > 1) {
            mPosition--;
            short_desc.setText(mStepList.get(mPosition).getShortDescription());
            desc.setText(mStepList.get(mPosition).getDescription());
            showHideExoPlayer(mPosition);

        }
    }

    private void showHideExoPlayer(int position) {
        if (mStepList.get(mPosition).getVideoURL() != null && !mStepList.get(mPosition).getVideoURL().equals("")) {

            mExoPlayerView.setVisibility(View.VISIBLE);
            mFrameLayout.setVisibility(View.VISIBLE);

            initializePlayer(Uri.parse(mStepList.get(mPosition).getVideoURL()));
            mExoPlayerView.setPlayer(mPlayer);
            initFullscreenButton();
            initFullscreenDialog();

        } else {
            mExoPlayerView.setVisibility(View.INVISIBLE);
            mFrameLayout.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.exo_next)
    void loadNextStep(View view) {
        if (mPosition < mStepList.size() - 1) {
            mPosition++;
            short_desc.setText(mStepList.get(mPosition).getShortDescription());
            desc.setText(mStepList.get(mPosition).getDescription());
            showHideExoPlayer(mPosition);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEP_LIST_KEY, mStepList);
    }


}
