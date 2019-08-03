package fail.toepic.recycleroverview.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BaseListAdapter<T,VH: RecyclerView.ViewHolder>(private val delegate: BaseAdapterDelegate<T, VH>)
    : ListAdapter<T, VH>(delegate.differ){

    init {
        delegate.callback = {
                key, holder ->
            val pos = holder.adapterPosition
            if(pos != -1 )
                delegate.eventLisner.invoke(key,holder,getItem(pos))
            Unit
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH
            = delegate.onCreateViewHolder(parent,viewType)

    override fun onBindViewHolder(holder: VH, position: Int)
            = delegate.onBindViewHolder(holder,position,getItem(position))


    override fun getItemViewType(position: Int): Int
            = delegate.getItemViewType(position,getItem(position)) ?: super.getItemViewType(position)

    override fun getItemId(position: Int): Long
            = delegate.getItemId(position) ?: super.getItemId(position)

    companion object{
        fun Inflate(
            parent : View,
            @LayoutRes layoutid : Int,
            root : ViewGroup? = null,
            attachRoot : Boolean = false
        ) = LayoutInflater.from(parent.context).inflate(layoutid, root, attachRoot)
    }

}

