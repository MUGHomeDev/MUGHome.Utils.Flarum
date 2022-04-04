package top.mughome.utils.flarum.managers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import top.mughome.utils.flarum.Flarum
import top.mughome.utils.flarum.models.*
import top.mughome.utils.flarum.utils.FlarumGetter
import top.mughome.utils.flarum.utils.FlarumParsers
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

/**
 * Flarum Discussion相关操作类，使用时需初始化为实例，返回值通过调用实例字段获取
 * @author Yang
 * @version 0.0.5
 * @since 0.0.5
 */
class DiscussionManager(private val cookie: Cookies, private var csrfToken: String) : Discussion, Included() {
    /**
     * 实例内使用字段 FlarumGetter
     * @see FlarumGetter
     */
    private val getter = FlarumGetter(cookie.sessionCookie)

    /**
     * 实例内使用字段 OkHttpClient
     */
    private val client = OkHttpClient()

    /**
     * 初始化接口字段
     */
    override val type = ModelType.DISCUSSION
    override var id: Int = 0

    override var title: String = ""
    override var postUserId: Int = 0
    override var discussionCreateDate: String = ""
    override var discussionLastPostedDate: String = ""
    override var discussionCreateStamp: Long = 0L
    override var discussionLastPostedStamp: Long = 0L
    override var commentCount: Int = 0
    override var viewCount: Int = 0

    override var included: MutableList<BasicModel> = mutableListOf()

    /**
     * 清空数据
     * @return 返回一个空的实例
     * @author Yang
     * @version 0.0.5
     * @since 0.0.5
     */
    fun clear(): DiscussionManager {
        id = 0
        title = ""
        postUserId = 0
        discussionCreateDate = ""
        discussionLastPostedDate = ""
        discussionCreateStamp = 0L
        discussionLastPostedStamp = 0L
        commentCount = 0
        viewCount = 0
        return this
    }

    /**
     * 获取一个Discussion Not Done Yet
     * @author Yang
     * @version 0.0.5
     * @since 0.0.5
     */
    fun getDiscussion(id: Int) {
        throw NotImplementedError()
    }

    /**
     * 创建一个新的Discussion
     * @author Yang
     * @param title Discussion标题
     * @param content Discussion内容
     * @param tags Discussion标签
     * @return 该方法返回值为Boolean，若成功即为true，失败即为false
     * @throws MalformedURLException 由URL产生，若产生，检查URL链接是否可用
     * @throws JSONException 由JSONObject实例的put方法产生，若产生，检查该lib源码
     * @throws NullPointerException 由response.body!!产生，若产生，检查response.body是否为null
     * @throws IOException 由response.body!!.string()产生，若产生，检查response.body是否不能转换为字符串
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @version 0.0.5
     * @since 0.0.5
     */
    suspend fun createDiscussion(title: String, content: String, tags: List<String>): Boolean {
        val url = URL(Flarum.baseUrl + "api/discussions")

        val discussionPostJson = JSONObject()
            .put(
                "data",
                JSONObject()
                    .put(
                        "relationships",
                        JSONObject()
                            .put(
                                "tags",
                                JSONObject()
                                    .put("data", tags.map {
                                        JSONObject()
                                            .put("type", "tags")
                                            .put("id", it)
                                    })
                            )
                    )
                    .put(
                        "attributes",
                        JSONObject()
                            .put("title", title)
                            .put("content", content)
                    )
                    .put("type", "discussions")
            )

        println(discussionPostJson.toString())

        val response = post(url, discussionPostJson)
        if (response.code != 201) {
            response.close()
            return false
        }

        csrfToken = response.headers["x-csrf-token"].toString()

        FlarumParsers.parseDiscussion(
            com.alibaba.fastjson.JSONObject.parseObject(response.body?.string().toString()),
            this
        )
        response.close()
        return true
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
                        "cookie", cookie.sessionCookie,
                        "Accept-Language", "zh-CN,zh;q=0.9",
                        "X-CSRF-Token", csrfToken,
                        "origin", Flarum.baseUrl
                    )
                )
                .post(
                    outputData.toString()
                        .toRequestBody("application/json".toMediaTypeOrNull())
                )
                .build()
        ).execute()
    }

    @Throws(IOException::class, IllegalStateException::class)
    private suspend fun get(
        url: URL,
    ) = withContext(Dispatchers.IO) {
        return@withContext client.newCall(
            Request.Builder()
                .url(url)
                .headers(
                    Headers.headersOf(
                        "User-Agent", Flarum.userAgent,
                        "cookie", cookie.sessionCookie,
                        "Content-Type", "application/json",
                    )
                )
                .get()
                .build()
        ).execute()
    }

    override fun toString(): String {
        return "[AccountManager: BasicModel: (type: $type, id: $id), Discussion: (title: $title, postUserId: $postUserId, discussionCreateDate: $discussionCreateDate, discussionCreateStamp: $discussionCreateStamp, discussionLastPostedDate: $discussionLastPostedDate, discussionLastPostedStamp: $discussionLastPostedStamp, commentCount: $commentCount, viewCount: $viewCount), Included: (included: $included)]"
    }
}