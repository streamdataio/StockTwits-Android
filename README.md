# StockTwits-Android
StockTwits is a demo Android application which displays a StockTwits feed in real time using streamdata.io.

### Dependencies ###
* For image display : <a href="https://github.com/koush/ion#get-ion" target="_blank">ION</a> & <a href="http://square.github.io/picasso/" target="_blank">Picasso</a>
* for HTML char escaping : <a href="https://commons.apache.org/proper/commons-lang/download_lang.cgi" target="_blank">Jakarta Commons Lang</a>

Dependencies for Streamdata.io integration :
* SSE: <a href="https://github.com/tylerjroach/eventsource-android" target="_blank">Eventsource-android</a>
* JSON library: <a href="https://github.com/FasterXML/jackson-databind" target="_blank">Jackson</a>
* JSON patch: <a href="https://github.com/fge/json-patch" target="_blank">JSON Patch</a>

### Getting started ###

To run the sample:

First clone this project from GitHub.

Sign in on <a href="http://streamdata.io" target="_blank">Streamdata.io</a> to create your account and get your Streamdata.io token.

Copy your token.

In the project, open the /src/main/assets/stocktwits.properties file. Paste your token in the following property: 

streamdataToken=YOUR_TOKEN_HERE

Open the project with Android Studio.

Run the app.

### Other resources ###

In addition, you may be interested in reading this blog post which complements this demo: http://streamdata.io/blog/android-stocktwits-real-time-feed/
