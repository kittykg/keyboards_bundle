package com.example.keyboardplay

sealed class KeyHoleValue(val string: String) {
    class Letter(val char: Char) : KeyHoleValue(char.toString())
    class ChangeMode() : KeyHoleValue("Ab3")
    class Backspace () : KeyHoleValue("BS")
    class Enter () : KeyHoleValue("EN")
    class Space () : KeyHoleValue("SP")

    override fun toString(): String {
        return string
    }
}