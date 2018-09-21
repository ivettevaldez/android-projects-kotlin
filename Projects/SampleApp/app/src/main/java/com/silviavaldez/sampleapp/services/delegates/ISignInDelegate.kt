package com.silviavaldez.sampleapp.services.delegates

interface ISignInDelegate {

    fun onSignInSuccess()

    fun onSignInFailure(error: String)
}