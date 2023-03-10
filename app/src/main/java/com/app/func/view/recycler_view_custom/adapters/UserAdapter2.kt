package com.app.func.view.recycler_view_custom.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.func.databinding.ItemUser2Binding
import com.app.func.view.recycler_view_custom.models.User

class UserAdapter2 : RecyclerView.Adapter<UserAdapter2.UserViewHolder>() {

    private var listUser = mutableListOf<User>()

    fun initData(users: List<User>) {
        listUser.clear()
        listUser.addAll(users)
        notifyDataSetChanged()
    }

    fun removeUser(position: Int) {
        listUser.removeAt(position)
        notifyItemRemoved(position)
    }

    fun removeUser(user: User) {
        listUser.remove(user)
    }

    fun undoRemoveUser(position: Int, user: User) {
        listUser.add(position, user)
        notifyItemInserted(position)
    }

    inner class UserViewHolder(val binding: ItemUser2Binding) :
        RecyclerView.ViewHolder(binding.root) {
        val consItem2 = binding.consItem2

        fun bindDataToView(user: User) {
            binding.textNameUser2.text = user.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemUser2Binding = ItemUser2Binding.inflate(layoutInflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = listUser[position]
        holder.bindDataToView(user)
    }

    override fun getItemCount(): Int = listUser.size

}