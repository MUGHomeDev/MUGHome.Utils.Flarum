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