package es.ua.eps.sqlite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val userList: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userValue: TextView = itemView.findViewById(R.id.uservalue)
        val usernameValue: TextView = itemView.findViewById(R.id.usernamevalue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // Inflar el layout de item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val (username, fullName) = userList[position]
        holder.userValue.text = username
        holder.usernameValue.text = fullName
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}