data class MemoryAddress(val pos : Pair<Int, Int>)

fun main() {
	fun part1(input : List<String>) : Long {
		// There is literally only one path
		val start = input.gridIndicesOf('S')[0]
		val path = mutableListOf(MemoryAddress(start))
		var total = 0L
		
		while (input.gridQuery(path.last().pos) != 'E') {
			val neighbourStep =
				Steps.entries.single {
					input.gridQuery(it.nextCoord(path.last().pos)) in listOf('.', 'E') && MemoryAddress(
						it.nextCoord(
							path.last().pos
						)
					) !in path
				}
			
			val nextElement = MemoryAddress(neighbourStep.nextCoord(path.last().pos))
			
			path.add(nextElement)
		}
		
		for (i in path.indices) {
			for (j in i + 1..<path.size) {
				val canCheat =
					(path[i].pos.first == path[j].pos.first || path[i].pos.second == path[j].pos.second) && cartesianDist(
						path[i].pos,
						path[j].pos
					) == 2
				
				if (canCheat) {
					val timeSaved = j - i - 2
					if (timeSaved >= 100) {
						total ++
					}
				}
			}
		}
		
		return total
	}
	
	fun part2(input : List<String>) : Long {
		// There is literally only one path
		val start = input.gridIndicesOf('S')[0]
		val path = mutableListOf(MemoryAddress(start))
		var total = 0L
		
		while (input.gridQuery(path.last().pos) != 'E') {
			val neighbourStep =
				Steps.entries.single {
					input.gridQuery(it.nextCoord(path.last().pos)) in listOf('.', 'E') && MemoryAddress(
						it.nextCoord(
							path.last().pos
						)
					) !in path
				}
			
			val nextElement = MemoryAddress(neighbourStep.nextCoord(path.last().pos))
			
			path.add(nextElement)
		}
		
		for (i in path.indices) {
			for (j in i + 1..<path.size) {
				val canCheat = cartesianDist(path[i].pos, path[j].pos) <= 20
				
				if (canCheat) {
					val timeSaved = j - i - cartesianDist(path[i].pos, path[j].pos)
					if (timeSaved >= 100) {
						total ++
					}
				}
			}
		}
		
		return total
	}
	
	val input = readInput("Day20")
	part1(input).println()
	part2(input).println()
}