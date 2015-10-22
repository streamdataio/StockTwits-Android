package com.streamdataio.stocktwits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

public class DetailView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);

        Intent i = getIntent();
        Tweet tweet = (Tweet) i.getSerializableExtra("tweet");
        User user = tweet.getUser();

        ((TextView) findViewById(R.id.text)).setText(tweet.getBody());
        ((TextView) findViewById(R.id.dateTime)).setText(tweet.getDateTime());

        if (tweet.getImgURL().isEmpty()) {
            // Here we avoid loading a default picture from network or disk memory
            // and leave the imageView empty
            ((ImageView) findViewById(R.id.pic)).setImageDrawable(null);
        } else {
            Picasso.with(DetailView.this)
                    .load(tweet.getImgURL())
                    .into((ImageView) findViewById(R.id.pic));
        }

        ((TextView) findViewById(R.id.username)).setText(user.getUsername());
        ((TextView) findViewById(R.id.name)).setText(user.getName());
        ((TextView) findViewById(R.id.bio)).setText(user.getBio());
        ((TextView) findViewById(R.id.joined)).setText((user.getJoin().isEmpty())?"":"Joined on " + user.getJoin());

        // avatar
        Ion.with((ImageView) findViewById(R.id.avatar))
                .placeholder(R.drawable.defaulticon)
                .error(R.drawable.defaulticon)
                .load(user.getAvatarURL());

        ((TextView) findViewById(R.id.location)).setText(user.getLocation());
        ((TextView) findViewById(R.id.followers)).setText((user.getFollowers().isEmpty())?"":user.getFollowers() + " followers");
        ((TextView) findViewById(R.id.following)).setText((user.getFollowing().isEmpty())?"":"Following " + user.getFollowing());
        ((TextView) findViewById(R.id.identity)).setText(user.getOff());
        ((TextView) findViewById(R.id.experience)).setText(user.getXpLevel());
        ((TextView) findViewById(R.id.holding_period)).setText(user.getHoldingPeriod());
        ((TextView) findViewById(R.id.approach)).setText((user.getApproach().isEmpty())?"":user.getApproach() + " Approach");
        ((TextView) findViewById(R.id.website)).setText((user.getWebURL().isEmpty())?"":"Website: " + user.getWebURL());
        ((TextView) findViewById(R.id.ideas)).setText((user.getIdeas().isEmpty())?"":user.getIdeas() + " ideas");

    }

}
