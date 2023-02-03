package instructure_record

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.guru2.R
import com.example.guru2.Records.MealRecModel
import kotlinx.android.synthetic.main.meal_record_form.view.*

class RecyclerInstMealAdapter():
    RecyclerView.Adapter<RecyclerInstMealAdapter.ViewHolder>() {
    private var arrayList = ArrayList<MealRecModel>()
    lateinit var ct: Context
    private lateinit var uidList: ArrayList<String>

    constructor(arrayList: ArrayList<MealRecModel>, context: android.content.Context, uidList: ArrayList<String>) : this() {
        this.arrayList = arrayList
        ct = context
        this.uidList = uidList
    }

    @NonNull
    //실제 리스트뷰가 어댑터에 연결된 다음에 뷰 홀더를 최초로 만들어낸다.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView=
            LayoutInflater.from(parent.context)
                .inflate(R.layout.instructure_meal_record_form, parent, false)
        val holder: ViewHolder = ViewHolder(inflatedView)
        return holder
    }


    override fun onBindViewHolder(@NonNull holder: ViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    override fun getItemCount(): Int {
        // 삼항 연산자
        return if (arrayList != null) arrayList.size else 0
    }

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val view: View = v
        fun bind(item: MealRecModel) {
            view.tv_eatDate.text = item.eatDate + "  " + item.eatTime
            view.tv_timeSlot.text = item.timeSlot
            view.tv_menuName.text = item.mealName + "  " + item.eatAmount
        }
    }

    // 리스너 인터페이스
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.mealRecClickListener = onItemClickListener
    }
    // setItemClickListener로 설정한 함수 실행
    private lateinit var mealRecClickListener : OnItemClickListener
}