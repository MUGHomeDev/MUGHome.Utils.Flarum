package top.mughome.utils.flarum.managers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import top.mughome.utils.flarum.Flarum
import top.mughome.utils.flarum.models.Cookies
import top.mughome.utils.flarum.models.ModelType
import top.mughome.utils.flarum.models.User
import top.mughome.utils.flarum.utils.FlarumGetter
import top.mughome.utils.flarum.utils.FlarumParsers
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

/**
 * Flarum账号相关操作类，使用时需初始化为实例，返回值通过调用实例字段获取
 * @author Yang
 * @version 0.0.5
 * @since 0.0.1-SNAPSHOT
 */
class AccountManager : User, Cookies {
    /**
     * 返回值：错误点
     */
    var wrongPoints = mutableListOf<String>()

    /**
     * 返回值：错误信息
     */
    var errorMessages = mutableListOf<String>()

    /**
     * 实例内使用字段：OkHttpClient实例，用于发送HTTP Request
     */
    private val client = OkHttpClient()

    /**
     * 初始化接口字段
     */
    override val type = ModelType.USER
    override var id: Int = 0
    override var username: String = ""
    override var displayName: String = ""
    override var avatarUrl: String = ""
    override var bgUrl: String = ""
    override var description: String = ""
    override var joinTime: String = ""
    override var joinTimeStamp: Long = 0L
    override var token: String = ""
    override var sessionC: String = ""

    override var sessionCookie: String = ""
    override var csrfToken: String = ""
    override var rememberCookie: String = ""

    /**
     * 清空数据
     * @return 返回一个空的实例
     * @author Yang
     * @version 0.0.5
     * @since 0.0.4
     */
    fun clear(): AccountManager {
        wrongPoints.clear()
        errorMessages.clear()

        id = 0
        username = ""
        displayName = ""
        avatarUrl = ""
        bgUrl = ""
        description = ""
        joinTime = ""
        joinTimeStamp = 0L
        token = ""

        sessionCookie = ""
        csrfToken = ""
        rememberCookie = ""

        return this
    }

    /**
     * Flarum账号登录
     * @author Yang
     * @param username 用户名
     * @param passwd 密码
     * @param remember 保存密码，默认为false
     * @return 该方法返回值为Boolean，若成功即为true，失败即为false
     * @throws MalformedURLException 由URL产生，若产生，检查URL链接是否可用
     * @throws JSONException 由JSONObject实例的put方法产生，若产生，检查该lib源码
     * @throws NullPointerException 由response.body!!产生，若产生，检查response.body是否为null
     * @throws IOException 由response.body!!.string()产生，若产生，检查response.body是否不能转换为字符串
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @version 0.0.4
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
        remember: Boolean = false
    ): Boolean {
        wrongPoints.clear()
        errorMessages.clear()

        getCookieAndCSRFToken()

        val url = URL(Flarum.baseUrl + "login")
        val json = JSONObject()
            .put("remember", remember)
            .put("password", passwd)
            .put("identification", username)
        val response = post(url, json)
        while (true) {
            if (response.body != null) {
                break
            }
        }
        if (response.code != 200) {
            response.close(); return false
        }

        val inputData = JSONObject(response.body!!.string())
        id = inputData.getInt("userId")
        this.token = inputData.getString("token")
        if (!remember) {
            getSessionCookie(response)
        } else {
            getRememberCookie(response)
        }

        csrfToken = response.headers["x-csrf-token"].toString()

        val userJson = FlarumGetter().getUser(id)
        FlarumParsers.parseUser(userJson, this)

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
     * @param doorkey 邀请码，默认为null
     * @return 该方法返回值为Boolean，若成功即为true，失败即为false
     * @throws MalformedURLException 由URL产生，若产生，检查URL链接是否可用
     * @throws JSONException 由JSONObject实例的put方法产生，若产生，检查该lib源码
     * @throws NullPointerException 由response.body!!产生，若产生，检查response.body是否为null
     * @throws IOException 由response.body!!.string()产生，若产生，检查response.body是否不能转换为字符串
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @version 0.0.4
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
        doorkey: String? = null
    ): Boolean {
        wrongPoints.clear()
        errorMessages.clear()

        val url = URL(Flarum.baseUrl + "register")
        val json = JSONObject()
            .put("username", username)
            .put("email", email)
            .put("password", passwd)
            .put("fof_terms_policy_1", policy)
            .put("fof-doorkey", doorkey)
        val response = post(url, json)
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

        getRememberCookie(response)
        id = uid

        val userJson = FlarumGetter().getUser(id)
        FlarumParsers.parseUser(userJson, this)
        response.close()
        return true
    }

    /**
     * Flarum账号忘记密码
     * @author Yang
     * @param email 用户邮箱
     * @return 该方法返回值为Boolean，若成功即为true，失败即为false
     * @throws MalformedURLException 由URL产生，若产生，检查URL链接是否可用
     * @throws JSONException 由JSONObject实例的put方法产生，若产生，检查该lib源码
     * @throws IOException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @version 0.0.4
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
    ): Boolean {
        getCookieAndCSRFToken()

        val url = URL(Flarum.baseUrl + "api/forget")
        val json = JSONObject().put("email", email)
        val response = post(url, json)
        while (true) {
            if (response.body != null) {
                break
            }
        }
        if (response.code != 200) return false

        getSessionCookie(response)
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
     * @version 0.0.4
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(MalformedURLException::class, IOException::class, IllegalStateException::class)
    suspend fun logout(token: String): Boolean {
        clear()
        getCookieAndCSRFToken()
        val url = URL(Flarum.baseUrl + "logout?token=$token")
        val response = get(url)
        while (true) {
            if (response.body != null) {
                break
            }
        }
        if (response.code != 200) return false

        sessionC = ""
        sessionCookie = ""
        rememberCookie = ""
        this.token = ""

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
     * @version 0.0.4
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
            getSessionCookie(response)
            csrfToken = response.headers["x-csrf-token"].toString()
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
        outputData: JSONObject
    ) = withContext(Dispatchers.IO) {
        return@withContext client.newCall(
            Request.Builder()
                .url(url)
                .headers(
                    Headers.headersOf(
                        "User-Agent", Flarum.userAgent,
                        "Content-Type", "application/json; charset=utf-8",
                        "cookie", sessionCookie,
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

    private fun getSessionCookie(r: Response) {
        val s = r.headers["set-cookie"].toString()
        sessionCookie = s.substring(0, s.indexOf(";"))
        sessionC = s.substring(0, s.indexOf(";"))
    }

    private fun getRememberCookie(r: Response) {
        val headers = r.headers("set-cookie")
        headers.forEach {
            when (it.substring(0, it.indexOf("="))) {
                "flarum_session" -> sessionCookie = it.substring(0, it.indexOf(";"))
                "flarum_remember" -> rememberCookie = it.substring(0, it.indexOf(";"))
            }
        }
    }

    override fun toString(): String {
        return "[AccountManager: BasicModel: (type: $type, id: $id), User: (username: $username, displayName: $displayName, avatarUrl: $avatarUrl, bgUrl: $bgUrl, description: $description, joinTime: $joinTime, joinTimeStamp: $joinTimeStamp, token: $token, sessionC: $sessionC), Cookies: (sessionCookie: $sessionCookie, rememberCookie: $rememberCookie, csrfToken: $csrfToken)]"
    }
}
