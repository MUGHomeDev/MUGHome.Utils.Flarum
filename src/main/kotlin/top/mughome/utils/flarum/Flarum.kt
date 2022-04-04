package top.mughome.utils.flarum

import top.mughome.utils.flarum.managers.AccountManager

/**
 * Flarum初始化类，使用前请先初始化
 * @author Yang
 * @version 0.0.5
 * @since 0.0.1-SNAPSHOT
 */
object Flarum {
    /**
     * 初始化用：Flarum链接
     */
    lateinit var baseUrl: String

    /**
     * 初始化用：User-Agent
     */
    lateinit var userAgent: String

    /**
     * 初始化用：用户背景图链接前缀
     */
    lateinit var coverPrefix: String

    /**
     * 存储的accountManager实例
     */
    val accountManager: AccountManager = AccountManager()
        get() = field.clear()

    /**
     * 初始化
     * @author Yang
     * @param baseUrl Flarum链接
     * @param userAgent User-Agent
     * @param coverPrefix 用户背景图链接前缀
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    fun init(baseUrl: String, userAgent: String, coverPrefix: String) {
        this.baseUrl = baseUrl
        this.userAgent = userAgent
        this.coverPrefix = coverPrefix
    }
}