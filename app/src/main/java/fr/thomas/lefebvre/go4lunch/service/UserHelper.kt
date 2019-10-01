package fr.thomas.lefebvre.go4lunch.ui.service


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import fr.thomas.lefebvre.go4lunch.model.database.User



class UserHelper {

    private val COLLECTION_NAME = "users"

    // --- COLLECTION REFERENCE ---

    fun getUsersCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    // --- CREATE ---

    fun createUser(uid: String, username: String, email: String, photoUrl:String, restaurantName: String?, restaurantUid:String?): Task<Void> {
        val userToCreate = User(uid, username, email,photoUrl,restaurantName,restaurantUid)
        return getUsersCollection().document(uid).set(userToCreate)
    }

    // --- GET ---

    fun getUser(uid: String): Task<DocumentSnapshot> {
        return getUsersCollection().document(uid).get()
    }
    fun getAllUser(): Task<QuerySnapshot> {
        return getUsersCollection().get()
    }
    fun getUserByPlaceId(restaurantUid:String): Task<QuerySnapshot> {
        return getUsersCollection().whereEqualTo("restaurantUid",restaurantUid).get()
    }

    // --- UPDATE ---


    fun updateUsername(name: String, uid: String): Task<Void> {
        return getUsersCollection().document(uid).update("name", name)
    }

    fun updateUserRestaurantUid(restaurantUid: String, uid: String): Task<Void> {
        return getUsersCollection().document(uid).update("restaurantUid", restaurantUid)
    }
    fun updateUserRestaurantName(restaurantName:String,uid:String):Task<Void>{
        return getUsersCollection().document(uid).update("restaurantName",restaurantName)
    }


    // --- DELETE ---

    fun deleteUser(uid: String):Task<Void> {
        return getUsersCollection().document(uid).delete()
    }

}