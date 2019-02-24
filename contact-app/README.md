# SIMPLE CONTACTS APP 

This is an android app for send OTP SMS to a list of contacts using the Twillo
service (With the free trial only verified phone numbers will get an SMS).

## Author
Shaleen Jain

## Libs used
* Various android official support libraries
* OkHTTP and Retrofit for a idiomatic and asyncronous way of making network requests that are not on the main thread
* ContraintLayout for flexiable and reliable view layout on various screen sizes and resolutions


## Good Practices
* Separations of concerns between Activities and Fragment.
* No long lasting static objects that can leak memory

## Structure of app
* Contacts list is statically defined in assets folder. Can be easily replaces with an API
* On-disk cache for storing local data (Due to time-constraint). Can be easily replaced with a database/API.
