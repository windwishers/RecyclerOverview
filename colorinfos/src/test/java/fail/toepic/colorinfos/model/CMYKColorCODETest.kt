package fail.toepic.colorinfos.model

import org.junit.Test

//import org.junit.Assert.*


class CMYKColorCODETest {
    @Test
    fun create() {

        (listOf<Float>(0.8f,0.8f,0.8f,0.8f) to "C80M80Y80K80").let {
            val (c,m,y,k) = it.first
            val value = it.second

            val cmyk = CMYK(c,m,y,k)
            assert(cmyk.code == value){ error("${cmyk.code} != $value") }

        }





    }
//
//    @Test
//    fun equal() {
//
//        val code : ColorCode = RGB(0,0,0)
//        val code2 : ColorCode = RGB(1,2,3)
//        val code3 : ColorCode = RGB.fromString("#000000") ?: RGB(0,0,0)
//
//        assert(code == code)
//        assert(code == code3)
//        assert(code2 != code)
//        assert(code2 != code3)
//
//    }


    @Test
    fun toRGB() {

        CMYK(0.8F, 0.8F, 0.8F, 0.8F).toRGB().let {rgb ->
            assert(rgb.red == 10)
            assert(rgb.green == 10)
            assert(rgb.blue == 10)
        }

        CMYK(0.0F, 0.0F, 0.0F, 0.0F).toRGB().let {rgb ->
            assert(rgb.red == 255)
            assert(rgb.green == 255)
            assert(rgb.blue == 255)
        }

        CMYK(1.0F, 1.0F, 1.0F, 1.0F).toRGB().let {rgb ->
            assert(rgb.red == 0)
            assert(rgb.green == 0)
            assert(rgb.blue == 0)
        }
    }


}
