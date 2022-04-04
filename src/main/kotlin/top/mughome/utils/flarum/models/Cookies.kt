package top.mughome.utils.flarum.models

/**
 * Flarum的Cookie返回值
 * @author Yang
 * @version 0.0.5
 * @since 0.0.4
 */
interface Cookies {
    /**
     * Session Cookie
     */
    var sessionCookie: String

    /**
     * RememberCookie
     */
    var rememberCookie: String

    /**
     * X-CSRF-Token
     */
    var csrfToken: String
}