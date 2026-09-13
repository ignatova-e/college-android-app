package ru.college.arithmetic

import org.junit.Assert.*
import org.junit.Test
import kotlin.random.Random

class ExerciseTest {
    @Test fun generatedOperandsAndDivisionAreValid() {
        val random = Random(42)
        val operations = mutableSetOf<Operation>()
        repeat(20_000) {
            val exercise = Exercise.generate(random)
            operations += exercise.operation
            assertTrue(exercise.left in 10..99)
            assertTrue(exercise.right in 10..99)
            if (exercise.operation == Operation.DIVIDE) {
                assertEquals(0, exercise.left % exercise.right)
                assertEquals(exercise.left, exercise.answer * exercise.right)
            }
        }
        assertEquals(Operation.entries.toSet(), operations)
    }

    @Test fun calculatesAllOperationsIncludingNegativeAndZero() {
        assertEquals(9801, Exercise(99, 99, Operation.MULTIPLY).answer)
        assertEquals(9, Exercise(90, 10, Operation.DIVIDE).answer)
        assertEquals(-89, Exercise(10, 99, Operation.SUBTRACT).answer)
        assertEquals(0, Exercise(10, 10, Operation.SUBTRACT).answer)
        assertEquals(198, Exercise(99, 99, Operation.ADD).answer)
    }
}
