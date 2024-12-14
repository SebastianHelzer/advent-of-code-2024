fun main() {
    fun countRoutes(point: TopographicPoint, points: List<TopographicPoint>): List<TopographicPoint> {
        if (point.elevation == 9) return listOf(point)
        val adjacentLocations = listOf(
            point.position.plus(Vector2(1, 0)),
            point.position.plus(Vector2(0, 1)),
            point.position.minus(Vector2(1, 0)),
            point.position.minus(Vector2(0, 1)),
        )
        return points
            .filter { adjacentLocations.contains(it.position) }
            .filter { it.elevation == point.elevation + 1 }
            .flatMap { countRoutes(it, points) }
    }
    
    fun part1(input: List<String>): Int {
        val topographicPoints = input.flatMapIndexed { x, s ->
            s.mapIndexed { y, c ->
                TopographicPoint(c.digitToIntOrNull() ?: Int.MIN_VALUE, x, y)
            }
        }
        
        return topographicPoints.filter { it.elevation == 0 }.sumOf {
            countRoutes(it, topographicPoints).distinct().size
        }
    }

    fun part2(input: List<String>): Int {
        val topographicPoints = input.flatMapIndexed { x, s ->
            s.mapIndexed { y, c ->
                TopographicPoint(c.digitToIntOrNull() ?: Int.MIN_VALUE, x, y)
            }
        }
        
        return topographicPoints.filter { it.elevation == 0 }.sumOf {
            countRoutes(it, topographicPoints).size
        }
    }
    
    val input = """
        0123
        1234
        8765
        9876
    """.trimIndent().lines()
    check(part1(input).apply { println() } == 1)
    
    val twoDestinationInput =
        """
            ...0...
            ...1...
            ...2...
            6543456
            7.....7
            8.....8
            9.....9
        """.trimIndent().lines()
    check(part1(twoDestinationInput).apply { println() } == 2)
    
    val fourDestinationInput =
        """
            ..90..9
            ...1.98
            ...2..7
            6543456
            765.987
            876....
            987....
        """.trimIndent().lines()
    check(part1(fourDestinationInput).apply { println() } == 4)
    
    val largerInput = """
        89010123
        78121874
        87430965
        96549874
        45678903
        32019012
        01329801
        10456732
    """.trimIndent().lines()
    check(part1(largerInput).apply { println() } == 36)
    
    part1(readInput("Day10")).println()
    
    val threeTrails = """
        .....0.
        ..4321.
        ..5..2.
        ..6543.
        ..7..4.
        ..8765.
        ..9....
    """.trimIndent().lines()
    check(part2(threeTrails).apply { println() } == 3)
    check(part2("""
        ..90..9
        ...1.98
        ...2..7
        6543456
        765.987
        876....
        987....
    """.trimIndent().lines()).apply { println() } == 13)
 
    check(part2(largerInput).apply { println() } == 81)
    
    part2(readInput("Day10")).println()
}

private data class TopographicPoint(val elevation: Int, val position: Vector2) {
    constructor(elevation: Int, x: Int, y: Int): this(elevation, Vector2(x, y))
}