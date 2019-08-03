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
import androidx.recyclerview.selection.*
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
        var isObserver = false
        when(key){
            "basic" -> {
                //UNIT
            }
            "Selection Observer" ->{
                isObserver = true
            }
            "single selection"-> {
                selectionPredicate = SelectionPredicates.createSelectSingleAnything<Long>()
            }
            "multi Selection" ->{
                selectionPredicate = SelectionPredicates.createSelectAnything<Long>()
            }
            "Selection HotSpot"->{
                SelectionDetailAdapter.isHotSpot = true
            }
        }


        list?.let {list ->
             val builder = SelectionTracker.Builder<Long>(
                 "Selection",
                 list,
                 StableIdKeyProvider(list),
                 SelectionDetailLookUp(list),
                 StorageStrategy.createLongStorage()
             )

            selectionPredicate?.let {
                builder.withSelectionPredicate(it)
            }
             tracker = builder.build()

            adapter.tracker = tracker
        }

        if (isObserver) {
            setObserver()
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