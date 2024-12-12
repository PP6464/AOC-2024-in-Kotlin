import java.math.*

fun findRegion(seed : Pair<Int, Int>, input : List<String>, coveredSoFar : Set<Pair<Int, Int>>) : Set<Pair<Int, Int>> {
	val width = input[0].length
	val height = input.size
	val region = mutableSetOf(seed)
	val startChar = input.gridQuery(seed)
	
	for (step in Steps.entries) {
		if (step.nextCoord(seed) in coveredSoFar) continue // Don't want to be stuck in an infinite recursion loop
		
		if (step.nextCoord(seed).first !in 0..<width || step.nextCoord(seed).second !in 0..<height) {
			continue
		} else if (input.gridQuery(step.nextCoord(seed)) != startChar) {
			continue
		} else {
			region.addAll(findRegion(step.nextCoord(seed), input, region + coveredSoFar))
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
			val area = region.size
			val corners = mutableListOf<Pair<Int, Int>>()
			val tags = mutableMapOf<Pair<Int, Int>, MutableList<Steps>>() // Lets us tag to count concave corners
			
			for (plant in region) {
				val edgesForPlant = mutableListOf<Steps>()
				
				// Count how many corners this plant in the region corresponds to
				for (step in Steps.entries) {
					val nextCoord = step.nextCoord(plant)
					
					if (nextCoord.first !in 0..<width || nextCoord.second !in 0..<height) {
						edgesForPlant.add(step)
						continue
					}
					
					if (input.gridQuery(nextCoord) != input.gridQuery(plant)) {
						edgesForPlant.add(step)
						if (nextCoord in tags) {
							tags[nextCoord] !!.add(step)
						} else {
							tags[nextCoord] = mutableListOf(step)
						}
					}
					
				}
				
//				for (edge in edgesForPlant) {
//					edges.add(plant to edge)
//				}
				
				// This checks for convex corners
				for (i in edgesForPlant.indices) {
					for (j in i + 1..<edgesForPlant.size) {
						if (edgesForPlant[i].turnRight() == edgesForPlant[j] || edgesForPlant[i].turnLeft() == edgesForPlant[j]) {
							corners += plant
						}
					}
				}
			}
			
			// Tracks concave corners, as those with two tags correspond to one corner there
			// those with three correspond to two corners there
			for (tag in tags) {
				var adjacentTaggers = 0

				for (i in 0..<tag.value.size) {
					for (j in i + 1..<tag.value.size) {
						if (tag.value[i].turnLeft() == tag.value[j] || tag.value[i].turnRight() == tag.value[j]) {
							adjacentTaggers ++
							corners += tag.key // The number of concave corners
						}
					}
				}
			}


			// If we have four 'corners' that are diagonally adjacent, and not all the same type, we need to subtract two for each of the adjacent groups
			var subtractCount = 0
			val cornersSimplified = corners.toSet().toList()

			for (i in cornersSimplified.indices) {
				for (j in i + 1..<cornersSimplified.size) {
					for (k in j + 1..<cornersSimplified.size) {
						for (l in k + 1..<cornersSimplified.size) {
							val sortedPts = listOf(corners[i], corners[j], corners[k], corners[l]).sortedWith(Comparator { o1, o2 ->
								return@Comparator if (o1.second < o2.second) -1
								else if (o1.second > o2.second) 1
								else if (o1.first < o2.first) -1
								else if (o1.first > o2.first) 1
								else 0
							}) // Sort by reading order

							val plants = sortedPts.map { input.gridQuery(it) }

							if (plants.count { it == input.gridQuery(region.first()) } != 2) continue

							// Check for diagonal adjacency
							if (sortedPts[1].first - sortedPts[0].first == 1 && sortedPts[1].second == sortedPts[0].second) {
								if (sortedPts[3].first - sortedPts[2].first == 1 && sortedPts[3].second == sortedPts[2].second) {
									if (sortedPts[2].second - sortedPts[0].second == 1 && sortedPts[2].first == sortedPts[0].first) {
										// They are diagonally adjacent and not all the same plant
										subtractCount += 2
									}
								}
							}
						}
					}
				}
			}
			
			totalPrice += (area * (corners.size - subtractCount)).toBigInteger()
		}
		
		
		return totalPrice
	}
	
	val input = readInput("Day12_test")
	part1(input).println()
	part2(input).println()
}