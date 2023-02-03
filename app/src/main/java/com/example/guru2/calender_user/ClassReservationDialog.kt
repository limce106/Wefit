package com.example.guru2.calender_user

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.guru2.NaviActivity
import com.example.guru2.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ClassReservationDialog : DialogFragment() {

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.getReference()
    val classReservationCheck : ClassReservationCheckDialog = ClassReservationCheckDialog() //수업 예약 확정 확인 팝업창

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
        return inflater.inflate(R.layout.fragment_class_reservation_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()

        val btn_reservation_yes = view?.findViewById<Button>(R.id.btn_reservation_yes) //예약 확정 버튼
        val btn_reservation_no = view?.findViewById<Button>(R.id.btn_reservation_no) //예약 확정 취소 버튼
        val reservationDate = view?.findViewById<TextView>(R.id.reservation_date) //예약 날짜
        val reservationStartTime = view?.findViewById<TextView>(R.id.reservation_start_time) //수업 시작 시간
        val reservationEndTime = view?.findViewById<TextView>(R.id.reservation_end_time) //수업 끝나는 시간
        val mActivity = activity as NaviActivity



        var date = arguments?.getString("key3").toString()
        var startTime = arguments?.getString("key4").toString()
        var endTime = arguments?.getString("key5").toString()

        reservationDate?.text = "$date"
        reservationStartTime?.text = "$startTime"
        reservationEndTime?.text = "$endTime"

        //수업 예약 확정 버튼 클릭시
        btn_reservation_yes?.setOnClickListener{


            val dataInput=Schedule("$date","PT 수업","$startTime ~ $endTime") //db에 저장할 데이터

            //db에 저장
            databaseReference.child("schedule").child(mActivity.loginUser()!!).push().setValue(dataInput)

            //다이얼로그 띄우기
            activity?.supportFragmentManager?.let { fragmentManager ->
                classReservationCheck.showNow(fragmentManager, "TAG_DIALOG_EVENT") }

            //다이얼 로그 종료하기
            try{this.dismiss()} catch (e: Exception){
                Log.d("dissmiss errer","$e")}


        }

        btn_reservation_no?.setOnClickListener(){

            //다이얼 로그 종료하기
            try{this.dismiss()} catch (e: Exception){
                Log.d("dissmiss errer","$e")}
        }


    }



}