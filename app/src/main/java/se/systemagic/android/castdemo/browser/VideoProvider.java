/**
 * Created by Viktor on 2015-07-24.
 *
 * Own version of itemloader. Does not actually fetch any data from url but just creates a fake
 * list of metadata objects including including Big Buck Bunny
 */

package se.systemagic.android.castdemo.browser;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.MediaTrack;
import com.google.android.gms.common.images.WebImage;

import android.net.Uri;
import android.provider.MediaStore;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class VideoProvider {

    public static final String KEY_DESCRIPTION = "description";

    private VideoProvider() {

    }

    public static List<MediaInfo> fake_load_data () {
        List<MediaInfo> mMediaInfoList = new ArrayList<MediaInfo>();
        MediaMetadata mMediaMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
        mMediaMetadata.putString(MediaMetadata.KEY_TITLE, "Big Buck Bunny");
        mMediaMetadata.putString(MediaMetadata.KEY_SUBTITLE, "Blender Foundation");
        mMediaMetadata.addImage(new WebImage(Uri.parse("https://peach.blender.org/wp-content/uploads/bbb-splash.thumbnail.png")));
        mMediaMetadata.addImage(new WebImage(Uri.parse("https://upload.wikimedia.org/wikipedia/commons/c/c5/Big_buck_bunny_poster_big.jpg")));
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject();
            jsonObj.put(KEY_DESCRIPTION, "'Big' Buck wakes up in his rabbit hole, only to be pestered by three critters, Gimera, Frank and Rinky. When Gimera kills a butterfly, Buck decides on a payback Predator-style");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMediaInfoList.add(new MediaInfo.Builder(
                "http://download.blender.org/peach/bigbuckbunny_movies/big_buck_bunny_720p_h264.mov")
                .setContentType("video/mov")
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setStreamDuration(597 * 1000)
                .setMediaTracks(null)
                .setCustomData(jsonObj)
                .setMetadata(mMediaMetadata)
                .build());

        MediaMetadata mMediaMetadata2 = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
        mMediaMetadata2.putString(MediaMetadata.KEY_TITLE, "The Grand Tournament - Trailer");
        mMediaMetadata2.putString(MediaMetadata.KEY_SUBTITLE, "Blizzard Entertainment");
        mMediaMetadata2.addImage(new WebImage(Uri.parse("http://i3.ytimg.com/vi/Fe7XDBtlQzg/maxresdefault.jpg")));
        mMediaMetadata2.addImage(new WebImage(Uri.parse("http://o.aolcdn.com/hss/storage/midas/3a173481f7edb75a623fca98ef6ea459/202356682/hearthstone.jpg")));
        JSONObject jsonObj2 = null;
        try {
            jsonObj2 = new JSONObject();
            jsonObj2.put(KEY_DESCRIPTION, "Mount up and make your way to the most fun festival in all of Hearthstone! Champions, noble steeds, pirates, and more await you in Hearthstone’s second expansion: The Grand Tournament!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mMediaInfoList.add(new MediaInfo.Builder(
                "http://srv60.clipconverter.cc/download/uZbYn4Gp4n%2BwZHBxl5qWanFi5KWmqW9o4pSXa3Bom2tpY2q0qc%2FMqHyf1qiZpa2d2A%3D%3D/The%20Grand%20Tournament%20Trailer.mp4")
                .setContentType("video/mp4")
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setStreamDuration(53 * 1000)
                .setMediaTracks(null)
                .setCustomData(jsonObj2)
                .setMetadata(mMediaMetadata2)
                .build());

        return mMediaInfoList;
    }

}
