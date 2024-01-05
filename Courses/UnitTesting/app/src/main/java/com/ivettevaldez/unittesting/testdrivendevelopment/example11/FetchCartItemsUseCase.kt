package com.ivettevaldez.unittesting.testdrivendevelopment.example11

import com.ivettevaldez.unittesting.testdrivendevelopment.example11.cartitems.CartItem
import com.ivettevaldez.unittesting.testdrivendevelopment.example11.networking.CartItemSchema
import com.ivettevaldez.unittesting.testdrivendevelopment.example11.networking.IFetchCartItemsHttpEndpoint
import com.ivettevaldez.unittesting.testdrivendevelopment.example11.networking.IFetchCartItemsHttpEndpoint.Callback
import com.ivettevaldez.unittesting.testdrivendevelopment.example11.networking.IFetchCartItemsHttpEndpoint.FailReason

class FetchCartItemsUseCase(private val fetchCartItemsHttpEndpoint: IFetchCartItemsHttpEndpoint) :
    Callback {

    private val listeners: ArrayList<Listener> = ArrayList()

    interface Listener {

        fun onCartItemsFetched(items: List<CartItem>)
        fun onFetchCartItemsFailed()
    }

    fun fetchCartItemsAndNotify(limit: Int) {
        fetchCartItemsHttpEndpoint.fetchCartItems(limit, this)
    }

    fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun onFetchCartItemsSucceeded(cartItemSchemas: List<CartItemSchema>) {
        for (listener in listeners) {
            listener.onCartItemsFetched(
                getCartItemsFromSchemas(cartItemSchemas)
            )
        }
    }

    override fun onFetchCartItemsFailed(reason: FailReason) {
        when (reason) {
            FailReason.GENERAL_ERROR,
            FailReason.NETWORK_ERROR -> {
                for (listener in listeners) {
                    listener.onFetchCartItemsFailed()
                }
            }
            else -> {
                throw RuntimeException("Invalid fail reason: $reason")
            }
        }
    }

    private fun getCartItemsFromSchemas(schemas: List<CartItemSchema>): List<CartItem> {
        val cartItems: MutableList<CartItem> = mutableListOf()
        for (schema in schemas) {
            cartItems.add(
                CartItem(
                    id = schema.id,
                    title = schema.title,
                    description = schema.description,
                    price = schema.price
                )
            )
        }
        return cartItems
    }
}