package top.mughome.utils.flarum.managers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import top.mughome.utils.flarum.Flarum
import top.mughome.utils.flarum.models.*
import top.mughome.utils.flarum.utils.FlarumGetter
import top.mughome.utils.flarum.utils.FlarumParsers
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class DiscussionManager(private val cookie: Cookies, private var csrfToken: String) : Discussion, Included() {
    private val trustAllCerts = arrayOf<TrustManager>(
        object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<X509Certificate?>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<X509Certificate?>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }
    )

    private val sslContext: SSLContext = SSLContext.getInstance("SSL")
    private var sslSocketFactory: SSLSocketFactory

    init {
        sslContext.init(null, trustAllCerts, java.security.SecureRandom())
        sslSocketFactory = sslContext.socketFactory
    }

    private val getter = FlarumGetter(cookie.sessionCookie)
    private val client = OkHttpClient.Builder()
        .proxy(
            Proxy(
                Proxy.Type.HTTP,
                InetSocketAddress("127.0.0.1", 11451)
            )
        )
        .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
        .build()

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

    fun getDiscussion(id: Int) {
        throw NotImplementedError()
    }

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