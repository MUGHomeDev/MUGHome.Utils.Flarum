package top.mughome.utils.flarum.models

/**
 * FlarumDiscussion数据类型
 * @author Yang
 * @version 0.0.5
 * @since 0.0.5
 */
interface Discussion : BasicModel {
    var title: String
    var postUserId: Int
    var discussionCreateDate: String
    var discussionLastPostedDate: String
    var discussionCreateStamp: Long
    var discussionLastPostedStamp: Long
    var commentCount: Int
    var viewCount: Int
}