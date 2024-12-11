import java.math.BigInteger

fun main() {
	fun part1(input : List<String>) : BigInteger {
		val numbers = input[0].split(" ").map(String::toBigInteger)
		
		val occurrenceMap = mutableMapOf<BigInteger, BigInteger>() // number on stones to number of times it occurs
		
		for (num in numbers) {
			occurrenceMap[num] = occurrenceMap[num]?.let { it + BigInteger.ONE } ?: BigInteger.ONE
		}
		
		repeat(25) { _ ->
			val occurrenceMapCopy = occurrenceMap.toMap()
			
			for ((number, occurrences) in occurrenceMapCopy.entries) {
				// The following reduces the count in occurrenceMap by occurrences
				// We don't just remove because this would cause even newly added instances to be removed
				// E.g. 10 would be mapped to 1 and 0, but if then we had 0, and removed all the 0, we would also lose the new 0
				occurrenceMap[number] = occurrenceMap[number]?.let { it - occurrences } ?: BigInteger.ZERO
				if (occurrenceMap[number] == BigInteger.ZERO) {
					occurrenceMap.remove(number)
				}
				
				if (number == BigInteger.ZERO) {
					occurrenceMap[BigInteger.ONE] = occurrenceMap[BigInteger.ONE]?.let { it + occurrences } ?: occurrences
					continue
				}
				
				val digs = number.toString()
				
				if (digs.length % 2 == 0) {
					// Split into two
					val newNumbers = digs.chunked(digs.length / 2).map(String::toBigInteger)
					
					occurrenceMap[newNumbers[0]] = occurrenceMap[newNumbers[0]]?.let { it + occurrences } ?: occurrences
					occurrenceMap[newNumbers[1]] = occurrenceMap[newNumbers[1]]?.let { it + occurrences } ?: occurrences
				} else {
					// Multiply by 2024
					occurrenceMap[number * BigInteger.valueOf(2024)] = occurrenceMap[number * BigInteger.valueOf(2024)]?.let { it + occurrences } ?: occurrences
				}
			}
		}
		
		return occurrenceMap.values.reduce { a, b -> a + b }
	}
	
	fun part2(input : List<String>) : BigInteger {
		val numbers = input[0].split(" ").map(String::toBigInteger)
		
		val occurrenceMap = mutableMapOf<BigInteger, BigInteger>() // number on stones to number of times it occurs
		
		for (num in numbers) {
			occurrenceMap[num] = occurrenceMap[num]?.let { it + BigInteger.ONE } ?: BigInteger.ONE
		}
		
		repeat(75) { _ ->
			val occurrenceMapCopy = occurrenceMap.toMap()
			
			for ((number, occurrences) in occurrenceMapCopy.entries) {
				// The following reduces the count in occurrenceMap by occurrences
				// We don't just remove because this would cause even newly added instances to be removed
				// E.g. 10 would be mapped to 1 and 0, but if then we had 0, and removed all the 0, we would also lose the new 0
				occurrenceMap[number] = occurrenceMap[number]?.let { it - occurrences } ?: BigInteger.ZERO
				if (occurrenceMap[number] == BigInteger.ZERO) {
					occurrenceMap.remove(number)
				}
				
				if (number == BigInteger.ZERO) {
					occurrenceMap[BigInteger.ONE] = occurrenceMap[BigInteger.ONE]?.let { it + occurrences } ?: occurrences
					continue
				}
				
				val digs = number.toString()
				
				if (digs.length % 2 == 0) {
					// Split into two
					val newNumbers = digs.chunked(digs.length / 2).map(String::toBigInteger)
					
					occurrenceMap[newNumbers[0]] = occurrenceMap[newNumbers[0]]?.let { it + occurrences } ?: occurrences
					occurrenceMap[newNumbers[1]] = occurrenceMap[newNumbers[1]]?.let { it + occurrences } ?: occurrences
				} else {
					// Multiply by 2024
					occurrenceMap[number * BigInteger.valueOf(2024)] = occurrenceMap[number * BigInteger.valueOf(2024)]?.let { it + occurrences } ?: occurrences
				}
			}
		}
		
		return occurrenceMap.values.reduce { a, b -> a + b }
	}
	
	val input = readInput("Day11")
	part1(input).println()
	part2(input).println()
}