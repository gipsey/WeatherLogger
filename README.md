# WeatherLogger

Demo app leveraging a specific https://openweathermap.org/ API, for displaying temperature for
user's location. The project is a base for a potential Kotlin Multiplatform project, supporting both
Android and iOS.

### Dependencies for running the project:

- Android Studio Arctic Fox (2020.3.1) Beta 3 or newer
- Kotlin plugin 1.5.10 or newer

### Use cases

When user opens the app, loading weather data for the current location should be request explicitly
by using the action button. By default the app displays previously downloaded weather data - if
there is any.