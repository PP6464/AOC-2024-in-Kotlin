// Gives a list of trails given a starting pt
fun traverse(map : List<String>, start : Pair<Int, Int>) : List<List<Pair<Int, Int>>> {
	val width = map[0].length
	val height = map.size
	val paths = mutableListOf<List<Pair<Int, Int>>>()
	val startHeight = map.gridQuery(start).digitToInt()
	
	for (step in Steps.entries) {
		val nextCoord = step.nextCoord(start)
		
		if (nextCoord.first !in 0..<width || nextCoord.second !in 0..<height) continue // Would go out of the map
		
		if (startHeight == 8 && map.gridQuery(nextCoord) == '9') {
			// This is a valid end to the path
			paths.add(listOf(nextCoord))
		} else {
			// Find all the adjacent pts with a height one greater
			if (map.gridQuery(nextCoord).digitToInt() - startHeight == 1) {
				val pathsOfOption = traverse(map, nextCoord)
				
				for (path in pathsOfOption) {
					paths.add(listOf(nextCoord) + path)
				}
			}
		}
	}
	
	return paths
}

fun main() {
	fun part1(input : List<String>) : Int {
		val trailHeads = input.gridIndicesOf('0')
		var totalScore = 0
		
		for (head in trailHeads) {
			totalScore += traverse(input, head).map { it.last() }.toSet().size
		}
		
		return totalScore
	}
	
	fun part2(input : List<String>) : Int {
		val trailHeads = input.gridIndicesOf('0')
		var totalRating = 0
		
		for (head in trailHeads) {
			totalRating += traverse(input, head).size
		}
		
		return totalRating
	}
	
	val input = readInput("Day10")
	part1(input).println()
	part2(input).println()
}