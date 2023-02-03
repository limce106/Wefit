package com.example.guru2.graph_trainer

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import com.example.guru2.NaviActivity
import com.example.guru2.R
import com.example.guru2.graph_user.GraphInputInbody
import com.example.guru2.graph_user.InbodyItem
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*


class GraphTrainer : Fragment() {

    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    val itemList = arrayListOf<InbodyItem>() //아이템 배열
    lateinit var strNickname: String
    var isTrainerExist: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //객체 생성
        val view = inflater.inflate(R.layout.fragment_graph2, container, false)
        val dialog: GraphInputInbody = GraphInputInbody().getInstance() //인바디 입력 팝업창
        val linechartWeight = view.findViewById<LineChart>(R.id.linchart_weight) //몸무게 그래프
        val linechartMuscle = view.findViewById<LineChart>(R.id.linechart_muscle) //골격근량 그래프
        val linechartBodyfat = view.findViewById<LineChart>(R.id.linechart_inbodyfat) //체지방량 그래프
        var dateList = mutableListOf<String>() //날짜 데이터 배열
        var weightList = mutableListOf<String>() //몸무게 데이터 배열
        var muscleList = mutableListOf<String>() //골격근량 데이터 배열
        var bodyfatList = mutableListOf<String>() //체지방량 데이터 배열
        val databaseReference: DatabaseReference = firebaseDatabase.getReference("Inbody") //db 연결
        val mActivity = activity as NaviActivity
        // 닉네임 입력 변화 확인
        var edtNickName = view.findViewById<EditText>(R.id.edtNickName)
        checkChanges(edtNickName)


        val btnOk = view.findViewById<Button>(R.id.btnNicknameOk)
        btnOk.setOnClickListener() {
            databaseReference.orderByChild("reg_id").equalTo(strNickname)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if(!::strNickname.isInitialized || strNickname == "") {
                            Toast.makeText(context, "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show()
                        }else{
                            if(dataSnapshot.exists()){
                                Toast.makeText(context, "확인되었습니다.", Toast.LENGTH_SHORT).show()
                                isTrainerExist = true
                                // 입력한 id 사용자의 uid 찾기
                                mActivity.strID = strNickname
                                Log.e("uiD", strNickname)
                                mActivity.findUidByID(mActivity.strID)
                                Log.e("uid", mActivity.strID)

                                //새로운 데이터 저장할 때 마다 데이터 불러오기
                                databaseReference.child(strNickname).addValueEventListener(object : ValueEventListener {
                                    @SuppressLint("SuspiciousIndentation")
                                    @RequiresApi(Build.VERSION_CODES.O)
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        itemList.clear()
                                        dateList.clear()
                                        weightList.clear()
                                        muscleList.clear()
                                        bodyfatList.clear()
                                        //db에서 데이터 불러오기
                                        dataSnapshot.children.forEach{
                                            val inbodyItem = it.getValue(InbodyItem::class.java)
                                            inbodyItem ?:return

                                            dateList.add(inbodyItem.date.toString()) //날짜 데이터 불러오기
                                            weightList.add(inbodyItem.weight.toString()) //몸무게 데이터 불러오기
                                            muscleList.add(inbodyItem.muscle.toString()) //골격근량 데이터 불러오기
                                            bodyfatList.add(inbodyItem.bodyfat.toString()) //체지방량 데이터 불러오기

                                        }
                                        val entries1 = ArrayList<Entry>()
                                        val entries2 = ArrayList<Entry>()
                                        val entries3 = ArrayList<Entry>()
                                        for(i in 0 until weightList.size){
                                            entries1.add(Entry(i.toFloat(),weightList[i].toFloat()))
                                        }
                                        for(i in 0 until muscleList.size){
                                            entries2.add(Entry(i.toFloat(),muscleList[i].toFloat()))
                                        }
                                        for(i in 0 until bodyfatList.size){
                                            entries3.add(Entry(i.toFloat(),bodyfatList[i].toFloat()))
                                        }
                                        val labels = ArrayList<String>()
                                        for(i in 0 until dateList.size){
                                            labels.add(dateList[i])
                                        }
                                        val dateset1 = LineDataSet(entries1,"")
                                        val dateset2 = LineDataSet(entries2,"")
                                        val dateset3 = LineDataSet(entries3,"")

                                        //그래프 디자인 변경
                                        dateset1.setColor(Color.GRAY)
                                        dateset2.setColor(Color.GRAY)
                                        dateset3.setColor(Color.GRAY)
                                        dateset1.setCircleColor(Color.GRAY)
                                        dateset2.setCircleColor(Color.GRAY)
                                        dateset3.setCircleColor(Color.GRAY)
                                        dateset1.setCircleHoleRadius(20F)
                                        dateset2.setCircleHoleRadius(20F)
                                        dateset3.setCircleHoleRadius(20F)
                                        dateset1.setValueTextSize(15F)
                                        dateset2.setValueTextSize(15F)
                                        dateset3.setValueTextSize(15F)
                                        dateset1.setLineWidth(2F)
                                        dateset2.setLineWidth(2F)
                                        dateset3.setLineWidth(2F)




                                        //각 차트 x축 정보 지정
                                        linechartWeight.xAxis.valueFormatter = IndexAxisValueFormatter(labels) //몸무게 차트 x축
                                        linechartMuscle.xAxis.valueFormatter = IndexAxisValueFormatter(labels) //골격근량 차트 x축
                                        linechartBodyfat.xAxis.valueFormatter = IndexAxisValueFormatter(labels) //체지방률 차트 x축

                                        //각 차트 y축 정보 지정
                                        linechartWeight.getTransformer((YAxis.AxisDependency.LEFT))
                                        linechartWeight.xAxis.position = XAxis.XAxisPosition.BOTTOM
                                        linechartMuscle.getTransformer((YAxis.AxisDependency.LEFT))
                                        linechartMuscle.xAxis.position = XAxis.XAxisPosition.BOTTOM
                                        linechartBodyfat.getTransformer((YAxis.AxisDependency.LEFT))
                                        linechartBodyfat.xAxis.position = XAxis.XAxisPosition.BOTTOM

                                        val data1 = LineData(dateset1)
                                        val data2 = LineData(dateset2)
                                        val data3 = LineData(dateset3)


                                        //x축 y축 기준
                                        linechartWeight.data = data1
                                        linechartWeight.invalidate()
                                        linechartMuscle.data = data2
                                        linechartMuscle.invalidate()
                                        linechartBodyfat.data = data3
                                        linechartBodyfat.invalidate()


                                    }
                                    override fun onCancelled(databaseError: DatabaseError) {
                                        // 디비를 가져오던중 에러 발생 시
                                        Log.e("ExerciseRecord", databaseError.toException().toString()) // 에러문 출력
                                    }

                                })
                            } else {
                                Toast.makeText(context, "존재하지 않는 회원입니다." +
                                        " 다시 입력하세요.", Toast.LENGTH_SHORT).show()
                                isTrainerExist = false
                            }
                        }
                    }

                    override fun onCancelled(@NonNull databaseError: DatabaseError) {
                        Log.e("UserID", databaseError.toException().toString()) // 에러문 출력
                    }
                })
        }







        return view

    }

    fun checkChanges(edtNickname: EditText) {

        edtNickname.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                strNickname = s.toString()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                           after: Int, ) {}

            override fun afterTextChanged(s: Editable) {
            }
        })
    }


}