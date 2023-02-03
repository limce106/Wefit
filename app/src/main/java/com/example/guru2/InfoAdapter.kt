package com.example.guru2

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class InfoAdapter {

    class InfoViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){

        val page_name:TextView=itemView.findViewById(R.id.page_name)
        val page_id:TextView=itemView.findViewById(R.id.page_id)
        val page_gender:TextView=itemView.findViewById(R.id.page_gender)
        val page_purpose:TextView=itemView.findViewById(R.id.page_purpose)


    }
}