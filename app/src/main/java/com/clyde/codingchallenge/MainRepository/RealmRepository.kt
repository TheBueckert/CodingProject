package com.clyde.codingchallenge.MainRepository

import android.content.ContentValues
import android.util.Log
import com.clyde.codingchallenge.models.Result
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import java.lang.Exception

object RealmRepository {
    var realm = Realm.getDefaultInstance()

    // this function will create a realm object for each item returned by the api search
    fun copyToRealm(resultsArray: List<Result>) {
        realm = Realm.getDefaultInstance()

        resultsArray.forEach { result ->
            try {
                realm.executeTransactionAsync({ bgRealm ->
                    bgRealm.copyToRealmOrUpdate(result)

                }, {// on success
                    Log.d(
                        ContentValues.TAG,
                        "copyToRealm: copied ${result.trackName} to ${realm.path}"
                    )

                }, { // on failure
                    Log.d(ContentValues.TAG, "copyToRealm: failed to copy ${result.trackName}")
                })

            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "copyToRealm: ", e)

            } finally {

            }
        }

        realm.close()
    }

    // search within the default realm for items matching the searchTerm
    // TODO REALM IS INCREDIBLY FINICKY WITH SEARCH PARAMETERS. EXACT MATCHES MAY RETURN 0 RESULTS
    // This function will probably be moved when creating a proper data repo class
    fun searchLocallyInRealm(searchTerm: String): RealmResults<Result>? {
        realm = Realm.getDefaultInstance()
        var searchResult: RealmResults<Result>? = null

        try {
            searchResult = realm.where<Result>()
                .contains("trackName", searchTerm)
                .limit(20)
                .findAll()

            if (!searchResult.isEmpty()) return searchResult


        } catch (e: Exception) {
            Log.e("RealmException", "onQueryTextSubmit: ", e)
        }

        realm.close()

        return searchResult

    }


}