package fr.thomas.lefebvre.go4lunch.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import fr.thomas.lefebvre.go4lunch.R
import fr.thomas.lefebvre.go4lunch.model.database.User
import java.lang.StringBuilder

class WorkMatesAdapter(
    val context: Context,
    private val listUser: List<User>,
    private val listener: (User) -> Unit
) :
    RecyclerView.Adapter<WorkMatesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkMatesAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_workmates_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    override fun onBindViewHolder(holder: WorkMatesAdapter.ViewHolder, position: Int) {
        holder.bind( context, listUser[position], listener)
    }

    class ViewHolder(user: View) : RecyclerView.ViewHolder(user) {

        val photoUser: ImageView = itemView.findViewById(R.id.photo_Work_Mates) as ImageView
        val messageWorkmates: TextView = itemView.findViewById(R.id.textView_List_Work_Mates) as TextView

        fun bind( context: Context, user: User, listener: (User) -> Unit) = with(itemView) {

             //set text
            val restaurantIsChoose=user.restaurantUid!=null
                if (restaurantIsChoose) {//if has choose a restaurant
                    val messageChoose =
                        StringBuilder("${user.name} " + context.getString(R.string.is_eating_at) + " ${user.restaurantName}")//build a message
                    messageWorkmates.text = messageChoose.toString()

                } else {//if has no choose a restaurant
                    val messageNotChoose =
                        StringBuilder("${user.name} " + context.getString(R.string.not_decided))//build a message
                    messageWorkmates.text = messageNotChoose.toString()
                    messageWorkmates.setTypeface(null,Typeface.ITALIC)
                }
            //set photo of user
            if (user.photoUrl == "null") {//if no photo
                photoUser.setImageResource(R.drawable.profil)

            } else {
                val photoUrl = user.photoUrl //if photo
                Picasso.get().load(photoUrl).into(photoUser)
            }


            itemView.setOnClickListener {
                listener(user)
            }

        }

    }
}