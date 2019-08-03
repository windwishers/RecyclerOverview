package fail.toepic.recycleroverview.basic_simple

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fail.toepic.colorinfos.model.CMYK
import fail.toepic.colorinfos.model.RGB
import fail.toepic.colorinfos.repository.ColorInfo
import fail.toepic.colorinfos.repository.HTML5ColorsRepository
import fail.toepic.recycleroverview.R

class BasicSimpleAdapter : RecyclerView.Adapter<ViewHolder>() {

    val list = mutableListOf<ColorInfo>()

    init {
        list.addAll(HTML5ColorsRepository.getGroupedColor("Red"))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_colroinfo_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = item.name
        val rgb : RGB? = when {
            item.code is RGB -> {
                val rgb = item.code as RGB
                holder.code.text = item.code.code
                holder.cmyk.text = rgb.toCMYK().code
                rgb
            }
            item.code is CMYK -> {
                val rgb = (item.code as CMYK).toRGB()
                holder.code.text = rgb.code
                holder.cmyk.text = item.code.code
                rgb
            }
            else -> null
        }

        rgb?.let {
            holder.rect.setBackgroundColor(Color.parseColor(it.code))
            holder.name.setTextColor(Color.parseColor(it.toComplementaryColor().code))

        }

    }

}



class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val name : TextView = itemView.findViewById(R.id.name)
    val code : TextView = itemView.findViewById(R.id.code)
    val cmyk : TextView = itemView.findViewById(R.id.cmyk)
    val rect : ImageView = itemView.findViewById(R.id.rect)
}