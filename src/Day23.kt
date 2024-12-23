import com.github.michaelbull.itertools.*

fun main() {
	fun part1(input : List<String>) : Int {
		val connections = input.map { it.split("-").toSet() }
		val groups = mutableSetOf<Set<String>>()
		
		for (i in 0..connections.size - 3) {
			println(i)
			
			for (j in i+1..connections.size - 2) {
				for (k in j+1..<connections.size) {
					val elems = (connections[i] + connections[j] + connections[k]).toSet()
					if (elems.size == 3) {
						// Three distinct connections, so can form a group
						groups.add(elems)
					}
				}
			}
		}
		
		return groups.count { it.count { s -> s.first() == 't' } >= 1 }
	}
	
	fun part2(input : List<String>) : String {
		var largestClique = emptySet<String>()
		val connections = input.map { it.split("-").toSet() }
		val computers = connections.flatten().toSet()
		
		println("Computers size: ${computers.size}")
		
		for ((index, computer) in computers.withIndex()) {
			println("Index: $index")
			
			val links = connections.filter { it.contains(computer) }
			val others = links.map { it.single { e -> e != computer } }
			var itsLargestClique = setOf(computer)
			
			for (i in others.size downTo (largestClique.size).coerceAtLeast(2)) {
				val combos = others.combinations(i)
				var formedClique = false
				
				for (combo in combos) {
					var formsClique = true
					val pairs = combo.pairCombinations()
					
					for (pair in pairs) {
						if (!connections.contains(setOf(pair.first, pair.second))) {
							formsClique = false
							break
						}
					}
					
					if (formsClique) {
						itsLargestClique = combo.toSet() + computer
						formedClique = true
						break
					}
				}
				
				if (formedClique) break
			}
			
			if (itsLargestClique.size == 1) {
				itsLargestClique = setOf(computer, others.first()) // You can definitely form a clique of size 2
			}
			
			if (largestClique.size < itsLargestClique.size) {
				largestClique = itsLargestClique
			}
		}
		
		return largestClique.toList().sorted().joinToString(",")
	}
	
	val input = readInput("Day23")
	part1(input).println()
	part2(input).println()
}