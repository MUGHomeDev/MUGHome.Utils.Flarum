package top.mughome.utils.flarum

object GlobalTempData {
    var discussionsInfo: LinkedHashMap<Int, FlarumDataModel.DiscussionInfo> = LinkedHashMap()
    var usersInfo: LinkedHashMap<Int, FlarumDataModel.UserInfo> = LinkedHashMap()
    var nextDiscussionLink: String = ""
    var nextDiscussionPostsLink: String = ""
    var postsInfo: LinkedHashMap<Int, FlarumDataModel.PostInfo> = LinkedHashMap()
    var XCSRFToken: String = ""
    var flarumSessionCookie: String = ""
    var userInfo: FlarumDataModel.UserInfo? = null

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