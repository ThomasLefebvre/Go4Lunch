package fr.thomas.lefebvre.go4lunch.ui.`object`

import fr.thomas.lefebvre.go4lunch.ui.service.IGoogleAPIService

object Common {
    private const val GOOGLE_API_URL="https://maps.googleapis.com/"

    val googleAPIService:IGoogleAPIService
        get()=RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)
}