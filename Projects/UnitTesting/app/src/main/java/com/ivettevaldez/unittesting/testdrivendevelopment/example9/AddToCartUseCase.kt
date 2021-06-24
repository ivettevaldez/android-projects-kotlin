package com.ivettevaldez.unittesting.testdrivendevelopment.example9

import com.ivettevaldez.unittesting.mockitofundamentals.example7.loginhttpendpoint.NetworkErrorException
import com.ivettevaldez.unittesting.testdrivendevelopment.example9.networking.AddToCartEndpoint
import com.ivettevaldez.unittesting.testdrivendevelopment.example9.networking.AddToCartEndpoint.EndpointResultStatus
import com.ivettevaldez.unittesting.testdrivendevelopment.example9.networking.CartItemScheme

class AddToCartUseCase(private val addToCartEndpoint: AddToCartEndpoint) {

    enum class UseCaseResult {

        SUCCESS,
        FAILURE,
        NETWORK_ERROR
    }

    fun addToCart(offerId: Int, amount: Int): UseCaseResult {
        return try {
            val result = addToCartEndpoint.addToCart(
                CartItemScheme(
                    offerId, amount
                )
            )

            when (result) {
                EndpointResultStatus.SUCCESS -> UseCaseResult.SUCCESS
                EndpointResultStatus.GENERAL_ERROR -> UseCaseResult.FAILURE
                EndpointResultStatus.AUTH_ERROR -> UseCaseResult.FAILURE
                else -> throw RuntimeException("Invalid status: $result")
            }
        } catch (ex: NetworkErrorException) {
            UseCaseResult.NETWORK_ERROR
        }
    }
}
