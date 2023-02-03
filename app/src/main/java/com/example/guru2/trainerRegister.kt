package com.example.guru2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.guru2.Message.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class trainerRegister : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth

    private lateinit var mDbRef: DatabaseReference

    lateinit var btn_finish_reg: Button
    lateinit var edtName: EditText
    lateinit var edtID: EditText
    lateinit var edtPW: EditText
    // lateinit var re_PW:EditText
    lateinit var edtTEL: EditText
    lateinit var rb_gender: RadioGroup
    lateinit var male: RadioButton
    lateinit var female: RadioButton


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trainer_register)

        btn_finish_reg=findViewById(R.id.btn_finish_reg)
        edtID=findViewById(R.id.edtID)
        edtPW=findViewById(R.id.edtPW)
        //
        //
        // re_PW=findViewById(R.id.re_PW)
        edtName=findViewById(R.id.edtName)
        edtTEL=findViewById(R.id.edtTEL)
        rb_gender=findViewById(R.id.rb_gender)
        male=findViewById(R.id.male)
        female=findViewById(R.id.female)

        //인증 초기화
        mAuth= Firebase.auth

        //DB 초기화
        mDbRef= Firebase.database.reference

        btn_finish_reg.setOnClickListener {

            val reg_id=edtID.text.toString()
            val reg_pw=edtPW.text.toString()
            val reg_name=edtName.text.toString()
            val reg_tel=edtTEL.text.toString()
            var str_gender:String=""
            var str_purpose:String=""

            if(rb_gender.checkedRadioButtonId==R.id.male){
                str_gender=male.text.toString()
            }
            if(rb_gender.checkedRadioButtonId==R.id.female){
                str_gender=female.text.toString()
            }



            signUp(reg_id,reg_pw,reg_name, reg_tel,str_gender, str_purpose)

        }


    }


    //회원가입
    fun signUp (reg_id:String, reg_pw:String, reg_name:String,
                reg_tel:String, str_gender:String, str_purpose:String){


        mAuth.createUserWithEmailAndPassword(reg_id,reg_pw)
            .addOnCompleteListener(this){ task->
                if(task.isSuccessful){
                    //성공 시 실행
                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,activity_login::class.java)
                    startActivity(intent)
                    addUserToDatabase(reg_id, reg_pw, reg_name, reg_tel,
                        str_gender, str_purpose, mAuth.currentUser?.uid!!)
                }else{
                    //실패 시 실행
                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                    Log.d("SignUp", "Error : ${task.exception}")
                }
            }
    }


    fun addUserToDatabase(reg_id:String, reg_pw:String, reg_name:String,
                          reg_tel:String, str_gender:String, str_purpose:String, uId:String)
    {
        mDbRef.child("user").child(uId).setValue(User(reg_id, reg_pw, reg_name, reg_tel, str_gender, str_purpose,uId))

    }



}

