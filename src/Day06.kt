fun isLooping(input : List<String>) : Boolean {
	val width = input[0].length
	val height = input.size
	var curPt = input.gridIndicesOf('^')[0]
	var curDir = Steps.UP
	
	val visitedSoFar = mutableSetOf(curPt to curDir)
	
	while (true) {
		if (input.gridQuery(curDir.nextCoord(curPt)) in listOf('#', 'O')) {
			curDir = curDir.turnRight()
		} else if (!visitedSoFar.add(curDir.nextCoord(curPt) to curDir)) {
			return true
		} else {
			curPt = curDir.nextCoord(curPt)
		}
		
		if (curDir.nextCoord(curPt).first !in 0..<width || curDir.nextCoord(curPt).second !in 0..<height) {
			break
		}
	}
	
	return false
}

fun main() {
	fun part1(input : List<String>) : Int {
		val width = input[0].length
		val height = input.size
		
		val visited = mutableSetOf(input.gridIndicesOf('^')[0])
		var dirn = Steps.UP
		var current = input.gridIndicesOf('^')[0]
		
		while (dirn.nextCoord(current).first in 0..<width && dirn.nextCoord(current).second in 0..<height) {
			if (input.gridQuery(dirn.nextCoord(current)) == '#') {
				dirn = dirn.turnRight()
			} else {
				current = dirn.nextCoord(current)
			}
			
			visited.add(current)
		}
		
		return visited.size
	}
	
	fun part2(input : List<String>) : Int {
		val width = input[0].length
		val height = input.size
		val places = mutableSetOf<Pair<Int, Int>>()
		
		val origPath = mutableSetOf(input.gridIndicesOf('^')[0] to Steps.UP)
		var dirn = Steps.UP
		var current = input.gridIndicesOf('^')[0]
		
		while (dirn.nextCoord(current).first in 0..<width && dirn.nextCoord(current).second in 0..<height) {
			if (input.gridQuery(dirn.nextCoord(current)) == '#') {
				dirn = dirn.turnRight()
			} else {
				current = dirn.nextCoord(current)
				origPath.add(current to dirn)
			}
		}
		
		// Try placing an obstruction in front of every element in origPath and see if it gives you a loop
//		for (p in origPath) { // Bear in mind p is the coord tied with the direction taken to reach this place
//			if (input.gridQuery(p.first) == '^' && p.second == Steps.UP) {
//				// We are at the start and going upwards, so can't place an obstacle here
//				continue
//			}
//
//			val pathDirn = p.second
//
//			if (pathDirn.nextCoord(p.first).first !in 0..<width || pathDirn.nextCoord(p.first).second !in 0..<height) {
//				break
//			}
//
//			if (input.gridQuery(pathDirn.nextCoord(p.first)) in listOf('#', '^')) {
//				continue // There is an obstruction there anyway, or the next char is the start, which we can't replace
//			}
//
//			// Replace the character in front of the current one with an obstacle
//			val placeToReplaceWith = pathDirn.nextCoord(p.first)
//
//			inputCopy[placeToReplaceWith.second] =
//				inputCopy[placeToReplaceWith.second]
//					.replaceRange(placeToReplaceWith.first..placeToReplaceWith.first, "O")
//
//			if (isLooping(inputCopy)) {
//				places.add(placeToReplaceWith)
//			}
//
//			inputCopy = input.toMutableList()
//		}
		for (x in 0..<width) {
			for (y in 0..<height) {
				if (input.gridQuery(x to y) != '.') continue
				
				val inputCopy = input.toMutableList()
				
				inputCopy[y] = inputCopy[y].replaceRange(x..x, "O")
				
				if (isLooping(inputCopy)) {
					places.add(x to y)
				}
			}
		}
		
		return places.size
	}
	
	val input = readInput("Day06")
	part1(input).println()
	part2(input).println()
}