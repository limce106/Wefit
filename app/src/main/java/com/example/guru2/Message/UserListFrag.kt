package com.example.guru2.Message

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guru2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList


class UserListFrag : Fragment() {

    var userList:ArrayList<User> = ArrayList()
    var mAuth: FirebaseAuth = Firebase.auth
    var mDbRef: DatabaseReference = Firebase.database.reference
    var adapter: UserAdapter = UserAdapter(this, userList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_user_list, container, false)
        val userRecyclerView: RecyclerView = view.findViewById(R.id.user_recyclerView)



        userRecyclerView.layoutManager = LinearLayoutManager(activity)
        userRecyclerView.adapter = adapter

        //사용자 정보 가져오기
        mDbRef.child("user").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    //유저 정보
                    val currentUser=postSnapshot.getValue(User::class.java)
                    //내 아이디와 사용자 정보가 다를 때만
                    if(mAuth.currentUser?.uid!=currentUser?.uId){
                        userList.add(currentUser!!)
                    }
                }
                adapter.notifyDataSetChanged()
                //데이터가 변경되면 함수가 실행됨
            }


            override fun onCancelled(error: DatabaseError) {
                //데이터가 캔슬됐을 때 실행됨
            }

        })


        return view
    }




}