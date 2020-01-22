package com.example.keyboardplay

import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.Button

class ReduceKeysKeyboard : InputMethodService() {
    private val characterBuilder = CharacterBuilder()

    override fun onBindInput() {
        super.onBindInput()
        listeners.forEach { l -> l.currentInputConnection = currentInputConnection }
    }

    private val listeners = mutableListOf<ButtonOnTouchListener>()

    override fun onCreateInputView(): View {
        val view = layoutInflater.inflate(R.layout.input, null)

        listeners.add(
            ButtonOnTouchListener(
                Buttons.E,
                view.findViewById<Button>(R.id.button),
                currentInputConnection,
                characterBuilder,
                listeners
            )
        )
        listeners.add(
            ButtonOnTouchListener(
                Buttons.T,
                view.findViewById<Button>(R.id.button2),
                currentInputConnection,
                characterBuilder,
                listeners
            )
        )
        listeners.add(
            ButtonOnTouchListener(
                Buttons.A,
                view.findViewById<Button>(R.id.button3),
                currentInputConnection,
                characterBuilder,
                listeners
            )
        )
        listeners.add(
            ButtonOnTouchListener(
                Buttons.O,
                view.findViewById<Button>(R.id.button4),
                currentInputConnection,
                characterBuilder,
                listeners
            )
        )
        listeners.add(
            ButtonOnTouchListener(
                Buttons.I,
                view.findViewById<Button>(R.id.button5),
                currentInputConnection,
                characterBuilder,
                listeners
            )
        )
        listeners.add(
            ButtonOnTouchListener(
                Buttons.N,
                view.findViewById<Button>(R.id.button6),
                currentInputConnection,
                characterBuilder,
                listeners
            )
        )
        listeners.add(
            ButtonOnTouchListener(
                Buttons.PUNC,
                view.findViewById<Button>(R.id.button7),
                currentInputConnection,
                characterBuilder,
                listeners
            )
        )
        listeners.add(
            ButtonOnTouchListener(
                Buttons.SHIFT,
                view.findViewById<Button>(R.id.button8),
                currentInputConnection,
                characterBuilder,
                listeners
            )
        )

        for (bl in listeners) {
            bl.buttonView.setBackgroundColor(Color.YELLOW)
        }

        view.findViewById<Button>(R.id.button)
            .setOnTouchListener(listeners.get(0))
        view.findViewById<Button>(R.id.button2)
            .setOnTouchListener(listeners.get(1))
        view.findViewById<Button>(R.id.button3)
            .setOnTouchListener(listeners.get(2))
        view.findViewById<Button>(R.id.button4)
            .setOnTouchListener(listeners.get(3))
        view.findViewById<Button>(R.id.button5)
            .setOnTouchListener(listeners.get(4))
        view.findViewById<Button>(R.id.button6)
            .setOnTouchListener(listeners.get(5))
        view.findViewById<Button>(R.id.button7)
            .setOnTouchListener(listeners.get(6))
        view.findViewById<Button>(R.id.button8)
            .setOnTouchListener(listeners.get(7))
        return view
    }

    override fun onUnbindInput() {
        characterBuilder.clearSequence()
        super.onUnbindInput()
    }


}
