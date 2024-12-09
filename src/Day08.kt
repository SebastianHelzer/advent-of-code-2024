import kotlin.time.measureTime

fun main() {
    fun parseInput(input: List<String>): List<Antenna> = input
        .flatMapIndexed { y, s -> s.mapIndexed { x, c -> Antenna(c, x, y) } }
        .filterNot { it.frequency == '.' }

    fun part1(input: List<String>): Int {
        val bounds = Rect(Vector2(0,0), Vector2(input.first().lastIndex, input.lastIndex))
        return parseInput(input).groupBy { it.frequency }
            .flatMap {
                it.value.flatMapIndexed { i, antenna ->
                    val rest = it.value.drop(i + 1)
                    rest.flatMap {
                        listOf(it.vector2 * 2 - antenna.vector2, antenna.vector2 * 2 - it.vector2)
                    }
                }
            }.distinct().filter { bounds.contains(it) }.size
    }

    fun part2(input: List<String>): Int {
        val bounds = Rect(Vector2(0,0), Vector2(input.first().lastIndex, input.lastIndex))
        return parseInput(input).groupBy { it.frequency }
            .flatMap {
                it.value.flatMapIndexed { i, antenna ->
                    val rest = it.value.drop(i + 1)
                    rest.flatMap {
                        buildList {
                            var x = 0
                            while (true) {
                                val newVector = it.vector2 + (it.vector2 - antenna.vector2) * x++
                                if(!bounds.contains(newVector)) break
                                add(newVector)
                            }
                            x = 0
                            while (true) {
                                val newVector = antenna.vector2 + (antenna.vector2 - it.vector2) * x++
                                if(!bounds.contains(newVector)) break
                                add(newVector)
                            }
                        }
                    }
                }
            }.distinct().size
    }

    val testInput = """
        ............
        ........0...
        .....0......
        .......0....
        ....0.......
        ......A.....
        ............
        ............
        ........A...
        .........A..
        ............
        ............
    """.trimIndent().lines()
    check(part1(testInput).apply { println() } == 14)

    check(part1(".AA".lines()) == 1)

    val input = readInput("Day08")
    measureTime {
        part1(input).println()
    }.println()

    check(part2("..AA".lines()) == 4)
    check(part2("..AA...".lines()) == 7)

    check(part2(testInput).apply { println() } == 34)
    measureTime {
        part2(input).println()
    }.println()
}

private data class Antenna(val frequency: Char, val vector2: Vector2) {
    constructor(frequency: Char, x: Int, y: Int): this(frequency, Vector2(x, y))
}

private data class Rect(val topLeft: Vector2, val bottomRight: Vector2) {
    operator fun contains(that: Vector2): Boolean {
        return that.x >= topLeft.x && that.y >= topLeft.y && that.y <= bottomRight.y && that.x <= bottomRight.x
    }
}

data class Vector2(val x: Int, val y: Int) {
    operator fun plus(that: Vector2): Vector2 = Vector2(this.x + that.x, this.y + that.y)
    operator fun minus(that: Vector2): Vector2 = Vector2(this.x - that.x, this.y - that.y)
    operator fun times(that: Int): Vector2 = Vector2(x * that, y * that)
}