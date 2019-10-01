package fr.thomas.lefebvre.go4lunch.model.database

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


data class User(
    val uid:String="",
    val name:String="",
    val email:String="",
    val photoUrl:String="",
    var restaurantName: String? ="",
    var restaurantUid:String?=""
)

