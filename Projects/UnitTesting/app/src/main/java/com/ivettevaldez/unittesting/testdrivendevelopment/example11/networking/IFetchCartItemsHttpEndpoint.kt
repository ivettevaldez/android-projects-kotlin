package com.ivettevaldez.unittesting.testdrivendevelopment.example11.networking


interface IFetchCartItemsHttpEndpoint {

    enum class FailReason {

        GENERAL_ERROR,
        NETWORK_ERROR
    }

    interface Callback {

        fun onFetchCartItemsSucceeded(cartItemSchemas: List<CartItemSchema>)
        fun onFetchCartItemsFailed(reason: FailReason)
    }

    fun fetchCartItems(limit: Int, callback: Callback)
}