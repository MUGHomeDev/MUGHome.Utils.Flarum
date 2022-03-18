package top.mughome.utils.flarum.models

/**
 * FlarumUser数据类型
 * @author Yang
 * @version 0.0.4
 * @since 0.0.4
 */
interface User {
    var id: Int
    var username: String
    var displayName: String
    var avatarUrl: String
    var bgUrl: String
    var description: String
    var joinTime: String
    var joinTimeStamp: Long
    var token: String
    var sessionC: String
}