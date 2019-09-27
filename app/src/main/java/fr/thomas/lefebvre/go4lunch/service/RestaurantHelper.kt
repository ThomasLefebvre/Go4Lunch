package fr.thomas.lefebvre.go4lunch.service

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import fr.thomas.lefebvre.go4lunch.model.database.Restaurant


class RestaurantHelper {

    private val COLLECTION_NAME = "restaurants"

    // --- COLLECTION REFERENCE ---

    fun getRestaurantsCollection(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
    }

    // --- CREATE ---

    fun createRestaurantr(uid: String, name: String, lat: Double, lng: Double, rating: Double?, like: Int?, openingHours: String?, photoUrl: String?, isVisited: Boolean?): Task<Void> {
        val restaurantToCreate = Restaurant(uid, name, lat, lng, rating, like,openingHours,photoUrl,isVisited)
        return getRestaurantsCollection().document(uid).set(restaurantToCreate)
    }

    // --- GET ---

    fun getRestaurant(uid: String): Task<DocumentSnapshot> {
        return getRestaurantsCollection().document(uid).get()
    }

    // --- UPDATE ---

    fun updateName(name: String, uid: String): Task<Void> {
        return getRestaurantsCollection().document(uid).update("name", name)
    }


    // --- DELETE ---

    fun deleteUser(uid: String): Task<Void> {
        return getRestaurantsCollection().document(uid).delete()
    }

}