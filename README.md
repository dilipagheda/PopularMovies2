# PopularMovies - Android App - Stage 2

Popular Movies app - Stage 2 - As part of Udacity Android nanodegree assignment.

This app uses service - http://api.themoviedb.org/3/movie/popular to fetch most popular / highest rated movies and display to the user.
user can change sort order from settings menu

# API KEY
Please put your api key in gradle.properties file as below:

`MOVIES_API_KEY = "<your key>"`




# Test cases passed:

Verify that when app launches the first time, It reads from shared preferences and loads data correctly.

Verify that when user scrolls more, app makes right network calls with right page parameters and loads more data.

Verify that when user changes preferences while app is running, app loads data correctly.

Verify favorite movies are loaded from database and no network call is made to fetch movies. Note: It will still make calls for vidoes and reviews as they are not cached in the database.

Verify that movie details, trailers and reviews are loading correctly for a given movie id.

Verify that pagination works on reviews call.

Verify the UI layout for all screens.

use the app for a while with different execution paths , check debug logs and make sure app doesn't crash.

Verify error cases for empty state, network down, network error.

Verify progress bar is shown and gets hidden at right time.

Verify on roation, app doesn't crash and state is preserved.




# Project specifications:

### User Interface - Layout
   

UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.

Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.

UI contains a screen for displaying the details for a selected movie.

Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.

Movie Details layout contains a section for displaying trailer videos and user reviews.



### User Interface - Function
   


When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.

When a movie poster thumbnail is selected, the movie details screen is launched.

When a trailer is selected, app uses an Intent to launch the trailer.

In the movies detail screen, a user can tap a button (for example, a star) to mark it as a Favorite. Tap the button on a favorite movie will unfavorite it.



### Network API Implementation
   


In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.

App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.

App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.



### Data Persistence
    

The titles and IDs of the user’s favorite movies are stored using Room and are updated whenever the user favorites or unfavorites a movie. No other persistence libraries are used.


When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the database.


### Android Architecture Components

Database is implemented using Room. No other persistence libraries are used.

Database is not re-queried unnecessarily. LiveData is used to observe changes in the database and update the UI accordingly.

Database is not re-queried unnecessarily after rotation. Cached LiveData from ViewModel is used instead.

### Bonus features
Extend the favorites database to store the movie poster, synopsis, user rating, and release date, and display them even when offline.

Implement sharing functionality to allow the user to share the first trailer’s YouTube URL from the movie details screen.

# Tasks done 
branch off current codebase

convert list view to recycle view so that in future pagination library can be used

instead of image poster, display title in a textview during refactoring for faster testing

implement repository, view model and live data pattern to fetch data in pages and update the UI. only update the title for now. - Done

implement Room database by defining entities and DAO. include only id and title of the movie at this stage.

implement detail screen which only displays title with a favourite button. click on button will make entry in favourites table. 

delete a favourite movie item from database when user taps on the button again in detail screen. (In OnCreate(), find out if movie is favourite and update the button likewise).

put APIKEY in the grade.properties file and exclude that file from commit

implement tabbed view on details screen. first tab shows details, second tab shows trailers and third tab shows reviews.

implement additional calls in detail layout that shows reviews. 

implement additional calls in detail layout that shows trailers. 

handle for network errors such as 500, down etc.

handle empty state on all service calls and display text message accordingly.

handle paging validations

put progress bar

when activity / fragment calls onResume, check the status code and display accordingly.

put intent on trailer to open it on youtube

put back preferences code and test with existing preferences. check correct calls are being made.

add favourites option in preferences and implement it.

bug - page no. is not reseting when user changes between high popular to top rating:fixed

use constraints layout instead of relative layout for better performance.

Implement sharing functionality to allow the user to share the first trailer’s YouTube URL from the movie details screen.

put back grid layout with images and detail layout shows data as specified.

use data binding in detail screen to bind data to views.

