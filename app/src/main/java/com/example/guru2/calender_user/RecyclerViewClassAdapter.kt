package com.example.guru2.calender_user


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.guru2.Message.Chat
import com.example.guru2.R
import kotlinx.android.synthetic.main.calender_class_item.view.*


class RecyclerViewClassAdapter(val itemList: ArrayList<ClassScheduleItem>, context: ClassDialog):RecyclerView.Adapter<RecyclerViewClassAdapter.RecyclerViewHolder>() {

    val classReservation :ClassReservationDialog = ClassReservationDialog() //수업 팝업창
    val mcontext = context
    val chat: Chat = Chat()



    //아이템 레이아웃과 결합
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.calender_class_item,parent,false)

        return RecyclerViewHolder(view)
    }

    //view에 내용 입력
    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.class_start_time.text = itemList[position].StartTime
        holder.class_end_time.text = itemList[position].EndTime
       // holder.join_size.text = itemList[position].JoinSize
        holder.join_max_size.text = itemList[position].JoinSize
        holder.itemView.btn_join.setOnClickListener{
            var bundle = Bundle() //번들 생성
            bundle.putString("key3",itemList[position].date) //번들에 값
            bundle.putString("key4",itemList[position].StartTime) //번들에 값
            bundle.putString("key5",itemList[position].EndTime) //번들에 값
            classReservation.arguments = bundle //값이 담긴 번들을 argunments에 담기

            //다이얼로그 띄우기
            mcontext.activity?.supportFragmentManager?.let { fragmentManager ->
                classReservation.showNow(fragmentManager, "TAG_DIALOG_EVENT")}



            //예약 목록 다이얼로그 종료하기
            try{mcontext.dismiss()} catch (e: Exception){
                Log.d("dissmiss errer","$e")}

        }

    }



    fun getstate(){
        return
    }

    //리스트 내 아이템 개수
    override fun getItemCount(): Int {
        return itemList.size
    }


    //레이아웃 내 View 연결
    inner class RecyclerViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val class_start_time = itemView.findViewById<TextView>(R.id.class_start_time)
        val class_end_time = itemView.findViewById<TextView>(R.id.class_end_time)
        val join_max_size = itemView.findViewById<TextView>(R.id.join_max_size)
        val btn_join = itemView.findViewById<Button>(R.id.btn_join)


    }

}