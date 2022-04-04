package top.mughome.utils.flarum.models

/**
 * Flarum Included数据
 * @author Yang
 * @version 0.0.5
 * @since 0.0.5
 * @see BasicModel
 */
abstract class Included {
    /**
     * 包含的数据
     * @author Yang
     * @see BasicModel
     * @version 0.0.5
     * @since 0.0.5
     */
    open lateinit var included: MutableList<BasicModel>

    /**
     * 获取一组依赖于BasicModel的数据
     * @author Yang
     * @see BasicModel
     * @version 0.0.5
     * @since 0.0.5
     */
    inline fun <reified T : BasicModel> getModel(): List<T> {
        return included.filterIsInstance(T::class.java)
    }
}