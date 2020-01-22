package com.example.keyboardplay

import android.graphics.Color
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputConnection
import android.widget.Button

class ButtonOnTouchListener(
    val buttonType: Buttons,
    val buttonView: Button,
    var currentInputConnection: InputConnection,
    val characterBuilder: CharacterBuilder,
    val buttonListenersList: List<ButtonOnTouchListener>
) :
    View.OnTouchListener {

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                characterBuilder.addButtonPress(buttonType)
                val textOnButton = buttonText.get(buttonType)
                if (textOnButton != null && characterBuilder.characterKeyIsPressed()) {
                    var commonChars = characterBuilder.charactersIntersection()
                    for (buttonListener in buttonListenersList.filter { x -> x.buttonType.ordinal in 0..5 }) {
                        val text = buttonText[buttonListener.buttonType]
                        if (text != null) {
                            var dim = true
                            for (commonChar in commonChars) {
                                if (text.contains(commonChar)) {
                                    dim = false
                                    break
                                }
                            }
                            if (dim) {
                                buttonListener.buttonView.setBackgroundColor(Color.GRAY)
                            }
                            buttonListener.buttonView.text = text.filter { c -> c in commonChars }
                        }
                    }
                }
            }

            MotionEvent.ACTION_UP -> {
                if (characterBuilder.getCharCode() == 128) {
                    delete()
                } else {
                    val character = characterBuilder.getCharacter()
                    if (character != null) {
                        typing(characterBuilder.getCharacter().toString())
                    }
                }
                characterBuilder.clearSequence()
                for (bl in buttonListenersList.filter { x -> x.buttonType.ordinal in 0..5 }) {
                    bl.buttonView.setBackgroundColor(Color.YELLOW)
                    bl.buttonView.text = buttonText[bl.buttonType]
                }
            }
        }
        return true
    }

    fun typing(string: String) {
        currentInputConnection.commitText(string, 1)
    }

    fun delete() {
        currentInputConnection.deleteSurroundingText(1, 0)
    }

    private val buttonText = hashMapOf<Buttons, String>(
        Buttons.E to "eshmwbkz?)%+\\@06",
        Buttons.T to "tsrdypkj!(#=/@167",
        Buttons.A to "arhfgvkq:-*^/\\27",
        Buttons.O to "odlcmvx;\"&^=+38",
        Buttons.I to "ilugpbxqjz,'&*#%489",
        Buttons.N to "nucfywxqjz.'\"-()59",
        Buttons.PUNC to "PUNC\n(DEL)",
        Buttons.SHIFT to "SHIFT\n(SPA)"
    )
}