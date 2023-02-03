package com.example.guru2.Records

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker.OnTimeChangedListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.guru2.NaviActivity
import com.example.guru2.R
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_input_meal.*
import kotlinx.android.synthetic.main.fragment_input_meal.view.*


class InputMealFragment : Fragment() {
    private var uri:String?=null
    lateinit var strMealDate: String
    lateinit var strTimeSlot: String
    lateinit var strEatTime: String
    lateinit var strMealName: String
    lateinit var strKcal: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_input_meal, container, false)

        // 저장하기 클릭 시 입력한 데이터 저장
        rootView.btnSaveMealrecord.setOnClickListener() {

            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference()

            val view: View = inflater.inflate(R.layout.fragment_input_meal, container, false)
            val edtMealDate: EditText = view.findViewById(R.id.edtMealDate)
            val edtTimeSlot: EditText = view.findViewById(R.id.edtTimeSlot)
            val tv_eatTime: TextView = view.findViewById(R.id.tv_eatTime)
            val edtMealName: EditText = view.findViewById(R.id.edtMealName)
            val edtEatAmount: EditText = view.findViewById(R.id.edtEatAmount)
            val mActivity = activity as NaviActivity

            // 입력 안 된 항목이 있다면
            if(!this::strMealDate.isInitialized || !this::strTimeSlot.isInitialized
                || !this::strEatTime.isInitialized || !this::strMealName.isInitialized
                || !this::strKcal.isInitialized || strMealDate == "" || strTimeSlot == ""
                || strEatTime == "" || strMealName == "" || strKcal == "") {
                Toast.makeText(context, "빈칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show()
            } else{ // 모두 입력 되었다면
                val dataInput = MealRecModel(
                    // mealImg.drawable,
                    strMealDate, strTimeSlot, strEatTime, strMealName, strKcal
                )
                myRef.child("mealrecord").child(mActivity.loginUser()!!).push().setValue(dataInput)
                mActivity.replaceRecord(RecordMain())
            }

        }

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tp_Choice_eatTime.setIs24HourView(true);
        // TimePicker 클릭 이벤트: 먹은 시간 텍스트 변경
        tp_Choice_eatTime.setOnTimeChangedListener(OnTimeChangedListener { timePicker, hour, minute -> // 오전 / 오후 를 확인하기 위한 if 문
            var hour = hour
            if (hour > 12) {
                hour -= 12
                tv_eatTime.setText("오후 " + hour + "시 " + minute + "분")
            } else if(hour == 12){
                tv_eatTime.setText("오후 " + hour + "시 " + minute + "분")
            }
            else {
                tv_eatTime.setText("오전 " + hour + "시 " + minute + "분")
            }
            strEatTime = tv_eatTime.text.toString()
        })

        edtMealDate.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                strMealDate = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int, ) {}

            override fun afterTextChanged(s: Editable) {
            }
        })

        edtTimeSlot.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                strTimeSlot = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int, ) {}

            override fun afterTextChanged(s: Editable) {
            }
        })

        edtMealName.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                strMealName = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int, ) {}

            override fun afterTextChanged(s: Editable) {
            }
        })

        edtEatAmount.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                strKcal = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int, ) {}

            override fun afterTextChanged(s: Editable) {
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            InputMealFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}