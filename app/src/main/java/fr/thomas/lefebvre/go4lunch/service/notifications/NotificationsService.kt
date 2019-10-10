package fr.thomas.lefebvre.go4lunch.service.notifications


import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.media.RingtoneManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.*
import fr.thomas.lefebvre.go4lunch.ui.activity.MainActivity
import android.content.Intent
import android.view.View
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import fr.thomas.lefebvre.go4lunch.R
import fr.thomas.lefebvre.go4lunch.model.database.User
import fr.thomas.lefebvre.go4lunch.ui.service.UserHelper
import java.io.File.separator


class NotificationsService : FirebaseMessagingService() {

    private val NOTIFICATION_ID = 7
    private val NOTIFICATION_TAG = "FIREBASE"
    private val currentUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    private val userHelper: UserHelper = UserHelper()

    var restaurantName=""
    var restaurantAdresse=""
    var listString=""

    var messageNotif:String=""




    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.notification!=null){
           checkNotificationIsActived()

        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendNewTokenToServer(FirebaseInstanceId.getInstance().token!!)
    }

    private fun sendNewTokenToServer(token: String) {
        Log.d("TOKEN", token)
    }


    private fun sendVisualNotification(messageNotif:String) {

        // create an Intent that will be shown when user will click on the Notification
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)



        // create a Channel (Android 8)
        val channelId = getString(R.string.default_notification_channel_id)



        // build a Notification object
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_burger)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(getString(R.string.notification_title))
            .setAutoCancel(true)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setStyle(NotificationCompat.BigTextStyle()
                .bigText(messageNotif))
//            .setStyle(inboxStyle)

        // 5 - Add the Notification to the Notification Manager and show it.
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 6 - Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelName = getString(R.string.chanel_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val mChannel = NotificationChannel(channelId, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }

        // 7 - Show notification
        notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun checkNotificationIsActived(){
        userHelper.getUser(currentUser!!.uid).addOnSuccessListener { documentSnapshot ->
            val user =documentSnapshot.toObject(User::class.java)

            if (user!!.notificationIsActived&&user.restaurantUid!=null){
                restaurantName= user!!.restaurantName!!
                restaurantAdresse=user!!.restaurantAdress!!
                getInfosForNotification(user.restaurantUid!!)

            }
        }
            .addOnFailureListener { exception ->
                Log.d("NOTIFICATIONS", "Error getting documents: ", exception)  }

    }
    private fun getInfosForNotification(idRestaurant:String){
        val listWorkmate = ArrayList<String>()
        userHelper.getUserByPlaceId(idRestaurant)
            .addOnSuccessListener { documents ->

                for (document in documents) {
                    val user = document.toObject(User::class.java)
                    val nameWorkmate=user.name
                    listWorkmate.add(nameWorkmate)
                     listString=listWorkmate.joinToString()

                }
                messageNotif=(getString(R.string.notification_title)+" "+restaurantName+" "+restaurantAdresse+" "
                        +getString(R.string.message_notif_with)+" "+listString)
                sendVisualNotification(messageNotif)


            }
            .addOnFailureListener { exception ->
                Log.w("DEBUG", "Error getting documents: ", exception)
            }
    }

}