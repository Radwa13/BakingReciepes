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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alfa.bakingreciepes.*;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import bakingrecipes.Data.Step;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by Alfa on 5/15/2018.
 */

public class DetailFragment extends Fragment {
    @Nullable
    @BindView(R.id.short_desc)
    protected TextView short_desc;
    @Nullable
    @BindView(R.id.desc)
    protected TextView desc;
    @Nullable
    @BindView(R.id.exo_next)
    protected ImageView next;
    @Nullable
    @BindView(R.id.exo_prev)
    protected ImageView previous;
    @Nullable
    @BindView(R.id.simpleExoPlayerView)
    protected SimpleExoPlayerView mExoPlayerView;
    @Nullable
    @BindView(R.id.main_media_frame)
    protected FrameLayout mFrameLayout;
    @BindView(R.id.thumbnail)
    ImageView thumbnailIV;
    @Nullable
    private ArrayList<Step> mStepList;
    private int mPosition;
    private long mPlayerPosition;
    @Nullable
    private Context mContext;
    @Nullable
    private SimpleExoPlayer mPlayer;
    private boolean landScape = false;
    private boolean mSetPlayWhenReady = true;
    private final String STEP_LIST_KEY = "stepListKey";
    public static final String STATE_RESUME_POSITION = "resumePosition";
    public static final String STATE_PLAY_WHEN_READY = "playerFullscreen";
    private FrameLayout mFullScreenButton;
    private boolean mExoPlayerFullscreen = false;
    private ImageView mFullScreenIcon;
    @Nullable
    private Dialog mFullScreenDialog;
    @Nullable
    private DetailFragment.CloseVideo mClickHandler;
    private final String STEP = "step: ";
    private ComponentListener componentListener;
    private boolean isTablet;
    private int LAST_POSITION=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        int resId = R.layout.detail_fragment;
        View v = inflater.inflate(resId, container, false);
        ButterKnife.bind(this, v);
        componentListener = new ComponentListener();

        if (v.findViewById(R.id.layoutForTablet) != null) {
            isTablet = true;
        }

        if (savedInstanceState != null) {
            mStepList = savedInstanceState.getParcelableArrayList(STEP_LIST_KEY);
            if (mExoPlayerFullscreen) {
                openFullscreenDialog();
            }

        }

        initFullscreenButton();
        initFullscreenDialog();

//
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && !mStepList.get(mPosition).getVideoURL().equals("") && !isTablet) {
            landScape = true;
            initFullscreenDialog();
            openFullscreenDialog();
            mClickHandler = (CloseVideo) getActivity();
        }


        short_desc.setText(mStepList.get(mPosition).getShortDescription());
        desc.setText(mStepList.get(mPosition).getDescription());


        if (!mStepList.get(mPosition).getThumbnailURL().equals("")) {
            String extension = mStepList.get(mPosition).getThumbnailURL().substring(mStepList.get(mPosition).getThumbnailURL().lastIndexOf("."));
            if (!extension.equals("mp4")) {
                Picasso.with(mContext)
                        .load(mStepList.get(mPosition).getThumbnailURL())
                        .error(R.drawable.thumbnail)
                        .into(thumbnailIV);
            }
        } else {
            thumbnailIV.setVisibility(View.GONE);
        }
        showHideExoPlayer();

        try {
            ((DetailActivity) getActivity()).setActionBarTitle(STEP + "" + (mPosition + 1));

        } catch (Exception e) {

        }
        return v;

    }

    public interface CloseVideo {
        void onBack();
    }

    public void setSteps(ArrayList<Step> stepList, int position) {
        mStepList = stepList;
        mPosition = position;
    }

    private void openFullscreenDialog() {
        if (mFullScreenDialog.isShowing()) {
            mFullScreenDialog.dismiss();
        }

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);
        mFullScreenDialog.addContentView(mExoPlayerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mExoPlayerFullscreen = true;
        mFullScreenDialog.show();

    }


    private void initFullscreenDialog() {

        mFullScreenDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {

                if (mExoPlayerFullscreen && !landScape) {
                    closeFullscreenDialog();
                    super.onBackPressed();

                } else {
                    exit();
                }
            }
        };
    }


    private void exit() {
        mClickHandler.onBack();

    }


    private void initializePlayer(Uri uri) {

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();
        mPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector, loadControl);
        String userAgent = Util.getUserAgent(mContext, "ExoPlayer");
        MediaSource mediaSource = new ExtractorMediaSource(uri, new DefaultDataSourceFactory(mContext, userAgent), new DefaultExtractorsFactory(), null, null);
        mPlayer.prepare(mediaSource);
        mPlayer.addListener(componentListener);
        mPlayer.setPlayWhenReady(true);
        mExoPlayerView.setPlayer(mPlayer);

    }

    private void releasePlayer() {
        if (mPlayer != null) {
            mPlayer.removeListener(componentListener);
            mPlayer.release();
            mPlayer = null;
        }
    }

    @OnClick(R.id.exo_prev)
    void loadPrevStep(View view) {
        if (mPosition > 1) {
            mPosition--;
            short_desc.setText(mStepList.get(mPosition).getShortDescription());
            desc.setText(mStepList.get(mPosition).getDescription());
            releasePlayer();
            if (mPlayer == null)
                initializePlayer(Uri.parse(mStepList.get(mPosition).getVideoURL()));

            showHideExoPlayer();
            ((DetailActivity) getActivity()).setActionBarTitle(STEP + "" + (mPosition + 1));

        }
    }

    @OnClick(R.id.exo_next)
    void loadNextStep(View view) {
        if (mPosition < mStepList.size() - 1) {
            mPosition++;
            short_desc.setText(mStepList.get(mPosition).getShortDescription());
            desc.setText(mStepList.get(mPosition).getDescription());
            releasePlayer();
            if (mPlayer == null)
                initializePlayer(Uri.parse(mStepList.get(mPosition).getVideoURL()));

            showHideExoPlayer();

            ((DetailActivity) getActivity()).setActionBarTitle(STEP + "" + (mPosition + 1));

        }
    }

    private void showHideExoPlayer() {
        if (mStepList.get(mPosition).getVideoURL() != null && !mStepList.get(mPosition).getVideoURL().equals("")) {

            mExoPlayerView.setVisibility(View.VISIBLE);
            mFrameLayout.setVisibility(View.VISIBLE);
        } else {
            mExoPlayerView.setVisibility(View.GONE);
            mFrameLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEP_LIST_KEY, mStepList);

    }


    private class ComponentListener extends DefaultEventListener {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            super.onPlayerStateChanged(playWhenReady, playbackState);

            String stateString;
            switch (playbackState) {
                case ExoPlayer.STATE_IDLE:
                    stateString = "ExoPlayer.STATE_IDLE      -";
                    // resume the player
//pause the player
                    mSetPlayWhenReady = playWhenReady;
                    break;
                case ExoPlayer.STATE_BUFFERING:
                    stateString = "ExoPlayer.STATE_BUFFERING -";
                    // resume the player
//pause the player
                    mSetPlayWhenReady = playWhenReady;

                    break;
                case ExoPlayer.STATE_READY:
                    stateString = "ExoPlayer.STATE_READY     -";
                    // resume the player
//pause the player
                    mSetPlayWhenReady = playWhenReady;
                    break;
                case ExoPlayer.STATE_ENDED:
                    stateString = "ExoPlayer.STATE_ENDED     -";
                    // resume the player
//pause the player
                    mSetPlayWhenReady = playWhenReady;
                    break;
                default:
                    stateString = "UNKNOWN_STATE             -";
                    mSetPlayWhenReady = false;

                    break;
            }
            Log.d(TAG, "changed state to " + stateString
                    + " playWhenReady: " + playWhenReady);
        }
    }

    private void initFullscreenButton() {

        PlaybackControlView controlView = mExoPlayerView.findViewById(R.id.exo_controller);
        mFullScreenIcon = controlView.findViewById(R.id.exo_fullscreen_icon);
        mFullScreenButton = controlView.findViewById(R.id.exo_fullscreen_button);
        mFullScreenButton.setOnClickListener(v -> {
            if (!mExoPlayerFullscreen)
                openFullscreenDialog();
            else
                closeFullscreenDialog();
        });
    }

    private void closeFullscreenDialog() {

        ((ViewGroup) mExoPlayerView.getParent()).removeView(mExoPlayerView);

        ((FrameLayout) getActivity().findViewById(R.id.main_media_frame)).addView(mExoPlayerView);
        mExoPlayerFullscreen = false;

        if (landScape && !isTablet) {
            exit();
        } else {
            mFullScreenDialog.dismiss();
            mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_fullscreen_expand));

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if (mPlayer == null && mPlayer == null) {
                initializePlayer(Uri.parse(mStepList.get(mPosition).getVideoURL()));
                mPlayer.setPlayWhenReady(mSetPlayWhenReady);
                mPlayer.seekTo(mPlayerPosition);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 && mPlayer == null)) {
            initializePlayer(Uri.parse(mStepList.get(mPosition).getVideoURL()));

        }
        int position=mPosition;
        if (getActivity().getIntent() != null&&LAST_POSITION==mPosition) {
            mPlayer.setPlayWhenReady(getActivity().getIntent().getBooleanExtra(STATE_PLAY_WHEN_READY, true));
            mPlayer.seekTo(getActivity().getIntent().getLongExtra(STATE_RESUME_POSITION, 0));
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        mPlayerPosition = mPlayer.getCurrentPosition();
        mSetPlayWhenReady = mPlayer.getPlayWhenReady();
        getActivity().getIntent().putExtra(STATE_RESUME_POSITION, mPlayerPosition);
        getActivity().getIntent().putExtra(STATE_PLAY_WHEN_READY, mSetPlayWhenReady);

        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
        LAST_POSITION=mPosition;
    }

    @Override
    public void onStop() {
        Log.d(TAG, "changed state to onStop");

        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            releasePlayer();
        }
    }
}



