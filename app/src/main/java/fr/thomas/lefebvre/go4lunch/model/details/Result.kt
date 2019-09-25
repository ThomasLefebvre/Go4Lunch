package fr.thomas.lefebvre.go4lunch.model.details

data class Result(
    val formatted_phone_number: String,
    val name: String,
    val opening_hours: OpeningHours,
    val photos: List<Photo>,
    val rating: Double,
    val website: String,
    val formatted_address: String
)