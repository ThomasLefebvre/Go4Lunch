package fr.thomas.lefebvre.go4lunch.utils

import java.util.*


class ConverterHelper ()
{


    fun computeDistance(deviceLat: Double,  deviceLng: Double,restaurantLat: Double, restaurantLng: Double): String {

        val R = 6371 // Radius of earth
        val latDistance = Math.toRadians(restaurantLat - deviceLat)
        val lonDistance = Math.toRadians(restaurantLng - deviceLng)
        val a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + (Math.cos(Math.toRadians(deviceLat)) * Math.cos(
            Math.toRadians(restaurantLat)
        )
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2))
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        var distance = R.toDouble() * c * 1000.0 // convert to meters

        val height = 0.0 - 0.0
        distance = Math.pow(distance, 2.0) + Math.pow(height, 2.0)
        //Rounded
        return Math.round(Math.sqrt(distance)).toString() + "m"
    }

    fun getDayOfWeek():Int{
        val calendar=Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_WEEK)-2
    }



}

