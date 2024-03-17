package com.raihanardila.findgithub.util

import androidx.recyclerview.widget.DiffUtil
import com.raihanardila.findgithub.core.data.model.UsersModel

class UserDiffCallback(private val oldList: List<UsersModel>, private val newList: List<UsersModel>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}
