import top.mughome.utils.flarum.Flarum

suspend fun main() {
    Flarum.init("https://bbs.mughome.top/", "TestClient/1.0.0", "https://bbs.mughome.top/assets/covers/")
    val manager = Flarum.accountManager
    manager.login("Yang", "yang1119", remember = true)
    println(manager.toString())
    manager.logout(manager.token)
    println(manager.toString())
    val anotherManager = Flarum.accountManager
    println(anotherManager.toString())
}