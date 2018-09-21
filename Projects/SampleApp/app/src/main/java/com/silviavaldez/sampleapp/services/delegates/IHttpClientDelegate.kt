package com.silviavaldez.sampleapp.services.delegates

interface IHttpClientDelegate {

    fun onSuccess(result: ArrayList<Any>)

    fun onFailure(error: String)
}