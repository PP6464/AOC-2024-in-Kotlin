import kotlin.math.*

// For this it is easier to use bottom left as origin
enum class NumericKeyboard {
	`0`,
	`1`,
	`2`,
	`3`,
	`4`,
	`5`,
	`6`,
	`7`,
	`8`,
	`9`,
	A;
	
	val pos
		get() = when (this) {
			`0` -> 1 to 0
			`1` -> 0 to 1
			`2` -> 1 to 1
			`3` -> 2 to 1
			`4` -> 0 to 2
			`5` -> 1 to 2
			`6` -> 2 to 2
			`7` -> 0 to 3
			`8` -> 1 to 3
			`9` -> 2 to 3
			A -> 2 to 0
		}
	
	fun toChar() : Char = when (this) {
		`0` -> '0'
		`1` -> '1'
		`2` -> '2'
		`3` -> '3'
		`4` -> '4'
		`5` -> '5'
		`6` -> '6'
		`7` -> '7'
		`8` -> '8'
		`9` -> '9'
		A -> 'A'
	}
	
	companion object {
		fun fromChar(c : Char) = when (c) {
			'0' -> `0`
			'1' -> `1`
			'2' -> `2`
			'3' -> `3`
			'4' -> `4`
			'5' -> `5`
			'6' -> `6`
			'7' -> `7`
			'8' -> `8`
			'9' -> `9`
			'A' -> A
			else -> throw Exception("Invalid char $c")
		}
	}
}

enum class DirectionKeyboard {
	LEFT,
	DOWN,
	UP,
	RIGHT,
	A;
	
	val pos
		get() = when (this) {
			LEFT -> 0 to 0
			DOWN -> 1 to 0
			UP -> 1 to 1
			RIGHT -> 2 to 0
			A -> 2 to 1
		}
	
	fun toChar() : Char = when (this) {
		LEFT -> '<'
		DOWN -> 'v'
		UP -> '^'
		RIGHT -> '>'
		A -> 'A'
	}
	
	
	companion object {
		fun fromChar(c : Char) = when (c) {
			'<' -> LEFT
			'>' -> RIGHT
			'^' -> UP
			'v' -> DOWN
			'A' -> A
			else -> throw Exception("Invalid char $c")
		}
	}
}

val getSeqCache = mutableSetOf<Pair<Triple<Boolean, Char, Char>, List<String>>>()
val solveCache = mutableSetOf<Pair<Pair<String, Long>, Long>>()

fun isOk(cmd : String, start : Pair<Long, Long>, isNum : Boolean) : Boolean {
	var curPos = start
	
	if (isNum) {
		for (c in cmd) {
			curPos = when (c) {
				'>' -> curPos.first + 1 to curPos.second
				'<' -> curPos.first - 1 to curPos.second
				'^' -> curPos.first to curPos.second + 1
				'v' -> curPos.first to curPos.second - 1
				else -> throw Exception()
			}
			
			if (curPos == 0L to 0L) {
				return false
			}
		}
	} else {
		for (c in cmd) {
			curPos = when (c) {
				'>' -> curPos.first + 1 to curPos.second
				'<' -> curPos.first - 1 to curPos.second
				'^' -> curPos.first to curPos.second + 1
				'v' -> curPos.first to curPos.second - 1
				else -> throw Exception()
			}
			
			if (curPos == 0L to 1L) {
				return false
			}
		}
	}
	
	return true
}

fun getSeq(isNum : Boolean, start : Char, end : Char) : List<String> {
	if (Triple(isNum, start, end) in getSeqCache.map { it.first }) return getSeqCache.single {
		it.first == Triple(
			isNum,
			start,
			end
		)
	}.second
	
	val (x, y) = if (isNum) NumericKeyboard.fromChar(start).pos else DirectionKeyboard.fromChar(start).pos
	val (X, Y) = if (isNum) NumericKeyboard.fromChar(end).pos else DirectionKeyboard.fromChar(end).pos
	
	var xSeq = ""
	repeat((X - x).absoluteValue) {
		xSeq += if (X > x) ">" else "<"
	}
	
	var ySeq = ""
	repeat((Y - y).absoluteValue) {
		ySeq += if (Y > y) "^" else "v"
	}
	
	val cmd1 = xSeq + ySeq
	val cmd2 = ySeq + xSeq
	val cmds = mutableListOf<String>()
	
	if (isOk(cmd1, x.toLong() to y.toLong(), isNum)) {
		cmds += cmd1
	}
	
	if (isOk(cmd2, x.toLong() to y.toLong(), isNum)) {
		cmds += cmd2
	}
	
	getSeqCache.add(Triple(isNum, start, end) to cmds)
	
	return cmds
}

fun findMinSeq(code : String, numBots : Long) : Long {
	// Find the best sequence for the ith bot
	fun solve(seq : String, i : Long) : Long {
		if (seq to i in solveCache.map { it.first }) return solveCache.single { it.first == seq to i }.second
		
		if (i == numBots) return seq.length.toLong()
		
		var start = if (i > 0) DirectionKeyboard.A else NumericKeyboard.A
		var length = 0L
		
		for (c in seq) {
			val end = if (i > 0) DirectionKeyboard.fromChar(c) else NumericKeyboard.fromChar(c)
			var subPath = -1L
			
			for (subSeq in getSeq(
				i == 0L,
				if (i > 0) (start as DirectionKeyboard).toChar() else (start as NumericKeyboard).toChar(),
				c
			)) {
				if (subPath < 0) {
					subPath = solve(subSeq + "A", i + 1L)
				} else {
					val alt = solve(subSeq + "A", i + 1L)
					
					if (alt < subPath) {
						subPath = alt
					}
				}
			}
			
			start = end
			length += subPath
		}
		
		solveCache.add(seq to i to length)
		
		return length
	}
	
	return solve(code, 0)
}

fun main() {
	fun part1(input : List<String>) : Long {
		var total = 0L
		
		for (code in input) {
			total += findMinSeq(code, 3) * code.take(3).toLong()
		}
		
		return total
	}
	
	fun part2(input : List<String>) : Long {
		var total = 0L
		
		for (code in input) {
			total += findMinSeq(code, 26) * code.take(3).toLong()
		}
		
		return total
	}
	
	val input = readInput("Day21")
	// Only call either part 1 or part 2, not both
	part1(input).println()
	part2(input).println()
}