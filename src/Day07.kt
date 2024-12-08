import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.math.BigInteger
import kotlin.math.pow
import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>): List<Pair<Long, List<Long>>> {
        return input.map {
            it.split(' ', ':').filterNot { it.isBlank() }.map { it.toLong() }.let {
                it.first() to it.drop(1)
            }
        }
    }

    fun validateEquation(
        testAndEquation: Pair<Long, List<Long>>,
        operators: List<(Long, Long) -> Long>
    ): Boolean {
        val (test, equation) = testAndEquation
        val first = equation.first()
        val rest = equation.drop(1)
        val numberOfOperators = operators.size
        val operatorSlots = rest.size
        val possibleOperatorCombinations = numberOfOperators.toDouble().pow(operatorSlots).toInt()
        for (operatorCombo in 0..possibleOperatorCombinations) {
            var value = first
            val operatorString = operatorCombo.toString(numberOfOperators).padStart(operatorSlots, '0')
            for (operatorIndex in 0..<operatorSlots) {
                if (value > test) break
                val operator = operatorString[operatorIndex].digitToInt()
                value = operators[operator](value, rest[operatorIndex])
            }
            if (value == test) return true
        }
        return false
    }

    fun part1(input: List<String>): BigInteger = runBlocking(Dispatchers.Default) {
        parseInput(input).map {
            async { if(validateEquation(it, listOf(
                    { a, b -> a + b },
                    { a, b -> a * b },
                ))) it.first else 0 }
        }.awaitAll().sumOf { it.toBigInteger() }
    }

    fun part2(input: List<String>): BigInteger = runBlocking(Dispatchers.Default) {
        parseInput(input).map {
            async { if(validateEquation(it, listOf(
                    { a, b -> a + b },
                    { a, b -> a * b },
                    { a, b -> "$a$b".toLong() },
                ))) it.first else 0 }
        }.awaitAll().sumOf { it.toBigInteger() }
    }

    val testInput = """
        190: 10 19
        3267: 81 40 27
        83: 17 5
        156: 15 6
        7290: 6 8 6 15
        161011: 16 10 13
        192: 17 8 14
        21037: 9 7 18 13
        292: 11 6 16 20
    """.trimIndent().lines()
    check(part1(testInput).toLong() == 3749L)
    part1(listOf("755796: 1 82 53 8 311 7 3 49 6 4"))

    val input = readInput("Day07")
    measureTime {
        part1(input).println()
    }.println()

    check(part2(testInput).toLong().apply { println() } == 11387L)
    measureTime {
        part2(input).println()
    }.println()
}