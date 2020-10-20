package com.clyde.codingchallenge.Application

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

// Will be ran when the application first opens. Creates a default realm that can be accessed globally
class CodingChallengeApplication :Application(){

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder().name("movie.realm").build()
        Realm.setDefaultConfiguration(config)
    }
}