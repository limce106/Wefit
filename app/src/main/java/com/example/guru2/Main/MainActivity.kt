package com.example.guru2.Main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.guru2.Message.UserListFrag
import com.example.guru2.NaviActivity
import com.example.guru2.R
import com.example.guru2.Records.ExerciseRecordFragment
import com.example.guru2.Records.InputExerciseFragment
import com.example.guru2.Records.InputMealFragment
import com.example.guru2.Records.MealRecordFragment
import com.example.guru2.RegisterActivity
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginTest=findViewById<Button>(R.id.loginTest)
        val frag = findViewById<Button>(R.id.frag)
        val chatTest=findViewById<Button>(R.id.chatTest)

        frag.setOnClickListener {   val intent = Intent(this, NaviActivity::class.java)
            startActivity(intent)  }


        loginTest.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        chatTest.setOnClickListener {
            val intent = Intent(this, UserListFrag::class.java)
            startActivity(intent)
        }

        mAuth= FirebaseAuth.getInstance()
        var currentUser = mAuth?.currentUser

        //이미 로그인한적이 있는지 확인 (자동로그인)
        /*  if (currentUser == null) {

              Timer().schedule(object : TimerTask() {
                  override fun run() {
                      val intent: Intent = Intent(applicationContext, MainActivity::class.java)
                      startActivity(intent)
                      finish()
                  }
              }, 2000)

          }else{

              Timer().schedule(object : TimerTask() {
                  override fun run() {
                      val intent: Intent = Intent(applicationContext, activity_login::class.java)
                      startActivity(intent)
                      finish()
                  }
              }, 2000)

          }*/
    }

    companion object{
        private var instance: MainActivity? = null
        fun getInstance(): MainActivity? {
            return instance
        }
    }

    fun changeFragment(index: Int) {


        when (index) {
            1 -> {
                val inputExerciseFragment = InputExerciseFragment();
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, inputExerciseFragment)
                    .commit()
            }

            2 -> {
                val inputMealFragment = InputMealFragment();
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, inputMealFragment)
                    .commit()
            }

            3 -> {
                val exerciseRecordFragment = ExerciseRecordFragment();
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, exerciseRecordFragment)
                    .commit()
            }

            4 -> {
                val mealRecordFragment = MealRecordFragment();
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragment_container, mealRecordFragment)
                    .commit()
            }
        }
    }
}