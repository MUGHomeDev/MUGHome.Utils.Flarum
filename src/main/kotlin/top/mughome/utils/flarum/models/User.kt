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
 * FlarumUser数据类型
 * @author Yang
 * @version 0.0.5
 * @since 0.0.4
 */
interface User : BasicModel {
    /**
     * 用户名
     */
    var username: String

    /**
     * 显示名称
     */
    var displayName: String

    /**
     * 头像地址
     */
    var avatarUrl: String

    /**
     * 背景地址
     */
    var bgUrl: String

    /**
     * 用户简介
     */
    var description: String

    /**
     * 用户注册时间
     */
    var joinTime: String

    /**
     * 用户注册时间戳
     */
    var joinTimeStamp: Long

    /**
     * 用户登录Token
     */
    var token: String

    /**
     * 用户Session Cookie
     */
    var sessionC: String
}