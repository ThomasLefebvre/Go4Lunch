package fr.thomas.lefebvre.go4lunch.model.details

data class OpeningHours(
    val open_now: Boolean,
    val periods: List<Period>,
    val weekday_text: List<String>
)