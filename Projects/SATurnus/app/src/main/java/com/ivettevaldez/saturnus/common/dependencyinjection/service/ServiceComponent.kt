package com.ivettevaldez.saturnus.common.dependencyinjection.service

import dagger.Subcomponent

@ServiceScope
@Subcomponent(modules = [ServiceModule::class])
interface ServiceComponent