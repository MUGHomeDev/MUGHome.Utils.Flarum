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