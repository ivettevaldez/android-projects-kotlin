@file:JvmName("Log")

package android.util

fun e(tag: String, message: String, throwable: Throwable): Int {
    println("ERROR - $tag: $message")
    throwable.printStackTrace()
    return 0
}

fun e(tag: String, message: String): Int {
    println("ERROR - $tag: $message")
    return 0
}

fun w(tag: String, message: String): Int {
    println("WARNING - $tag: $message")
    return 0
}

fun i(tag: String, message: String): Int {
    println("INFO - $tag: $message")
    return 0
}