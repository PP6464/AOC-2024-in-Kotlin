fun main() {
	fun part1(input : List<String>) : Long {
		val numbers = input.map(String::toLong).toMutableList()
		
		repeat(2000) {
			for ((i, n) in numbers.withIndex()) {
				val firstNum = (n xor n * 64) % 16777216
				val secondNum = (firstNum xor firstNum / 32) % 16777216
				val thirdNum = (secondNum xor secondNum * 2048) % 16777216
				
				numbers[i] = thirdNum
			}
		}
		
		return numbers.sum()
	}
	
	fun part2(input : List<String>) : Long {
		val numbers = input.map { mutableListOf(it.toLong()) }.toMutableList()
		
		repeat(2000) {
			for ((i, n) in numbers.withIndex()) {
				val firstNum = (n.last() xor n.last() * 64) % 16777216
				val secondNum = (firstNum xor firstNum / 32) % 16777216
				val thirdNum = (secondNum xor secondNum * 2048) % 16777216
				
				numbers[i] += thirdNum
			}
		}
		
		val prices = numbers.map { it.map { n -> n % 10 } }
		val changes = mutableListOf<List<Long>>()
		
		for (price in prices) {
			changes.add(price.zipWithNext { a, b -> b - a })
		}
		
		val cache = mutableSetOf<Pair<List<Long>, Long>>()
		
		for (i in 0..changes.first().size - 5) {
			println(i)
			val changesAtIndex = changes.map { it.subList(i, i + 4) }
			
			for (changeList in changesAtIndex) {
				if (changeList in cache.map { it.first }) continue
				
				var sellPrice = 0L
				
				for ((index, change) in changes.withIndex()) {
					var firstPlace = -1
					
					for (j in 0..change.size - 5) {
						if (change.subList(j, j + 4) == changeList) {
							firstPlace = j
							break
						}
					}
					
					if (firstPlace >= 0) {
						sellPrice += prices[index][firstPlace + 4]
					}
				}
				
				cache.add(changeList to sellPrice)
			}
		}
		
		return cache.maxOf { it.second }
	}
	
	val input = readInput("Day22")
	part1(input).println()
	part2(input).println()
}