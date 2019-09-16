package fr.thomas.lefebvre.go4lunch.ui.model

data class NearbyPlaces(
    val html_attributions: List<Any>,
    val next_page_token: String,
    val results: List<Result>,
    val status: String
)