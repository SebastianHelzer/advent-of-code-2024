fun main() {
    fun parseRules(rulesStrings: List<String>): List<Pair<Int, Int>> {
        return rulesStrings.map { it.split("|").let { it.first().toInt() to it.last().toInt() } }
    }

    fun parseUpdates(updatesString: List<String>): List<List<Int>> {
        return updatesString.map { it.split(",").map { it.toInt() } }
    }

    fun parseInput(input: List<String>): Pair<List<Pair<Int, Int>>, List<List<Int>>> {
        val splitIndex = input.indexOf("")
        val rulesStrings = input.take(splitIndex)
        val updatesString = input.takeLast(input.lastIndex - splitIndex)
        return parseRules(rulesStrings) to parseUpdates(updatesString)
    }

    fun validateUpdate(rules: List<Pair<Int, Int>>, update: List<Int>): Boolean {
        val seenPages = mutableListOf<Int>()
        update.forEach { page ->
            // Rules 75|47 --- The first must be before the last
            val invalidPages = rules.filter { it.first == page }.map { it.second }
            if (invalidPages.intersect(seenPages.toSet()).isNotEmpty()) return false
            seenPages.add(page)
        }
        return true
    }

    fun part1(input: List<String>): Int {
        val (rules, updates) = parseInput(input)
        return updates
            .filter { validateUpdate(rules, it) }
            .sumOf { it[it.lastIndex/2] }
    }

    fun reorderUpdate(rules: List<Pair<Int, Int>>, update: List<Int>): List<Int> {
        return buildList {
            update.forEach { page ->
                val invalidPages = rules.filter { it.first == page }.map { it.second }
                val firstInvalidPageIndex = indexOfFirst { invalidPages.contains(it) }
                if (firstInvalidPageIndex != -1) {
                    add(firstInvalidPageIndex, page)
                } else {
                    add(page)
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        val (rules, updates) = parseInput(input)
        return updates
            .filterNot { validateUpdate(rules, it) }
            .map { reorderUpdate(rules, it) }
            .sumOf { it[it.lastIndex/2] }
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)

    val input = readInput("Day05")
    part1(input).println()

    check(part2(testInput) == 123)
    part2(input).println()
}
