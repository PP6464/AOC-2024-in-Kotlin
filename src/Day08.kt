import com.github.michaelbull.itertools.*

fun main() {
	fun part1(input : List<String>) : Int {
		val width = input[0].length
		val height = input.size
		val antinodes = mutableSetOf<Pair<Int, Int>>()
		
		val freqs = input.joinToString("").toSet().toMutableSet().apply { remove('.') }
		
		for (freq in freqs) {
			val freqPlaces = input.gridIndicesOf(freq)
			
			for (perm in freqPlaces.pairPermutations()) {
				// The new pt is the second pt + vector from first to second pt
				val newPt = 2 * perm.second.first - perm.first.first to 2 * perm.second.second - perm.first.second
				if (newPt.first in 0..<width && newPt.second in 0..<height) {
					antinodes.add(newPt)
				}
			}
		}
		
		return antinodes.size
	}
	
	fun part2(input : List<String>) : Int {
		val width = input[0].length
		val height = input.size
		val antinodes = mutableSetOf<Pair<Int, Int>>()
		
		val freqs = input.joinToString("").toSet().toMutableSet().apply { remove('.') }
		
		for (freq in freqs) {
			val freqPlaces = input.gridIndicesOf(freq)
			
			for (perm in freqPlaces.pairPermutations()) {
				// Direction vector
				val vec = perm.second.first - perm.first.first to perm.second.second - perm.first.second
				
				val currPt = mutableListOf(perm.second.first, perm.second.second)
				
				// Repeatedly add vec to second pt until we leave the grid
				while (currPt.first() in 0..<width && currPt.last() in 0..<height) {
					antinodes.add(currPt.first() to currPt.last())
					
					currPt[0] = currPt.first() + vec.first
					currPt[1] = currPt.last() + vec.second
				}
			}
		}
		
		return antinodes.size
	}
	
	val input = readInput("Day08")
	part1(input).println()
	part2(input).println()
}