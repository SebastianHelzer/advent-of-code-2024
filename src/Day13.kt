fun main() {
    
    fun parseVector2(input: String): Vector2Long {
        return Regex("\\d+").findAll(input).map { it.value.toLong() }.toList().let { Vector2Long(it[0], it[1]) }
    }
    
    fun parseInput(input: List<String>): List<ClawMachine> {
        return input.windowed(4, 4, partialWindows = true) {
            ClawMachine(
                parseVector2(it[0]),
                parseVector2(it[1]),
                parseVector2(it[2])
            )
        }
    }
    
    """
        Button A: X+94, Y+34
        Button B: X+22, Y+67
        Prize: X=8400, Y=5400

        Button A: X+26, Y+66
        Button B: X+67, Y+21
        Prize: X=12748, Y=12176

        Button A: X+17, Y+86
        Button B: X+84, Y+37
        Prize: X=7870, Y=6450

        Button A: X+69, Y+23
        Button B: X+27, Y+71
        Prize: X=18641, Y=10279
    """.trimIndent().lines().let {
        val value = parseInput(it).mapNotNull { it.findSolution() }.sumOf { (a, b) -> a * 3 + b }
        check(value == 480L)
        val inflatedValue = parseInput(it).mapNotNull { it.findModifiedSolution() }.sumOf { (a, b) -> a * 3 + b }
        check(inflatedValue.apply { this.println() } > 480)
    }
    
    readInput("Day13").let {
        parseInput(it).mapNotNull { it.findSolution() }.sumOf { (a, b) -> a * 3 + b }.println()
        parseInput(it).mapNotNull { it.findModifiedSolution() }.sumOf { (a, b) -> a * 3 + b }.println()
    }
}

data class ClawMachine(
    val buttonA: Vector2Long,
    val buttonB: Vector2Long,
    val prize: Vector2Long,
) {
    
    fun findSolution(): Pair<Long, Long>? {
        return findSolution(0)
    }
    fun findModifiedSolution(): Pair<Long, Long>? {
        return findSolution(10_000_000_000_000)
    }
    private fun findSolution(prizeOffset: Long): Pair<Long, Long>? {
        val e = prize.x + prizeOffset.toDouble()
        val h = prize.y + prizeOffset.toDouble()
        val d = buttonB.x.toDouble()
        val g = buttonB.y.toDouble()
        val c = buttonA.x.toDouble()
        val f = buttonA.y.toDouble()
        
        check(e > prizeOffset)
        check(h > prizeOffset)
        
        val b_t = c * h - f * e
        val b_b = c * g - d * f
        val b = b_t / b_b
        val a = (d * h - g * e) / (d * f - g * c)
        return (a.toLong() to b.toLong()).takeUnless { (a - it.first > 0) || b - it.second > 0 }
    }
}


data class Vector2Long(val x: Long, val y: Long) {
    operator fun plus(that: Vector2Long): Vector2Long = Vector2Long(this.x + that.x, this.y + that.y)
    operator fun minus(that: Vector2Long): Vector2Long = Vector2Long(this.x - that.x, this.y - that.y)
    operator fun times(that: Long): Vector2Long = Vector2Long(x * that, y * that)
}