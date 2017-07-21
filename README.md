# rdp-test
RDP Integration Test. Test API Include the UI (web-form) generated.

## Pre-requisites
* After clone the project to your local machine manually create new file with name ``key.properties`` which containing these fields :
```
api_endpoint : [endpoint to rdp api]
get_notif_endpoint : [endpoint for notification]
mid : [mid from rdp]
secret_key : [secret key for signing purpose from rdp]
```
* Make sure you have notification handler up and running to handle notification post-payment (UITest). In this case I use [simple web-services](https://secure-hollows-99342.herokuapp.com/) using Lumen
hosted on Heroku. Github link (<stubb!!!>)

* Make sure you have [Chromedriver](https://sites.google.com/a/chromium.org/chromedriver/) installed in your local machine

## Test process
Test scenario and report can be found in repo [wiki](https://github.com/bolon/rdp-test/wiki).
