package top.mughome.utils.flarum

import com.alibaba.fastjson.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

/**
 * 用于获取Flarum相关数据的类，使用时需用Session Cookie（可选）实例化
 * @author Yang
 * @version 0.0.1-SNAPSHOT
 * @since 0.0.1-SNAPSHOT
 */
class FlarumGetter(private val sessionC: String = "") {
    /**
     * 实例内使用字段：List中的添加顺序，用于排序
     */
    private var addedOrder = 0

    /**
     * 伴生类，用于静态方法
     */
    companion object {
        /**
         * 用于清空缓存数据
         * @author Yang
         * @version 0.0.1-SNAPSHOT
         * @since 0.0.1-SNAPSHOT
         */
        fun refreshPostsList() {
            GlobalTempData.postsInfo = LinkedHashMap()
            GlobalTempData.nextDiscussionPostsLink = ""
        }
    }

    /**
     * 实例内使用字段：URL缓存
     */
    private lateinit var url: URL

    /**
     * 实例内使用字段：OkHttpClient，用于发送HTTP Request
     */
    private val client = OkHttpClient()

    /**
     * 刷新讨论列表
     * @author Yang
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    fun refreshDiscussionList() {
        GlobalTempData.discussionsInfo = LinkedHashMap()
        GlobalTempData.usersInfo = LinkedHashMap()
        GlobalTempData.nextDiscussionLink = ""
        addedOrder = 0
    }

    /**
     * 获取Discussion
     * @author Yang
     * @param id Discussion对应id
     * @return 返回一个包含该Discussion的JSONObject
     * @throws MalformedURLException 由URL产生，若产生，检查URL是否可用
     * @throws IOException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws NullPointerException 由强制忽略空值产生，若产生，检查该lib源码
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(
        MalformedURLException::class,
        IOException::class,
        IllegalStateException::class,
        NullPointerException::class
    )
    suspend fun getDiscussion(id: Int): JSONObject {
        url = URL("${Flarum.baseUrl}api/discussions/$id")
        return get()
    }

    /**
     * 获取Post
     * @author Yang
     * @param id Post对应id
     * @return 返回一个包含该Post的JSONObject
     * @throws MalformedURLException 由URL产生，若产生，检查URL是否可用
     * @throws IOException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws NullPointerException 由强制忽略空值产生，若产生，检查该lib源码
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(
        MalformedURLException::class,
        IOException::class,
        IllegalStateException::class,
        NullPointerException::class
    )
    suspend fun getPost(id: Int): JSONObject {
        url = URL("${Flarum.baseUrl}api/posts/$id")
        return get()
    }

    /**
     * 获取User
     * @author Yang
     * @param id User对应id
     * @return 返回一个包含该User的JSONObject
     * @throws MalformedURLException 由URL产生，若产生，检查URL是否可用
     * @throws IOException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws NullPointerException 由强制忽略空值产生，若产生，检查该lib源码
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(
        MalformedURLException::class,
        IOException::class,
        IllegalStateException::class,
        NullPointerException::class
    )
    suspend fun getUser(id: Int): JSONObject {
        url = URL("${Flarum.baseUrl}api/users/$id")
        return get()
    }

    /**
     * 获取Discussions
     * @author Yang
     * @return 返回一个包含Discussions的JSONObject
     * @throws MalformedURLException 由URL产生，若产生，检查URL是否可用
     * @throws IOException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws NullPointerException 由强制忽略空值产生，若产生，检查该lib源码
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(
        MalformedURLException::class,
        IOException::class,
        IllegalStateException::class,
        NullPointerException::class
    )
    suspend fun getDiscussions(): JSONObject {
        url = if (GlobalTempData.nextDiscussionLink == "") {
            URL("${Flarum.baseUrl}api/discussions?include=user,tags&sort&page[offset]=0")
        } else {
            URL(GlobalTempData.nextDiscussionLink)
        }
        return get()
    }

    /**
     * 获取Posts
     * @author Yang
     * @return 返回一个包含Posts的JSONObject
     * @throws MalformedURLException 由URL产生，若产生，检查URL是否可用
     * @throws IOException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws NullPointerException 由强制忽略空值产生，若产生，检查该lib源码
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(
        MalformedURLException::class,
        IOException::class,
        IllegalStateException::class,
        NullPointerException::class
    )
    suspend fun getPosts(): JSONObject {
        url = URL("${Flarum.baseUrl}api/posts")
        return get()
    }

    /**
     * 获取Users
     * @author Yang
     * @return 返回一个包含Users的JSONObject
     * @throws MalformedURLException 由URL产生，若产生，检查URL是否可用
     * @throws IOException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws NullPointerException 由强制忽略空值产生，若产生，检查该lib源码
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(
        MalformedURLException::class,
        IOException::class,
        IllegalStateException::class,
        NullPointerException::class
    )
    suspend fun getUsers(): JSONObject {
        url = URL("${Flarum.baseUrl}api/users")
        return get()
    }

    /**
     * 获取Discussions
     * @author Yang
     * @param id Discussion对应id
     * @return 返回一个包含和所给Discussion相关的所欲Posts的JSONObject
     * @throws MalformedURLException 由URL产生，若产生，检查URL是否可用
     * @throws IOException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws IllegalStateException 由OkHttpClient实例的execute方法产生，若产生，检查该lib源码
     * @throws NullPointerException 由强制忽略空值产生，若产生，检查该lib源码
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(
        MalformedURLException::class,
        IOException::class,
        IllegalStateException::class,
        NullPointerException::class
    )
    suspend fun getPostsFromDiscussionId(id: Int): JSONObject {
        url = URL("${Flarum.baseUrl}api/posts?filter[discussion]=$id")
        return get()
    }

    @Throws(IOException::class, IllegalStateException::class, NullPointerException::class)
    private suspend fun get() = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .headers(
                Headers.headersOf(
                    "User-Agent", Flarum.userAgent,
                    "Content-Type", "application/json",
                    "cookie", sessionC
                )
            )
            .get()
            .build()
        val response = client.newCall(request).execute()
        while (true) {
            if (response.body != null) {
                break
            }
        }
        val responseString = response.body?.string()
        val json = JSONObject.parse(responseString) as JSONObject
        response.close()
        return@withContext json
    }
}