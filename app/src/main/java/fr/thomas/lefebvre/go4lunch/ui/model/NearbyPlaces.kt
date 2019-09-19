package fr.thomas.lefebvre.go4lunch.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NearbyPlaces(
    val html_attributions: List<String>,
    val next_page_token: String,
    val results: List<Result>,
    val status: String
): Parcelable

@Parcelize
data class Result(
    val geometry: Geometry,
    val icon: String,
    val id: String,
    val name: String,
    val opening_hours: OpeningHours,
    val photos: List<Photo>,
    val place_id: String,
    val plus_code: PlusCode,
    val price_level: Int,
    val rating: Double,
    val reference: String,
    val scope: String,
    val types: List<String>,
    val user_ratings_total: Int,
    val vicinity: String,
    var distance:String
):Parcelable

@Parcelize
data class OpeningHours(
    val open_now: Boolean
): Parcelable

@Parcelize
data class Geometry(
    val location: Location,
    val viewport: Viewport
):Parcelable

@Parcelize
data class Location(
    val lat: Double,
    val lng: Double
):Parcelable

@Parcelize
data class Northeast(
    val lat: Double,
    val lng: Double
): Parcelable

@Parcelize
data class Photo(
    val height: Int,
    val html_attributions: List<String>,
    val photo_reference: String,
    val width: Int,
    val photo_Url:String
): Parcelable

@Parcelize
data class PlusCode(
    val compound_code: String,
    val global_code: String
):Parcelable

@Parcelize
data class Southwest(
    val lat: Double,
    val lng: Double
): Parcelable

@Parcelize
data class Viewport(
    val northeast: Northeast,
    val southwest: Southwest
):Parcelable