package com.example.guesstheobject.ui

class ShapeProgressManager {

    private val shapeNames = ShapeRepository.shapes.map { it.name }
    private var currentIndex = 0
    private val triesMap = mutableMapOf<String, Int>()

    val currentShape: Shape get() = ShapeRepository.shapes[currentIndex]

    val attempt: Int get() = triesMap[currentShape.name] ?: 0

    var isCompleted = false
        private set

    fun advanceTries(shape: Shape) {
        triesMap[shape.name] = (triesMap[shape.name] ?: 0) + 1
    }

    fun nextShape() {
        if (currentIndex >= ShapeRepository.shapes.size - 1) {
            isCompleted = true
            return
        }
        currentIndex++
        triesMap.clear()
    }

    fun resetShape() {
        triesMap[currentShape.name] = 0
    }

    fun reset() {
        currentIndex = 0
        triesMap.clear()
    }
}
