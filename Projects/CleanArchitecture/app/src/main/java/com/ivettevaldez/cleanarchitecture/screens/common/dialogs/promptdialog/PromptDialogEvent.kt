package com.ivettevaldez.cleanarchitecture.screens.common.dialogs.promptdialog

class PromptDialogEvent(val clickedButton: Button) {

    enum class Button {

        POSITIVE, NEGATIVE
    }
}