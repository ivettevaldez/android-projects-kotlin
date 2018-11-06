package com.silviavaldez.sampleapp.services.delegates

interface ISignOutDelegate {

    fun onSignOutSuccess()

    fun onSignOutFailure(error: String)
}