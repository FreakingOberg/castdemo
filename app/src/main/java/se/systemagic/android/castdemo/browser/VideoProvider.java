/**
 * Created by Viktor on 2015-07-24.
 *
 * Own version of itemloader. Does not actually fetch any data from url but just creates a fake
 * list of metadata objects including including Big Buck Bunny
 */

package se.systemagic.android.castdemo.browser;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.common.images.WebImage;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public final class VideoProvider {

    private VideoProvider() {

    }

    public static List<MediaInfo> fake_load_data () {
        List<MediaInfo> mMediaInfoList = new ArrayList<MediaInfo>();
        MediaMetadata mMediaMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
        mMediaMetadata.putString(MediaMetadata.KEY_TITLE, "Big Buck Bunny");
        mMediaMetadata.putString(MediaMetadata.KEY_SUBTITLE, "Blender Foundation");
        mMediaMetadata.addImage(new WebImage(Uri.parse("https://peach.blender.org/wp-content/uploads/bbb-splash.thumbnail.png")));
        mMediaInfoList.add(new MediaInfo.Builder(
                "http://download.blender.org/peach/bigbuckbunny_movies/big_buck_bunny_720p_h264.mov")
                .setContentType("video/mov")
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setMetadata(mMediaMetadata)
                .build());

        MediaMetadata mMediaMetadata2 = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
        mMediaMetadata2.putString(MediaMetadata.KEY_TITLE, "The Grand Tournament - Trailer");
        mMediaMetadata2.putString(MediaMetadata.KEY_SUBTITLE, "Blizzard Entertainment");
        mMediaMetadata2.addImage(new WebImage(Uri.parse("http://i3.ytimg.com/vi/Fe7XDBtlQzg/maxresdefault.jpg")));
        mMediaInfoList.add(new MediaInfo.Builder(
                "http://r17---sn-a5m7ln7d.googlevideo.com/videoplayback?sver=3&lmt=1437185375508871&signature=732AAB53C2C92188AEE2BAB4C61399535E096988.EA395C931927CB94E200987B51A23E9E5E91D243&ms=au&pl=23&mv=m&mt=1437751054&dur=52.360&mn=sn-a5m7ln7d&mm=31&ratebypass=yes&initcwndbps=5876250&expire=1437772781&itag=22&upn=yJ9O6XSNyU4&id=o-ACFODrkakOMAXFfLwuwnfoewhJ-4cjnOOhGP0SMT4bp3&fexp=901816,916930,9406618,9407152,9407992,9408710,9408737,9410706,9414855,9415365,9415435,9415485,9415795,9416126,9416222,9416326,9417095&sparams=dur,id,initcwndbps,ip,ipbits,itag,lmt,mime,mm,mn,ms,mv,pl,ratebypass,source,upn,expire&mime=video/mp4&source=youtube&ipbits=0&key=yt5&ip=104.238.126.222&signature=&title=[wapdoze.com]The-Grand-Tournament-Trailer")
                .setContentType("video/mov")
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setMetadata(mMediaMetadata2)
                .build());

        return mMediaInfoList;
    }
}
