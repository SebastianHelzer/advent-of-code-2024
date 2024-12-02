fun main() {
    fun part1(input: List<String>): Int = input
        .map { it.split(regex = Regex("\\s+")).map { it.toInt() } }
        .count { report -> report.isSafe() }

    fun part2(input: List<String>): Int = input
        .map { it.split(regex = Regex("\\s+")).map { it.toInt() } }
        .count { report ->
            report.isSafe() || report.indices.any { report.toMutableList().apply { removeAt(it) }.isSafe() }
        }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private fun List<Int>.isSafe(): Boolean = windowed(2) { (first, second) -> first - second }
    .let { diffs -> diffs.all { it in 1..3 } || diffs.all { it in -3 .. -1 } }
