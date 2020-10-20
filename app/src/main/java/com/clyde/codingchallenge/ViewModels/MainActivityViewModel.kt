package com.clyde.codingchallenge.ViewModels

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.clyde.codingchallenge.MainRepository.Repository
import com.clyde.codingchallenge.models.MovieItem
import com.clyde.codingchallenge.models.Result
import io.realm.Realm
import io.realm.RealmResults
import io.realm.kotlin.where
import java.lang.Exception

// This class is the view model that passes queries from the view to the api handling classes
// It also returns the data from the Api to the view after formatting it properly
class MainActivityViewModel : ViewModel() {

    // The search term is modified by the view and starts an api query
    private var viewSearchTerm: MutableLiveData<String> = MutableLiveData()
    var realm = Realm.getDefaultInstance()

    // The string required here will become a livedata object, changing it starts the query
    fun setSearchTerm(newSearchTerm: String) {
        val update = newSearchTerm
        if (viewSearchTerm.value == update) {
            return
        }
        viewSearchTerm.value = update
    }

    // The movie data is transformed based on the returns from the api query
    // This livedata changed here is being listened to by the mainActivity
    val moviesQueryResultObject: LiveData<MovieItem> = Transformations
        .switchMap(viewSearchTerm) { viewSearchTermString ->
            Repository.getMovies(viewSearchTermString)
        }


    fun cancelJobs() {
        Repository.cancelJobs()
    }

    // this funciton will create a realm object for each item returned by the api search
    fun copyToRealm(resultsArray: List<Result>) {
        realm = Realm.getDefaultInstance()

        resultsArray.forEach { result ->
            try {
                realm.executeTransactionAsync({ bgRealm ->
                    bgRealm.copyToRealmOrUpdate(result)

                }, {// on success
                    Log.d(TAG, "copyToRealm: copied ${result.trackName} to ${realm.path}")

                }, { // on failure
                    Log.d(TAG, "copyToRealm: failed to copy ${result.trackName}")
                })

            } catch (e: Exception) {
                Log.e(TAG, "copyToRealm: ", e)

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

            if(!searchResult.isEmpty()) return searchResult


        } catch (e: Exception) { Log.e("RealmException", "onQueryTextSubmit: ", e) }

        finally { }

        realm.close()

        return searchResult

    }


}


