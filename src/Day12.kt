import java.math.*

fun findRegion(seed : Pair<Int, Int>, input : List<String>, coveredSoFar : Set<Pair<Int, Int>>) : Set<Pair<Int, Int>> {
	val width = input[0].length
	val height = input.size
	val region = mutableSetOf(seed)
	val startChar = input.gridQuery(seed)
	
	for (step in Steps.entries) {
		val nextCoord = step.nextCoord(seed)
		
		if (nextCoord in coveredSoFar) continue // Don't want to be stuck in an infinite recursion loop
		
		if (nextCoord.first !in 0..<width || nextCoord.second !in 0..<height) {
			continue
		} else if (input.gridQuery(nextCoord) != startChar) {
			continue
		} else {
			region.addAll(findRegion(nextCoord, input, region + coveredSoFar))
		}
	}
	
	return region
}

fun main() {
	fun part1(input : List<String>) : Int {
		val regions = mutableListOf<Set<Pair<Int, Int>>>()
		val width = input[0].length
		val height = input.size
		var totalPrice = 0
		
		// Create all the regions
		for (i in 0..<width) {
			for (j in 0..<height) {
				val curPt = i to j
				var inRegion = false
				
				for (region in regions) {
					if (curPt in region) {
						inRegion = true
						break
					}
				}
				
				if (! inRegion) {
					regions.add(findRegion(curPt, input, emptySet()))
				}
			}
		}
		
		for (region in regions) {
			if (input.gridQuery(region.first()) == '.') continue
			
			var perimeter = 0
			val area = region.size
			
			for (plant in region) {
				for (step in Steps.entries) {
					val nextCoord = step.nextCoord(plant)
					
					if (nextCoord.first !in 0..<width || nextCoord.second !in 0..<height) {
						perimeter += 1
						continue
					}
					
					if (input.gridQuery(nextCoord) != input.gridQuery(plant)) {
						perimeter += 1
					}
				}
			}
			
			totalPrice += perimeter * area
		}
		
		return totalPrice
	}
	
	fun part2(input : List<String>) : BigInteger {
		val regions = mutableListOf<Set<Pair<Int, Int>>>()
		val width = input[0].length
		val height = input.size
		var totalPrice = BigInteger.ZERO
		
		// Create all the regions
		for (i in 0..<width) {
			for (j in 0..<height) {
				val curPt = i to j
				var inRegion = false
				
				for (region in regions) {
					if (curPt in region) {
						inRegion = true
						break
					}
				}
				
				if (! inRegion) {
					regions.add(findRegion(curPt, input, emptySet()))
				}
			}
		}
		
		for (region in regions) {
			if (input.gridQuery(region.first()) == '.') continue
			
			val area = region.size
			val corners = mutableListOf<Pair<Int, Int>>()
			
			val altInput =
				input.withIndex().map { (y, s) -> s.withIndex().map { (x, _) -> if (x to y in region) 1 else - 1 } }
			
			// A very questionable way: convolution with kernels for corners
			val cornerMatrices = listOf(
				// Checks for concave in top right
				listOf(
					listOf(1, - 1),
					listOf(1, 1),
				),
				// Concave in top left
				listOf(
					listOf(- 1, 1),
					listOf(1, 1),
				),
				// Concave in bottom left
				listOf(
					listOf(1, 1),
					listOf(- 1, 1),
				),
				// Concave in bottom right
				listOf(
					listOf(1, 1),
					listOf(1, - 1),
				),
				// Convex in bottom-left
				listOf(
					listOf(- 1, 0),
					listOf(1, - 1),
				),
				// Convex in bottom-right
				listOf(
					listOf(0, - 1),
					listOf(- 1, 1),
				),
				// Convex in top-right
				listOf(
					listOf(- 1, 1),
					listOf(0, - 1),
				),
				// Convex in top-left
				listOf(
					listOf(1, - 1),
					listOf(- 1, 0),
				),
			)
			
			var cornerCount = 0
			
			for (i in 0..7) {
				for (x in 0..<altInput[0].size - 1) {
					for (y in 0..<altInput.size - 1) {
						val overlapTopLeft = x to y
						val matrix = cornerMatrices[i]
						val requiredScore = if (i < 4) 4 else 3 // Concave corners require 4, convex corners require 3
						
						val actualScore = matrix[0][0] * altInput[overlapTopLeft.second][overlapTopLeft.first] +
								matrix[0][1] * altInput[overlapTopLeft.second][overlapTopLeft.first + 1] +
								matrix[1][0] * altInput[overlapTopLeft.second + 1][overlapTopLeft.first] +
								matrix[1][1] * altInput[overlapTopLeft.second + 1][overlapTopLeft.first + 1]
						
						if (requiredScore == actualScore) {
							cornerCount += 1
							corners.add(overlapTopLeft)
						}
					}
				}
			}
			
			totalPrice += (area * cornerCount).toBigInteger()
		}
		
		
		return totalPrice
	}
	
	val input = readInput("Day12")
	part1(input).println()
	part2(input).println()
}