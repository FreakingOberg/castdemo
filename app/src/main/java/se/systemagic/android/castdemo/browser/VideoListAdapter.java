package se.systemagic.android.castdemo.browser;

/**
 * Created by Viktor on 2015-07-22.
 */

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager;
import se.systemagic.android.castdemo.R;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import com.androidquery.AQuery;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder>{

    private static final float mAspectRatio = 9f / 16f;
    private static final String TAG = "VideoListAdapter";
    private final ItemClickListener mClickListener;
    private final VideoCastManager mCastManager;
    private List<MediaInfo> videos;

    public VideoListAdapter(ItemClickListener clickListener) {
        mClickListener = clickListener;
        mCastManager = VideoCastManager.getInstance();
    }

    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        View parent = LayoutInflater.from(context).inflate(R.layout.browse_row, viewGroup, false);
        return ViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(VideoListAdapter.ViewHolder viewHolder, final int position) {
        final MediaInfo item = videos.get(position);
        MediaMetadata mm = item.getMetadata();

        viewHolder.setTitle(mm.getString(MediaMetadata.KEY_TITLE));
        viewHolder.setDescription(mm.getString(MediaMetadata.KEY_SUBTITLE));
        viewHolder.setImage(mm.getImages().get(0).getUrl().toString());
        viewHolder.mMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.itemClicked(v, item, position);
            }
        });

        viewHolder.mImgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.itemClicked(v, item, position);
            }
        });

        viewHolder.mTextContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mClickListener.itemClicked(v, item, position);
            }
        });
        viewHolder.mMenu.setVisibility(mCastManager.isConnected() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return videos == null ? 0 : videos.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final View mParent;
        private final View mMenu;
        private final View mTextContainer;
        private TextView mTitleView;
        private TextView mDescriptionView;
        private ImageView mImgView;
        private AQuery mAquery;

        public static ViewHolder newInstance(View parent) {
            ImageView imgView = (ImageView) parent.findViewById(R.id.imageView1);
            TextView titleView = (TextView) parent.findViewById(R.id.textView1);
            TextView descriptionView = (TextView) parent.findViewById(R.id.textView2);
            View menu = parent.findViewById(R.id.menu);
            View textContainer = parent.findViewById(R.id.text_container);
            AQuery aQuery = new AQuery(parent);
            return new ViewHolder(parent, imgView, textContainer, titleView, descriptionView, menu, aQuery);
        }

        private ViewHolder(View parent, ImageView imgView, View textContainer, TextView titleView,
                           TextView descriptionView, View menu, AQuery aQuery) {
            super(parent);
            mParent = parent;
            mImgView = imgView;
            mTextContainer = textContainer;
            mTitleView = titleView;
            mDescriptionView = descriptionView;
            mMenu = menu;
            mAquery = aQuery;
        }

        public void setTitle(String title) {
            mTitleView.setText(title);
        }

        public void setDescription(String description) {
            mDescriptionView.setText(description);
        }

        public void setImage(String imgUrl) {
            mAquery.id(mImgView).width(114).image(imgUrl,
                    true, true, 0, R.drawable.default_video, null, 0, mAspectRatio);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            mParent.setOnClickListener(listener);
        }

        public ImageView getImageView() {
            return mImgView;
        }
    }

    public void setData(List<MediaInfo> data) {
        videos = data;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void itemClicked(View v, MediaInfo item, int position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
}
