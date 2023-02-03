package com.example.guru2.calender_user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.guru2.NaviActivity
import com.example.guru2.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class RecyclerViewAdapter(val itemList: ArrayList<Schedule>, context:android.content.Context,uidList: ArrayList<String>):RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>(){


    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    val mActivity = context as NaviActivity
    val databaseReference: DatabaseReference = firebaseDatabase.getReference("schedule").child(mActivity.loginUser()!!) //db 연결
    val ct = context
    var uidList:ArrayList<String> = uidList

    //아이템 레이아웃과 결합
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calender_item,parent,false)
        return RecyclerViewHolder(view)
    }

    //view에 내용 입력
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.time.text = itemList[position].time
        holder.type.text = itemList[position].type
        holder.date.text = itemList[position].date
    }

    //리스트 내 아이템 개수
    override fun getItemCount(): Int {
        return itemList.size
    }


    //레이아웃 내 View 연결
    inner class RecyclerViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val time = itemView.findViewById<TextView>(R.id.time)
        val type = itemView.findViewById<TextView>(R.id.schedule_type)
        val date = itemView.findViewById<TextView>(R.id.calender_date)
    }

    //데이터 삭제 함수
    fun removeData(position: Int){

        databaseReference.child(uidList[position]).removeValue().addOnSuccessListener {
            Toast.makeText(ct, "삭제 완료", Toast.LENGTH_SHORT).show() }
             itemList.removeAt(position)
            notifyItemRemoved(position)//특정 아이템 1개 삭제
            notifyItemRangeChanged(position, itemList.size)

    }



}