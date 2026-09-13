package ru.college.arithmetic

import kotlin.random.Random

enum class Operation(val symbol: String) { MULTIPLY("*"), DIVIDE("/"), SUBTRACT("-"), ADD("+") }

data class Exercise(val left: Int, val right: Int, val operation: Operation) {
    val answer: Int get() = when (operation) {
        Operation.MULTIPLY -> left * right
        Operation.DIVIDE -> left / right
        Operation.SUBTRACT -> left - right
        Operation.ADD -> left + right
    }

    companion object {
        fun generate(random: Random = Random.Default): Exercise {
            val operation = Operation.entries.random(random)
            return if (operation == Operation.DIVIDE) {
                val divisor = random.nextInt(10, 100)
                val quotient = random.nextInt(1, 99 / divisor + 1)
                Exercise(divisor * quotient, divisor, operation)
            } else {
                Exercise(random.nextInt(10, 100), random.nextInt(10, 100), operation)
            }
        }
    }
}
