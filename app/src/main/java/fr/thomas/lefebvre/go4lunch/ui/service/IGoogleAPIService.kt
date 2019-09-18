package fr.thomas.lefebvre.go4lunch.ui.service

import fr.thomas.lefebvre.go4lunch.ui.model.NearbyPlaces
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface IGoogleAPIService {
    @GET
    fun getNearbyPlaces(@Url url:String):Call<NearbyPlaces>


}