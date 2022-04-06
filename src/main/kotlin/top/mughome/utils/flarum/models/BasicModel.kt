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
package top.mughome.utils.flarum.models

/**
 * Flarum基础数据类型
 * @author Yang
 * @version 0.0.5
 * @since 0.0.5
 * @see ModelType
 * @see User
 * @see Discussion
 */
interface BasicModel {
    /**
     * id
     */
    var id: Int

    /**
     * 类型
     */
    val type: ModelType

    /**
     * 需重写 toString 方法
     */
    override fun toString(): String
}

/**
 * Flarum基础数据类别
 * @author Yang
 * @version 0.0.5
 * @since 0.0.5
 */
enum class ModelType {
    /**
     * 用户
     */
    USER,

    /**
     * 帖子
     */
    DISCUSSION,

    /**
     * 回复
     */
    POST
}