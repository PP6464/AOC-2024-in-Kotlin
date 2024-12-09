import java.math.*

fun main() {
	fun part1(input : List<String>) : BigInteger {
		val denseText = input[0]
		
		val actualFile : MutableList<String> = mutableListOf()
		
		for ((i, c) in denseText.withIndex()) {
			if (i % 2 == 0) {
				repeat(c.digitToInt()) {
					actualFile += ((i / 2).toString())
				}
			} else {
				repeat(c.digitToInt()) {
					actualFile += "."
				}
			}
		}
		
		var checksum = BigInteger.ZERO
		
		for ((i, c) in actualFile.withIndex()) {
			if (c != ".") {
				checksum += (i * c.toInt()).toBigInteger()
			} else {
				try {
					// Move the last number
					val lastNumber = actualFile.withIndex().toList().subList(i + 1, actualFile.size).last { it.value != "." }
					checksum += (i * lastNumber.value.toInt()).toBigInteger()
					actualFile[lastNumber.index] = "."
				} catch (e : NoSuchElementException) {
					// No such element, so break
					break
				}
			}
		}
		
		return checksum
	}
	
	fun part2(input : List<String>) : BigInteger {
		val denseText = input[0]
		
		var actualFiles = mutableListOf<String>()
		var checksum = BigInteger.ZERO
		
		for ((i, c) in denseText.withIndex()) {
			if (i % 2 == 0) {
				actualFiles.add((1..c.digitToInt()).map { i / 2 }.joinToString(",")) // The file id repeated
			} else if (c.digitToInt() > 0) {
				actualFiles.add((1..c.digitToInt()).map { '.' }.joinToString(","))
			}
		}
		
		actualFiles = actualFiles.joinToString(",").split(",").toMutableList()
		
		// go through the files in decreasing id order and then move them
		val ids = (denseText.length - 1) / 2 downTo 0
		
		for (id in ids) {
			val fileSize = actualFiles.count { it == id.toString() }
			val origStart = actualFiles.withIndex().first { it.value == id.toString() }.index
			
			// The starting positions for free spaces
			val startingDots = actualFiles.withIndex().filter { it.value == "." && actualFiles.getOrNull(it.index - 1) != "." }
			var moveTo = -1
			
			for (startDot in startingDots) {
				var segmentLength = 1
				
				while (actualFiles.getOrNull(startDot.index + segmentLength) == ".") segmentLength++
				
				if (segmentLength >= fileSize) {
					moveTo = startDot.index
					break
				}
			}
			
			// Don't have anywhere to move to, or suboptimal to move
			if (moveTo == -1 || moveTo > origStart) {
				continue
			}
			
			// Move to the place otherwise
			for (i in 0..<fileSize) {
				actualFiles[moveTo + i] = id.toString()
				actualFiles[origStart + i] = "."
			}
		}
		
		for ((i, c) in actualFiles.withIndex()) {
			if (c != ".") checksum += (i * c.toInt()).toBigInteger()
		}
		
		return checksum
	}
	
	val input = readInput("Day09")
	part1(input).println()
	part2(input).println()
}