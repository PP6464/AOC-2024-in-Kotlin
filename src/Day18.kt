data class Byte(val pos: Pair<Int, Int>)

data class ByteEdge(val start : Byte, val end : Byte)

data class Memory(val bytes: List<Byte>, val edges: List<ByteEdge>, val source : Byte, val target : Byte) {
	private val newBytes = mutableSetOf(source)
	private val checkedBytes = mutableSetOf<Byte>()
	private val results = mutableSetOf(source to 0)
	
	fun dijkstra() : Long {
		while (true) {
			val resultsCopy = results.toSet()
			val newBytesCopy = newBytes.toSet()
			
			for (byte in newBytesCopy) {
				val score = results.single { it.first == byte }.second
				val neighbours = edges.filter { it.start == byte }.map { it.end }.filter { it !in checkedBytes }
				
				for (neighbour in neighbours) {
					val current = results.filter { it.first == neighbour }
					var minScore = -1
					
					if (current.isNotEmpty()) {
						minScore = current.single().second
					}
					
					if (minScore > 0 && minScore > score + 1) {
						results.add(neighbour to score + 1)
					} else if (minScore < 0) {
						results.add(neighbour to score + 1)
					}
					
					newBytes.add(neighbour)
				}
				
				checkedBytes.add(byte)
				newBytes.remove(byte)
			}
			
			if (resultsCopy == results) break
		}
		
		return results.single { it.first == target }.second.toLong()
	}
}

fun main() {
	fun part1(input : List<String>, bytesToSim : Int = 1024, size : Int = 71) : Long {
		val grid = (0..<size).map { (0..<size).joinToString("") { "." } }.toMutableList()
		
		for (i in 0..<bytesToSim) {
			val (x, y) = input[i].split(",").map(String::toInt)
			
			grid[y] = grid[y].replaceRange(x..x, "#")
		}
		
		val bytes = grid.gridIndicesOf('.').map { Byte(it) }
		val edges = mutableListOf<ByteEdge>()
		
		for (i in bytes.indices) {
			for (j in i+1..<bytes.size) {
				if (Steps.isAdjacent(bytes[i].pos, bytes[j].pos)) {
					edges.add(ByteEdge(start = bytes[i], end = bytes[j]))
					edges.add(ByteEdge(start = bytes[j], end = bytes[i]))
				}
			}
		}
		
		return Memory(bytes, edges, Byte(0 to 0), Byte(size - 1 to size - 1)).dijkstra()
	}
	
	fun part2(input : List<String>) : Int {
		var a = 2750
		
		while (true) {
			try {
				println(a)
				part1(input, a, 71)
				a ++
			} catch (e : NoSuchElementException) {
				break
			}
		}
		
		return a
	}
	
	val input = readInput("Day18")
	println(input[part2(input) - 1])
}