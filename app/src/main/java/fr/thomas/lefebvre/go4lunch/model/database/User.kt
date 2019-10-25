package fr.thomas.lefebvre.go4lunch.model.database

data class User(
    val uid:String="",
    val name:String="",
    val email:String="",
    val photoUrl:String="",
    var restaurantName: String? ="",
    var restaurantAdress:String?="",
    var restaurantUid:String?="",
    var notificationIsActived: Boolean =true,
    var listRestaurantLiked:ArrayList<String> = arrayListOf()

)

