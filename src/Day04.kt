enum class Directions {
	// They're ordered this way so that the sum of opposite direction's ordinals = 7
	UP,
	RIGHT,
	UP_RIGHT,
	UP_LEFT,
	DOWN_RIGHT,
	DOWN_LEFT,
	LEFT,
	DOWN;
	
	private fun newCoordinates(x : Int, y : Int) : Pair<Int, Int> {
		return when (this) {
			UP -> x to y - 1
			DOWN -> x to y + 1
			RIGHT -> x + 1 to y
			LEFT -> x - 1 to y
			UP_RIGHT -> x + 1 to y - 1
			DOWN_RIGHT -> x + 1 to y + 1
			DOWN_LEFT -> x - 1 to y + 1
			UP_LEFT -> x - 1 to y - 1
		}
	}
	
	fun newCoordinates(coords : Pair<Int, Int>) : Pair<Int, Int> {
		return newCoordinates(coords.first, coords.second)
	}
	
	fun isOpposite(other: Directions) : Boolean {
		return ordinal + other.ordinal == 7
	}
}

// Queries a list of rows at column x (0-indexed) and row y (0-indexed where top = 0)
fun List<String>.gridQuery(coords : Pair<Int, Int>) : Char = get(coords.second)[coords.first]

fun List<String>.gridIndicesOf(c : Char) : List<Pair<Int, Int>> {
	val gridIndices = mutableListOf<Pair<Int, Int>>()
	
	for (y in indices) {
		for (x in 0..<get(y).length) {
			if (get(y)[x] == c) {
				gridIndices.add(x to y)
			}
		}
	}
	
	return gridIndices
}

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