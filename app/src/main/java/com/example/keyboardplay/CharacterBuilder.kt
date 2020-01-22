package com.example.keyboardplay

class CharacterBuilder {
    private val buttonsSequence = mutableListOf<Int>()

    private val charCodeMapping = hashMapOf<Int, Char>(
        1 to 'e',
        2 to 't',
        4 to 'a',
        8 to 'o',
        16 to 'i',
        32 to 'n',
        3 to 's',
        5 to 'h',
        6 to 'r',
        10 to 'd',
        24 to 'l',
        48 to 'u',
        40 to 'c',
        9 to 'm',
        36 to 'f',
        34 to 'y',
        33 to 'w',
        20 to 'g',
        18 to 'p',
        17 to 'b',
        12 to 'v',
        7 to 'k',
        56 to 'x',
        52 to 'q',
        50 to 'j',
        49 to 'z',

        65 to 'E',
        66 to 'T',
        68 to 'A',
        72 to 'O',
        80 to 'I',
        96 to 'N',
        67 to 'S',
        69 to 'H',
        70 to 'R',
        74 to 'D',
        88 to 'L',
        112 to 'U',
        104 to 'C',
        73 to 'M',
        100 to 'F',
        98 to 'Y',
        97 to 'W',
        84 to 'G',
        82 to 'P',
        81 to 'B',
        76 to 'V',
        71 to 'K',
        120 to 'X',
        116 to 'Q',
        114 to 'J',
        113 to 'Z',

        64 to ' ',
        160 to '.',
        144 to ',',
        136 to ';',
        132 to ':',
        130 to '!',
        129 to '?',
        176 to '\'',
        168 to '"',
        164 to '-',
        162 to '(',
        161 to ')',
        152 to '&',
        148 to '*',
        146 to '#',
        145 to '%',
        140 to '^',
        138 to '=',
        137 to '+',
        134 to '/',
        133 to '\\',
        131 to '@',

        193 to '0',
        194 to '1',
        196 to '2',
        200 to '3',
        208 to '4',
        224 to '5',
        195 to '6',
        198 to '7',
        216 to '8',
        240 to '9',

        192 to '\n'
    )

    fun getCharCode(): Int {
        var charCode = 0
        for (id in buttonsSequence) {
            charCode += Math.pow(2.toDouble(), id.toDouble()).toInt()
        }
        return charCode
    }

    fun addButtonPress(button: Buttons) {
        buttonsSequence.add(button.ordinal)
    }

    fun getCharacter(): Char? {
        return charCodeMapping.get(getCharCode())
    }

    fun clearSequence() {
        buttonsSequence.clear()
    }

    fun characterKeyIsPressed(): Boolean {
        return buttonsSequence.filter { x -> x in 0..5 }.size >= 2
    }

    fun charactersIntersection(): Set<Char> {
        var buttons = buttonsSequence.filter { x -> x in 0..5 }
        var charArrays = mutableListOf<Set<Char>>()
        for (button in buttons) {
            val string = buttonText[button]
            if (string != null) {
                charArrays.add(string.toCharArray().toSet())
            }
        }
        return charArrays.reduce { a, b -> a.intersect(b) }
    }

    private val buttonText = hashMapOf<Int, String>(
        Buttons.E.ordinal to "eshmwbkz?)%+\\@06",
        Buttons.T.ordinal to "tsrdypkj!(#=/@167",
        Buttons.A.ordinal to "arhfgvkq:-*^/\\27",
        Buttons.O.ordinal to "odlcmvx;\"&^=+38",
        Buttons.I.ordinal to "ilugpbxqjz,'&*#%489",
        Buttons.N.ordinal to "nucfywxqjz.'\"-()59"
    )
}