package com.example.guru2.Records

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class RecordTabAdapter(fragmentManager:FragmentManager,val tabCount: Int): FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return tabCount
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0-> ExerciseRecordFragment()
            1-> MealRecordFragment()
            else -> return ExerciseRecordFragment()
        }
    }


}