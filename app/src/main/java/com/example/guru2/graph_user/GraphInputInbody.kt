package com.example.guru2.graph_user

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import com.example.guru2.NaviActivity
import com.example.guru2.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_graph_input_inbody.*


class GraphInputInbody : DialogFragment() {

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference: DatabaseReference = firebaseDatabase.getReference()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_graph_input_inbody, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) //다이얼로그 배경 투명하게
        return view
    }


    fun getInstance(): GraphInputInbody {
        return GraphInputInbody()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datepicker= view.findViewById<DatePicker>(R.id.datepicker)

        btn_inbody_add.setOnClickListener{

            val weight = input_weight.text.toString()
            val muscle = input_muscle.text.toString()
            val bodyfat = input_bodyfat.text.toString()
            var date = datepicker.year.toString()+ "-"+(datepicker.month+1).toString()+"-"+datepicker.dayOfMonth.toString()

            val dataInput= InbodyItem("$date","$weight","$muscle","$bodyfat") //db에 저장할 데이터


            val mActivity = activity as NaviActivity

            //db에 저장
            databaseReference.child("Inbody").child(mActivity.loginUser()!!).push().setValue(dataInput)


            //다이얼 로그 종료하기
            try{this.dismiss()}
            catch (e: Exception){
                Log.d("dissmiss errer","$e")}

        }

    }


}