package com.example.guru2

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.guru2.Recommend.Instructure_Recommend_Fragment
import com.example.guru2.Recommend.Trainer_Recommend_Fragment
import com.example.guru2.Records.RecordMain
import com.example.guru2.calender_trainer.CalenderTrainer
import com.example.guru2.calender_user.Calender
import com.example.guru2.databinding.ActivityNaviBinding
import com.example.guru2.graph_user.Graph
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import instructure_record.Instructure_Record_Main
import kotlinx.android.synthetic.main.activity_navi.*


private const val TAG_RECOMMEND = "recommend_fragment" //운동 추천 프래그먼트
private const val TAG_RECORD = "record_fragment" //운동 및 식단 기록 프래그먼트
private const val TAG_CALENDAR = "calender_fragment" //일정 예약 프래그먼트
private const val TAG_GRAPH = "graph_fragment" //변화 프래그먼트
private const val TAG_MESSAGE = "message_fragment" //메세지 프래그먼트




class NaviActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNaviBinding
    lateinit var recordfragment: Fragment
    var isInputRecord:Boolean=false
    var strID: String = ""
    var isCheckID: String = ""
    var uidByID: String = ""
    var strTabPosition: String = ""
    var mAuth : FirebaseAuth= Firebase.auth
    var mDbRef: DatabaseReference = FirebaseDatabase.getInstance().reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNaviBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //뒤로가기 버튼 생성
        setFragment(TAG_CALENDAR, Calender()) //시작 프래그먼트
        bottomNavigationView.selectedItemId = R.id.calendarFragment


        binding.bottomNavigationView.setOnItemSelectedListener { item ->
            when(item.itemId){
                //각 프래그먼트 연결하기
                R.id.recommendFragment -> setFragment(TAG_RECOMMEND,Trainer_Recommend_Fragment())
                R.id.recordFragment-> setFragment(TAG_RECORD, RecordMain())
                R.id.calendarFragment -> setFragment(TAG_CALENDAR, Calender())
                R.id.graphFragment -> setFragment(TAG_GRAPH, Graph())
                R.id.messageFragment -> setFragment(TAG_MESSAGE,myPage())
            }
            true
        }
    }


    companion object{
        private var instance:NaviActivity = NaviActivity()
        fun getInstance(): NaviActivity {
            return instance
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId==R.id.logout){
            mAuth.signOut()
            val intent= Intent(this,activity_login::class.java)
            startActivity(intent)
            finish()
        }

        return true
    }


    //프래그먼트 세팅
    fun setFragment(tag:String, fragment: Fragment) {



        val manager: FragmentManager = supportFragmentManager
        val fragTransition = manager.beginTransaction()

        val recommend = manager.findFragmentByTag(TAG_RECOMMEND)
        val record = manager.findFragmentByTag(TAG_RECORD)
        val calender = manager.findFragmentByTag(TAG_CALENDAR)
        val graph = manager.findFragmentByTag(TAG_GRAPH)
        val message = manager.findFragmentByTag(TAG_MESSAGE)


        if (manager.findFragmentByTag(tag) == null) {
            fragTransition.add(R.id.main_frame, fragment, tag)
        }


        if (recommend != null) {
            fragTransition.hide(recommend)
        }
        if (record != null) {
            fragTransition.hide(record)
        }
        if (calender != null) {
            fragTransition.hide(calender)
        }
        if (graph != null) {
            fragTransition.hide(graph)
        }
        if (message != null) {
            fragTransition.hide(message)
        }


        if (tag == TAG_RECOMMEND) {
            if (recommend != null) {
                fragTransition.show(recommend)
            }
        } else if (tag == TAG_RECORD) {
            if (record != null) {
                    fragTransition.show(record)
            }
        } else if (tag == TAG_CALENDAR) {
            if (calender != null) {
                fragTransition.show(calender)
            }
        }else if (tag == TAG_GRAPH) {
            if (graph != null) {
                fragTransition.show(graph)
            }
        } else if (tag == TAG_MESSAGE) {
            if (message != null) {
                fragTransition.show(message)
            }
        }

            fragTransition.commitAllowingStateLoss()

    }


    fun replaceChat(fragment: Fragment){
        supportFragmentManager.beginTransaction().add(R.id.main_frame,fragment, TAG_MESSAGE).addToBackStack(null).commit()
    }


    //프래그먼트 교체 함수
    fun replaceRecord(fragment: Fragment){
        //기록 프래그먼트 새로 불러오기
        supportFragmentManager.beginTransaction().replace(R.id.main_frame,fragment, TAG_RECORD).addToBackStack(null).commit()
        recordfragment=fragment
        isInputRecord=true
    }



    fun changeTab(fragment: Fragment){
        //기록 프래그먼트 새로 불러오기
        supportFragmentManager.beginTransaction().replace(R.id.recordFrame,fragment).commit()
    }

    fun changeinstTab(fragment: Fragment){
        //기록 프래그먼트 새로 불러오기
        supportFragmentManager.beginTransaction().replace(R.id.recordframe,fragment).commit()
    }

    fun loginUser(): String? {

        var user= FirebaseAuth.getInstance().currentUser
        var userId= user?.uid

        return userId;
    }

    fun findUidByID(id: String){
        val database = FirebaseDatabase.getInstance()
        val databaseRef = database.getReference("user")
        var uid: String = String()

        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) { // 반복문으로 데이터 List를 추출해냄
                    if(snapshot.child("reg_id").value.toString() == id){
                        uid = snapshot.child("uid").value.toString()
                        uidByID = uid
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 디비를 가져오던중 에러 발생 시
                Log.e("User", databaseError.toException().toString()) // 에러문 출력
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.page_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun refreshFrag(fragment: Fragment){
        //기록 프래그먼트 새로 불러오기
        supportFragmentManager.beginTransaction().detach(fragment).attach(fragment).commit()
    }


}
