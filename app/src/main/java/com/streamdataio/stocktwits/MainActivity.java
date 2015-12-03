package com.streamdataio.stocktwits;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.google.common.base.Preconditions;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import tylerjroach.com.eventsource_android.EventSource;
import tylerjroach.com.eventsource_android.EventSourceHandler;
import tylerjroach.com.eventsource_android.MessageEvent;


public class MainActivity extends AppCompatActivity implements EventSourceHandler {
    private static final String TAG  = "StockTwits";
    private static final String STREAMDATA_TOKEN_PROPERTY_NAME = "streamdataToken";
    private static final String STOCKTWITS_API_PROPERTY_NAME = "api";

    private static final String STREAMDATA_PROXY_ADDRESS = "https://streamdata.motwin.net/";
    private static final String STREAMDATA_TOKEN_HEADER_NAME = "X-Sd-Token";

    private static final String PROPERTIES_FILE = "stocktwits.properties";

    /* NOTE: this API used without authentication limits to 200 calls/hour */
    private static final String DEFAULT_STOCKTWITS_API = "https://api.stocktwits.com/api/2/streams/symbol/EURUSD.json";

    private String streamdata_token = "";
    private String stockTwits_api = "";
    private final ObjectMapper jsonObjectMapper = new ObjectMapper();
    private JsonNode data;
    private Map<String, String> requestHeaders = new HashMap<String, String>();
    private EventSource eventSource;
    private ArrayList<Tweet> tweets = new ArrayList<Tweet>();

    ListView mainListView;
    MainViewListAdapter mainListViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        // Read properties from the /assets directory
        Resources resources = this.getResources();
        AssetManager assetManager = resources.getAssets();
        try {
            InputStream inputStream = assetManager.open(PROPERTIES_FILE);
            Properties properties = new Properties();
            properties.load(inputStream);
            // read token
            streamdata_token = properties.getProperty(STREAMDATA_TOKEN_PROPERTY_NAME);
            Preconditions.checkState(streamdata_token != null && streamdata_token.length() > 0 && !"YOUR_TOKEN_HERE".equals(streamdata_token),
                    "streamdata.io token is not set. Please enter it in the properties file.");
            // read api if provided, otherwise use DEFAULT_STOCKTWITS_API.
            stockTwits_api = properties.getProperty(STOCKTWITS_API_PROPERTY_NAME, DEFAULT_STOCKTWITS_API);

            // build request headers map
            requestHeaders.put(STREAMDATA_TOKEN_HEADER_NAME, streamdata_token);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(),e);
        }

        mainListView = (ListView) findViewById(R.id.listView);
        mainListViewAdapter = new MainViewListAdapter(this,tweets);
        mainListView.setAdapter(mainListViewAdapter);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tweet t = tweets.get(position);
                Intent i = new Intent(MainActivity.this, DetailView.class);
                Bundle mBundle = new Bundle();
                mBundle.putSerializable("tweet", t);
                i.putExtras(mBundle);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        // disconnect eventSource
        disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // connect/reconnect eventSource
        connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // disconnect eventSource
        disconnect();
        Picasso.with(this).cancelTag(this);
    }


    /* ******************************* SSEHandler implementation ************************************ */
    @Override
    public void onConnect() throws Exception {
        if (Log.isLoggable(TAG, Log.VERBOSE))
            Log.v(TAG, "SSE Connected");
    }

    @Override
    public void onMessage(String event, MessageEvent message) throws Exception {
        if (Log.isLoggable(TAG, Log.VERBOSE))
            Log.v(TAG, event);
        try {
            if ("data".equals(event)) {
                data = jsonObjectMapper.readTree(message.data);
                updateList();
            } else if ("patch".equals(event)) {
                JsonNode patchNode = jsonObjectMapper.readTree(message.data);
                JsonPatch patch = JsonPatch.fromJson(patchNode);
                data = patch.apply(data);
                // Call GC to avoid unnecessary mem overload
                //System.gc();
                updateList();
                runOnUiThread(new Runnable() {
                    //@Override
                    public void run() {
                        // Alert user of new activity
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "New recent activity",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 0);
                        toast.show();
                    }
                });
            } else {
                // If error is due to too many calls to API make a toast instead of having app crash
                if (message.toString().contains("429 Unknown Error")) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Too many API calls. Please close app and try again later.",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    disconnect();
                } else {
                    throw new RuntimeException("Unexpected SSE message: " + event);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    @Override
    public void onError(Throwable t) {
        Log.e(TAG, t.getMessage(), t);
    }

    @Override
    public void onClosed(boolean willReconnect) {
        if (Log.isLoggable(TAG, Log.VERBOSE))
            Log.v(TAG, "reconnect=" + willReconnect);
    }

    /* ******************************* END SSEHandler implementation ************************************ */


    private void updateList() {
        JsonNode jsonData = data.get("messages");
        tweets.clear();

        for (Iterator<JsonNode> i = jsonData.iterator(); i.hasNext(); ) {
            JsonNode jsonTweet = i.next();
            // creating user from json object
            JsonNode userNode = jsonTweet.get("user");

            User user = new User(
                    "@" + jsonTweet.get("user").get("username").asText(),
                    (!userNode.path("name").isMissingNode())?userNode.get("name").asText():"",
                    (!userNode.path("bio").isMissingNode())?StringEscapeUtils.unescapeHtml4(userNode.get("bio").asText()):"",
                    (!userNode.path("join_date").isMissingNode())?userNode.get("join_date").asText():"",
                    (!userNode.path("avatar_url").isMissingNode())?userNode.get("avatar_url").asText():"",
                    (!userNode.path("location").isMissingNode())?userNode.get("location").asText():"",
                    (!userNode.path("followers").isMissingNode())?userNode.get("followers").asText():"",
                    (!userNode.path("following").isMissingNode())?userNode.get("following").asText():"",
                    (!userNode.path("identity").isMissingNode())?userNode.get("identity").asText():"",
                    (!userNode.path("trading_strategy").path("experience").isMissingNode())?userNode.get("trading_strategy").get("experience").asText():"",
                    (!userNode.path("trading_strategy").path("holding_period").isMissingNode())?userNode.get("trading_strategy").get("holding_period").asText():"",
                    (!userNode.path("trading_strategy").path("approach").isMissingNode())?userNode.get("trading_strategy").get("approach").asText():"",
                    (!userNode.path("website_url").isMissingNode())?userNode.get("website_url").asText():"",
                    (!userNode.path("ideas").isMissingNode())?userNode.get("ideas").asText():""
            );

            // created_at format "[date]T[time]Z".
            // Note: T = Tag and Z = Zeit
            // build readable date time
            String date = (!jsonTweet.path("created_at").isMissingNode())?jsonTweet.get("created_at").asText().replace("T", " at ").replace("Z", ""):"";

            // unescape the HTML in the text
            String body = StringEscapeUtils.unescapeHtml4((!jsonTweet.path("body").isMissingNode())?jsonTweet.get("body").asText():"");

            String imageUrl = "";
            if (!jsonTweet.path("entities").path("chart").path("large").isMissingNode()) {
                // here you can get
                // path("entities").path("chart").path("thumb")
                // path("entities").path("chart").path("large")
                // path("entities").path("chart").path("original")
                // we pick "large" for better display result
                imageUrl = jsonTweet.path("entities").path("chart").path("large").asText();
            }

            // Create new tweet object
            Tweet tweet = new Tweet(body,date,imageUrl,user);
            tweets.add(tweet);
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainListViewAdapter.updateData(tweets);
                mainListViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void connect() {
        // Detect network connection
        ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isNetworkConnected =
                (activeNetwork != null) &&
                (activeNetwork.isConnectedOrConnecting());

        if (isNetworkConnected) {
            try {
                eventSource = new EventSource(new URI(STREAMDATA_PROXY_ADDRESS), new URI(stockTwits_api), this, requestHeaders);
            } catch (URISyntaxException e) {
                Log.e(TAG, e.getMessage(), e);
            }
        } else {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(),
                            "No network connection",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void disconnect() {
        if (eventSource != null) {
            eventSource.close();
        }
        eventSource = null;
    }
}
