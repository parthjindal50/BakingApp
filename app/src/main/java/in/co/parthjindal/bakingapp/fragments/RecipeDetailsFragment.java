package in.co.parthjindal.bakingapp.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.bumptech.glide.Glide;
import in.co.parthjindal.bakingapp.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

public class RecipeDetailsFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String EXTRA_IMAGE_URL_ID = "EXTRA_IMAGE_URL_ID";
    private static final String EXTRA_DESCRIPTION_ID = "EXTRA_DESCRIPTION_ID";
    private static final String EXTRA_BOOLEAN_TWO_PANE = "EXTRA_BOOLEAN_TWO_PANE";
    private static final String EXTRA_VIDEO_URL_ID = "EXTRA_VIDEO_URL_ID";
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;
    @BindView(R.id.recipe_step_detail_video)
    SimpleExoPlayerView mPlayerView;
    private SimpleExoPlayer player;
    private static final String PLAYBACKPOSITION = "PLAYBACKPOSITION";
    private long playBackPosition;
    private static final String PLAYWHENREADY = "PLAYWHENREADY";
    private boolean playWhenReady = true;
    private static final String CURRENTWINDOW = "CURRENTWINDOW";
    private int currentWindow;
    String video;
    Boolean mTwoPane;
    String description;
    Unbinder unbinder;

    @BindView(R.id.recipe_step_image)
    ImageView stepImage;

    @BindView(R.id.recipe_step_desc_card)
    CardView cardView;

    @BindView(R.id.recipe_step_detail_text)
    TextView descTextView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            description = getArguments().getString(EXTRA_DESCRIPTION_ID);
            mTwoPane = getArguments().getBoolean(EXTRA_BOOLEAN_TWO_PANE);
            Log.v("RecipestepSinglePage", "mTwoPane is " + mTwoPane);
        }
    }

    public static RecipeDetailsFragment newInstance(String videoUrl, String description, String imageUrl, boolean twoPane) {
        Log.d("RecipeStepSingle", "in newInstance");
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_DESCRIPTION_ID, description);
        arguments.putString(EXTRA_VIDEO_URL_ID, videoUrl);
        arguments.putString(EXTRA_IMAGE_URL_ID, imageUrl);
        arguments.putBoolean(EXTRA_BOOLEAN_TWO_PANE, twoPane);
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean(PLAYWHENREADY);
            currentWindow = savedInstanceState.getInt(CURRENTWINDOW);
            playBackPosition = savedInstanceState.getLong(PLAYBACKPOSITION);
        }

        View view = inflater.inflate(R.layout.fragment_single_step_page, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public static RecipeDetailsFragment newInstance(String videoUrl, String description, String imageUrl) {
        Log.d("RecipeStepSingle", "in newInstance");
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_DESCRIPTION_ID, description);
        arguments.putString(EXTRA_VIDEO_URL_ID, videoUrl);
        arguments.putString(EXTRA_IMAGE_URL_ID, imageUrl);
        RecipeDetailsFragment fragment = new RecipeDetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    public RecipeDetailsFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(PLAYWHENREADY, playWhenReady);
        outState.putInt(CURRENTWINDOW, currentWindow);
        outState.putLong(PLAYBACKPOSITION, playBackPosition);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        descTextView.setText(description);

        String imageUrl = getArguments().getString(EXTRA_IMAGE_URL_ID);

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .into(stepImage);
            setViewVisibility(stepImage, false);
        } else {
            setViewVisibility(stepImage, false);
        }

        video = getArguments().getString(EXTRA_VIDEO_URL_ID);

    }

    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), "RecipeStepSinglePageFragment");

        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            @Override
            public void onPlay() {
                player.setPlayWhenReady(true);
            }

            @Override
            public void onPause() {
                player.setPlayWhenReady(false);
            }

            @Override
            public void onSkipToPrevious() {
                player.seekTo(playBackPosition);
            }
        });
        mediaSession.setActive(true);
    }

    private void setViewVisibility(View view, boolean show) {
        if (show) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (video != null && !video.isEmpty()) {
            setViewVisibility(mPlayerView, true);
            initializeMediaSession();
            initPlayer(Uri.parse(video));

            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                if (!mTwoPane) {
                    expandVideoView(mPlayerView);
                    setViewVisibility(cardView, false);
                    hideSystemUI();
                }

            }

            playBackPosition = player.getCurrentPosition();

        } else {
            setViewVisibility(mPlayerView, false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        releasePlayer();
    }

    private void expandVideoView(SimpleExoPlayerView exoPlayer) {
        exoPlayer.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;
        exoPlayer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
    }


    private void hideSystemUI() {
        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        cardView.setVisibility(View.INVISIBLE);
    }

    public void initPlayer(Uri videoURL) {
        if (player == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);
            mPlayerView.setPlayer(player);
            player.addListener(this);
            String userAgent = Util.getUserAgent(getContext(), "StepVideo");
            MediaSource mediaSource = new ExtractorMediaSource(videoURL,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);
            player.prepare(mediaSource);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(playBackPosition);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void releasePlayer() {
        if (player != null) {
            playBackPosition = player.getCurrentPosition();
            player.stop();
            player.release();
            player = null;
        }

        if (mediaSession != null) {
            mediaSession.setActive(false);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PLAYING, player.getCurrentPosition(), 1f);
        } else if (playbackState == ExoPlayer.STATE_READY) {
            stateBuilder.setState(PlaybackStateCompat.STATE_PAUSED, player.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }
}