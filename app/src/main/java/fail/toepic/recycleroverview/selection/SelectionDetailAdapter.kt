package fail.toepic.recycleroverview.selection

import android.graphics.Color
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Space
import android.widget.TextView
import androidx.recyclerview.selection.DefaultSelectionTracker
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fail.toepic.colorinfos.model.RGB
import fail.toepic.colorinfos.repository.ColorInfo
import fail.toepic.recycleroverview.R

class SelectionDetailAdapter : ListAdapter<ColorInfo, ViewHolder>(DIFF_CALLBACK){


    var tracker: SelectionTracker<Long>? = null

    init {
        setHasStableIds(true)  //  이걸 안주면 itemid가 -1 이 나와서 여기 저기 터짐.
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return android.view.LayoutInflater.from(parent.context).inflate(R.layout.item_selection, parent, false).let {
            ViewHolder(it)
        }
    }

    fun get(position: Int): ColorInfo {
        return super.getItem(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.also {

            holder.bind(it,tracker?.isSelected(position.toLong()) ?: false)
        }
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    companion object{
        var isHotSpot: Boolean = false
    }
}




val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ColorInfo>() {

    override fun areItemsTheSame(oldItem: ColorInfo, newItem: ColorInfo): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: ColorInfo, newItem: ColorInfo): Boolean {
        return false
    }

}


class ViewHolder(view : View) : RecyclerView.ViewHolder(view){

    private val name : TextView = itemView.findViewById(R.id.name)
    private val hexcode : TextView = itemView.findViewById(R.id.hexcode)
    private val leftSpace : Space = itemView.findViewById(R.id.lspace)
    private val rightSpace : Space = itemView.findViewById(R.id.rspace)
    private val plate : View = itemView.findViewById(R.id.plate)

    fun bind(color: ColorInfo, isSelect: Boolean) {
        name.text = color.name
        hexcode.text = color.code.code
        setSelected(isSelect)
        plate.setBackgroundColor(Color.parseColor(color.code.code))
        name.setTextColor(Color.parseColor((color.code as RGB).toComplementaryColor().code))
        hexcode.setTextColor(Color.parseColor((color.code as RGB).toComplementaryColor().code))
    }

    private fun setSelected(isSelect: Boolean) {
        leftSpace.visibility = if (isSelect) View.VISIBLE else View.GONE
        rightSpace.visibility = if (!isSelect) View.VISIBLE else View.GONE
    }

    fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long>? =
        object : ItemDetailsLookup.ItemDetails<Long>() {

            override fun inSelectionHotspot(e: MotionEvent): Boolean = SelectionDetailAdapter.isHotSpot  // 원래는 이러면 안됨.
            override fun getSelectionKey(): Long? = itemId
            override fun getPosition(): Int = adapterPosition

        }


}