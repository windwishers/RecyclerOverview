package fail.toepic.recycleroverview.basic_simple


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import fail.toepic.recycleroverview.R

/**
 * A simple [Fragment] subclass.
 */
class BasicSimpleFragment : Fragment() {

    var list : RecyclerView? = null
    var adapter = BasicSimpleAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basic_simple, container, false).apply {
            list = findViewById(R.id.list)
            list?.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
            list?.adapter = adapter
        }
    }


}
