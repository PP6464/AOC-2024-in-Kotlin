fun main() {
	fun numberSatisfiesRules(number : Int, page : List<Int>, rules : List<Pair<Int, Int>>) : Boolean {
		val index = page.indexOf(number)
		
		val rulesBefore = rules.filter { it.first == number }
		val rulesAfter = rules.filter { it.second == number }
		
		for (ruleBefore in rulesBefore) {
			if (ruleBefore.second !in page.subList(index, page.size) && ruleBefore.second in page) {
				return false
			}
		}
		
		for (ruleAfter in rulesAfter) {
			if (ruleAfter.first !in page.subList(0, index) && ruleAfter.first in page) {
				return false
			}
		}
		
		return true
	}
	
	fun pageIsHappy(page : List<Int>, rules : List<Pair<Int, Int>>) : Boolean {
		for (number in page) {
			if (! numberSatisfiesRules(number, page, rules)) return false
		}
		
		return true
	}
	
	fun part1(input : List<String>) : Int {
		val rules = mutableListOf<Pair<Int, Int>>()
		var count = 0
		
		for (line in input) {
			count ++
			if (line.contains("|")) {
				rules.add(line.split("|")[0].toInt() to line.split("|")[1].toInt())
			} else break
		}
		
		val pages = input.subList(count, input.size).map {
			it.split(",").map(String::toInt)
		}
		
		var total = 0
		
		for (page in pages) {
			if (pageIsHappy(page, rules)) {
				total += page[(page.size - 1) / 2]
			}
		}
		
		return total
	}
	
	fun part2(input : List<String>) : Int {
		val rules = mutableListOf<Pair<Int, Int>>()
		var count = 0
		
		for (line in input) {
			count ++
			if (line.contains("|")) {
				rules.add(line.split("|")[0].toInt() to line.split("|")[1].toInt())
			} else break
		}
		
		val pages = input.subList(count, input.size).map {
			it.split(",").map(String::toInt)
		}
		
		var total = 0
		
		for ((i, page) in pages.withIndex()) {
			println("Page $i / ${pages.size}")
			
			if (pageIsHappy(page, rules)) continue
			
			val rectifiedUpdate = mutableListOf<Int>()
			
			page.map { p ->
				val pagesAfterwards = rules.filter { it.first == p }.map { it.second }
				
				val whereToPlace = rectifiedUpdate
					.indexOfFirst { it in pagesAfterwards }
					.let { if (it == -1) rectifiedUpdate.size else it }
				
				rectifiedUpdate.add(whereToPlace, p)
			}
			
			println(rectifiedUpdate)
			
			total += rectifiedUpdate[(page.size - 1) / 2]
		}
		
		return total
	}
	
	val input = readInput("Day05")
	part1(input).println()
	part2(input).println()
}