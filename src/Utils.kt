import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.*

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name : String) = Path("src/inputs/$name.txt").readText().trim().lines()

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun <T> List<T>.toPair() : Pair<T, T> {
	assert(size == 2)
	
	return get(0) to get(1)
}

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
	
	fun isOpposite(other : Directions) : Boolean {
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

// Steps are like Directions but for when you can't go diagonally
enum class Steps {
	UP,
	DOWN,
	LEFT,
	RIGHT;
	
	fun nextCoord(coord : Pair<Int, Int>) : Pair<Int, Int> {
		return when (this) {
			UP -> coord.first to coord.second - 1
			DOWN -> coord.first to coord.second + 1
			LEFT -> coord.first - 1 to coord.second
			RIGHT -> coord.first + 1 to coord.second
		}
	}
	
	fun turnRight() : Steps {
		return when (this) {
			UP -> RIGHT
			DOWN -> LEFT
			LEFT -> UP
			RIGHT -> DOWN
		}
	}
	
	fun turnLeft() : Steps {
		return when (this) {
			UP -> LEFT
			DOWN -> RIGHT
			LEFT -> DOWN
			RIGHT -> UP
		}
	}
	
	fun opposite() : Steps {
		return when (this) {
			UP -> DOWN
			DOWN -> UP
			LEFT -> RIGHT
			RIGHT -> LEFT
		}
	}
	
	companion object {
		fun isAdjacent(a : Pair<Int, Int>, b : Pair<Int, Int>) : Boolean {
			return (a.first - b.first).absoluteValue + (a.second - b.second).absoluteValue == 1
		}
		
		fun dirnBetween(a : Pair<Int, Int>, b : Pair<Int, Int>) : Steps {
			return if ((a.first - b.first).absoluteValue + (a.second - b.second).absoluteValue != 1) {
				throw Exception("The points must be adjacent")
			} else if (a.second - b.second == 1) {
				UP
			} else if (b.second - a.second == 1) {
				DOWN
			} else if (b.first - a.first == 1) {
				RIGHT
			} else {
				LEFT
			}
		}
	}
}