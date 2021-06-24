package com.ivettevaldez.unittesting.testdrivendevelopment.example9.networking

interface AddToCartEndpoint {

    enum class EndpointResultStatus {

        SUCCESS, GENERAL_ERROR, AUTH_ERROR
    }

    fun addToCart(cartItemScheme: CartItemScheme): EndpointResultStatus
}