package fr.thomas.lefebvre.go4lunch.utils


class ConverterHelper() {


    fun computeDistance(deviceLat: Double, deviceLng: Double, restaurantLat: Double, restaurantLng: Double): String {

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

    fun getDayOfWeek(dayInt: Int): Int {
        if (dayInt == 1) {
            return 6
        } else return (dayInt - 2)
    }

    fun getStringOpening(oppening: Boolean, openString: String, closedString: String): String {
        when (oppening) {
            true -> return openString
            false -> return closedString
            else -> return ""
        }
    }

    fun getRatingBarFloatRound(rating: Double): Float {
        val ratingBarFloat = (rating * ((3.0f / 5.0f)))//convert rating to 3.0
        return Math.round(ratingBarFloat * 10.0f) / 10.0f//round rating to 1 decimal
    }


}

