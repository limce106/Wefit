package com.example.guru2.Message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guru2.NaviActivity
import com.example.guru2.R


class UserAdapter(private val context: UserListFrag, private val userList:ArrayList<User>):
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {







    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view:View=LayoutInflater.from(parent.context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)

    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser=userList[position]
        holder.nameText.text=currentUser.reg_name
        val chat: Chat = Chat()
        val bundle = Bundle()
        holder.itemView.setOnClickListener{


           // context.activity?.supportFragmentManager?.let { fragmentManager -> chat}

            bundle.putString("name",currentUser.reg_name)
            bundle.putString("name",currentUser.uId)


            val mActivity = context.activity as NaviActivity

            mActivity.replaceChat(chat)

            mActivity.supportFragmentManager.beginTransaction().hide(context)



        }
    }

    class UserViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val nameText: TextView =itemView.findViewById(R.id.name_text)
    }

}