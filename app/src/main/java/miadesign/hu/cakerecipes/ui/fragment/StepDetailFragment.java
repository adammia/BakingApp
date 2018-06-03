package miadesign.hu.cakerecipes.ui.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import miadesign.hu.cakerecipes.R;
import miadesign.hu.cakerecipes.data.idlingresource.EspressoIdlingResource;
import miadesign.hu.cakerecipes.data.model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailFragment extends Fragment {

    @BindView(R.id.step_short_description)
    AppCompatTextView stepShortDescription;

    @BindView(R.id.step_video)
    PlayerView videoPlayerView;

    @BindView(R.id.video_image)
    ImageView videoImage;

    @BindView(R.id.no_internet_connection)
    TextView noInternetConnection;

    @BindView(R.id.step_description)
    AppCompatTextView stepDescription;

    SimpleExoPlayer player;
    boolean playWhenReady = false;
    int currentWindow = 0;
    long playbackPosition = 0;
    private View rootView;

    @Nullable
    private EspressoIdlingResource espressoIdlingResource;

    public StepDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Create view
        rootView = inflater.inflate(R.layout.fragment_step_detail, container, false);

        ButterKnife.bind(this, rootView);

        espressoIdlingResource = getEspressoIdlingResource();
        espressoIdlingResource.setIdleState(false);

        // Release player if it is not null
        if (player != null) {
            releasePlayer();
        }

        if (getArguments() != null) {

            // Get step data
            Step step = getArguments().getParcelable(rootView.getContext().getString(R.string.pass_step_data_to_fragment_bundle));

            if (savedInstanceState != null) {
                playWhenReady = savedInstanceState.getBoolean("playWhenReady");
                playbackPosition = savedInstanceState.getLong("playbackPosition");
            }

            // Set step data
            if (step != null) {
                stepShortDescription.setText(step.getShortDescription());

                stepDescription.setText(step.getDescription());

                if (checkInternetConnection()) {
                    noInternetConnection.setVisibility(View.GONE);
                    if ((step.getVideoURL() != null)
                            && (step.getVideoURL().length() >= 3)) {
                        if ((step.getVideoURL().substring(step.getVideoURL().length() - 3)).equals("mp4")) {
                            videoPlayerView.setVisibility(View.VISIBLE);
                            videoImage.setVisibility(View.GONE);
                            // Set player
                            initializePlayer(rootView.getContext(), step.getVideoURL());
                        } else {
                            videoPlayerView.setVisibility(View.GONE);
                            videoImage.setVisibility(View.VISIBLE);
                        }
                    } else {
                        if ((step.getThumbnailURL() != null)
                                && (step.getThumbnailURL().length() >= 3)) {
                            if (step.getThumbnailURL().substring(step.getThumbnailURL().length() - 3).equals("mp4")) {
                                videoPlayerView.setVisibility(View.VISIBLE);
                                videoImage.setVisibility(View.GONE);
                                // Set player
                                initializePlayer(rootView.getContext(), step.getThumbnailURL());
                            } else {
                                videoPlayerView.setVisibility(View.GONE);
                                videoImage.setVisibility(View.VISIBLE);
                                RequestOptions options = new RequestOptions()
                                        .centerCrop()
                                        .placeholder(R.drawable.no_video)
                                        .error(R.drawable.no_video);

                                Glide.with(this)
                                        .load(step.getThumbnailURL())
                                        .apply(options)
                                        .into(videoImage);
                            }
                        } else {
                            videoPlayerView.setVisibility(View.GONE);
                            videoImage.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    videoPlayerView.setVisibility(View.GONE);
                    videoImage.setVisibility(View.GONE);
                    noInternetConnection.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(rootView.getContext(), rootView.getContext().getString(R.string.no_passed_data), Toast.LENGTH_SHORT).show();
            }
        }
        return rootView;
    }

    private void initializePlayer(Context context, String videoUrl) {

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(context),
                new DefaultTrackSelector(), new DefaultLoadControl());

        videoPlayerView.setPlayer(player);

        Uri uri = Uri.parse(videoUrl);
        MediaSource mediaSource = buildMediaSource(uri);
        player.prepare(mediaSource, true, false);

        player.seekTo(currentWindow, playbackPosition);

        player.setPlayWhenReady(playWhenReady);

        espressoIdlingResource.setIdleState(true);

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            if ((player != null) && (playWhenReady)) {
                player.setPlayWhenReady(true);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (player != null) {
            if (player.getPlayWhenReady()) {
                player.setPlayWhenReady(false);
                playWhenReady = true;
            } else {
                playWhenReady = false;
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        releasePlayer();
    }

    private void releasePlayer() {

        if (player != null) {
            videoPlayerView.setPlayer(null);
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) rootView.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (player != null) {
            outState.putBoolean("playWhenReady", playWhenReady);
            outState.putLong("playbackPosition", player.getCurrentPosition());
        } else {
            releasePlayer();
        }
    }

    @VisibleForTesting
    @NonNull
    public EspressoIdlingResource getEspressoIdlingResource() {
        if (espressoIdlingResource == null) {
            espressoIdlingResource = new EspressoIdlingResource();
        }
        return espressoIdlingResource;
    }
}
