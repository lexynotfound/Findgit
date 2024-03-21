package com.raihanardila.findgithub.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.raihanardila.findgithub.R
import com.raihanardila.findgithub.core.data.model.FavoriteUsersModel
import com.raihanardila.findgithub.databinding.ItemUsersBinding

class FavoriteUserAdapter(private val onFavoriteDeleteClickListener: OnFavoriteDeleteClickListener) : RecyclerView.Adapter<FavoriteUserAdapter.FavViewHolder>() {
    val listFavorite = ArrayList<FavoriteUsersModel>()

    @SuppressLint("NotifyDataSetChanged")
    fun setListNotes(listFavorite: List<FavoriteUsersModel>) {
        this.listFavorite.clear()
        this.listFavorite.addAll(listFavorite)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val binding = ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

    inner class FavViewHolder(val binding: ItemUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: FavoriteUsersModel) {
            binding.apply {
                usernameTextView.text = favorite.username
                Glide.with(root.context)
                    .load(favorite.avatarUrl)
                    .into(avatarImageView)

                buttonLove.setImageResource(R.drawable.ic_fv_clarity_solid)
                buttonLove.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        onFavoriteDeleteClickListener.onFavoriteDeleteClick(position)
                    }
                }
            }
        }
    }

    interface OnFavoriteDeleteClickListener {
        fun onFavoriteDeleteClick(position: Int)
    }


    fun removeItem(position: Int) {
        listFavorite.removeAt(position)
        notifyItemRemoved(position)
    }
}