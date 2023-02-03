package instructure_record

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import com.example.guru2.NaviActivity
import com.example.guru2.R
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Instructure_Record_Main : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_instructure_record_main, container, false)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_record) // 탭 레이아웃
        val recordframe = view.findViewById<FrameLayout>(R.id.recordframe)
        val mActivity = activity as NaviActivity
        mActivity.supportFragmentManager.beginTransaction().add(R.id.recordframe,
            Instructure_Exercise_Fragment()).commit()

        //탭이 선택되었을 때 페이지가 바꾸도록 함
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab!!.position == 0){
                    mActivity.changeinstTab(Instructure_Exercise_Fragment())
                } else if(tab.position == 1){
                    mActivity.changeinstTab(Instructure_Meal_Fragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        // 닉네임 입력 변화 확인
        var edtNickName = view.findViewById<EditText>(R.id.edtNickName)
        checkChanges(edtNickName)

        val database = FirebaseDatabase.getInstance()
        val databaseReference = database.getReference("user") // DB 테이블 연결
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
            mActivity.strID = strNickname
            mActivity.isCheckID = isTrainerExist.toString()
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

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Instructure_Record_Main().apply {
                arguments = Bundle().apply {

                }
            }
    }
}