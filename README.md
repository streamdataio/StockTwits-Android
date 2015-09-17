# StockTwits-Android
StockTwits is a demo Android application which displays a StockTwits feed in real time using streamdata.io.

### Dependencies ###
* For image display : [ION](https://github.com/koush/ion#get-ion) & [Picasso](http://square.github.io/picasso/)
* for HTML char escaping : [Jakarta Commons Lang](https://commons.apache.org/proper/commons-lang/download_lang.cgi)

Dependencies for Streamdata.io integration :
* SSE: [Eventsource-android](https://github.com/tylerjroach/eventsource-android)
* JSON library: [Jackson](https://github.com/FasterXML/jackson-databind)
* JSON patch: [JSON Patch](https://github.com/fge/json-patch)

### Getting started ###

To run the sample:

First clone this project from GitHub.

Sign in on <a href="http://streamdata.io" target="_blank">Streamdata.io</a> to create your account and get your Streamdata.io token.

Copy your token.

In the project, open the /src/main/assets/stocktwits.properties file. Paste your token in the following property: 

streamdataToken=YOUR_TOKEN_HERE

Open the project with Android Studio.

Run the app.
