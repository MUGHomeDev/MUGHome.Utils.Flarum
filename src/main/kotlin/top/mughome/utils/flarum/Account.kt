package top.mughome.utils.flarum

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

/**
 * Flarum账号相关操作类，使用时需初始化为实例，返回值通过调用实例字段获取
 * @author Yang
 * @version 0.0.1-SNAPSHOT
 * @since 0.0.1-SNAPSHOT
 */
class Account {
    /**
     * 返回值：错误点
     */
    var wrongPoints = mutableListOf<String>()

    /**
     * 返回值：错误信息
     */
    var errorMessages = mutableListOf<String>()

    /**
     * 实例内使用字段：适用于Flarum的session cookie
     */
    private var sessionCookie: SessionCookie? = null

    /**
     * 返回值：Flarum登陆成功后的返回值
     */
    var flarumLogin: FlarumLogin? = null

    /**
     * 返回值：Flarum注册成功后的返回值
     */
    var flarumRegister: FlarumRegister? = null

    /**
     * 返回值：获取到的随机cookie以及CSRF-Token
     */
    var cookieAndCSRFToken: CookieAndCSRFToken? = null

    /**
     * 实例内使用字段：OkHttpClient实例，用于发送HTTP Request
     */
    private val client = OkHttpClient()

    /**
     * Flarum的Session Cookie返回值数据类型
     * @author Yang
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    data class SessionCookie(
        val value: String
    )

    /**
     * Flarum的Remember Cookie返回值数据类型
     * @author Yang
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    data class RememberCookie(
        val value: String
    )

    /**
     * Flarum登录成功后的返回值数据类型。包含Session Cookie，uid，Remember Cookie（如果登录时remember = true）
     * @author Yang
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    data class FlarumLogin(
        val userId: Int,
        val token: String,
        val sessionCookie: SessionCookie,
        val rememberCookie: RememberCookie? = null
    )

    /**
     * Flarum注册成功后的返回值数据类型。包含Session Cookie，uid，Remember Cookie
     * @author Yang
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    data class FlarumRegister(
        val userId: Int,
        val sessionCookie: SessionCookie,
        val rememberCookie: RememberCookie
    )

    /**
     * 获取的随机Session Cookie和CSRF-Token数据类型
     * @author Yang
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    data class CookieAndCSRFToken(
        val sessionCookie: SessionCookie,
        val csrfToken: String
    )

    /**
     * Flarum账号登录
     * @author Yang
     * @param username 用户名
     * @param passwd 密码
     * @param sessionC 已有的Session Cookie
     * @param csrfToken 校验用X-CSRF-Token
     * @param remember 保存密码，默认为false
     * @return 该方法返回值为Boolean，若成功即为true，失败即为false
     * @throws MalformedURLException 由URL产生，若产生，检查URL链接是否可用
     * @throws JSONException 由JSONObject实例的put方法产生，若产生，检查该lib源码
     * @throws NullPointerException 由response.body!!产生，若产生，检查response.body是否为null
     * @throws IOException 由response.body!!.string()产生，若产生，检查response.body是否不能转换为字符串
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(
        MalformedURLException::class,
        JSONException::class,
        IOException::class,
        NullPointerException::class,
        IllegalStateException::class
    )
    suspend fun login(
        username: String,
        passwd: String,
        sessionC: String,
        csrfToken: String,
        remember: Boolean = false
    ): Boolean {
        flarumLogin = null
        wrongPoints.clear()
        errorMessages.clear()

        val url = URL(Flarum.baseUrl + "login")
        val json = JSONObject()
            .put("remember", remember)
            .put("password", passwd)
            .put("identification", username)
        val response = post(url, SessionCookie(sessionC), csrfToken, json)
        while (true) {
            if (response.body != null) {
                break
            }
        }
        if (response.code != 200) {
            response.close(); return false
        }

        val inputData = JSONObject(response.body!!.string())
        val uid = inputData.getInt("userId")
        val token = inputData.getString("token")
        if (!remember) {
            val session = response.headers["set-cookie"]
            if (session != null) {
                flarumLogin = FlarumLogin(
                    userId = uid,
                    token = token,
                    sessionCookie = SessionCookie(session.substring(0, session.indexOf(";"))),
                )
            }
        } else {
            val headers = response.headers("set-cookie")
            var sessionCNew = SessionCookie("")
            var rememberC = RememberCookie("")
            headers.forEach {
                when (it.substring(0, it.indexOf("="))) {
                    "flarum_session" -> sessionCNew =
                        SessionCookie(it.substring(0, it.indexOf(";")))
                    "flarum_remember" -> rememberC =
                        RememberCookie(it.substring(0, it.indexOf(";")))
                }
            }
            flarumLogin = FlarumLogin(
                sessionCookie = sessionCNew,
                rememberCookie = rememberC,
                userId = uid,
                token = token
            )
        }
        response.close()
        return true
    }

    /**
     * Flarum账号注册
     * @author Yang
     * @param username 用户名
     * @param email 用户邮箱
     * @param passwd 密码
     * @param policy 用户是否同意站点政策
     * @param sessionC 已有的Session Cookie
     * @param csrfToken 校验用X-CSRF-Token
     * @param doorkey 邀请码，默认为null
     * @return 该方法返回值为Boolean，若成功即为true，失败即为false
     * @throws MalformedURLException 由URL产生，若产生，检查URL链接是否可用
     * @throws JSONException 由JSONObject实例的put方法产生，若产生，检查该lib源码
     * @throws NullPointerException 由response.body!!产生，若产生，检查response.body是否为null
     * @throws IOException 由response.body!!.string()产生，若产生，检查response.body是否不能转换为字符串
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(
        MalformedURLException::class,
        JSONException::class,
        IOException::class,
        NullPointerException::class,
        IllegalStateException::class
    )
    suspend fun register(
        username: String,
        email: String,
        passwd: String,
        policy: Boolean,
        sessionC: String,
        csrfToken: String,
        doorkey: String? = null
    ): Boolean {
        flarumRegister = null
        wrongPoints.clear()
        errorMessages.clear()

        val url = URL(Flarum.baseUrl + "register")
        val json = JSONObject()
            .put("username", username)
            .put("email", email)
            .put("password", passwd)
            .put("fof_terms_policy_1", policy)
            .put("fof-doorkey", doorkey)
        val response = post(url, SessionCookie(sessionC), csrfToken, json)
        while (true) {
            if (response.body != null) {
                break
            }
        }
        val inputData = JSONObject(response.body!!.string())
        if (response.code != 200) {
            if (inputData.has("errors")) {
                val errArray = inputData.getJSONArray("errors")
                if (errArray.getJSONObject(0).getInt("status") == 422) {
                    for (i in 0 until errArray.length()) {
                        val it = errArray.getJSONObject(i)
                        val point = it.getJSONObject("source").getString("pointer")
                        errorMessages.add(it.getString("detail"))
                        when (point) {
                            "/data/attributes/fof-doorkey" -> wrongPoints.add("Doorkey")
                            "/data/attributes/password" -> wrongPoints.add("Password")
                            "/data/attributes/email" -> wrongPoints.add("Email")
                            "/data/attributes/username" -> wrongPoints.add("Username")
                            "/data/attributes/fof_terms_policy_1" -> wrongPoints.add("Policy")
                        }
                    }
                }
            }
            response.close()
            return false
        }

        val uid = inputData.getJSONObject("data").getInt("id")

        val headers = response.headers("set-cookie")
        var sessionCNew = SessionCookie("")
        var rememberC = RememberCookie("")
        headers.forEach {
            when (it.substring(0, it.indexOf("="))) {
                "flarum_session" -> sessionCNew =
                    SessionCookie(it.substring(0, it.indexOf(";")))
                "flarum_remember" -> rememberC =
                    RememberCookie(it.substring(0, it.indexOf(";")))
            }
        }
        flarumRegister = FlarumRegister(
            sessionCookie = sessionCNew,
            rememberCookie = rememberC,
            userId = uid
        )
        response.close()
        return true
    }

    /**
     * Flarum账号忘记密码
     * @author Yang
     * @param email 用户邮箱
     * @param sessionC 已有的Session Cookie
     * @return 该方法返回值为Boolean，若成功即为true，失败即为false
     * @throws MalformedURLException 由URL产生，若产生，检查URL链接是否可用
     * @throws JSONException 由JSONObject实例的put方法产生，若产生，检查该lib源码
     * @throws IOException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(
        MalformedURLException::class,
        JSONException::class,
        IOException::class,
        IllegalStateException::class
    )
    suspend fun forgetPassword(
        email: String,
        sessionC: String
    ): Boolean {
        sessionCookie = null

        val url = URL(Flarum.baseUrl + "api/forget")
        val json = JSONObject().put("email", email)
        val response = post(url, SessionCookie(sessionC), outputData = json)
        while (true) {
            if (response.body != null) {
                break
            }
        }
        if (response.code != 200) return false

        val s = response.headers["set-cookie"]
        if (s != null) sessionCookie = SessionCookie(s.substring(0, s.indexOf(";")))
        response.close()
        return true
    }

    /**
     * Flarum账号登出
     * @author Yang
     * @param token 已有的用户token，登出后失效
     * @return 该方法返回值为Boolean，若成功即为true，失败即为false
     * @throws MalformedURLException 由URL产生，若产生，检查URL链接是否可用
     * @throws IOException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(MalformedURLException::class, IOException::class, IllegalStateException::class)
    suspend fun logout(token: String): Boolean {
        sessionCookie = null

        val url = URL(Flarum.baseUrl + "logout?token=$token")
        val response = get(url)
        while (true) {
            if (response.body != null) {
                break
            }
        }
        if (response.code != 200) return false

        val s = response.headers["set-cookie"]
        if (s != null) sessionCookie = SessionCookie(s.substring(0, s.indexOf(";")))
        response.close()
        return true
    }

    /**
     * 获取随机Session Cookie及CSRF-Token
     * @author Yang
     * @return 该方法返回值为Boolean，若成功即为true，失败即为false
     * @throws MalformedURLException 由URL产生，若产生，检查URL链接是否可用
     * @throws IOException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws NullPointerException 若cookie为空产生，若产生，检查该lib源码
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(MalformedURLException::class, IOException::class, IllegalStateException::class)
    suspend fun getCookieAndCSRFToken(): Boolean {
        val url = URL(Flarum.baseUrl)
        val response = get(url)
        while (true) {
            if (response.body != null) {
                break
            }
        }
        return if (response.code == 200) {
            val sessionC = response.headers["set-cookie"]
            val csrfToken = response.headers["x-csrf-token"]
            cookieAndCSRFToken = CookieAndCSRFToken(
                sessionCookie = SessionCookie(sessionC!!.substring(0, sessionC.indexOf(";"))),
                csrfToken = csrfToken!!
            )
            true
        } else {
            false
        }
    }

    @Throws(IOException::class, IllegalStateException::class)
    private suspend fun get(
        url: URL,
        sessionC: String = ""
    ) = withContext(Dispatchers.IO) {
        return@withContext client.newCall(
            Request.Builder()
                .url(url)
                .headers(
                    Headers.headersOf(
                        "User-Agent", Flarum.userAgent,
                        "cookie", sessionC,
                        "Content-Type", "application/json",
                    )
                )
                .get()
                .build()
        ).execute()
    }

    @Throws(IOException::class, IllegalStateException::class)
    private suspend fun post(
        url: URL,
        sessionC: SessionCookie,
        csrfToken: String = "",
        outputData: JSONObject
    ) = withContext(Dispatchers.IO) {
        return@withContext client.newCall(
            Request.Builder()
                .url(url)
                .headers(
                    Headers.headersOf(
                        "User-Agent", Flarum.userAgent,
                        "Content-Type", "application/json; charset=utf-8",
                        "cookie", sessionC.value,
                        "Accept-Language", "zh-CN,zh;q=0.9",
                        "X-CSRF-Token", csrfToken
                    )
                )
                .post(
                    outputData.toString()
                        .toRequestBody("application/json".toMediaTypeOrNull())
                )
                .build()
        ).execute()
    }
}
