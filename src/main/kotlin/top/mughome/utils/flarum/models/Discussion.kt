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
 * FlarumDiscussion数据类型
 * @author Yang
 * @version 0.0.5
 * @since 0.0.5
 */
interface Discussion : BasicModel {
    /**
     * Discussion标题
     */
    var title: String

    /**
     * 最后一个回复的用户id
     */
    var postUserId: Int

    /**
     * Discussion创建时间
     */
    var discussionCreateDate: String

    /**
     * 最后一个回复的时间
     */
    var discussionLastPostedDate: String

    /**
     * Discussion创建时间戳
     */
    var discussionCreateStamp: Long

    /**
     * 最后一个回复的时间戳
     */
    var discussionLastPostedStamp: Long

    /**
     * 评论数
     */
    var commentCount: Int

    /**
     * 观看数
     */
    var viewCount: Int
}