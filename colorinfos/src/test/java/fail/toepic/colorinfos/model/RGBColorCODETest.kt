package fail.toepic.colorinfos.model

import fail.toepic.colorinfos.repository.HTML5ColorsRepository
import org.junit.Test

//import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class RGBColorCODETest {
    @Test
    fun create() {

        // #000000
        var code = RGB.fromString("#000000") ?: error("널이 아니여야함.")

        assert(code.red == 0){ error("레드 값 다름 ")}
        assert(code.green == 0){ error("그린 값 다름 ")}
        assert(code.blue == 0){ error("블루 값 다름 ")}
        assert(code.code == "#000000"){error("코드 값다름")}


        code = RGB.fromString("#FFFFFF") ?: error("널이 아니여야함.")

        assert(code.red == 255){ error("레드 값 다름 ")}
        assert(code.green == 255){ error("그린 값 다름 ")}
        assert(code.blue == 255){ error("블루 값 다름 ")}
        assert(code.code.toUpperCase() == "#FFFFFF"){error("코드 값다름 ${code.code} == #FFFFFF  ")}


        code = RGB(255,255,255)

        assert(code.red == 255){ error("레드 값 다름 ")}
        assert(code.green == 255){ error("그린 값 다름 ")}
        assert(code.blue == 255){ error("블루 값 다름 ")}
        assert(code.code.toUpperCase() == "#FFFFFF"){error("코드 값다름 ${code.code} == #FFFFFF  ")}



    }

    @Test
    fun equal() {

        val code : ColorCode = RGB(0,0,0)
        val code2 : ColorCode = RGB(1,2,3)
        val code3 : ColorCode = RGB.fromString("#000000") ?: RGB(0,0,0)

        assert(code == code)
        assert(code == code3)
        assert(code2 != code)
        assert(code2 != code3)

    }


    @Test
    fun toCMYK() {

        RGB(255,255,255).toCMYK().let {cmyk ->
            assert(cmyk.cyan == 0F){ error(" ${cmyk.cyan} ")}
            assert(cmyk.magenta == 0f){ println(" ${cmyk.magenta} ")}
            assert(cmyk.yellow == 0f){ println(" ${cmyk.yellow} ")}
            assert(cmyk.key == 0f){ println(" ${cmyk.key} ")}
        }

        RGB(0,0,0).toCMYK().let {cmyk ->
            assert(cmyk.cyan == 0F){ error(" ${cmyk.cyan} ")}
            assert(cmyk.magenta == 0f){ println(" ${cmyk.magenta} ")}
            assert(cmyk.yellow == 0f){ println(" ${cmyk.yellow} ")}
            assert(cmyk.key == 1f){ println(" ${cmyk.key} ")}
        }


        RGB(255,0,0).toCMYK().let {cmyk ->
            assert(cmyk.cyan == 0F){ error(" ${cmyk.cyan} ")}
            assert(cmyk.magenta == 1f){ println(" ${cmyk.magenta} ")}
            assert(cmyk.yellow == 1f){ println(" ${cmyk.yellow} ")}
            assert(cmyk.key == 0f){ println(" ${cmyk.key} ")}
        }


        RGB(255,0,0).toCMYK().let {cmyk ->
            assert(cmyk.cyan == 0F){ error(" ${cmyk.cyan} ")}
            assert(cmyk.magenta == 1f){ println(" ${cmyk.magenta} ")}
            assert(cmyk.yellow == 1f){ println(" ${cmyk.yellow} ")}
            assert(cmyk.key == 0f){ println(" ${cmyk.key} ")}
        }

        for (colorInfo in HTML5ColorsRepository.getGroupedColor("Red")) {
            val rgb = colorInfo.code as RGB

            println("${rgb.code} ${rgb.red} ${rgb.green} ${rgb.blue}  => ${rgb.toCMYK().code}")

        }

        RGB(238,232,170).toCMYK().let {cmyk ->
            println("code : ${cmyk.code}")
            assert(cmyk.cyan == 0F){ error(" ${cmyk.cyan} ")}
            assert(cmyk.magenta == 0.03f){ error(" ${cmyk.magenta} ")}
            assert(cmyk.yellow == 0.29f){ error(" ${cmyk.yellow} ")}
            assert(cmyk.key == 0.07f){ error(" ${cmyk.key} ")}
        }

    }
}
