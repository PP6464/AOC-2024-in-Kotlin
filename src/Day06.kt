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