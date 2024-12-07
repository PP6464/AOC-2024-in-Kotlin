import java.math.BigInteger
import kotlin.math.*

fun main() {
	fun part1(input : List<String>) : BigInteger {
		var total = BigInteger.ZERO
		
		for (eqn in input) {
			val testValue = eqn.split(": ")[0].toBigInteger()
			val numbers = eqn.split(": ")[1].split(" ").map(String::toBigInteger)
			
			for (i in 0..<2.0.pow(numbers.size - 1).toInt()) {
				val bits = i.toString(2).padStart(numbers.size - 1, '0').toList().map(Char::digitToInt)
				
				val evaluatesTo = numbers.reduceIndexed { index, acc, j ->
					if (bits[index - 1] == 0) acc + j else acc * j
				}
				
				if (evaluatesTo == testValue) {
					total += testValue
					break
				}
			}
		}
		
		return total
	}
	
	fun part2(input : List<String>) : BigInteger {
		var total = BigInteger.ZERO
		
		for (eqn in input) {
			val testValue = eqn.split(": ")[0].toBigInteger()
			val numbers = eqn.split(": ")[1].split(" ").map(String::toBigInteger)
			
			for (i in 0..<3.0.pow(numbers.size - 1).toInt()) {
				val bits = i.toString(3).padStart(numbers.size - 1, '0').toList().map(Char::digitToInt)
				
				val evaluatesTo = numbers.reduceIndexed { index, acc, j ->
					when (bits[index - 1]) {
						0 -> acc + j
						1 -> acc * j
						2 -> (acc.toString() + j.toString()).toBigInteger()
						else -> acc
					}
				}
				
				if (evaluatesTo == testValue) {
					total += testValue
					break
				}
			}
		}
		
		return total
	}
	
	val input = readInput("Day07")
	part1(input).println()
	part2(input).println()
}