package top.mughome.utils.flarum.models

interface BasicModel {
    var id: Int
    val type: ModelType
    override fun toString(): String
}

enum class ModelType {
    USER,
    DISCUSSION,
    POST
}