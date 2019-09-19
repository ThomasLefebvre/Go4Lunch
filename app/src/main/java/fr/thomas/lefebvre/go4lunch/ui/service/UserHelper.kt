package fr.thomas.lefebvre.go4lunch.ui.service


import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import fr.thomas.lefebvre.go4lunch.ui.model.Restaurant
import fr.thomas.lefebvre.go4lunch.ui.model.User




class UserHelper {

    private val COLLECTION_NAME = "users"

    // --- COLLECTION REFERENCE ---

    fun getUsersCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    // --- CREATE ---

    fun createUser(uid: String, username: String, email: String,photoUrl:String,restaurantName:String,restaurantUid:String): Task<Void> {
        val userToCreate = User(uid, username, email,photoUrl,restaurantName,restaurantUid)
        return getUsersCollection().document(uid).set(userToCreate)
    }

    // --- GET ---

    fun getUser(uid: String): Task<DocumentSnapshot> {
        return getUsersCollection().document(uid).get()
    }

    // --- UPDATE ---

    fun updateUsername(name: String, uid: String): Task<Void> {
        return getUsersCollection().document(uid).update("name", name)
    }

    fun updateResturantLunch(uid: String, restaurantLunch: Restaurant?): Task<Void> {
        return getUsersCollection().document(uid).update("isMentor", restaurantLunch)
    }

    // --- DELETE ---

    fun deleteUser(uid: String):Task<Void> {
        return getUsersCollection().document(uid).delete()
    }

}