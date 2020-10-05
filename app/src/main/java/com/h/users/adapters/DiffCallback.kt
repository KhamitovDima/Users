package com.h.users.adapters

import androidx.recyclerview.widget.DiffUtil
import com.h.users.data.User

class DiffCallback(oldList: List<User>, newList: List<User>): DiffUtil.Callback() {
    private val oldList = oldList.toList()
    private val newList = newList.toList()
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size
    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos] == newList[newPos]
    }
    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        return oldList[oldPos] == newList[newPos]
    }
    override fun getChangePayload(oldPos: Int, newPos: Int): Any? {
        return super.getChangePayload(oldPos, newPos)
    }
}