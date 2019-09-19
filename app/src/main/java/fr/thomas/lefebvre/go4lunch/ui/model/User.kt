package fr.thomas.lefebvre.go4lunch.ui.model

data class User(
    val uid:String,
    val name:String,
    val email:String,
    val photoUrl:String,
    var restaurantName: String? =null,
    var restaurantUid:String?=null
)