fun main() {
	fun part1(input : List<String>) : Int {
		val splitPlace = input.withIndex().first { it.value.isEmpty() }.index
		val grid = input.subList(0, splitPlace).toMutableList()
		val moves = input.subList(splitPlace + 1, input.size).joinToString("") { it.removeSuffix("\n") }
		
		val width = grid[0].length
		val height = grid.size
		
		for (move in moves) {
			val (x, y) = grid.gridIndicesOf('@')[0]
			
			when (move) {
				'<' -> {
					val indexToMove = grid[y].substring(0..x).lastIndexOf('.')
					if (indexToMove > 0 && '#' !in grid[y].substring(indexToMove, x)) {
						val newString = grid[y].toMutableList()
						newString.add(x + 1, '.')
						newString.removeAt(indexToMove)
						
						grid[y] = newString.joinToString("")
					}
				}
				
				'^' -> {
					val indexToMove = grid.map { it[x] }.subList(0, y).lastIndexOf('.')
					if (indexToMove > 0 && '#' !in grid.map { it[x] }.subList(indexToMove, y)) {
						val newString = grid.map { it[x] }.toMutableList()
						newString.add(y + 1, '.')
						newString.removeAt(indexToMove)
						
						for (i in 0..<height) {
							grid[i] = grid[i].replaceRange(x..x, newString[i].toString())
						}
					}
				}
				
				'v' -> {
					val indexToMove = grid.map { it[x] }.subList(y + 1, height).indexOf('.')
					if (indexToMove >= 0 && '#' !in grid.map { it[x] }.subList(y + 1, y + 1 + indexToMove)) {
						val newString = grid.map { it[x] }.toMutableList()
						newString.removeAt(y + 1 + indexToMove)
						newString.add(y, '.')
						
						for (i in 0..<height) {
							grid[i] = grid[i].replaceRange(x..x, newString[i].toString())
						}
					}
				}
				
				'>' -> {
					val indexToMove = grid[y].substring(x + 1, width).indexOf('.')
					if (indexToMove >= 0 && '#' !in grid[y].substring(x + 1, x + 1 + indexToMove)) {
						val newString = grid[y].toMutableList()
						newString.removeAt(x + 1 + indexToMove)
						newString.add(x, '.')
						
						grid[y] = newString.joinToString("")
					}
				}
				
				else -> {}
			}
		}
		
		var total = 0
		
		for (good in grid.gridIndicesOf('O')) {
			total += good.first + good.second * 100
		}
		
		return total
		
	}
	
	fun part2(input : List<String>) : Int {
		val newInput = input.map {
			it.replace(".", "..")
				.replace("#", "##")
				.replace("O", "[]")
				.replace("@", "@.")
		}
		
		val splitIndex = newInput.withIndex().first { it.value.isEmpty() }.index
		
		val grid = newInput.subList(0, splitIndex).toMutableList()
		val moves = newInput.subList(splitIndex + 1, newInput.size).joinToString("") { it.removeSuffix("\n") }
		
		val width = grid[0].length
		
		fun canMove(boxPos : Pair<Int, Int> /*The position of the [*/, step : Steps) : Boolean {
			when (step) {
				Steps.UP, Steps.DOWN -> {
					if (grid.gridQuery(step.nextCoord(boxPos)) == '.' && grid.gridQuery(
							Steps.RIGHT.nextCoord(
								step.nextCoord(
									boxPos
								)
							)
						) == '.'
					) {
						return true
					}
					
					if (grid.gridQuery(step.nextCoord(boxPos)) == '#' || grid.gridQuery(
							Steps.RIGHT.nextCoord(
								step.nextCoord(
									boxPos
								)
							)
						) == '#'
					) {
						return false
					}
					
					if (grid.gridQuery(step.nextCoord(boxPos)) == '[') {
						return canMove(step.nextCoord(boxPos), step)
					}
					
					val leftAbove = grid.gridQuery(step.nextCoord(boxPos))
					val rightAbove = grid.gridQuery(Steps.RIGHT.nextCoord(step.nextCoord(boxPos)))
					
					return (leftAbove == '.' || canMove(
						Steps.LEFT.nextCoord(step.nextCoord(boxPos)),
						step
					)) && (rightAbove == '.' || canMove(step.nextCoord(Steps.RIGHT.nextCoord(boxPos)), step))
				}
				
				else -> return true // this fn is only for checking vertically, not horizontally
			}
		}
		
		fun move(boxPos : Pair<Int, Int>, step : Steps) {
			when (step) {
				Steps.UP, Steps.DOWN -> {
					val nextCoord = step.nextCoord(boxPos)
					if (grid.gridQuery(nextCoord) == '.' && grid.gridQuery(
							Steps.RIGHT.nextCoord(
								nextCoord
							)
						) == '.'
					) {
						grid[nextCoord.second] = grid[nextCoord.second].replaceRange(boxPos.first..boxPos.first + 1, "[]")
						grid[boxPos.second] = grid[boxPos.second].replaceRange(boxPos.first..boxPos.first + 1, "..")
						
						return
					}
					
					if (grid.gridQuery(nextCoord) == '#' || grid.gridQuery(
							Steps.RIGHT.nextCoord(
								nextCoord
							)
						) == '#'
					) {
						return
					}
					
					if (grid.gridQuery(nextCoord) == '[') {
						move(nextCoord, step)
						move(boxPos, step)
						
						return
					}
					
					val leftAbove = grid.gridQuery(nextCoord)
					val rightAbove = grid.gridQuery(Steps.RIGHT.nextCoord(nextCoord))
					
					if (leftAbove == '.') {
						move(Steps.RIGHT.nextCoord(nextCoord), step)
						move(boxPos, step)
					} else if (rightAbove == '.') {
						move(Steps.LEFT.nextCoord(nextCoord), step)
						move(boxPos, step)
					} else {
						move(Steps.LEFT.nextCoord(nextCoord), step)
						move(Steps.RIGHT.nextCoord(nextCoord), step)
						move(boxPos, step)
					}
				}
				
				else -> {}
			}
		}
		
		for (move in moves) {
			val (x, y) = grid.gridIndicesOf('@')[0]
			
			when (move) {
				'<' -> {
					val indexToMove = grid[y].substring(0..x).lastIndexOf('.')
					if (indexToMove > 0 && '#' !in grid[y].substring(indexToMove, x)) {
						val newString = grid[y].toMutableList()
						newString.add(x + 1, '.')
						newString.removeAt(indexToMove)
						
						grid[y] = newString.joinToString("")
					}
				}
				
				'^' -> {
					if (grid[y - 1][x] == '.') {
						grid[y - 1] = grid[y - 1].replaceRange(x..x, "@")
						grid[y] = grid[y].replaceRange(x..x, ".")
					} else if (grid[y - 1][x] == '[' && canMove(x to y - 1, Steps.UP)) {
						move(x to y - 1, Steps.UP)
						grid[y - 1] = grid[y - 1].replaceRange(x..x, "@")
						grid[y] = grid[y].replaceRange(x..x, ".")
					} else if (grid[y - 1][x] == ']' && canMove(x - 1 to y - 1, Steps.UP)) {
						move(x - 1 to y - 1, Steps.UP)
						grid[y - 1] = grid[y - 1].replaceRange(x..x, "@")
						grid[y] = grid[y].replaceRange(x..x, ".")
					}
				}
				
				'v' -> {
					if (grid[y + 1][x] == '.') {
						grid[y + 1] = grid[y + 1].replaceRange(x..x, "@")
						grid[y] = grid[y].replaceRange(x..x, ".")
					} else if (grid[y + 1][x] == '[' && canMove(x to y + 1, Steps.DOWN)) {
						move(x to y + 1, Steps.DOWN)
						grid[y + 1] = grid[y + 1].replaceRange(x..x, "@")
						grid[y] = grid[y].replaceRange(x..x, ".")
					} else if (grid[y + 1][x] == ']' && canMove(x - 1 to y + 1, Steps.DOWN)) {
						move(x - 1 to y + 1, Steps.DOWN)
						grid[y + 1] = grid[y + 1].replaceRange(x..x, "@")
						grid[y] = grid[y].replaceRange(x..x, ".")
					}
				}
				
				'>' -> {
					val indexToMove = grid[y].substring(x + 1, width).indexOf('.')
					if (indexToMove >= 0 && '#' !in grid[y].substring(x + 1, x + 1 + indexToMove)) {
						val newString = grid[y].toMutableList()
						newString.removeAt(x + 1 + indexToMove)
						newString.add(x, '.')
						
						grid[y] = newString.joinToString("")
					}
				}
				
				else -> {}
			}
		}
		
		var total = 0
		
		for ((x, y) in grid.gridIndicesOf('[')) {
			total += x + 100 * y
		}
		
		return total
	}
	
	val input = readInput("Day15")
	part1(input).println()
	part2(input).println()
}