package fr.thomas.lefebvre.go4lunch.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Northeast(
    val lat: Double,
    val lng: Double
): Parcelable