package com.example.guru2.calender_trainer

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guru2.NaviActivity
import com.example.guru2.R
import com.example.guru2.calender_user.IndividualExerciseDialog
import com.example.guru2.calender_user.RecyclerViewAdapter
import com.example.guru2.calender_user.Schedule
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_calender.*
import java.time.LocalDate


class CalenderTrainer : Fragment() {


    var firestore: FirebaseFirestore? = null
    val itemList = arrayListOf<TrainerItem>() //아이템 배열
    val dataList = arrayListOf<TrainerItem>() //db 데이터 배열
    val ListAdapter = RecyclerViewAdapterTrainer(itemList) //어댑터
    @RequiresApi(Build.VERSION_CODES.O)
    val todayDate: LocalDate = LocalDate.now() //오늘 날짜
    @RequiresApi(Build.VERSION_CODES.O)
    var date: String = todayDate.toString()//캘린더에서 선택한 날짜
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    val trainerDialog: TrainerCalenderDialog = TrainerCalenderDialog()
    val dialog: TrainerCalenderDialog = TrainerCalenderDialog().getInstance() //수업 예약 팝업창


    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_calender_trainer, container, false)
        val recyclerview = view.findViewById<RecyclerView>(R.id.recyclerview_calender_trainer) //리사이클러 뷰 객체
        val calenderviewTrainer = view.findViewById<CalendarView>(R.id.cal_trainer) //캘린더
        firestore = FirebaseFirestore.getInstance() //파이어스토어 인스턴스 초기화
        val mActivity = activity as NaviActivity

        //새로운 데이터 저장할 때 마다 데이터 불러오기
        val databaseReference: DatabaseReference = firebaseDatabase.getReference("scheduleTrainer").child(mActivity.loginUser()!!) //db 연결
        databaseReference.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SuspiciousIndentation")
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()
                itemList.clear()
                //db에서 데이터 불러오기
                dataSnapshot.children.forEach{
                    val schedule_trainer = it.getValue(TrainerItem::class.java)
                    schedule_trainer ?:return
                    dataList.add(schedule_trainer) //데이터리스트에 추가

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


        //처음 일정 예약 페이지를 로딩했을 때 오늘의 일정 뜨기
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


        recyclerview.adapter = ListAdapter //어댑터 연결
        recyclerview.layoutManager = LinearLayoutManager(activity)


        //날짜별로 클릭시
        calenderviewTrainer.setOnDateChangeListener(object : CalendarView.OnDateChangeListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onSelectedDayChange(p0: CalendarView, p1: Int, p2: Int, p3: Int) {
                itemList.clear()
                date = p1.toString() + "-" + (p2 + 1).toString() + "-" + p3.toString()

                var bundle = Bundle() //번들 생성
                bundle.putString("key2", date) //번들에 값 담기
                trainerDialog.arguments = bundle //값이 담긴 번들을 argunments에 담기
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
                Log.d("hhh", date)
            }
        })


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //객체 생성
        val btnAdd = view.findViewById<FloatingActionButton>(R.id.btn_add_class) //일정 추가하기 버튼


        //일정 추가하기 버튼 클릭시
        btnAdd.setOnClickListener {

            //다이얼로그 띄우기
            activity?.supportFragmentManager?.let { fragmentManager ->
                dialog.showNow(fragmentManager, "TAG_DIALOG_EVENT")
            }


        }



        itemTouch()//아이템 삭제 실행

    }

    //아이템 삭제를 위한 함수
    fun itemTouch() {
        val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                ListAdapter.removeData(viewHolder.layoutPosition)
            }

        }
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(view?.findViewById<RecyclerView>(R.id.recyclerview_calender_trainer))
    }

}