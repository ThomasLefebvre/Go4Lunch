package fr.thomas.lefebvre.go4lunch

import fr.thomas.lefebvre.go4lunch.utils.ConverterHelper
import junit.framework.Assert.assertEquals
import org.junit.Test

class ConverterTest (){

    val convertHelper=ConverterHelper()

    @Test
    fun calculDistance(){//currentLat: Double, currentLng: Double, placeLat: Double, placeLng: Double
        val currentLat=35.50
        val currentLng=35.50
        val placeLat=35.50
        val placeLng=35.50
        assertEquals("0m",convertHelper.computeDistance(currentLat,currentLng,placeLat,placeLng))
    }
}