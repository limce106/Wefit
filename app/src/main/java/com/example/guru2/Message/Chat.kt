package com.example.guru2.Message


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.guru2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class Chat : Fragment() {


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

        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        //넘어온 데이터 변수에 담기
        var receiverName = arguments?.getString("name").toString()//대화상대 이름
        var receiverUid = arguments?.getString("uid").toString()//대화상대 Uid



        var mAuth = FirebaseAuth.getInstance() //인증 객체
        var mDbRef:DatabaseReference = FirebaseDatabase.getInstance().reference //DB 객체



        //접속자 Uid
        val senderUid=mAuth.currentUser?.uid
        //보낸이방
        var senderRoom:String =receiverUid+senderUid
        //받는이방
        var receiverRoom:String =senderUid+receiverUid

        val btn_send: Button = view.findViewById(R.id.btn_send)
        val message_edit: EditText = view.findViewById(R.id.message_edit)

        var messageList:ArrayList<Message> = ArrayList()

        var chat_RecyclerView: RecyclerView = view.findViewById(R.id.chat_recyclerView)    //RecyclerView

        //    var name = arguments?.getString("name").toString()
        //    var uid = arguments?.getString("uid").toString()

        val messageAdapter: MessageAdapter = MessageAdapter(requireActivity(), messageList)


        chat_RecyclerView.layoutManager= LinearLayoutManager(activity)
        chat_RecyclerView.adapter=messageAdapter

        //액션바에 상대방 이름 보여주기
        //NaviActivity().supportActionBar?.title =receiverName



        //메시지 전송 버튼
        //입력한 메시지는 DB에 저장이 되고 DB에 저장된 메시지를 화면에 보여줌
        btn_send.setOnClickListener {

            val message=message_edit.text.toString()
            val messageObject= Message(message,senderUid)

            //데이터 저장
            mDbRef.child("chats").child(senderRoom).child("messages").push()
                .setValue(messageObject).addOnSuccessListener {
                    //저장성공하면
                    mDbRef.child("chats").child(receiverRoom).child("messages").push()
                        .setValue(messageObject)}
        }

        //입력값 초기화
        message_edit.setText("")

        //메시지 가져오기
        mDbRef.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()

                    for(postSnapshat in snapshot.children){
                        val message=postSnapshat.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    //적용
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })


        return view
    }



}