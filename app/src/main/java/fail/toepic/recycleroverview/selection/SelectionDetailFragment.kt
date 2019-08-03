package fail.toepic.recycleroverview.selection


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fail.toepic.colorinfos.repository.HTML5ColorsRepository
import fail.toepic.recycleroverview.R
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class SelectionDetailFragment : Fragment() {

    private var title : TextView? = null
    private var list : RecyclerView? = null
    private val adapter = SelectionDetailAdapter()
    private var tracker : SelectionTracker<Long>? = null
    private var key : String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        key = arguments?.let {
            SelectionDetailFragmentArgs.fromBundle(it).key
        }

        title?.text = key


        var selectionPredicate: SelectionTracker.SelectionPredicate<Long>? = null
        SelectionDetailAdapter.isHotSpot = false
        when(key){
            "basic" -> {
                //UNIT
            }
            "Selection Observer" ->{
                setObserver()
            }
            "single selection"-> {
                selectionPredicate = SelectionPredicates.createSelectSingleAnything<Long>()
            }
            "multi Selection" ->{
                selectionPredicate = SelectionPredicates.createSelectAnything<Long>()
            }
            "Selection Host Spot"->{
                SelectionDetailAdapter.isHotSpot = true
            }
        }


        list?.let {list ->
             val builder = SelectionTracker.Builder<Long>(
                 "Selection",
                 list,
//                    StableIdKeyProvider(list), //  androidx.recyclerview.widget.RecyclerView$ViewHolder.getAdapterPosition() 이 난다.
                 KeyProvider(list),
                 SelectionDetailLookUp(list),
                 StorageStrategy.createLongStorage()
             )

            selectionPredicate?.let {
                builder.withSelectionPredicate(it)
            }
             tracker = builder.build()

            adapter.tracker = tracker
        }

        adapter.submitList(HTML5ColorsRepository.colorInfos)
    }

    private fun setObserver() {
        tracker?.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
            override fun onItemStateChanged(key: Long, selected: Boolean) {
                super.onItemStateChanged(key, selected)
                Toast.makeText(
                    this@SelectionDetailFragment.context,
                    adapter.get(key.toInt()).name,
                    Toast.LENGTH_SHORT
                ).show()
            }

            @SuppressLint("SetTextI18n")
            override fun onSelectionChanged() {
                super.onSelectionChanged()
                tracker?.let { tracker ->
                    if (tracker.hasSelection()) {
                                title?.text = "${tracker.selection.size()}/${adapter.itemCount}"
                    } else {
                                title?.text = key
                    }
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_selection_detail, container, false).apply {
            title = findViewById<TextView>(R.id.title)
            list = findViewById<RecyclerView>(R.id.list)

            list?.let {list->
                list.layoutManager = LinearLayoutManager(this.context,RecyclerView.VERTICAL,false)
                list.adapter = adapter
                list.setHasFixedSize(true)
            }

        }
    }

}

/**  StableIdKeyProvider 스테이블 키 프로바이더와 코드가 거의 동일 함.
 * 다만 androidx.recyclerview.widget.RecyclerView$ViewHolder.getAdapterPosition()
 * 에서 널포인트익셉션이 나는 문제로 널체크를 추가한 버전이다.  */
class KeyProvider(val recyclerView: RecyclerView) : ItemKeyProvider<Long>(SCOPE_CACHED) {

    private val mPositionToKey = SparseArray<Long>()
    @SuppressLint("UseSparseArrays")
    private val mKeyToPosition = HashMap<Long, Int>()


    init {
        recyclerView.addOnChildAttachStateChangeListener(
            object : RecyclerView.OnChildAttachStateChangeListener {
                override fun onChildViewAttachedToWindow(view: View) {
                    onAttached(view)
                }

                override fun onChildViewDetachedFromWindow(view: View) {
                    onDetached(view)
                }
            }
        )
    }

    internal /* synthetic access */ fun onAttached(view: View) {
        val holder = recyclerView.findContainingViewHolder(view)
        val position = holder?.adapterPosition  ?: RecyclerView.NO_POSITION
        val id = holder?.itemId ?: RecyclerView.NO_ID
        if (position != RecyclerView.NO_POSITION && id != RecyclerView.NO_ID) {
            mPositionToKey.put(position, id)
            mKeyToPosition[id] = position
        }
    }

    internal /* synthetic access */ fun onDetached(view: View) {
        val holder = recyclerView.findContainingViewHolder(view)
        val position = holder?.adapterPosition ?: RecyclerView.NO_POSITION
        val id = holder?.itemId ?: RecyclerView.NO_ID
        if (position != RecyclerView.NO_POSITION && id != RecyclerView.NO_ID) {
            mPositionToKey.delete(position)
            mKeyToPosition.remove(id)
        }
    }
    override fun getKey(position: Int): Long? {
        return mPositionToKey.get(position, null)
    }

    override fun getPosition(key: Long): Int {
        return if (mKeyToPosition.containsKey(key)) {
            mKeyToPosition[key]!!
        } else RecyclerView.NO_POSITION
    }

}