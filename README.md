# PregoPizza User App
Android Development Version 2

|Name            |Student No|
|----------------|----------|
|Keith Maher     | 20074612 |

* This app was developed for Mobile Application module in Waterford Institute of Technology.
* My main goal for this version of the app was to get a order and menu system up and running.
* You can see the menu without having to register.
* Once you register the menu changes and you see your cart and previous orders.
* The user can update their details and delete items from their cart.

# UX
* The user view menu without logging in.
* the navigation system is a nav drawer which was difficult to implement.

# DX
* No testing included.
* I tried to implement notifications when an order status is changed and might have locked access to the files.
* Validation was included where possiable
-> Cart = empty, Place order button no functional.
-> Profile = cant delete without confirming.

# GitHub
* I used Github coming close to the end of the Assignment to track and log any changes.

# Persistence
* The persistance within this app is Firebase realtime storage.
* The menu is stored in a JSON within Firebase and read into the App.
* Adding to order created an order object in the firebase and when the order request is
* finally submitted to Firebase that order object is then delete.
* Each user has there own cart and can have many orders.

# References
* https://www.youtube.com/channel/UCllewj2bGdqB8U9Ld15INAg
* https://ddrohan.github.io/wit-mad-1-2018-index.html
* https://firebase.google.com/docs/android/setup
* https://developer.android.com/guide/topics/ui/layout/recyclerview.html
* https://stackoverflow.com/questions/22736092/declaring-arraylist-in-java-constructor
* https://developer.android.com/guide/topics/ui/dialogs.html
* https://ddrohan.github.io/wit-mad-1-2018/topic04-persistence//talk-9-persistence/persistence.pdf
* https://developer.android.com/training/permissions/requesting
* https://firebase.google.com/docs/functions/get-started?authuser=0
