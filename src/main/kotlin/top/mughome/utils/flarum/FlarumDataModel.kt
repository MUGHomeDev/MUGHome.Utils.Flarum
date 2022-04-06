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

/**
 * Flarum相关数据类型
 * @author Yang
 * @version 0.0.5
 * @since 0.0.1-SNAPSHOT
 */
@Deprecated("请使用依赖于BasicModel的新数据类型")
class FlarumDataModel {
    /**
     * FlarumDiscussion数据类型
     * @author Yang
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    data class DiscussionInfo(
        val id: Int,
        val title: String,
        val postUserId: Int,
        val discussionCreateDate: String,
        val discussionLastPostedDate: String,
        val discussionCreateStamp: Long,
        val discussionLastPostedStamp: Long,
        val commentCount: Int,
        val viewCount: Int,
        val postInfoList: MutableMap<Int, Int> = mutableMapOf()
    )

    /**
     * FlarumPost数据类型
     * @author Yang
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    data class PostInfo(
        val id: Int,
        val postCreateTime: String,
        val postCreateTimeStamp: Long,
        var postContentHtml: String = "",
        val postUserId: Int
    )
}