package fr.thomas.lefebvre.go4lunch.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RestaurantFormatted(
    val id:String?=null,
    val name:String?=null,
    val address:String?=null,
    val openNow: Boolean?=null,
    var rating:Double?=null,
    var photoUrl:String?=null,
    val distance:String?=null
):Parcelable