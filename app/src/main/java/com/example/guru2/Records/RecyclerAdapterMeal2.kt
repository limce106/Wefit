package com.example.guru2.Records

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.guru2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.meal_record_form.view.*


class RecyclerAdapterMeal2():
    RecyclerView.Adapter<RecyclerAdapterMeal2.ViewHolder>() {
    private var arrayList = ArrayList<MealRecModel>()
    lateinit var ct: Context
    private lateinit var uidList: ArrayList<String>

    constructor(arrayList: ArrayList<MealRecModel>, context: android.content.Context, uidList: ArrayList<String>) : this() {
        this.arrayList = arrayList
        ct = context
        this.uidList = uidList
    }

    @NonNull
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView=
            LayoutInflater.from(parent.context)
                .inflate(R.layout.meal_record_form, parent, false)
        val holder: ViewHolder = ViewHolder(inflatedView)
        return holder
    }


    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])

        holder.iv_delete.setOnClickListener(){
            removeData(position)
        }
    }

    override fun getItemCount(): Int {
        // 삼항 연산자
        return if (arrayList != null) arrayList.size else 0
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val view: View = v
        val iv_delete: ImageView = view.findViewById(R.id.iv_delete)

        fun bind(item: MealRecModel) {
            view.tv_eatDate.text = item.eatDate + "  " + item.eatTime
            view.tv_timeSlot.text = item.timeSlot
            view.tv_menuName.text = item.mealName + "  " + item.eatAmount
        }
    }

    // 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mealRecClickListener = onItemClickListener
    }
    // setItemClickListener로 설정한 함수 실행
    private lateinit var mealRecClickListener : OnItemClickListener

    //데이터 삭제 함수
    fun removeData(position: Int){
        val database = FirebaseDatabase.getInstance()
        var user= FirebaseAuth.getInstance().currentUser
        var userId= user?.uid
        val myRef = database.getReference("mealrecord").child(userId!!)

        val builder = AlertDialog.Builder(ct)
        builder.setTitle("삭제")
            .setMessage("해당 항목을 삭제하시겠습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener{ dialog, id ->
                    myRef.child(uidList[position]).removeValue().addOnSuccessListener {
                        Toast.makeText(ct, "삭제 완료", Toast.LENGTH_SHORT).show() }
                    arrayList.removeAt(position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, arrayList.size)
                })
            .setNegativeButton("취소",
                DialogInterface.OnClickListener{ dialog, id ->
                    dialog.cancel()
                })

        // 다이얼로그 띄우기
        builder.show()
    }
}
