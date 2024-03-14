package com.raihanardila.findgithub.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.raihanardila.findgithub.R
import com.raihanardila.findgithub.core.data.model.UsersModel
import de.hdodenhof.circleimageview.CircleImageView
import java.text.FieldPosition

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val userList = ArrayList<UsersModel>()
    private var onUserClickListener: OnUserClickListener? = null

    fun setOnUserClickListener(listener: OnUserClickListener) {
        onUserClickListener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setUsers(users: List<UsersModel>){
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.UserViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_users, parent, false)
        return  UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.usernameTextView.text = currentUser.login

        // Load image using Glide
        Glide.with(holder.itemView)
            .load(currentUser.avatarURL)
            .transform(CircleCrop())
            .into(holder.avatarImageView)

        holder.itemView.setOnClickListener {
            onUserClickListener?.onUserClick(currentUser)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        val avatarImageView: CircleImageView = itemView.findViewById(R.id.avatarImageView)
    }

    interface OnUserClickListener {
        fun onUserClick(user: UsersModel)
    }
}