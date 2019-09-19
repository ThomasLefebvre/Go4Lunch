package fr.thomas.lefebvre.go4lunch.ui.model

data class Restaurant(
    val id:Int,
    val name:String,
    val address:String,
    val openingHours: String,
    var rating:Double?=null,
    var photoUrl:String?=null,
    var isVisited:Boolean?=null
)