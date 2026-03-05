package com.example.guesstheobject.ui

class LearnProgressManager {

    private val triesMap = mutableMapOf<Char, Int>()

    var currentLetter: Char = 'A'
        private set

    /** How many rounds completed for the current letter (0, 1, or 2) */
    val attempt: Int
        get() = triesMap[currentLetter] ?: 0

    fun getTries(letter: Letter): Int {
        return triesMap[letter.character] ?: 0
    }

    fun advanceTries(letter: Letter) {
        val current = triesMap[letter.character] ?: 0
        triesMap[letter.character] = current + 1
    }

    /** Move to the next letter and reset its try count */
    fun nextLetter() {
        currentLetter = when (currentLetter) {
            in 'A'..'Y' -> currentLetter + 1
            else -> 'A'
        }
        triesMap[currentLetter] = 0
    }

    fun reset() {
        triesMap.clear()
        currentLetter = 'A'
    }
}
