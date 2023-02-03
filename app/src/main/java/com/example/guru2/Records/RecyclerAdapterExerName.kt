package com.example.guru2.Records

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.guru2.R
import kotlinx.android.synthetic.main.exercise_name_form.view.*

class RecyclerAdapterExerName(private var ExerciseNames: ArrayList<ExerciseNameModel>):
    RecyclerView.Adapter<RecyclerAdapterExerName.ViewHolder>() {

    override fun getItemCount(): Int = ExerciseNames.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int){
        // 운동 항목 클릭 이벤트
        holder.itemView.setOnClickListener {
            nameClickListener.onClick(it, position)
        }
        holder.apply {
            itemView.tag = ExerciseNames[position]
        }
        holder.bind(ExerciseNames[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            ViewHolder {
        val inflatedView=LayoutInflater.from(parent.context).inflate(R.layout.exercise_name_form, parent, false)
        return ViewHolder(inflatedView)
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        fun bind(item: ExerciseNameModel) {
            view.tv_exerciseName.text = item.exerciseName
        }
    }

    // 리사이클러뷰 클릭 이벤트
    // 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.nameClickListener = onItemClickListener
    }
    // setItemClickListener로 설정한 함수 실행
    private lateinit var nameClickListener : OnItemClickListener
}