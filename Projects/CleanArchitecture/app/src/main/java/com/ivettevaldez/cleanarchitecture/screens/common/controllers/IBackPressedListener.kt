package com.ivettevaldez.cleanarchitecture.screens.common.controllers

interface IBackPressedListener {

    /**
     * Returns true if the listener handled the back press; false otherwise.
     */
    fun onBackPressed(): Boolean
}