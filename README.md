# CarFinder

I've used for this project the following libraries, besides some standard libraries of the Android Framework:

* Google Play Services: To be able to interact with teh Google Maps SDK
* Retrofit + OkHttp: To make network calls.
* GSON: For serialization of objects and the reflection engine for Retrofit.
* RXJava2: For asynchronous operations and helper to use the Observable pattern thought the app.
* Koin: For dependency injection.
* Picasso: For image loading and caching.
* Junit: For unit testing.
* Mockk: To mock dependencies on the unit test.
* MockWebServer: To mock network calls on the unit tests.

The intrumented tests are missing, due to time constraints, but they were intended to be done using Espresso and to maximize the test coverage of the proposed structures.
