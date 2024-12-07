import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicInteger
import kotlin.time.measureTime

fun main() {
    fun getOneStepForwardPosition(
        orientation: Char,
        position: Position
    ) = when (orientation) {
        '^' -> position.first - 1 to position.second
        'v' -> position.first + 1 to position.second
        '>' -> position.first to position.second + 1
        '<' -> position.first to position.second - 1
        else -> throw Exception("invalid guard orientation. Found $orientation at $position")
    }

    val guardChars = listOf('^','>','v','<')

    fun getNextGuardOrientation(guardOrientation: Char) = guardChars[(guardChars.indexOf(guardOrientation) + 1) % guardChars.size]

    fun runGuardPath(
        guardOrientation: Char,
        guardPosition: Position,
        map: List<List<Char>>,
    ): Pair<Set<Pair<Char, Position>>, Boolean> {
        val visitedPositions = mutableSetOf<Pair<Char, Position>>()
        val mutableMap = map.map { it.toMutableList() }.toMutableList()
        var newGuardOrientation = guardOrientation
        var newGuardPosition = guardPosition
        while (true) {
            val oneStepForwardPosition = getOneStepForwardPosition(newGuardOrientation, newGuardPosition)
            try {
                val oneStepForwardObject = mutableMap[oneStepForwardPosition]
                if (!visitedPositions.add(newGuardOrientation to newGuardPosition)) return visitedPositions to true
                when (oneStepForwardObject) {
                    '#' -> newGuardOrientation = getNextGuardOrientation(newGuardOrientation)
                    else -> newGuardPosition = oneStepForwardPosition
                }
            } catch (e: IndexOutOfBoundsException) {
                visitedPositions.add(newGuardOrientation to newGuardPosition)
                return visitedPositions to false
            }
        }
    }

    fun parseData(input: List<String>): Triple<Char, Position, List<List<Char>>> {
        val map = input.map { it.toCharArray().toList() }
        val guardPosition = map.map { it.indexOfFirst { guardChars.contains(it) } }.let { it.indexOfFirst { it != -1 } to (it.firstOrNull { it != -1 } ?: -1) }
        val guardOrientation = map[guardPosition]
        return Triple(guardOrientation, guardPosition, map)
    }

    fun part1(input: List<String>): Int {
        val (guardOrientation, guardPosition, map) = parseData(input)
        val set = runGuardPath(guardOrientation, guardPosition, map).first.map { it.second }.toSet()
        return set.size
    }

    fun addObstacle(map: List<List<Char>>, position: Position): List<List<Char>> =
        map.map { it.toMutableList() }.toMutableList().apply { this[position] = '#' }

    fun part2(input: List<String>): Int {
        val count = AtomicInteger(0)
        runBlocking(Dispatchers.Default) {
            val (guardOrientation, guardPosition, map) = parseData(input)
            val (originalPath, _) = runGuardPath(guardOrientation, guardPosition, map)
            originalPath
                .map { it.second }
                .toSet()
                .forEach {
                    launch {
                        if(runGuardPath(guardOrientation, guardPosition, addObstacle(map, it)).second) count.incrementAndGet()
                    }
                }
        }

        return count.get()
    }

    val testInput = """
        ....#.....
        .........#
        ..........
        ..#.......
        .......#..
        ..........
        .#..^.....
        ........#.
        #.........
        ......#...
    """.trimIndent().lines()
    check(part1(testInput) == 41)

    val input = readInput("Day06")
    measureTime {
        // 7.78ms
        part1(input).println()
    }.println()

    check(part2(testInput) == 6)
    measureTime {
        // 729ms in order
        // 462ms parallelized
        part2(input).println()
    }.println()
}

typealias Position = Pair<Int, Int>

private operator fun <E> MutableList<MutableList<E>>.set(position: Position, value: E) {
    this[position.first][position.second] = value
}


operator fun <E> List<List<E>>.get(position: Position): E =
    this[position.first][position.second]
