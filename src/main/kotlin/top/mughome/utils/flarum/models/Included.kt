package top.mughome.utils.flarum.models

abstract class Included {
    open lateinit var included: MutableList<BasicModel>
    inline fun <reified T : BasicModel> getModel(): List<T> {
        return included.filterIsInstance(T::class.java)
    }
}