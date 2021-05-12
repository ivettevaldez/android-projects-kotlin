package com.ivettevaldez.saturnus.screens.common.dialogs.prompt

class PromptDialogEvent(val clickedButton: Button) {

    enum class Button {
        POSITIVE, NEGATIVE
    }
}