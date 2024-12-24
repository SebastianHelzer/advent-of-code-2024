fun main() {
    fun parseInput(input: List<String>): List<List<Char>> {
        return input.map { it.toCharArray().asList() }
    }
    
    data class GardenPlot(val char: Char, val positions: Set<Position>) {
        val fences: List<Fence> = buildList {
            positions.forEach { position ->
                if(!positions.contains(position.copy(first = position.first.plus(1)))) {
                    add(Fence(position, Direction.Bottom))
                }
                if(!positions.contains(position.copy(first = position.first.minus(1)))) {
                    add(Fence(position, Direction.Top))
                }
                if(!positions.contains(position.copy(second = position.second.plus(1)))) {
                    add(Fence(position, Direction.Right))
                }
                if(!positions.contains(position.copy(second = position.second.minus(1)))) {
                    add(Fence(position, Direction.Left))
                }
            }
        }
        val perimeter = fences.size
        val area = positions.size
        val price = perimeter * area
        val numSides: Int by lazy {
            val mutableList = fences.toMutableList()
            fences.forEach { fence ->
                when(fence.direction) {
                    Direction.Right, Direction.Left -> {
                        val element = fence.copy(position = fence.position.copy(first = fence.position.first + 1))
                        if(fences.contains(element)) {
                            mutableList.remove(fence)
                        }
                    }
                    Direction.Top, Direction.Bottom -> {
                        val element = fence.copy(position = fence.position.copy(second = fence.position.second + 1))
                        if(fences.contains(element)) {
                            mutableList.remove(fence)
                        }
                    }
                }
            }
            mutableList.size
        }
        val bulkDiscountPrice = area * numSides
    }
    
    fun toGardenPlots(chars: List<List<Char>>): List<GardenPlot> {
        return buildList {
            chars.forEachIndexed { row, line ->
                line.forEachIndexed { col, c ->
                    val position = Position(row, col)
                    val leftPlot = firstOrNull { it.char == c && it.positions.contains(Position(row - 1, col)) }
                    val topPlot = firstOrNull { it.char == c && it.positions.contains(Position(row, col - 1)) }
                    when {
                        leftPlot != null && topPlot != null -> {
                            remove(leftPlot)
                            remove(topPlot)
                            add(leftPlot.copy(positions = leftPlot.positions.plus(topPlot.positions).plus(position)))
                        }
                        leftPlot != null -> {
                            remove(leftPlot)
                            add(leftPlot.copy(positions = leftPlot.positions.plus(position)))
                        }
                        topPlot != null -> {
                            remove(topPlot)
                            add(topPlot.copy(positions = topPlot.positions.plus(position)))
                        }
                        else -> {
                            add(GardenPlot(c, setOf(position)))
                        }
                    }
                }
            }
        }
    }
    
    """
        AAAA
        BBCD
        BBCC
        EEEC
    """.trimIndent().lines().let {
        val plots = toGardenPlots(parseInput(it))
        check(plots.size == 5)
        check(plots.first { it.char == 'D' }.perimeter == 4)
        check(plots.first { it.char == 'D' }.numSides == 4)
        check(plots.first { it.char == 'A' }.perimeter == 10)
        check(plots.first { it.char == 'A' }.numSides == 4)
        check(plots.first { it.char == 'C' }.perimeter == 10)
        check(plots.first { it.char == 'C' }.numSides == 8)
        check(plots.first { it.char == 'B' }.perimeter == 8)
        check(plots.sumOf { it.price } == 140)
    }
    
    """
        AB
        BB
    """.trimIndent().lines().let {
        val plots = toGardenPlots(parseInput(it))
        check(plots.size == 2)
    }
    
    """
        OOOOO
        OXOXO
        OOOOO
        OXOXO
        OOOOO
    """.trimIndent().lines().let {
        val plots = toGardenPlots(parseInput(it))
        check(plots.size == 5)
        check(plots.first { it.char == 'O' }.perimeter == 36)
        check(plots.sumOf { it.price } == 772)
    }
    
    """
        RRRRIICCFF
        RRRRIICCCF
        VVRRRCCFFF
        VVRCCCJFFF
        VVVVCJJCFE
        VVIVCCJJEE
        VVIIICJJEE
        MIIIIIJJEE
        MIIISIJEEE
        MMMISSJEEE
    """.trimIndent().lines().let {
        val plots = toGardenPlots(parseInput(it))
        check(plots.sumOf { it.price } == 1930)
        check(plots.sumOf { it.bulkDiscountPrice } == 1206)
    }
    
    readInput("Day12").let {
        val plots = toGardenPlots(parseInput(it))
        println(plots.sumOf { it.price })
        println(plots.sumOf { it.bulkDiscountPrice })
    }
}


enum class Direction { Top, Bottom, Left, Right }
data class Fence(val position: Position, val direction: Direction)