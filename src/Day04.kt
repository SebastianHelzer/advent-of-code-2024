fun main() {
    fun getDir(int: Int): Pair<Int, Int> = (int % 3) - 1 to (int / 3) - 1

    fun part1(input: List<String>): Int {
        val charArray = input.map { it.toCharArray() }
        var count = 0
        charArray.forEachIndexed { row, chars ->
            chars.forEachIndexed { col, char ->
                if (char == 'X') {
                    (0..8).forEach {
                        val (dx, dy) = getDir(it)
                        if(
                            charArray.getOrNull(row + dy * 1)?.getOrNull(col + dx * 1) == 'M' &&
                            charArray.getOrNull(row + dy * 2)?.getOrNull(col + dx * 2) == 'A' &&
                            charArray.getOrNull(row + dy * 3)?.getOrNull(col + dx * 3) == 'S'
                        ) count++
                    }
                }
            }
        }
        return count
    }

    fun part2(input: List<String>): Int {
        val charArray = input.map { it.toCharArray() }
        var count = 0
        charArray.forEachIndexed { row, chars ->
            chars.forEachIndexed { col, char ->
                if (char == 'A') {
                    val corners = buildList {
                        add(charArray.getOrNull(row + 1)?.getOrNull(col + 1))
                        add(charArray.getOrNull(row - 1)?.getOrNull(col + 1))
                        add(charArray.getOrNull(row - 1)?.getOrNull(col - 1))
                        add(charArray.getOrNull(row + 1)?.getOrNull(col - 1))
                    }.filterNotNull().joinToString("")
                    if(
                        corners.length == 4 &&
                        (corners == "MMSS" ||
                        corners == "SMMS" ||
                        corners == "SSMM" ||
                        corners == "MSSM")
                    ) count++
                }
            }
        }
        return count
    }

    val smallTestInput = """
        ..X...
        .SAMX.
        .A..A.
        XMAS.S
        .X....
    """.trimIndent()
    check(part1(smallTestInput.lines()) == 4)

    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18)

    val input = readInput("Day04")
    part1(input).println()

    check(part2("""
        M.S
        .A.
        M.S
    """.trimIndent().lines()) == 1)
    check(part2("""
        SAM
        MAS
        XAM
    """.trimIndent().lines()) == 0)
    check(part2(testInput) == 9)
    part2(input).println()
}
