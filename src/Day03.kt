import kotlin.text.*

fun main() {
	val mulRegex = Regex("mul\\(\\d{1,3},\\d{1,3}\\)")
	val mulDoDontRegex = Regex("mul\\(\\d{1,3},\\d{1,3}\\)|do\\(\\)|don't\\(\\)")
	
	fun part1(input : List<String>) : Int {
		val allInput = input.joinToString("")
		return mulRegex.findAll(allInput).sumOf {
			val integers = it.value.split(",")
			integers[0].substring(4).toInt() * integers[1].takeWhile { c -> c != ')' }.toInt()
		}
	}
	
	fun part2(input : List<String>) : Int {
		val allInput = input.joinToString("")
		
		var total = 0
		var `do` = true
		
		for (match in mulDoDontRegex.findAll(allInput)) {
			if (match.value == "do()") {
				`do` = true
			} else if (match.value == "don't()") {
				`do` = false
			} else if (`do`) {
				val integers = match.value.split(",")
				total += integers[0].substring(4).toInt() * integers[1].takeWhile { c -> c != ')' }.toInt()
			}
		}
		
		return total
	}
	
	val input = readInput("Day03")
	part1(input).println()
	part2(input).println()
}