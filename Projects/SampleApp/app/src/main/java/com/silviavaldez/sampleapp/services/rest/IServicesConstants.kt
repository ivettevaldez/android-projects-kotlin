package com.silviavaldez.sampleapp.services.rest

interface IServicesConstants {

    companion object {

        private const val DUMMY_SERVER = "http://demo1976201.mockable.io/"

        const val TIMEOUT = 15000 // Milliseconds

        val signInUrl = "${getServer()}api/auth/sign_in"
        val signOutUrl = "${getServer()}api/auth/sign_out"

        private fun getServer(): String {
            return DUMMY_SERVER
        }
    }
}