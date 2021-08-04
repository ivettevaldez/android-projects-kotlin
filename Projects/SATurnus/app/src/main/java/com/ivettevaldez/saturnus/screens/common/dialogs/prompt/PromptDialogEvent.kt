package com.ivettevaldez.saturnus.screens.common.dialogs.prompt

open class PromptDialogEvent(open val clickedButton: Button) {

    enum class Button {
        POSITIVE,
        NEGATIVE
    }
}