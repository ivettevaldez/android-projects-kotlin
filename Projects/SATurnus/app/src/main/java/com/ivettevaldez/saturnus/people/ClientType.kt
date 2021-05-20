package com.ivettevaldez.saturnus.people

import java.io.Serializable

class ClientType {

    enum class Type : Serializable {
        ISSUING, RECEIVER
    }

    companion object {

        private const val CLIENT_ISSUING = "Emisor"
        private const val CLIENT_RECEIVER = "Receptor"

        fun getString(type: Type): String {
            return when (type) {
                Type.ISSUING -> CLIENT_ISSUING
                Type.RECEIVER -> CLIENT_RECEIVER
            }
        }

        fun getValue(type: String): Type? {
            return when (type) {
                CLIENT_ISSUING -> Type.ISSUING
                CLIENT_RECEIVER -> Type.RECEIVER
                else -> null
            }
        }
    }
}