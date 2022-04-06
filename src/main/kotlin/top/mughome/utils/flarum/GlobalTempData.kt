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

import top.mughome.utils.flarum.models.User

/**
 * 旧版所使用的的全局临时数据
 * @author Yang
 * @version 0.0.2
 * @since 0.0.1-SNAPSHOT
 */
@Deprecated("数据请使用各Manager实例管理")
object GlobalTempData {
    var discussionsInfo: LinkedHashMap<Int, FlarumDataModel.DiscussionInfo> = LinkedHashMap()
    var usersInfo: LinkedHashMap<Int, User> = LinkedHashMap()
    var nextDiscussionLink: String = ""
    var nextDiscussionPostsLink: String = ""
    var postsInfo: LinkedHashMap<Int, FlarumDataModel.PostInfo> = LinkedHashMap()
    var XCSRFToken: String = ""
    var flarumSessionCookie: String = ""
    var userInfo: User? = null

    fun clear() {
        discussionsInfo = LinkedHashMap()
        usersInfo = LinkedHashMap()
        nextDiscussionLink = ""
        nextDiscussionPostsLink = ""
        postsInfo = LinkedHashMap()
        XCSRFToken = ""
        flarumSessionCookie = ""
        userInfo = null
    }
}