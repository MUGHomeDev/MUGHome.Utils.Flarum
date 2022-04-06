/*
 * MUGHome.Utils.Flarum - A Flarum Util for Java/Kotlin
 * https://github.com/MUGHomeDev/MUGHome.Utils.Flarum
 * Copyright (C) 2021-2022  MUGHome
 *
 * This Library is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 *
 * Please contact us by email contact@mail.mughome.top
 * if you need additional information or have any questions
 */
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
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'", Locale.CHINA)
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