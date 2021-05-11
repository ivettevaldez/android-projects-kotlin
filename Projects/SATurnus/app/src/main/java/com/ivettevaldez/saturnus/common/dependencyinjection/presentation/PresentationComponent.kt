package com.ivettevaldez.saturnus.common.dependencyinjection.presentation

import dagger.Subcomponent

@PresentationScope
@Subcomponent(modules = [PresentationModule::class])
interface PresentationComponent