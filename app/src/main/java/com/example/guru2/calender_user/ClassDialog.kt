package com.example.guru2.calender_user

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guru2.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_class_dialog.*
import java.time.LocalDate


class ClassDialog : DialogFragment() {


    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    @RequiresApi(Build.VERSION_CODES.O)
    val todayDate: LocalDate = LocalDate.now() //오늘 날짜

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val view = inflater.inflate(R.layout.fragment_class_dialog, container, false)
        return view
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()

        var date = arguments?.getString("key1").toString() //캘린더에서 선택한 날짜 불러오기

        if(date=="null")//null일 경우 오늘 날짜 넣기
            date=todayDate.toString()

        var text_date = view?.findViewById<TextView>(R.id.class_date)
        val recyclerviewclass= view?.findViewById<RecyclerView>(R.id.recyclerviewclass) //리사이클러 뷰 객체
        var itemList = arrayListOf<ClassScheduleItem>() //아이템 배열
        val ListAdapter = RecyclerViewClassAdapter(itemList, this) //어댑터
        var dataList = arrayListOf<ClassScheduleItem>() //db 데이터 배열

        text_date?.text = "$date" //캘린더에서 선택한 날짜


        //새로운 데이터 저장할 때 마다 데이터 불러오기
        val databaseReference: DatabaseReference = firebaseDatabase.getReference("class") //db 연결
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                itemList.clear()
                dataList.clear()
                //db에서 데이터 불러오기
                dataSnapshot.children.forEach{
                    val class_schedule = it.getValue(ClassScheduleItem::class.java)
                    class_schedule ?:return

                    dataList.add(class_schedule) //데이터리스트에 추가

                }

                //날짜별 데이터 넣기
                for(i in 0 until dataList.size)
                {
                    if(dataList[i].date==date)
                    {
                        itemList.add(dataList[i]) //리사이클러뷰에 리스트 추가
                    }
                }

                //리스트가 변경됨을 어댑터에 알림
                ListAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("ExerciseRecord", databaseError.toException().toString()) // 에러문 출력
            }

        })

        //처음 일정 예약 페이지를 로딩했을 때 수업 뜨기
        itemList.clear()
        //날짜별 데이터 넣기
        for(i in 0 until dataList.size)
        {
            if(dataList[i].date==date)
            {
                itemList.add(dataList[i]) //리사이클러뷰에 리스트 추가
            }
        }
        //리스트가 변경됨을 어댑터에 알림
        ListAdapter.notifyDataSetChanged()


        recyclerviewclass?.adapter = ListAdapter //어댑터 연결
        recyclerviewclass?.layoutManager = LinearLayoutManager(activity)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


    fun getInstance(): ClassDialog {
        return ClassDialog()
    }

}