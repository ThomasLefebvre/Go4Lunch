package fr.thomas.lefebvre.go4lunch.service


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.*
import fr.thomas.lefebvre.go4lunch.model.database.User



class UserHelper {

    private val COLLECTION_NAME = "users"

    // --- COLLECTION REFERENCE ---

    fun getUsersCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    // --- CREATE ---

    fun createUser(uid: String, username: String, email: String, photoUrl:String, restaurantName: String?,restaurantAdress:String?, restaurantUid:String?,notificationIsActived: Boolean,listRestaurantLiked:ArrayList<String>): Task<Void> {
        val userToCreate = User(uid, username, email,photoUrl,restaurantName,restaurantAdress,restaurantUid,notificationIsActived,listRestaurantLiked)
        return getUsersCollection().document(uid).set(userToCreate)
    }

    // --- GET ---

    fun getUser(uid: String): Task<DocumentSnapshot> {
        return getUsersCollection().document(uid).get()
    }

    fun getUserByPlaceId(restaurantUid:String): Task<QuerySnapshot> {
        return getUsersCollection().whereEqualTo("restaurantUid",restaurantUid).get()
    }


    // --- UPDATE ---



    fun updateUserRestaurantUid(restaurantUid: String, uid: String): Task<Void> {
        return getUsersCollection().document(uid).update("restaurantUid", restaurantUid)
    }
    fun updateUserRestaurantName(restaurantName:String,uid:String):Task<Void>{
        return getUsersCollection().document(uid).update("restaurantName",restaurantName)
    }

    fun updateUserAdress(restaurantAdress:String,uid:String):Task<Void>{
        return getUsersCollection().document(uid).update("restaurantAdress",restaurantAdress)
    }

    fun updateUserNotificationState(notificationIsActived: Boolean,uid:String):Task<Void>{
        return getUsersCollection().document(uid).update("notificationIsActived",notificationIsActived)
    }

    fun udpateUserRestaurantLike(restaurantUidString:String,uid:String):Task<Void>{
        return getUsersCollection().document(uid).update("listRestaurantLiked",FieldValue.arrayUnion(restaurantUidString))
    }


    // --- DELETE ---

    fun deleteUser(uid: String):Task<Void> {
        return getUsersCollection().document(uid).delete()
    }

}