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

import top.mughome.utils.flarum.managers.AccountManager

/**
 * Flarum初始化类，使用前请先初始化
 * @author Yang
 * @version 0.0.5
 * @since 0.0.1-SNAPSHOT
 */
object Flarum {
    /**
     * 初始化用：Flarum链接
     */
    lateinit var baseUrl: String

    /**
     * 初始化用：User-Agent
     */
    lateinit var userAgent: String

    /**
     * 初始化用：用户背景图链接前缀
     */
    lateinit var coverPrefix: String

    /**
     * 存储的accountManager实例
     */
    val accountManager: AccountManager = AccountManager()
        get() = field.clear()

    /**
     * 初始化
     * @author Yang
     * @param baseUrl Flarum链接
     * @param userAgent User-Agent
     * @param coverPrefix 用户背景图链接前缀
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    fun init(baseUrl: String, userAgent: String, coverPrefix: String) {
        this.baseUrl = baseUrl
        this.userAgent = userAgent
        this.coverPrefix = coverPrefix
    }
}