package com.h.users.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.h.users.R
import com.h.users.data.User
import kotlinx.android.synthetic.main.user_list_item.view.*

class RecyclerAdapter(onCardListener: OnCardListener) : RecyclerView.Adapter<RecyclerAdapter.UserHolder>() {
    private  val TAG = "RecyclerAdapter"
    private var users: List<User> = ArrayList()
    private var usersOld: List<User> = ArrayList()
    private val mOnCardListener = onCardListener


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_list_item, parent, false)
        return UserHolder(itemView, mOnCardListener)

    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val currentUser: User = users.get(position)
        holder.textViewName.text = currentUser.first_name + " " + currentUser.last_name

        val requestOptions = RequestOptions()
            .placeholder(R.color.white)
        Glide.with(holder.itemView.context)
            .setDefaultRequestOptions(requestOptions)
            .load(currentUser.avatar)
            .into(holder.imageUser)
    }

    fun setUsersInAdapter(users: List<User>) {
        usersOld=this.users
        this.users=users
        updateList(usersOld,this.users)
        notifyDataSetChanged()
    }

    private fun updateList(old:List<User>, new:List<User>) {
        val callback = DiffCallback(old, new)
        DiffUtil.calculateDiff((callback)).dispatchUpdatesTo(this)
    }

    class UserHolder(itemView: View, var userListener: OnCardListener) :
            RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var textViewName: TextView
        var imageUser: ImageView

        init {
            textViewName = itemView.username
            imageUser = itemView.user_image
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            userListener.onCardClick(adapterPosition)
        }
    }
}