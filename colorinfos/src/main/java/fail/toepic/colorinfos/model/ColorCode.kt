package fail.toepic.colorinfos.model

import kotlin.math.roundToInt

@Suppress("unused")
sealed class ColorCode(val model: TYPE, vararg  values : Pair<Int,Float>){

    private val valueMap : MutableMap<Int,Float> = mutableMapOf()

    init{
        for ((key,map) in values) {
            valueMap[key] = map
        }
    }

    val code : String
        get() = makeCodeString()

    fun getCode(key : Int) : Float? = valueMap[key]
    fun getCodeMap() : Map<Int,Float> = valueMap.toMap()
    /** 리스트는 구조 분해를 사용하게 될 경우 순서가 중요하기 때문에 상속 하는 분에서 지원하도록 함. */
    abstract fun getCodeList() : List<Float>
    abstract fun makeCodeString() : String

    enum class TYPE{
        RGB,HSL,CMYK
    }
}

//<editor-fold desc="RGB 지원">
/**  RGB 컬러값을 지원 한다. 단 ARGB 아직 미지원 지원하지 않음. */
data class RGB(val red: Int,val green:Int, val blue:Int):
    ColorCode(TYPE.RGB,RED to red.toFloat(), GREEN to green.toFloat(), BLUE to blue.toFloat()) {


    private var _code : String? = null


    override fun makeCodeString(): String = _code ?: run{
        "#${red.toString(16).padStart(2, '0')}" +
                green.toString(16).padStart(2, '0') +
                blue.toString(16).padStart(2, '0')

    }


    override fun getCodeList(): List<Float> {
        return listOf(getCode(RED) ?: -1F ,getCode(GREEN) ?: -1F, getCode(BLUE) ?: -1F)
    }

    fun toComplementaryColor() : RGB{
        return RGB(255-red,255-green,255-blue)
    }

    fun toCMYK() : CMYK{

        val black = arrayListOf(1f - red/ 255f,1f - green/ 255f,1f - blue/ 255f ).min() ?: 0f
        var cyan  = 0f
        var magenta = 0f
        var yellow = 0f

        if( black != 1f){
            cyan = (1f - (red / 255f) - black) / (1f - black)
            magenta = (1f - (green / 255f) - black) / (1f - black)
            yellow = (1f - (blue / 255f) - black) / (1f - black)
        }
        cyan = round(cyan,100f)
        magenta = round(magenta,100f)
        yellow = round(yellow,100f)

        return CMYK(cyan,magenta,yellow,round(black,100f))
    }

    fun round( number : Float,mask : Float = 1f) = (number * mask).roundToInt() / mask

    companion object{
        const val RED  = 1
        const val GREEN  = 2
        const val BLUE  = 3

        //language=RegExp  // #RRGGBB
        private val patten = "^#([ABCDEF0-9]{2})([ABCDEF0-9]{2})([ABCDEF0-9]{2})".toRegex()

        fun fromString(code:String) : RGB?{

            return patten.find(code)?.let { result ->

                val (_,r,g,b) = result.groupValues
                RGB(r.toInt(16),g.toInt(16),b.toInt(16))
            }
        }
    }
}
//</editor-fold>


//<editor-fold desc="CMYK 지원">
class CMYK(val cyan: Float, val magenta : Float, val yellow: Float, val key:Float ):
    ColorCode(TYPE.CMYK,
        CYAN to cyan,
        MAGENTA to magenta,
        YELLOW to yellow,
        KEY to key
    ){

    override fun getCodeList(): List<Float> {

        return listOf(
            getCode(CYAN) ?: -1F ,
            getCode(MAGENTA) ?: -1F,
            getCode(YELLOW) ?: -1F,
            getCode(KEY) ?: -1F
        )
    }

    override fun makeCodeString(): String = "C${(cyan*100).toInt()}M${(magenta*100).toInt()}" +
            "Y${(yellow*100).toInt()}K${(key*100).toInt()}"

    fun toRGB() : RGB{
        val k1 = 1 - key
        return RGB(
            (255 * ( 1 - cyan) * k1).toInt() ,
            (255 * ( 1 - magenta) * k1).toInt(),
            (255 * ( 1 - yellow) * k1).toInt()
        )
    }

    companion object{
        const val CYAN = 1
        const val MAGENTA  = 2
        const val YELLOW  = 3
        const val KEY  = 4
        @Suppress("unused")
        const val BLACK  = KEY  // K is Black.

    }

}
//</editor-fold>

//data class HSL(val code: String) : ColorCode(TYPE.HSL,code)

