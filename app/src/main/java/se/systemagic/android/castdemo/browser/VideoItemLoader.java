package se.systemagic.android.castdemo.browser;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.android.gms.cast.MediaInfo;

import java.util.List;

/**
 * Created by Viktor on 2015-07-24.
 */
public class VideoItemLoader extends AsyncTaskLoader<List<MediaInfo>> {

    private static final String TAG = "VideoItemLoader";

    public VideoItemLoader(Context context) {
        super(context);
    }

    @Override
    public List<MediaInfo> loadInBackground() {
        return VideoProvider.load_data();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    /**
     * Handles a request to stop the Loader.
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }
}
