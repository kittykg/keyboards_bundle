package com.example.keyboardplay

import android.inputmethodservice.InputMethodService
import android.util.Log
import android.view.View

class FutureKeyboard : InputMethodService() {
    override fun onCreateInputView(): View {
        return MyRenderer(this)
    }

    fun typeLetter(letter: Char) {
        currentInputConnection.commitText(letter.toString(), 1)
    }

    fun backspace() {
        currentInputConnection.deleteSurroundingText(1, 0)
    }

    fun getRecentText(): String {
        return currentInputConnection.getTextBeforeCursor(10, 0).toString()
    }
}