package com.example.guru2.Records

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.example.guru2.NaviActivity
import com.example.guru2.R
import com.google.android.material.tabs.TabLayout



class RecordMain : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_record_main, container, false)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_record) // 탭 레이아웃
        val recordframe = view.findViewById<FrameLayout>(R.id.recordFrame)

        val mActivity = activity as NaviActivity
        mActivity.supportFragmentManager.beginTransaction().add(R.id.recordFrame,ExerciseRecordFragment()).commit()

        //탭이 선택되었을 때 페이지가 바꾸도록 함
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab!!.position == 0){
                    mActivity.changeTab(ExerciseRecordFragment())
                } else if(tab.position == 1){
                    mActivity.changeTab(MealRecordFragment())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        return view
    }
}
