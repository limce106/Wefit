package com.example.guru2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class activity_login : AppCompatActivity() {

    lateinit var mAuth:FirebaseAuth
    lateinit var btn_login:Button
    lateinit var edit_id:EditText
    lateinit var edit_pw:EditText


    private val TAG:String=activity_login::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth= Firebase.auth

        btn_login=findViewById(R.id.btn_login)
        edit_id=findViewById(R.id.edit_id)
        edit_pw=findViewById(R.id.edit_pw)




        btn_login.setOnClickListener {
            val loginID = edit_id.text.toString()
            val loginPW = edit_pw.text.toString()

            login(loginID, loginPW)

            if (edit_id.text.isEmpty() || edit_pw.text.isEmpty()) {
                Toast.makeText(this, "모든 항목을 채워주세요", Toast.LENGTH_SHORT)
                    .show()
            }

        }

    }
    //로그인 버튼
    private fun login(loginId:String, loginPW:String){
        mAuth.signInWithEmailAndPassword(loginId, loginPW)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this,NaviActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    finish()
                } else {

                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                    //로그를 통해서 실패 원인을 알 수 있도록 Log.d 생성
                    Log.d("Login", "Error: ${task.exception}")
                }
            }
    }

}