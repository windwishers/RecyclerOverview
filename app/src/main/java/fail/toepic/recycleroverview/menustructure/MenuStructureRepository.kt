package fail.toepic.recycleroverview.menustructure

object MenuStructureRepository {

    val menuMap : MutableMap<String,List<Item>> = mutableMapOf()

    init {
        menuMap.apply {
            put("BASIC", arrayListOf(
                Item.getTitle("RECYCLER BASIC"),
                Item.getButton("simple",true),
                Item.getButton("LayoutManager",false),
                Item.getButton("ItemAnimator",false),
                Item.getButton("ItemDecoration",false),
                Item.getButton("EdgeEffectFactory",false)
            ))
            put("SELECTION", arrayListOf(
                Item.getTitle("RECYCLER SELECTION"),
                Item.getButton("basic",true),
                Item.getButton("Selection Observer",true),
                Item.getButton("single selection",true),
                Item.getButton("multi Selection",true),
                Item.getButton("Selection Host Spot",true)
            ))
        }
    }

    fun getList(key : String) = menuMap[key] ?: arrayListOf()

}


data class Item(val type : Int,val name:String, val enable : Boolean = true){

    companion object{
        const val TITLE = 0
        const val BUTTON = 1
        fun getTitle(name: String) = Item(TITLE,name)
        fun getButton(name: String,enable: Boolean = true) = Item(BUTTON,name,enable = enable)
    }
}