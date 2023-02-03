package com.example.guru2.Recommend

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.guru2.R
import com.example.guru2.Records.ExerciseRecModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.trainer_recommend_form.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class RecyclerAdapterTrainerRecommend():
    RecyclerView.Adapter<RecyclerAdapterTrainerRecommend.ViewHolder>() {
    private var arrayList = ArrayList<ExerciseRecModel>()
    lateinit var ct: Context
    private lateinit var uidList: ArrayList<String>

    constructor(arrayList: ArrayList<ExerciseRecModel>, context: android.content.Context, uidList: ArrayList<String>) : this() {
        this.arrayList = arrayList
        ct = context
        this.uidList = uidList
    }

    @NonNull
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView=
            LayoutInflater.from(parent.context)
                .inflate(R.layout.trainer_recommend_form, parent, false)
        return ViewHolder(inflatedView)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {

        holder.bind(arrayList[position])

        holder.iv_check.setOnClickListener(){
            checkExercise(position)
        }
    }

    override fun getItemCount(): Int {
        // 삼항 연산자
        return if (arrayList != null) arrayList.size else 0
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        private var view: View = v
        val iv_check: ImageView = view.findViewById(R.id.iv_check)
        fun bind(item: ExerciseRecModel) {
            view.tv_recommendExerName.text = item.exerName2
            view.tv_recommendSetCount.text = item.count + "회" + item.set + "세트"
        }
    }

    // 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.exerciseRecClickListener = onItemClickListener
    }
    // setItemClickListener로 설정한 함수 실행
    private lateinit var exerciseRecClickListener : OnItemClickListener

    //데이터 삭제, 추가 함수
    @RequiresApi(Build.VERSION_CODES.O)
    fun checkExercise(position: Int){
        val database = FirebaseDatabase.getInstance()
        var user= FirebaseAuth.getInstance().currentUser
        var userId= user?.uid
        val myRef = database.getReference("exerciserecommend").child(userId!!)
        val myRef2 = database.getReference("exerciserecord").child(userId!!)
        var now = LocalDate.now()

        var Strnow = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"))

        val builder = AlertDialog.Builder(ct)
        builder.setTitle("운동 완료")
            .setMessage("해당 운동을 완료하셨습니까?")
            .setPositiveButton("확인",
                DialogInterface.OnClickListener{ dialog, id ->
                    arrayList[position].exerDate = Strnow
                    val dataInput = ExerciseRecModel(
                        arrayList[position].exerName2, arrayList[position].exerDate, arrayList[position].set, arrayList[position].count
                    )
                    myRef2.push().setValue(arrayList[position])
                    myRef.child(uidList[position]).removeValue().addOnSuccessListener {
                        Toast.makeText(ct, "해당 운동을 기록했습니다.", Toast.LENGTH_SHORT).show() }
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