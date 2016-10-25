# Project 2 - NYTNews

NYTNews is an android app that allows a user to search for articles on web using simple filters. The app utilizes [New York Times Search API](http://developer.nytimes.com/docs/read/article_search_api_v2).

Time spent: 12 hours spent in total

## User Stories

The following **required** functionality is completed:

* [X] User can **search for news article** by specifying a query and launching a search. Search displays a grid of image results from the New York Times Search API.
* [X] User can click on "settings" which allows selection of **advanced search options** to filter results
* [X] User can configure advanced search filters such as:
  * [X] Begin Date (using a date picker)
  * [X] News desk values (Arts, Fashion & Style, Sports)
  * [X] Sort order (oldest or newest)
* [X] Subsequent searches have any filters applied to the search results
* [X] User can tap on any article in results to view the contents in an embedded browser.
* [X] User can **scroll down to see more articles**. The maximum number of articles is limited by the API search.

The following **optional** features are implemented:

* [X] Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
  *[X] When hitting limits error toast will show (Catch-all of issue with query api)
  *[X] When there are internet issues (Wifi down), another toast will show as well
* [X] Used the **ActionBar SearchView** or custom layout as the query box instead of an EditText
* [ ] User can **share an article link** to their friends or email it to themselves
* [X] Replaced Filter Settings Activity with a lightweight modal overlay
* [X] Improved the user interface and experiment with image assets and/or styling and coloring
  *[X] In previous projects, I went with very cool colors(https://github.com/VictorHom/Flickster) or very warm colors (https://github.com/VictorHom/PomoList). I went very simple this time with a pastel blue/purple in the toolbar, the cursors, and text colors.

The following **bonus** features are implemented:

* [X] Use the [RecyclerView](http://guides.codepath.com/android/Using-the-RecyclerView) with the `StaggeredGridLayoutManager` to display improve the grid of image results
* [X] For different news articles that only have text or only have images, use [Heterogenous Layouts](http://guides.codepath.com/android/Heterogenous-Layouts-inside-RecyclerView) with RecyclerView
  * [X] The 2 views are image on top / text on bottom vs text on top / image on bottom. The type was simply based on the length of the title which was arbitrary in deciding
* [X] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler).
* [ ] Leverages the [data binding support module](http://guides.codepath.com/android/Applying-Data-Binding-for-Views) to bind data into layout templates.
* [X] Replace all icon drawables and other static image assets with [vector drawables](http://guides.codepath.com/android/Drawables#vector-drawables) where appropriate.
* [X] Replace Picasso with [Glide](http://inthecheesefactory.com/blog/get-to-know-glide-recommended-by-google/en) for more efficient image rendering.
* [X] Uses [retrolambda expressions](http://guides.codepath.com/android/Lambda-Expressions) to cleanup event handling blocks.
* [ ] Leverages the popular [GSON library](http://guides.codepath.com/android/Using-Android-Async-Http-Client#decoding-with-gson-library) to streamline the parsing of JSON data.
* [ ] Leverages the [Retrofit networking library](http://guides.codepath.com/android/Consuming-APIs-with-Retrofit) to access the New York Times API.
* [X] Replace the embedded `WebView` with [Chrome Custom Tabs](http://guides.codepath.com/android/Chrome-Custom-Tabs) using a custom action button for sharing. (_**2 points**_)

The following **additional** features are implemented:

* [X] List anything else that you can get done to improve the app functionality!
  * [X] I was unsure of the filter settings. You can set your filter settings in the modal, but you can also toggle the filter to whether or not apply the toggle to the search or not.

## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/jv9aGLJ.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />
<img src='http://i.giphy.com/3oz8xvwETSl44lrzJm.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

Link since my file might be too big
http://imgur.com/jv9aGLJ

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.
I got the "infinite scrolling" to work at one point. Then I built extra features and the performance of the api calls from the scrolling seem to get worst. The issue has not been fixed.

In debugger mode, it is working as I step through the code, so it is making it tough to optimize :(

Update: I needed to make additional calls when there was a status code 429 since I was making queries too quickly! The scrolling works now.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [OkHttp](https://github.com/square/okhttp) - An HTTP & HTTP/2 client for Android and Java applications
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android

## License

    Copyright 2016 Victor

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.