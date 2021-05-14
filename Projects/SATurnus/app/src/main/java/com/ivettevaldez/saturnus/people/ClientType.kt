package com.ivettevaldez.saturnus.people

import java.io.Serializable

class ClientType {

    enum class Type : Serializable {
        ISSUING, RECEIVER
    }

    companion object {

        private const val CLIENT_ISSUING = "ISSUING"
        private const val CLIENT_RECEIVER = "RECEIVER"

        fun getString(type: Type): String {
            return when (type) {
                Type.ISSUING -> CLIENT_ISSUING
                Type.RECEIVER -> CLIENT_RECEIVER
            }
        }
    }
}