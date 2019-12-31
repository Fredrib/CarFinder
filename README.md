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

To solve the problem I've used the MVVM design pattern and the reason for that is based on the nature of the UI on this project which would be just a form or visualization of the data provided by the webservice. If there were more complex interactions between the Presentation Layer and the Domain Layer, I would possibly use the MVP pattern, to avoid putting too much logic into the View layer.

For the Model layer I choosed the repository pattern, looking at flexibility if I decided later to cache/persist some data. I've end up using the repository to cache the car selection, as this allowed me to easily share the car selection between the view models an activites.

The decision for using different activities for each screen was based on simplicity to implement the Instrumented tests, so each screen would be able to be launched individually while testing the UI of that particular screen. Otherwise I would have to implement the flow to get to the sencondary screen in every single text of that screen, even that not being part of the test.

I lot of thing were not implemented on this project due to the time constraint and my availability to work on it, but for future improvements I would add the Intrumented Tests, add support for bigger screens (where both screens could be show on the same activity) and a bottom sheet with greater details of the car upon selection.
