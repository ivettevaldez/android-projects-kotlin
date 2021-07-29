package com.ivettevaldez.saturnus.people.clienttype

import java.io.Serializable

object ClientType {

    private const val CLIENT_ISSUING = "Emisor"
    private const val CLIENT_RECEIVER = "Receptor"

    private const val POSITION_ISSUING: Int = 1
    private const val POSITION_RECEIVER: Int = 2

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
            else -> throw RuntimeException("@@@@@ Unsupported type: $type")
        }
    }

    fun getPosition(type: String): Int {
        return when (type) {
            CLIENT_ISSUING -> POSITION_ISSUING
            CLIENT_RECEIVER -> POSITION_RECEIVER
            else -> throw RuntimeException("@@@@@ Unsupported type: $type")
        }
    }
}