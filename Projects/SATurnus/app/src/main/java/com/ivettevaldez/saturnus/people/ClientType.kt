package com.ivettevaldez.saturnus.people

import java.io.Serializable

object ClientType {

    private const val CLIENT_ISSUING = "Emisor"
    private const val CLIENT_RECEIVER = "Receptor"

    enum class Type : Serializable {

        ISSUING,
        RECEIVER
    }

    fun getString(type: Type): String {
        return when (type) {
            Type.ISSUING -> CLIENT_ISSUING
            Type.RECEIVER -> CLIENT_RECEIVER
        }
    }

    fun getValue(type: String): Type {
        return when (type) {
            CLIENT_ISSUING -> Type.ISSUING
            CLIENT_RECEIVER -> Type.RECEIVER
            else -> throw RuntimeException("@@@@@ Invalid type: $type")
        }
    }

    fun getPosition(type: String): Int {
        return when (type) {
            CLIENT_ISSUING -> 1
            CLIENT_RECEIVER -> 2
            else -> throw RuntimeException("@@@@@ Invalid type: $type")
        }
    }
}