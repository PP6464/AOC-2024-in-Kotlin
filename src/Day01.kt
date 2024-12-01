import kotlin.math.*

fun main() {
	fun part1(input : List<String>) : Int {
		val list1 = input.map { it.split("   ")[0].toInt() }.sorted()
		val list2 = input.map { it.split("   ")[1].toInt() }.sorted()
		return list1.zip(list2).sumOf { (it.second - it.first).absoluteValue }
	}
	
	fun part2(input : List<String>) : Int {
		val list1 = input.map { it.split("   ")[0].toInt() }.sorted()
		val list2 = input.map { it.split("   ")[1].toInt() }.sorted()
		return list1.sumOf { it * list2.count { x -> x == it } }
	}
	
	val input = readInput("Day01")
	part1(input).println()
	part2(input).println()
}