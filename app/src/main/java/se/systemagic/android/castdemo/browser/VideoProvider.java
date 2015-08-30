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
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;

public final class VideoProvider {

    private static final String TAG = "VideoProvider";

    public static final String KEY_DESCRIPTION = "description";


    private VideoProvider() {

    }

    public static List<MediaInfo> load_data () {
        List<MediaInfo> mMediaInfoList = new ArrayList<MediaInfo>();
        mMediaInfoList.add(createMediaInfoForStreamer("lirik", "Lirik", "http://streamernews.tv/wp-content/uploads/2015/03/liriksubhotlinemarch14_2015_.jpg", "http://streamernews.tv/wp-content/uploads/2015/03/liriksubhotlinemarch14_2015_.jpg"));
        mMediaInfoList.add(createMediaInfoForStreamer("onenationofgamers", "Trump", "http://cdn0.dailydot.com/uploaded/images/original/2014/12/16/10600470_1494067640864563_749847439709905759_n.jpg", "http://cdn0.dailydot.com/uploaded/images/original/2014/12/16/10600470_1494067640864563_749847439709905759_n.jpg"));
        return mMediaInfoList;
    }

    private static MediaInfo createMediaInfoForStreamer(String name, String userName, String img1, String img2) {
        MediaMetadata mMediaMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
        mMediaMetadata.putString(MediaMetadata.KEY_TITLE, userName);
        mMediaMetadata.putString(MediaMetadata.KEY_SUBTITLE, "Twitch");
        mMediaMetadata.addImage(new WebImage(Uri.parse(img1)));
        mMediaMetadata.addImage(new WebImage(Uri.parse(img2)));
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject();
            jsonObj.put(KEY_DESCRIPTION, "Description...");
        } catch (JSONException e) {
            e.printStackTrace();
        }

       return new MediaInfo.Builder(getTwitchUrl(name))
                .setContentType("video/mov")
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setStreamDuration(597 * 1000)
                .setMediaTracks(null)
                .setCustomData(jsonObj)
                .setMetadata(mMediaMetadata)
                .build();
    }

    private static String getTwitchUrl(String streamerName){
        String response = new String();
        try {
            URL twitch_token = new URL(
                    new String("https://api.twitch.tv/api/channels/"
                            + streamerName +
                            "/access_token"));

            BufferedReader reader = new BufferedReader(new InputStreamReader(twitch_token.openConnection().getInputStream()));

            String stringBuffer;
            String stringText = "";
            while ((stringBuffer = reader.readLine()) != null) {
                stringText += stringBuffer;
            }

            reader.close();

            String new_url = "http://usher.twitch.tv/api/channel/hls/" + streamerName + ".m3u8?";

            try {
                JSONObject jsonObject = new JSONObject(stringText);

                String sig = jsonObject.getString("sig");
                String token = jsonObject.getString("token").replace("\\", "");

                new_url += "player=twitchweb"
                        + "&sig=" + URLEncoder.encode(sig, "UTF-8")
                        + "&token=" + URLEncoder.encode(token,"UTF-8")
                        + "&allow_audio_only=true"
                        + "&allow_source=true"
                        + "&type=any"
                        + "&p=6";

            } catch (JSONException e) {

            }

            URL twitch_stream = new URL(new_url);

            Log.d(TAG,new_url);

            BufferedReader reader2 = new BufferedReader(new InputStreamReader(twitch_stream.openConnection().getInputStream()));

            stringText = "";
            while ((stringBuffer = reader2.readLine()) != null) {
                stringText += stringBuffer;
            }

            reader2.close();

            //response = stringText.split("#EXT-X-MEDIA")[4].split(",")[9].split("\"")[2];
            //response = "http://devimages.apple.com/iphone/samples/bipbop/bipbopall.m3u8";
            response = stringText.split("#EXT-X-MEDIA")[4].split("\"low\"")[2];

        } catch (MalformedInputException e) {
            response = e.toString();
        } catch (IOException e) {
            response = e.toString();
        }
        return response;
    }
}
