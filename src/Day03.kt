fun main() {
    fun parseCommands(memory: String): List<Command> {
        val multiplyCommandRegex = Regex("mul\\([0-9]{1,3},[0-9]{1,3}\\)")

        val multiplyCommands = multiplyCommandRegex.findAll(memory).map {
            val regex = Regex("[0-9]{1,3}")
            val (a,b) = regex.findAll(it.value).map { it.value.toInt() }.toList()
            Command.Multiply(a, b) to it.range
        }

        val doCommands = Regex("do\\(\\)").findAll(memory).map { Command.Do to it.range }
        val doNotCommands = Regex("don't\\(\\)").findAll(memory).map { Command.DoNot to it.range }

        return buildList {
            addAll(multiplyCommands)
            addAll(doCommands)
            addAll(doNotCommands)
        }.sortedBy { it.second.first }.map { it.first }
    }

    fun computeCommand(command: Command.Multiply): Int = command.a * command.b

    fun part1(input: List<String>): Int {
        return input.map { parseCommands(it) }.flatten().filterIsInstance<Command.Multiply>().sumOf { computeCommand(it) }
    }

    fun part2(input: List<String>): Int {
        var enabled = true
        val commands = input.map { parseCommands(it) }
        return commands.flatten().fold(0) { a, b ->
            when(b) {
                Command.Do -> {
                    enabled = true
                    a
                }

                Command.DoNot -> {
                    enabled = false
                    a
                }

                is Command.Multiply -> if (enabled) {a + computeCommand(b)} else a
            }
        }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)
    val part2 = part2(testInput)
    check(part2 == 48)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

private sealed class Command {
    data object Do: Command()
    data object DoNot: Command()
    data class Multiply(val a: Int, val b: Int): Command()
}