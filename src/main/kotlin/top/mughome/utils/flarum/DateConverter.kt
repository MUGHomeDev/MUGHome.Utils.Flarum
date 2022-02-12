package top.mughome.utils.flarum

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * 日期转换工具静态类
 * @author siscon, Yang
 * @version 0.0.1-SNAPSHOT
 * @since 0.0.1-SNAPSHOT
 */
object DateConverter {
    /**
     * String日期转换为时间戳
     * @author siscon, Yang
     * @param time yyyy-MM-dd'T'HH:mm:ssZ格式日期
     * @return 返回转换后的时间戳
     * @throws NullPointerException 由SimpleDateFormat实例化产生，若产生，检查该lib源码
     * @throws IllegalArgumentException 由SimpleDateFormat实例化产生，若产生，检查该lib源码
     * @throws ParseException 由SimpleDateFormat实例的parse方法产生，若产生，检查传入字符串是否符合格式
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(NullPointerException::class, IllegalArgumentException::class, ParseException::class)
    fun dateToStamp(time: String): Long {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.CHINA)
        val stamp: Long = if (time != "") {
            sdf.parse(time)!!.time / 1000
        } else {
            val currentTime = System.currentTimeMillis()
            currentTime / 1000
        }
        return stamp
    }

    /**
     * 时间戳转换为String日期
     * @author siscon, Yang
     * @param s 时间戳格式日期
     * @return 返回转换后yyyy-MM-dd HH:mm:ss格式的String日期
     * @throws NullPointerException 由SimpleDateFormat实例化产生，若产生，检查该lib源码
     * @throws IllegalArgumentException 由SimpleDateFormat实例化产生，若产生，检查该lib源码
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(NullPointerException::class, IllegalArgumentException::class)
    fun stampToDate(s: Long): String =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(Date(s * 1000))
}