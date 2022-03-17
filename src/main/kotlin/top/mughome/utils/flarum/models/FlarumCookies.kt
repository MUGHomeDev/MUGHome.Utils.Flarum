package top.mughome.utils.flarum.models

/**
 * Flarum的Cookie返回值
 * @author Yang
 * @version 0.0.4
 * @since 0.0.4
 */
interface FlarumCookies {
    var sessionCookie: String
    var rememberCookie: String
    var csrfToken: String
}