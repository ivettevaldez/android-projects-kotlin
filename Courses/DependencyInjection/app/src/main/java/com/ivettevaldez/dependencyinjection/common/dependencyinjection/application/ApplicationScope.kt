package com.ivettevaldez.dependencyinjection.common.dependencyinjection.application

import dagger.hilt.migration.AliasOf
import javax.inject.Scope
import javax.inject.Singleton

@Scope
@AliasOf(Singleton::class)
annotation class ApplicationScope