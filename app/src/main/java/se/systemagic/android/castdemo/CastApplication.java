package se.systemagic.android.castdemo;

import com.google.android.gms.cast.CastStatusCodes;
import com.google.android.gms.cast.MediaQueueItem;
import com.google.android.gms.cast.MediaStatus;
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager;
import com.google.android.libraries.cast.companionlibrary.cast.callbacks.VideoCastConsumerImpl;
import com.google.android.libraries.cast.companionlibrary.cast.exceptions.NoConnectionException;
import com.google.android.libraries.cast.companionlibrary.cast.exceptions
        .TransientNetworkDisconnectionException;
import com.google.android.libraries.cast.companionlibrary.cast.player.VideoCastControllerActivity;

import android.app.Application;

public class CastApplication extends Application {

    private static final String TAG = "CastApplication";
    private static String APPLICATION_ID;
    public static final double VOLUME_INCREMENT = 0.05;
    public static final int PRELOAD_TIME_S = 20;

    /*
 * (non-Javadoc)
 * @see android.app.Application#onCreate()
 */
    @Override
    public void onCreate() {
        super.onCreate();
        APPLICATION_ID = getString(R.string.app_id);

        // initialize VideoCastManager
        VideoCastManager.
                initialize(this, APPLICATION_ID, VideoCastControllerActivity.class, null).
                setVolumeStep(VOLUME_INCREMENT).
                enableFeatures(VideoCastManager.FEATURE_NOTIFICATION |
                        VideoCastManager.FEATURE_LOCKSCREEN |
                        VideoCastManager.FEATURE_WIFI_RECONNECT |
                        VideoCastManager.FEATURE_CAPTIONS_PREFERENCE |
                        VideoCastManager.FEATURE_DEBUGGING);

    }

    /**
     * Loading queue items. The only reason we are using this instead of using the VideoCastManager
     * directly is to get around an issue on receiver side for HLS + VTT for a queue; this will be
     * addressed soon and the following workaround will be removed.
     */
    public void loadQueue(MediaQueueItem[] items, int startIndex)
            throws TransientNetworkDisconnectionException, NoConnectionException {
        final VideoCastManager castManager = VideoCastManager.getInstance();
        castManager.addVideoCastConsumer(new VideoCastConsumerImpl() {
            @Override
            public void onMediaQueueOperationResult(int operationId, int statusCode) {
                if (operationId == VideoCastManager.QUEUE_OPERATION_LOAD) {
                    if (statusCode == CastStatusCodes.SUCCESS) {
                        castManager.setActiveTrackIds(new long[]{});
                    }
                    castManager.removeVideoCastConsumer(this);
                }
            }
        });
        castManager.queueLoad(items, startIndex, MediaStatus.REPEAT_MODE_REPEAT_OFF, null);
    }
}
