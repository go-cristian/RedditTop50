[![Build Status](https://travis-ci.org/iyubinest/RedditTop50.svg?branch=master)](https://travis-ci.org/iyubinest/RedditTop50)

REDDIT TOP STORIES CLIENT
======

A sample Android app which showcases common open source libraries.
The App tries to use a clean OOP structure that's focused on tests, the first design decision was 
to take divide the app in two big components `data` & `ui`, the first one contains all the data 
suppliers like http, or components to save images, the main reason for this to exist is based on the 
`Repository Pattern`.
 
The UI components are based on the `MVP` pattern without using the same names, where a `Source` is 
a components that provides the data for a UI trough the `View` and the `Repositories` handles the 
way of retains and gets the data. 

This project doesn't contains any comments because it tries to be self-commented, this approach 
encourages that the code is the documentation and it requires to be updated as it evolves.

Features
---------

 * List of top stories on reddit
 * List pagination
 * List Swipe to refresh
 * Memory cache for rotation 
 * Layout with different number of columns depending on the device
 * Show preview image
 * Save preview image


Screenshots
---------

![alt text](https://github.com/iyubinest/RedditTop50/blob/master/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png?raw=true "App Icon")
![alt text](https://github.com/iyubinest/RedditTop50/blob/master/art/list.png "Screnshoot Reddit Posts")
![alt text](https://github.com/iyubinest/RedditTop50/blob/master/art/detail.png "Screnshoot Reddit Preview")


Libraries
---------

 * Dagger
 * ButterKnife
 * Retrofit
 * Auto-Parcel
 * Gson
 * Glide
 * PhotoView
 * Espresso
 * JUnit
 * Mockito
 * MockWebServer
 
 
TO-DO
---------

 * Check for fixes


License
-------

    Copyright 2016 Cristian Gómez

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
