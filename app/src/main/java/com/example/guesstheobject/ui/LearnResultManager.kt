package com.example.guesstheobject.ui

class LearnResultManager {

    // Map of letter/shape char to list of accuracy scores per stroke
    private val accuracyMap = mutableMapOf<Char, MutableList<Float>>()

    fun recordAccuracy(letter: Char, accuracy: Float) {
        accuracyMap.getOrPut(letter) { mutableListOf() }.add(accuracy)
    }

    fun getAccuracy(letter: Char): Float {
        val scores = accuracyMap[letter] ?: return 0f
        return scores.average().toFloat()
    }

    fun getOverallAccuracy(): Float {
        if (accuracyMap.isEmpty()) return 0f
        val allScores = accuracyMap.values.flatten()
        return allScores.average().toFloat()
    }

    // Letters with average accuracy >= 75% are "good"
    fun getGoodLetters(): List<Char> {
        return accuracyMap.entries
            .filter { it.value.average() >= 75.0 }
            .map { it.key }
            .sorted()
    }

    // Letters with average accuracy < 75% need focus
    fun getLettersToFocus(): List<Char> {
        return accuracyMap.entries
            .filter { it.value.average() < 75.0 }
            .map { it.key }
            .sorted()
    }

    fun reset() {
        accuracyMap.clear()
    }
}
