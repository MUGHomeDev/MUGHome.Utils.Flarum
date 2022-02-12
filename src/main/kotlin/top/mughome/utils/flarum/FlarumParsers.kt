package top.mughome.utils.flarum

import com.alibaba.fastjson.JSONObject
import java.text.ParseException
import java.util.regex.Pattern
import java.util.regex.PatternSyntaxException

/**
 * Flarum解析静态类
 * @author Yang
 * @version 0.0.1-SNAPSHOT
 * @since 0.0.1-SNAPSHOT
 */
object FlarumParsers {

    /**
     * 解析带有User和Tag作为Included的Discussion JSONObject
     * @author Yang
     * @param json 带有User和Tag作为Included的Discussion JSONObject
     * @return 该方法返回值为Boolean，成功为true，失败自行捕捉Exception
     * @throws NullPointerException 由DateConverter产生
     * @throws IllegalArgumentException 由DateConverter产生
     * @throws ParseException 由DateConverter产生
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(NullPointerException::class, IllegalArgumentException::class, ParseException::class)
    fun parseDiscussionWithUT(json: JSONObject): Boolean {
        val linksJson = json.getJSONObject("links")
        val dataJson = json.getJSONArray("data")
        val includedJson = json.getJSONArray("included")

        GlobalTempData.nextDiscussionLink = linksJson.getString("next")

        dataJson.forEach {
            it as JSONObject
            if (it.getString("type") == "discussions") {
                val id = it.getString("id").toInt()
                if (!GlobalTempData.discussionsInfo.containsKey(id)) {
                    val attributes = it.getJSONObject("attributes")
                    val createStamp =
                        DateConverter.dateToStamp(attributes.getString("createdAt"))
                    val lPostStamp =
                        DateConverter.dateToStamp(attributes.getString("lastPostedAt"))

                    val discussion = FlarumDataModel.DiscussionInfo(
                        id = id,
                        title = attributes.getString("title"),
                        discussionCreateStamp = createStamp,
                        discussionLastPostedStamp = lPostStamp,
                        discussionCreateDate = DateConverter.stampToDate(createStamp),
                        discussionLastPostedDate = DateConverter.stampToDate(lPostStamp),
                        commentCount = attributes.getInteger("commentCount"),
                        viewCount = attributes.getInteger("views"),
                        postUserId = it.getJSONObject("relationships").getJSONObject("user")
                            .getJSONObject("data").getString("id").toInt()
                    )
                    GlobalTempData.discussionsInfo[id] = discussion
                }
            }
        }

        includedJson.forEach {
            it as JSONObject
            if (it.getString("type") == "users") {
                val id = it.getString("id").toInt()
                if (!GlobalTempData.usersInfo.containsKey(id)) {
                    val attributes = it.getJSONObject("attributes")
                    val joinTimeStamp = DateConverter.dateToStamp(
                        it.getJSONObject("attributes").getString("joinTime")
                    )
                    val joinTime = DateConverter.stampToDate(joinTimeStamp)

                    val user = FlarumDataModel.UserInfo(
                        id = id,
                        username = attributes.getString("username"),
                        avatarUrl = attributes.getString("avatarUrl"),
                        displayName = attributes.getString("displayName"),
                        joinTimeStamp = joinTimeStamp,
                        joinTime = joinTime
                    )
                    GlobalTempData.usersInfo[id] = user
                }
            }
        }
        return true
    }

    /**
     * 解析带有DiscussionDetail的JSONObject
     * @author Yang
     * @param json 带有DiscussionDetail的JSONObject
     * @return 该方法返回值为Boolean，成功为true，失败自行捕捉Exception
     * @throws NullPointerException 由DateConverter产生
     * @throws IllegalArgumentException 由DateConverter产生
     * @throws ParseException 由DateConverter产生
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(NullPointerException::class, IllegalArgumentException::class, ParseException::class)
    fun parseDiscussionDetail(json: JSONObject, discussionId: Int): Boolean {
        val dataJson = json.getJSONArray("data")
        val includedJson = json.getJSONArray("included")

        dataJson.forEach {
            it as JSONObject
            val number = it.getJSONObject("attributes").getInteger("number")
            val id = it.getString("id").toInt()
            if (!GlobalTempData.postsInfo.containsKey(id) && it.getJSONObject("attributes")
                    .getString("contentType") == "comment"
            ) {
                val contentHtml = it.getJSONObject("attributes").getString("contentHtml")
                val postCreateStamp = DateConverter.dateToStamp(
                    it.getJSONObject("attributes").getString("createdAt")
                )

                val post = FlarumDataModel.PostInfo(
                    id = id,
                    postContentHtml = contentHtml,
                    postCreateTimeStamp = postCreateStamp,
                    postCreateTime = DateConverter.stampToDate(postCreateStamp),
                    postUserId = it.getJSONObject("relationships").getJSONObject("user")
                        .getJSONObject("data").getString("id").toInt()
                )
                GlobalTempData.postsInfo[id] = post

                if (!GlobalTempData.discussionsInfo[discussionId]?.postInfoList?.containsKey(
                        number
                    )!!
                ) {
                    GlobalTempData.discussionsInfo[discussionId]?.postInfoList?.put(number, id)
                }
            }
        }

        includedJson.forEach {
            it as JSONObject
            if (it.getString("type") == "users" && !GlobalTempData.usersInfo.containsKey(
                    it.getString(
                        "id"
                    ).toInt()
                )
            ) {
                val joinTimeStamp = DateConverter.dateToStamp(
                    it.getJSONObject("attributes").getString("joinTime")
                )
                val id = it.getString("id").toInt()
                val user = FlarumDataModel.UserInfo(
                    id = id,
                    username = it.getJSONObject("attributes").getString("username"),
                    avatarUrl = it.getJSONObject("attributes").getString("avatarUrl"),
                    displayName = it.getJSONObject("attributes").getString("displayName"),
                    joinTimeStamp = joinTimeStamp,
                    joinTime = DateConverter.stampToDate(joinTimeStamp)
                )
                GlobalTempData.usersInfo[id] = user
            }
        }
        return true
    }

    /**
     * 解析带有User的JSONObject
     * @author Yang
     * @param outJson 带有DiscussionDetail的JSONObject
     * @param sessionC 登录时返回的Session Cookie
     * @param token 登录时返回的token
     * @return 该方法返回值为Boolean，成功为true，失败自行捕捉Exception
     * @throws NullPointerException 由DateConverter产生
     * @throws IllegalArgumentException 由DateConverter产生
     * @throws ParseException 由DateConverter产生
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    fun parseUser(outJson: JSONObject, sessionC: String = "", token: String = ""): Boolean {
        val json = outJson.getJSONObject("data")
        val id = json.getString("id").toInt()
        val attributes = json.getJSONObject("attributes")
        val joinTimeStamp =
            DateConverter.dateToStamp(json.getJSONObject("attributes").getString("joinTime"))
        val joinTime = DateConverter.stampToDate(joinTimeStamp)

        val user = FlarumDataModel.UserInfo(
            id = id,
            username = attributes.getString("username"),
            avatarUrl = attributes.getString("avatarUrl"),
            displayName = attributes.getString("displayName"),
            joinTimeStamp = joinTimeStamp,
            joinTime = joinTime,
            bgUrl = "${Flarum.coverPrefix}${attributes.getString("cover")}",
            description = attributes.getString("bio"),
            sessionC = sessionC,
            token = token
        )
        GlobalTempData.userInfo = user
        return true
    }

    /**
     * 解析下载HTML链接
     * @author Yang
     * @param oldHtml 旧HTML
     * @param postId Post的id
     * @param CSRFToken 校验用X-CSRF-Token
     * @return 返回一个下载链接String
     * @throws PatternSyntaxException 由正则解析产生，若产生，检查HTML是否合法
     * @throws IllegalStateException 由正则解析产生，若产生，检查该lib源码
     * @version 0.0.1-SNAPSHOT
     * @since 0.0.1-SNAPSHOT
     */
    @Throws(PatternSyntaxException::class, IllegalStateException::class)
    fun parseHtmlDLink(oldHtml: String, postId: Int, CSRFToken: String): String {
        val patternLeft = "<div class=\"ButtonGroup\" data-fof-upload-download-uuid=\""
        val patternRight = "\"><div class=\"Button hasIcon Button--icon Button--primary\">"
        val filePatternLeft = "<div class=\"Button\">"
        val filePatternRight = "</div>"
        var newHTMLData = ""
        val regForUUID = "(?<=$patternLeft).*?(?=$patternRight)"
        val regForFileNameAndSize = "(?<=$filePatternLeft).*?(?=$filePatternRight)"
        val patternForUUID = Pattern.compile(regForUUID)
        val patternForFileNameAndSize = Pattern.compile(regForFileNameAndSize)
        val matcherForUUID = patternForUUID.matcher(oldHtml)
        val matcherForFileNameAndSize = patternForFileNameAndSize.matcher(oldHtml)
        while (matcherForUUID.find()) {
            val uuid = matcherForUUID.group()
            val downloadLink =
                "https://bbs.mughome.top/api/fof/download/$uuid/$postId/$CSRFToken"
            matcherForFileNameAndSize.find()
            val downLoadFileName = matcherForFileNameAndSize.group()
            matcherForFileNameAndSize.find()
            val downLoadFileSize = matcherForFileNameAndSize.group()
            newHTMLData = oldHtml.replace(
                patternLeft + uuid + patternRight +
                        "<i class=\"fas fa-download\"></i></div>" +
                        filePatternLeft + downLoadFileName + filePatternRight +
                        filePatternLeft + downLoadFileSize + filePatternRight,
                "<a href=\"$downloadLink\" target=\"_blank\">【下载文件:$downLoadFileName-|-$downLoadFileSize】</a>"
            )
        }
        return if (newHTMLData == "") {
            oldHtml
        } else {
            newHTMLData
        }
    }
}