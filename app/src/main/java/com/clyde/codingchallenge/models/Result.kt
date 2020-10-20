package com.clyde.codingchallenge.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

// This is the form that JSON objects take in the array returned from a query
// Required to have default values for the empty constructor required by RealmObject
@Parcelize
open class Result(
    var artistName: String? = null,
    var artworkUrl100: String? = null,
    var artworkUrl30: String? = null,
    var artworkUrl60: String? = null,
    var collectionArtistId: Int = 0,
    var collectionArtistViewUrl: String? = null,
    var collectionCensoredName: String? = null,
    var collectionExplicitness: String? = null,
    var collectionHdPrice: Double = 0.0,
    var collectionId: Int = 0,
    var collectionName: String? = null,
    var collectionPrice: Double = 0.0,
    var collectionViewUrl: String? = null,
    var contentAdvisoryRating: String? = null,
    var country: String? = null,
    var currency: String? = null,
    var discCount: Int = 0,
    var discNumber: Int = 0,
    var hasITunesExtras: Boolean? = null,
    var kind: String? = null,
    var longDescription: String? = null,
    var previewUrl: String? = null,
    var primaryGenreName: String? = null,
    var releaseDate: String? = null,
    var shortDescription: String? = null,
    var trackCensoredName: String? = null,
    var trackCount: Int = 0,
    var trackExplicitness: String? = null,
    var trackHdPrice: Double = 0.0,
    var trackHdRentalPrice: Double = 0.0,
    var trackId: Int = 0,
    @PrimaryKey
    var trackName: String? = null,
    var trackNumber: Int = 0,
    var trackPrice: Double = 0.0,
    var trackRentalPrice: Double =0.0,
    var trackTimeMillis: Int = 0,
    var trackViewUrl: String? = null,
    var wrapperType: String? = null
) : Parcelable, RealmObject(

){

}

