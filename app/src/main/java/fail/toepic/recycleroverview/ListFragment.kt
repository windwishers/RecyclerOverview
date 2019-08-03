package fail.toepic.recycleroverview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import fail.toepic.recycleroverview.basic.BasicFragmentDirections

class ListFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false).apply {
            findViewById<View>(R.id.basic).setOnClickListener {
                findNavController().navigate(NavGraphDirections.moveBasic())
            }
            findViewById<View>(R.id.selection).setOnClickListener {
                findNavController().navigate(NavGraphDirections.moveSelection())
            }
        }
    }
}
