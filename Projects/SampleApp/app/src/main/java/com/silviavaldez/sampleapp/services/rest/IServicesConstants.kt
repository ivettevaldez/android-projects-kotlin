package com.silviavaldez.sampleapp.services.rest

interface IServicesConstants {

    companion object {

        private const val DUMMY_SERVER = "http://demo1976201.mockable.io/"

        const val TIMEOUT = 15000

        val signInUrl = "${getServer()}api/auth/sign_in"

        private fun getServer(): String {
            return DUMMY_SERVER
        }
    }
}