package fr.thomas.lefebvre.go4lunch.ui.service

import fr.thomas.lefebvre.go4lunch.model.details.DetailsPlace
import fr.thomas.lefebvre.go4lunch.model.nearby.NearbyPlaces
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface IGoogleAPIService {
    @GET
    fun getNearbyPlaces(@Url url:String):Call<NearbyPlaces>

    @GET
    fun getDetailsPlace(@Url url:String):Call<DetailsPlace>
}