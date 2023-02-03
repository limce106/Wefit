package com.example.guru2.calender_user

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.example.guru2.NaviActivity
import com.example.guru2.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate


class IndividualExerciseDialog : DialogFragment() {


    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.getReference()
    @RequiresApi(Build.VERSION_CODES.O)
    val todayDate: LocalDate = LocalDate.now() //오늘 날짜



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //다이얼로그 배경 투명하게
        val view= inflater.inflate(R.layout.fragment_individual_exercise_dialog, container, false)

        return view

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStart() {
        super.onStart()
        //객체 생성
        val btn_indi_add = view?.findViewById<Button>(R.id.btn_indi_add) //개인 운동 일정 추가하기 버튼
        var edit_hour :Int = 0 //개인 운동 일정 시간
        var edit_minute:Int= 0 //개인 운동 일정 분
        val timePicker = view?.findViewById<TimePicker>(R.id.time_individual)
        var date = arguments?.getString("key1").toString()

        if(date=="null")//null일 경우 오늘 날짜 넣기
            date=todayDate.toString()

        timePicker?.setOnTimeChangedListener{ timePicker, hourOfDay, minute ->

            edit_hour  = hourOfDay //시간 불러오기
            edit_minute =minute //분 불러오기

        }


        //개인 운동 일정 추가하기 버튼 클릭시
        btn_indi_add?.setOnClickListener{
            var hour:String = ""
            var minute:String=""
            //시간 출력이 00:00 꼴로 되도록
            if(edit_minute<10)
                minute="0$edit_minute"
            else
                minute="$edit_minute"

            if(edit_hour<10)
                hour="0$edit_hour"
            else
                hour="$edit_hour"

            val dataInput=Schedule("$date","개인 운동","$hour:$minute") //db에 저장할 데이터

            val mActivity = activity as NaviActivity

            //db에 저장
            databaseReference.child("schedule").child(mActivity.loginUser()!!).push().setValue(dataInput)

            //다이얼 로그 종료하기
            try{this.dismiss()} catch (e: Exception){
                Log.d("dissmiss errer","$e")}
        }

    }

    fun getInstance(): IndividualExerciseDialog {
        return IndividualExerciseDialog()
    }


}


