# NearByMVP
A simple application that consumes a public location based rest service ( Foursquare ) to query a list of businesses near you. 
Application should display and sort those businesses based on distance from the device’s current location.

Additionally the application use a caching mechanism to store your queried data so it can still be displayed if the
device cannot access the internet.

## Design Architecture
MVP( Model-View-Presenter).
