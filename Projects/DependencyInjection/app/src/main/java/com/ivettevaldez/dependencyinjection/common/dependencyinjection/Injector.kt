package com.ivettevaldez.dependencyinjection.common.dependencyinjection

import com.ivettevaldez.dependencyinjection.questions.FetchQuestionDetailsUseCase
import com.ivettevaldez.dependencyinjection.questions.FetchQuestionsUseCase
import com.ivettevaldez.dependencyinjection.screens.common.dialogs.DialogsNavigator
import com.ivettevaldez.dependencyinjection.screens.common.navigation.ScreensNavigator
import com.ivettevaldez.dependencyinjection.screens.common.viewsmvc.ViewMvcFactory
import java.lang.reflect.Field

class Injector(private val component: PresentationComponent) {

    fun inject(client: Any) {
        for (field in getAllFields(client)) {
            if (isAnnotatedForInjection(field)) {
                injectField(client, field)
            }
        }
    }

    private fun getAllFields(client: Any): Array<out Field> {
        return client::class.java.declaredFields
    }

    private fun isAnnotatedForInjection(field: Field): Boolean {
        val fieldAnnotations = field.annotations
        for (annotation in fieldAnnotations) {
            if (annotation is Service) {
                return true
            }
        }
        return false
    }

    private fun injectField(client: Any, field: Field) {
        val isAccessibleInitially = field.isAccessible
        field.isAccessible = true
        field.set(client, getServiceForClass(field.type))
        field.isAccessible = isAccessibleInitially
    }

    private fun getServiceForClass(type: Class<*>): Any {
        return when (type) {
            ScreensNavigator::class.java -> {
                component.screensNavigator()
            }
            DialogsNavigator::class.java -> {
                component.dialogsNavigator()
            }
            ViewMvcFactory::class.java -> {
                component.viewMvcFactory()
            }
            FetchQuestionsUseCase::class.java -> {
                component.fetchQuestionsUseCase()
            }
            FetchQuestionDetailsUseCase::class.java -> {
                component.fetchQuestionDetailsUseCase()
            }
            else -> {
                throw Exception("Unsupported Service type: $type")
            }
        }
    }
}