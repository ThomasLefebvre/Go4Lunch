package fr.thomas.lefebvre.go4lunch

import fr.thomas.lefebvre.go4lunch.utils.ConverterHelper
import junit.framework.Assert.assertEquals
import org.junit.Test
import java.util.*

class ConverterTest() {

    val convertHelper = ConverterHelper()

    @Test
    fun calculDistance() {//currentLat: Double, currentLng: Double, placeLat: Double, placeLng: Double
        val currentLat = 35.50
        val currentLng = 35.50
        val placeLat = 35.50
        val placeLng = 35.50
        assertEquals("0m", convertHelper.computeDistance(currentLat, currentLng, placeLat, placeLng))
    }

    @Test
    fun getIntOfDay() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY)
        assertEquals(calendar.get(Calendar.DAY_OF_WEEK) - 2, convertHelper.getDayOfWeek(4))
    }

    @Test
    fun getOpeningString() {
        val openingResponse = true
        assertEquals("Open", convertHelper.getStringOpening(openingResponse, "Open", "Closed"))
    }

    @Test
    fun getRatingRestaurant() {
        val ratingToFive: Double = 4.5
        assertEquals(2.7f, convertHelper.getRatingBarFloatRound(ratingToFive))
    }


}