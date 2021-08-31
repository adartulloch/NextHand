package com.example.nexthand

import android.app.Application
import com.example.nexthand.models.Contact
import com.example.nexthand.models.Inquiry
import com.example.nexthand.models.Item
import com.parse.Parse
import com.parse.ParseObject

class ParseApplication : Application() {
    // Initializes Parse SDK as soon as the application is created
    override fun onCreate() {
        super.onCreate()
        ParseObject.registerSubclass(Item::class.java)
        ParseObject.registerSubclass(Inquiry::class.java)
        ParseObject.registerSubclass(Contact::class.java)
        Parse.initialize(Parse.Configuration.Builder(this)
                .applicationId(BuildConfig.APPLICATION_ID)
                .clientKey(BuildConfig.CLIENT_KEY)
                .server("https://parseapi.back4app.com")
                .enableLocalDataStore()
                .build())
    }
}