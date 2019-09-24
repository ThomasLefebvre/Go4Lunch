package fr.thomas.lefebvre.go4lunch.model.database

data class Restaurant (
    val uid:String,
    val name:String,
    val lat: Double,
    val lng:Double,
    val rating:Double?,
    val like:Int?,
    val openingHours:String?,
    val photoUrl:String?,
    val isVisited:Boolean?



)