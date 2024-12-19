fun main() {
	fun part1(input : List<String>) : Int {
		val towels = input[0].split(", ")
		
		val patterns = input.subList(2, input.size)
		
		fun isPossible(pattern : String) : Boolean {
			if (pattern == "") return true
			
			for (towel in towels) {
				if (pattern.startsWith(towel)) {
					if (isPossible(pattern.substring(towel.length))) return true
				}
			}
			
			return false
		}
		
		var total = 0
		
		for (pattern in patterns) {
			if (isPossible(pattern)) {
				total ++
			}
		}
		
		return total
	}
	
	fun part2(input : List<String>) : Long {
		val towels = input[0].split(", ")
		
		val patterns = input.subList(2, input.size)
		
		val cache = mutableListOf<Pair<String, Long>>()
		
		fun numberOfWays(pattern : String) : Long {
			if (pattern == "") return 1
			
			if (pattern in cache.map { it.first }) return cache.single { it.first == pattern }.second
			
			var count = 0L
			
			for (towel in towels) {
				if (pattern.startsWith(towel)) {
					count += numberOfWays(pattern.substring(towel.length))
				}
			}
			
			cache.add(pattern to count)
			
			return count
		}
		
		var total = 0L
		
		for (pattern in patterns) {
			println(pattern)
			total += numberOfWays(pattern)
		}
		
		return total
	}
	
	val input = readInput("Day19")
	part1(input).println()
	part2(input).println()
}