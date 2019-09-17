package fr.thomas.lefebvre.go4lunch.ui.model

import android.os.Parcelable
import androidx.lifecycle.ViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class NearbyPlaces(
    val html_attributions: List<String>,
    val next_page_token: String,
    val results: List<Result>,
    val status: String
): Parcelable