import kotlin.math.*

fun main() {
	fun reportIsSafe(report : String) : Boolean {
		val levels = report.split(" ").map(String::toInt)
		val sign = (levels[1] - levels[0]).sign
		
		return levels
			.zipWithNext()
			.map { p -> p.second - p.first }
			.all { d -> d.absoluteValue in 1..3 && d.sign == sign }
	}
	
	fun reportIsSafe(levels : List<Int>) : Boolean = levels
		.zipWithNext()
		.map { p -> p.second - p.first }
		.all { d -> d.absoluteValue in 1..3 && d.sign == (levels[1] - levels[0]).sign }
	
	fun part1(input : List<String>) : Int {
		return input.count(::reportIsSafe)
	}
	
	fun part2(input : List<String>) : Int {
		return input.count {
			if (reportIsSafe(it)) {
				return@count true
			} else {
				val levels = it.split(" ")
				
				for (i in levels.indices) {
					if (reportIsSafe(levels.withIndex().filter { (idx, _) -> idx != i }.map { (_, v) -> v.toInt() })) {
						return@count true
					}
				}
				
				false
			}
		}
	}
	
	val input = readInput("Day02")
	part1(input).println()
	part2(input).println()
}