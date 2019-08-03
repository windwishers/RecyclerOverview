package fail.toepic.recycleroverview.base

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

// TODO 각 아이템을 받아 갈 것인가? 아니면 인터페이스를 넘길 것 인가?
interface BaseAdapterDelegate<T,VH: RecyclerView.ViewHolder>{
    val differ : DiffUtil.ItemCallback<T>

    /** delegate -> adapter  */
    var callback : (key : String ,holder : VH) -> Unit?
    /** adapter -> delegate */
    val eventLisner : (key : String,Holder : VH,item : T) -> Unit?

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : VH
    fun onBindViewHolder(holder: VH, position: Int, item: T)
    /** 널을 반환 하는 경우에는 Adapter 쪽 함수를 호출 합니다.   */
    fun getItemViewType(position: Int, item: T): Int? = null
    /** 널을 반환 하는 경우에는 Adapter 쪽 함수를 호출 합니다.   */
    fun getItemId(position: Int) : Long? = null



}

