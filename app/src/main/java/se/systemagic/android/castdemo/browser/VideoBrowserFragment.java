/**
 * Created by Viktor on 2015-07-24.
 */

package se.systemagic.android.castdemo.browser;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager;
import com.google.android.libraries.cast.companionlibrary.cast.callbacks.VideoCastConsumerImpl;
import com.google.android.libraries.cast.companionlibrary.utils.Utils;

import java.util.List;

import se.systemagic.android.castdemo.R;
import se.systemagic.android.castdemo.mediaplayer.LocalPlayerActivity;

public class VideoBrowserFragment extends Fragment implements VideoListAdapter.ItemClickListener, LoaderManager.LoaderCallbacks<List<MediaInfo>> {

    private static final String TAG = "VideoBrowserFragment";
    private static final String CATALOG_URL =
            "http://commondatastorage.googleapis.com/gtv-videos-bucket/CastVideos/f.json";
    private RecyclerView mRecyclerView;
    private VideoListAdapter mAdapter;
    private View mEmptyView;
    private View mLoadingView;
    private VideoCastManager mCastManager;
    private VideoCastConsumerImpl mCastConsumer;

    public VideoBrowserFragment () {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_browser_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.list);
        mEmptyView = getView().findViewById(R.id.empty_view);
        mLoadingView = getView().findViewById(R.id.progress_indicator);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mAdapter = new VideoListAdapter(this);
        mAdapter.setData(VideoProvider.fake_load_data());
        mRecyclerView.setAdapter(mAdapter);
        getLoaderManager().initLoader(0, null, this);
        mCastManager = VideoCastManager.getInstance();
        mCastConsumer = new VideoCastConsumerImpl() {
            @Override
            public void onConnected() {
                super.onConnected();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDisconnected() {
                super.onDisconnected();
                mAdapter.notifyDataSetChanged();
            }
        };
        mCastManager.addVideoCastConsumer(mCastConsumer);
    }

    @Override
    public void onDetach() {
        mCastManager.removeVideoCastConsumer(mCastConsumer);
        super.onDetach();
    }

    @Override
    public void itemClicked(View view, MediaInfo item, int position) {
        if (view instanceof ImageButton) {
            Log.d(TAG, "menu was clicked");
            //com.google.sample.cast.refplayer.utils.Utils.showQueuePopup(getActivity(), view, item);
        } else {
            String transitionName = getString(R.string.transition_image);
            VideoListAdapter.ViewHolder viewHolder =
                    (VideoListAdapter.ViewHolder) mRecyclerView.findViewHolderForPosition(position);
            Pair<View, String> imagePair = Pair
                    .create((View) viewHolder.getImageView(), transitionName);
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeSceneTransitionAnimation(getActivity(), imagePair);

            Intent intent = new Intent(getActivity(), LocalPlayerActivity.class);
            intent.putExtra("media", Utils.mediaInfoToBundle(item));
            intent.putExtra("shouldStart", false);
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        }
    }

    @Override
    public Loader<List<MediaInfo>> onCreateLoader(int id, Bundle args) {
        return new VideoItemLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<MediaInfo>> loader, List<MediaInfo> data) {
        mAdapter.setData(data);
        mLoadingView.setVisibility(View.GONE);
        mEmptyView.setVisibility(null == data || data.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<MediaInfo>> loader) {
        mAdapter.setData(null);
    }
}
