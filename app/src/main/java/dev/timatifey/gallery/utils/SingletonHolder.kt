package dev.timatifey.gallery.utils

open class SingletonHolder<out T : Any, in A>(private val factory: (A) -> T) {
    @Volatile
    private var instance: T? = null

    fun getInstance(arg: A): T {
        val checkInstance = instance
        if (checkInstance != null) {
            return checkInstance
        }

        return synchronized(this) {
            val checkInstanceAgain = instance
            if (checkInstanceAgain != null) {
                checkInstanceAgain
            } else {
                val created = factory(arg)
                instance = created
                created
            }
        }
    }
}