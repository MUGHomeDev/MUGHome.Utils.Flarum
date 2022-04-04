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