package com.example.guru2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.guru2.Message.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



/**
 * A simple [Fragment] subclass.
 * Use the [myPage.newInstance] factory method to
 * create an instance of this fragment.
 */
class myPage : Fragment() {

    var mAuth = FirebaseAuth.getInstance()
    var mDbRef:DatabaseReference = FirebaseDatabase.getInstance().reference

    //소현
    private val firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference: DatabaseReference = firebaseDatabase.getReference("user") //db 연결

    var user=FirebaseAuth.getInstance().currentUser
    var userId= user?.uid

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_my_page, container, false)



        val page_name : TextView = view.findViewById(R.id.page_name)
        val page_id : TextView = view.findViewById(R.id.page_id)
        val page_gender : TextView = view.findViewById(R.id.page_gender)
        val page_purpose : TextView = view.findViewById(R.id.page_purpose)



        databaseReference
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach{
                        val user = it.getValue(User::class.java)
                        user ?:return

                        page_name.text=user.reg_name
                        page_id.text=user.reg_id
                        page_gender.text=user.str_gender
                        page_purpose.text=user.str_purpose

                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    // 디비를 가져오던중 에러 발생 시
                    Log.e("ExerciseRecord", error.toException().toString()) // 에러문 출력
                }

            })



        // Inflate the layout for this fragment

        return view
    }






    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment myPage.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            myPage().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}