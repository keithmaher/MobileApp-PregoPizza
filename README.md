# PregoPizza User App
Android Development Version 1

|Name            |Student No|
|----------------|----------|
|Keith Maher     | 20074612 |

* This app was developed for Mobile Application module in Waterford Institute of Technology.
* My main goal for this version of the app was to get a cart and menu system up and running.
* The user inputs a name, number and delivery address upon completition of an order.
* V2 will hopefully include option for Delivery or Collection and linking tables wit a sub menu of sides/pizza's and other menu options.

# UX
* Originally I was begining to over complicate my app with over the top UX.
* I finaly decided to keep it clean with very basic but functional Activities.

# DX
* No testing included.
* Issues with adding List<> to an Object and getting a running total in Cart Ativity.
* Validation was included where possiable -> Cart = empty, Place order button no functional.

# GitHub
* I used Github coming close to the end of the Assignment to track and log any changes.

# Persistence
* The persistance within this app is Firebase realtime storage.
* The menu is stored in a JSON within Firebase and read into the App.
* Adding to cart created an order object in the firebase and when the order request is
* finally submitted to Firebase that order object is then delete.

# References
* https://www.youtube.com/channel/UCllewj2bGdqB8U9Ld15INAg
* https://ddrohan.github.io/wit-mad-1-2018-index.html
* https://firebase.google.com/docs/android/setup
* https://developer.android.com/guide/topics/ui/layout/recyclerview.html
* https://stackoverflow.com/questions/22736092/declaring-arraylist-in-java-constructor
* https://developer.android.com/guide/topics/ui/dialogs.html
