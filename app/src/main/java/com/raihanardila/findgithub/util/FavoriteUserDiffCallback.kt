package com.raihanardila.findgithub.util

import androidx.recyclerview.widget.DiffUtil
import com.raihanardila.findgithub.core.data.model.FavoriteUsersModel

class FavoriteUserDiffCallback(
    private val oldList: List<FavoriteUsersModel>,
    private val newList: List<FavoriteUsersModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].username == newList[newItemPosition].username
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
