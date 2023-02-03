package com.example.guru2.Records

import android.app.Dialog
import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.guru2.NaviActivity
import com.example.guru2.R
import com.google.firebase.database.FirebaseDatabase

class popup_exerciseCount(private val context: Context, private val activity: NaviActivity) {
    private val dialog = Dialog(context)
    lateinit var strExerciseDate: String
    lateinit var strSet: String
    lateinit var strCount: String

    fun saveData(clickedExerciseName: String, dataName: String){
        dialog.setContentView(R.layout.popup_exercisecount)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val edtExerciseDate = dialog.findViewById<EditText>(R.id.edtExerciseDate)
        val edt_set = dialog.findViewById<EditText>(R.id.edt_set)
        val edt_count = dialog.findViewById<EditText>(R.id.edt_count)
        val btnCancel2 = dialog.findViewById<Button>(R.id.btnCancel2)
        val btnOk2 = dialog.findViewById<Button>(R.id.btnOk2)
        val mActivity = activity as NaviActivity

        checkChanges(edtExerciseDate, edt_set, edt_count)

        btnOk2.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference()

            // 입력한 데이터 저장
            if(!::strExerciseDate.isInitialized || !::strSet.isInitialized
                || !::strCount.isInitialized || strExerciseDate == ""
                || strSet == "" || strCount == "") {
                Toast.makeText(context, "빈칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                val dataInput = ExerciseRecModel(
                    clickedExerciseName, strExerciseDate, strSet, strCount
                )
                myRef.child(dataName).child(mActivity.loginUser()!!).push().setValue(dataInput)

                dialog.dismiss()
                mActivity.replaceRecord(RecordMain())
            }
        }

        btnCancel2.setOnClickListener {
            dialog.dismiss()
        }
    }

    fun saveData2(clickedExerciseName: String, dataName: String, uid: String){
        dialog.setContentView(R.layout.popup_exercisecount)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)
        dialog.show()

        val edtExerciseDate = dialog.findViewById<EditText>(R.id.edtExerciseDate)
        val edt_set = dialog.findViewById<EditText>(R.id.edt_set)
        val edt_count = dialog.findViewById<EditText>(R.id.edt_count)
        val btnCancel2 = dialog.findViewById<Button>(R.id.btnCancel2)
        val btnOk2 = dialog.findViewById<Button>(R.id.btnOk2)

        checkChanges(edtExerciseDate, edt_set, edt_count)

        btnOk2.setOnClickListener {
            val database = FirebaseDatabase.getInstance()
            val myRef = database.getReference()

            // 입력한 데이터 저장
            if(!::strExerciseDate.isInitialized || !::strSet.isInitialized
                || !::strCount.isInitialized || strExerciseDate == ""
                || strSet == "" || strCount == "") {
                Toast.makeText(context, "빈칸을 모두 채워주세요.", Toast.LENGTH_SHORT).show()
            }
            else{
                val dataInput = ExerciseRecModel(
                    clickedExerciseName, strExerciseDate, strSet, strCount
                )
                myRef.child(dataName).child(uid).push().setValue(dataInput)

                dialog.dismiss()
                Toast.makeText(context, "해당 회원 운동 추천에 추가하였습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        btnCancel2.setOnClickListener {
            dialog.dismiss()
        }
    }

    interface ButtonClickListener {
        fun onClicked()
    }

    private lateinit var  onClickListener: ButtonClickListener

    fun setOnClickedListener(listener: ButtonClickListener) {
        onClickListener = listener
    }

    fun checkChanges(edtExerciseDate: EditText, edt_set: EditText, edt_count: EditText) {

        edtExerciseDate.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                strExerciseDate = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int, ) {}

            override fun afterTextChanged(s: Editable) {
            }
        })

        edt_set.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                strSet = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int, ) {}

            override fun afterTextChanged(s: Editable) {
            }
        })

        edt_count.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                strCount = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int, ) {}

            override fun afterTextChanged(s: Editable) {
            }
        })
    }
}