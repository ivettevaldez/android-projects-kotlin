package com.ivettevaldez.dependencyinjection.networking

import com.ivettevaldez.dependencyinjection.common.Constants

class UrlProvider {

    fun getBaseUrl1(): String = Constants.BASE_URL

    fun getBaseUrl2(): String = "other_base_url"
}