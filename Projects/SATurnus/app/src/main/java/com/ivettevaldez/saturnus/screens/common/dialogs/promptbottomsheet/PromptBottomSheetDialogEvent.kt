package com.ivettevaldez.saturnus.screens.common.dialogs.promptbottomsheet

open class PromptBottomSheetDialogEvent(open val clickedButton: Button) {

    enum class Button {
        OPTION_ONE,
        OPTION_TWO
    }
}