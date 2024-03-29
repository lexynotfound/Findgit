package com.raihanardila.findgithub.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.raihanardila.findgithub.R
import com.raihanardila.findgithub.core.data.model.UsersModel
import com.raihanardila.findgithub.ui.interfaces.OnLoveButtonClickListener
import com.raihanardila.findgithub.util.UserDiffCallback
import de.hdodenhof.circleimageview.CircleImageView

class UserAdapter(private val onLoveButtonClickListener: OnLoveButtonClickListener) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var userList = ArrayList<UsersModel>()
    private var onUserClickListener: OnUserClickListener? = null

    fun setOnUserClickListener(listener: OnUserClickListener) {
        this.onUserClickListener = listener
    }

    fun setUsers(newList: List<UsersModel>) {
        val diffCallback = UserDiffCallback(userList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        userList.clear()
        userList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_users, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.usernameTextView.text = currentUser.login

        // Load image using Glide
        Glide
            .with(holder.itemView)
            .load(currentUser.avatarURL)
            .transform(CircleCrop())
            .into(holder.avatarImageView)

        holder.itemView.setOnClickListener {
            onUserClickListener?.onUserClick(currentUser)
        }

        holder.itemView.findViewById<View>(R.id.buttonLove).setOnClickListener {
            val position = holder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                onLoveButtonClickListener.onLoveButtonClick(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun getItem(position: Int): UsersModel? {
        return if (position in 0 until userList.size) {
            userList[position]
        } else {
            null
        }
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val usernameTextView: TextView = itemView.findViewById(R.id.usernameTextView)
        val avatarImageView: CircleImageView = itemView.findViewById(R.id.avatarImageView)
    }

    interface OnUserClickListener {
        fun onUserClick(user: UsersModel)
    }
}
