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