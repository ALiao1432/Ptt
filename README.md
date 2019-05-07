# Ptt
An app fetching data from https://www.ptt.cc/bbs/index.html and use Jsoup to parse HTML responses, then let user explore them intuitively

# Screenshot
![initial screen](https://i.imgur.com/K9J0M6P.png) ![FavFragment](https://i.imgur.com/4WPTMNV.png) ![HotFragment](https://i.imgur.com/0GqUHxZ.png)
![PttClassFragment](https://i.imgur.com/6LXSasi.png) ![ArticleListFragment](https://i.imgur.com/qqrk7Vf.png) ![ArticleFragment](https://i.imgur.com/GtkEkUg.png)
!["LOAD NEW PUSH" button in ArticleFragment](https://i.imgur.com/9i1v6vE.png) ![loading new push in ArtucleFragment](https://i.imgur.com/GKOGPeg.png)

# Used library
* [RxBinding](https://github.com/JakeWharton/RxBinding) : let us to interact with UI in reactive way, also include RxJava2 and RxAndroid

* [Retrofit](https://square.github.io/retrofit/) : building service based on the response from ptt, we also need [GsonConverterFactory](https://github.com/square/retrofit/blob/master/retrofit-converters/gson/src/main/java/retrofit2/converter/gson/GsonConverterFactory.java) and [RxJava2CallAdapterFactory](https://github.com/square/retrofit/blob/master/retrofit-adapters/rxjava2/src/main/java/retrofit2/adapter/rxjava2/RxJava2CallAdapterFactory.java) to convert JSON type result to java object and make it observable

* [OkHttp](https://square.github.io/okhttp/) : to create interceptor for debugging

* [GSON](https://github.com/google/gson) : for converting JSON data to java objects easily


* [Glide](https://github.com/bumptech/glide) : for loading image


* [NetworkState](https://github.com/ALiao1432/NetworkState) : the library for monitoring if the network is connecting via Wifi, Mobile or not connecting. It provide the ability for app to reload the data when reconnection to valid network

* [Material Components](https://material.io/develop/android/docs/getting-started/) : use the latest ui components from google

* [MorphView](https://github.com/ALiao1432/MorphView) : the library can perform animation defined by svg files

# More detail
![Navigation architecture](https://i.imgur.com/f6gmWP1.png)

- - -

![Overall working flow](https://i.imgur.com/Xlq4bdT.png)

- - -

![Strategy for no network connection](https://i.imgur.com/6RnnAD1.png)

# [demo](https://photos.app.goo.gl/iLqpHSYSDLQ5y7t69)
