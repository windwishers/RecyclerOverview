package fail.toepic.recycleroverview.selection


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fail.toepic.recycleroverview.NavGraphDirections

import fail.toepic.recycleroverview.R
import fail.toepic.recycleroverview.base.BaseAdapterDelegate
import fail.toepic.recycleroverview.base.BaseListAdapter
import fail.toepic.recycleroverview.menustructure.Item
import fail.toepic.recycleroverview.menustructure.MenuStructureRepository

/**
 * A simple [Fragment] subclass.
 */
class SelectionFragment : Fragment() {

    private var list: RecyclerView? = null
    private var adapter : BaseListAdapter<Item, Holder>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list = view.findViewById<RecyclerView>(R.id.list)

        adapter = BaseListAdapter(delegate)


        setRecyclerView(
            list,
            LinearLayoutManager(this@SelectionFragment.context, RecyclerView.VERTICAL, false),
//            GridLayoutManager(this@SelectionFragment.context,3,RecyclerView.HORIZONTAL,false),
            adapter ?: BaseListAdapter(delegate)
        )
        //TODO 어거먼트를 이용한 추상화 고려 할 것
        adapter?.submitList(MenuStructureRepository.getList("SELECTION"))

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_basic_v2, container, false)

    }

    /** 리사이클러뷰 설정. */
    private fun setRecyclerView(
        list: RecyclerView?,
        layoutManager: RecyclerView.LayoutManager?,
        adapter: BaseListAdapter<Item, Holder>
    ) {
        // 레이아웃 메니저.
        list ?: return
        layoutManager?.let {
            list.layoutManager = layoutManager
            list.adapter = adapter
            // 어뎁터.
            // 아이템데코레이터
            // 에니메이터.
        }
    }
}

val delegate : BaseAdapterDelegate<Item, Holder> = object :
    BaseAdapterDelegate<Item, Holder> {

    override var callback: (key: String, holder: Holder) -> Unit? = {_,_->}

    override val eventLisner: (key: String, Holder: Holder,item : Item) -> Unit? = {
            _, holder, item -> holder.itemView.findNavController().navigate(SelectionFragmentDirections.moveDetail(item.name)) }

    override val differ: DiffUtil.ItemCallback<Item>
        get() = object : DiffUtil.ItemCallback<Item>(){
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem.name == newItem.name
            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean = oldItem == newItem
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return when(viewType){
            Item.BUTTON -> Holder(BaseListAdapter.Inflate(parent,R.layout.item_basic,parent)).apply {
                button?.setOnClickListener {
                    callback.invoke("BUTTON",this)
                }
            }
            Item.TITLE -> Holder(BaseListAdapter.Inflate(parent,R.layout.item_title,parent)).apply {

            }
            else -> Holder(BaseListAdapter.Inflate(parent,R.layout.item_basic,parent))
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int, item: Item) {
        when(item.type){
            Item.BUTTON -> {
                holder.button?.text = item.name
                holder.button?.isEnabled = item.enable
            }
            Item.TITLE ->{
                holder.title?.text = item.name
            }
        }
    }

    override fun getItemViewType(position: Int, item: Item): Int? = item.type

}




class Holder(view: View) : RecyclerView.ViewHolder(view){
    val button : Button? = view.findViewById(R.id.button)
    val title : TextView? = view.findViewById(R.id.title)
}
