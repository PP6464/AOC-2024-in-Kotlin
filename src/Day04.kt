fun main() {
	fun part1(input : List<String>) : Int {
		val width = input[0].length
		val height = input.size
		var appearances = 0
		
		val xPlaces = input.gridIndicesOf('X')
		
		// For each X, check how many XMAS there is
		for (xPlace in xPlaces) {
			val possibleDirections = Directions.entries.toMutableList()
			
			// Check x-coordinate bounds
			if (xPlace.first <= 2) {
				// Can't go any further left eventually
				possibleDirections.remove(Directions.LEFT)
				possibleDirections.remove(Directions.UP_LEFT)
				possibleDirections.remove(Directions.DOWN_LEFT)
			} else if (xPlace.first >= width - 3) {
				// Can't go any further right eventually
				possibleDirections.remove(Directions.RIGHT)
				possibleDirections.remove(Directions.UP_RIGHT)
				possibleDirections.remove(Directions.DOWN_RIGHT)
			}
			
			// Check y-coordinate bounds
			if (xPlace.second <= 2) {
				// Can't go any further up eventually
				possibleDirections.remove(Directions.UP)
				possibleDirections.remove(Directions.UP_RIGHT)
				possibleDirections.remove(Directions.UP_LEFT)
			} else if (xPlace.second >= height - 3) {
				// Can't go any further down eventually
				possibleDirections.remove(Directions.DOWN)
				possibleDirections.remove(Directions.DOWN_LEFT)
				possibleDirections.remove(Directions.DOWN_RIGHT)
			}
			
			for (direction in possibleDirections) {
				var newCoords = direction.newCoordinates(xPlace)
				
				if (input.gridQuery(newCoords) == 'M') {
					newCoords = direction.newCoordinates(newCoords)
					
					if (input.gridQuery(newCoords) == 'A') {
						newCoords = direction.newCoordinates(newCoords)
						
						if (input.gridQuery(newCoords) == 'S') {
							appearances += 1
						}
					}
				}
			}
		}
		
		return appearances
	}
	
	fun part2(input : List<String>) : Int {
		val width = input[0].length
		val height = input.size
		val aPlaces = input.gridIndicesOf('A')
		val diagonalDirections =
			listOf(Directions.UP_LEFT, Directions.UP_RIGHT, Directions.DOWN_RIGHT, Directions.DOWN_LEFT)
		var appearances = 0
		
		for (aPlace in aPlaces) {
			if (aPlace.first in 1..<width-1) {
				if (aPlace.second in 1..<height-1) {
					val mDirns = mutableListOf<Directions>()
					val sDirns = mutableListOf<Directions>()
					
					for (dirn in diagonalDirections) {
						if (input.gridQuery(dirn.newCoordinates(aPlace)) == 'M') {
							mDirns.add(dirn)
						} else if (input.gridQuery(dirn.newCoordinates(aPlace)) == 'S') {
							sDirns.add(dirn)
						}
					}
					
					if (mDirns.size == 2 && sDirns.size == 2) {
						val mDirnsOpposite = mDirns[1].isOpposite(mDirns[0])
						val sDirnsOpposite = sDirns[1].isOpposite(sDirns[0])
						
						if (!mDirnsOpposite && !sDirnsOpposite) {
							appearances += 1
						}
					}
				}
			}
		}
		
		return appearances
	}
	
	val input = readInput("Day04")
	part1(input).println()
	part2(input).println()
}