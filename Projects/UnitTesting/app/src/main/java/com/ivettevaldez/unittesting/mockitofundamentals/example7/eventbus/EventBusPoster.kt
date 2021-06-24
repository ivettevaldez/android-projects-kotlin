package com.ivettevaldez.unittesting.mockitofundamentals.example7.eventbus

interface EventBusPoster {

    fun postEvent(event: Any)
}