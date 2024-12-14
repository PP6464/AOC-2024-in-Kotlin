import java.io.File

fun main() {
	fun part1(input : List<String>) : Int {
		val spaceWidth = 101
		val spaceHeight = 103
		
		// Bots positions paired to their velocities
		val bots = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
		
		for (botInfo in input) {
			val posRegex = Regex("(?<=p=)(\\d+),(\\d+)")
			val velRegex = Regex("(?<=v=)(-?\\d+),(-?\\d+)")
			
			val pos = posRegex.find(botInfo) !!.groupValues.takeLast(2).map(String::toInt).toPair()
			val vel = velRegex.find(botInfo) !!.groupValues.takeLast(2).map(String::toInt).toPair()
			
			bots.add(pos to vel)
		}
		
		repeat(100) {
			for ((index, bot) in bots.withIndex()) {
				var newPos =
					(bot.first.first + bot.second.first) % spaceWidth to (bot.first.second + bot.second.second) % spaceHeight
				if (newPos.first < 0) {
					newPos = newPos.first + spaceWidth to newPos.second
				}
				if (newPos.second < 0) {
					newPos = newPos.first to newPos.second + spaceHeight
				}
				bots[index] = newPos to bot.second
			}
		}
		
		var first = 0
		var second = 0
		var third = 0
		var fourth = 0
		
		for (botPos in bots.map { it.first }) {
			if (botPos.first > (spaceWidth - 1) / 2) {
				if (botPos.second > (spaceHeight - 1) / 2) {
					fourth += 1
				} else if (botPos.second < (spaceHeight - 1) / 2) {
					first += 1
				}
			} else if (botPos.first < (spaceWidth - 1) / 2) {
				if (botPos.second > (spaceHeight - 1) / 2) {
					third += 1
				} else if (botPos.second < (spaceHeight - 1) / 2) {
					second += 1
				}
			}
		}
		
		return first * second * third * fourth
	}
	
	fun part2(input : List<String>) : Int {
		val spaceWidth = 101
		val spaceHeight = 103
		
		// Bots positions paired to their velocities
		val bots = mutableListOf<Pair<Pair<Int, Int>, Pair<Int, Int>>>()
		
		for (botInfo in input) {
			val posRegex = Regex("(?<=p=)(\\d+),(\\d+)")
			val velRegex = Regex("(?<=v=)(-?\\d+),(-?\\d+)")
			
			val pos = posRegex.find(botInfo) !!.groupValues.takeLast(2).map(String::toInt).toPair()
			val vel = velRegex.find(botInfo) !!.groupValues.takeLast(2).map(String::toInt).toPair()
			
			bots.add(pos to vel)
		}
		
		var iters = 0
		
		for (i in 0..7000) {
			iters ++
			
			for ((index, bot) in bots.withIndex()) {
				var newPos =
					(bot.first.first + bot.second.first) % spaceWidth to (bot.first.second + bot.second.second) % spaceHeight
				if (newPos.first < 0) {
					newPos = newPos.first + spaceWidth to newPos.second
				}
				if (newPos.second < 0) {
					newPos = newPos.first to newPos.second + spaceHeight
				}
				bots[index] = newPos to bot.second
			}
			
			var newText = ""
			
			repeat(spaceHeight) { y ->
				repeat(spaceWidth) { x ->
					newText += if (x to y in bots.map { it.first }) {
						'B'
					} else {
						'_'
					}
				}
				
				newText += '\n'
			}
			
			val asGrid = newText.split("\n").subList(0, spaceHeight)
			val regionsOfInterest = mutableListOf<Set<Pair<Int, Int>>>()
			
			for (place in asGrid.gridIndicesOf('B')) {
				var isInRegion = false
				
				for (region in regionsOfInterest) {
					if (place in region) {
						isInRegion = true
						break
					}
				}
				
				if (isInRegion) continue
				
				regionsOfInterest.add(findRegion(place, asGrid, emptySet()))
			}
			
			println("Iteration $iters")
			
			if (regionsOfInterest.count { it.size >= 100 } > 0) {
				File("test_$iters.txt").writeText(newText)
				break
			}
		}
		
		return iters
	}
	
	val input = readInput("Day14")
	part1(input).println()
	part2(input).println()
}