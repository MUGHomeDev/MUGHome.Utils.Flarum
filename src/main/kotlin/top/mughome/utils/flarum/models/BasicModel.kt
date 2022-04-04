package top.mughome.utils.flarum.models

/**
 * Flarum基础数据类型
 * @author Yang
 * @version 0.0.5
 * @since 0.0.5
 * @see ModelType
 * @see User
 * @see Discussion
 */
interface BasicModel {
    /**
     * id
     */
    var id: Int

    /**
     * 类型
     */
    val type: ModelType

    /**
     * 需重写 toString 方法
     */
    override fun toString(): String
}

/**
 * Flarum基础数据类别
 * @author Yang
 * @version 0.0.5
 * @since 0.0.5
 */
enum class ModelType {
    /**
     * 用户
     */
    USER,

    /**
     * 帖子
     */
    DISCUSSION,

    /**
     * 回复
     */
    POST
}