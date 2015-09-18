package com.streamdataio.stocktwits;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainViewListAdapter extends BaseAdapter {
    private final LayoutInflater inflater;
    private ArrayList<Tweet> tweets;
    private final Activity activity;

    /**
     * constructor for this mainListViewAdapter
     *
     * @param context
     * @param aTweetList
     */
    public MainViewListAdapter(final Context context, ArrayList<Tweet> aTweetList) {
        tweets = aTweetList;
        inflater = LayoutInflater.from(context);
        activity = (Activity)context;
    }

    public void updateData(ArrayList<Tweet> tweets){
        this.tweets = new ArrayList<Tweet>(tweets);
    }

    @Override
    public int getCount() {
        return tweets != null ? tweets.size() : 0;
    }

    @Override
    public Object getItem(int i) {
        return tweets != null ? tweets.get(i) : null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewWrapper viewWrapper;
        Picasso mPicasso = Picasso.with(activity);
        // Next line allows visual tracking of image source in ui (net, disk or mem)
        // mPicasso.setIndicatorsEnabled(true);
        // NOTE: red ribbon= network, green ribbon= memory, purple ribbon= disk
        if(convertView == null) {
            viewWrapper = new ViewWrapper();
            convertView = inflater.inflate(R.layout.stocktweet, null);
            viewWrapper.tweet = (TextView) convertView.findViewById(R.id.tweet);
            viewWrapper.name = (TextView) convertView.findViewById(R.id.name);
            viewWrapper.userName = (TextView) convertView.findViewById(R.id.username);
            viewWrapper.date = (TextView) convertView.findViewById(R.id.dateTime);
            viewWrapper.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            viewWrapper.img = (ImageView) convertView.findViewById(R.id.tweet_img);
            convertView.setTag(viewWrapper);
        } else {
            viewWrapper = (ViewWrapper) convertView.getTag();
        }

        if(tweets != null){
            // Set ui here
            viewWrapper.tweet.setText(tweets.get(position).getBody());
            viewWrapper.name.setText(tweets.get(position).getUser().getName());
            viewWrapper.userName.setText(tweets.get(position).getUser().getUsername());
            viewWrapper.date.setText(tweets.get(position).getDateTime());
            Ion.with(viewWrapper.avatar)
                    .placeholder(R.drawable.defaulticon)
                    .error(R.drawable.defaulticon)
                    .load(tweets.get(position).getUser().getAvatarURL());
            if (tweets.get(position).getImgURL().isEmpty()){
                // Here we avoid loading a default picture from network or disk memory
                // and leave the imageView empty
                viewWrapper.img.setImageDrawable(null);
            } else {
                mPicasso.with(activity)
                        .load(tweets.get(position).getImgURL())
                        .into(viewWrapper.img);
            }
        }
        return convertView;
    }


    /* **************************** Class ViewWrapper **************************** */
    private static class ViewWrapper {
        TextView tweet;
        TextView name;
        TextView userName;
        TextView date;
        ImageView avatar;
        ImageView img;
    }
}


