import java.lang.Integer.min

// Pos is the "value" of the node in effect
data class MazeNode(val pos : Pair<Int, Int>)

data class MazeEdge(val start : MazeNode, val end : MazeNode, val step : Steps)

data class Maze(
	val nodes : List<MazeNode>,
	val edges : List<MazeEdge>,
	val source : MazeNode,
	val target : MazeNode
) {
	fun weirdDijkstra(start : MazeNode = source) : Set<Triple<MazeNode, Int, Steps>> {
		val triples = if (start == source) mutableSetOf(
			Triple(source, 1000, Steps.UP),
			Triple(source, 0, Steps.RIGHT),
		) else mutableSetOf(
			Triple(target, 0, Steps.DOWN),
			Triple(target, 0, Steps.LEFT),
		)
		
		val checkedNodes = mutableSetOf<MazeNode>()
		val nodesToCheck = mutableSetOf(start)
		
		while (true) {
			val triplesCopy = triples.toSet()
			val checkCopy = nodesToCheck.toSet()
			
			for (node in checkCopy) {
				val neighbours = edges.filter { it.start == node }.map { it.end }
				
				for (neighbour in neighbours) {
					val nextStep = Steps.stepBetween(node.pos, neighbour.pos)
					
					var smallestScore = Int.MAX_VALUE
					
					val stepsToNode = triples.filter { it.first == node }
					
					for ((_, minScore, step) in stepsToNode) {
						val newScore = minScore + if (step == nextStep) 1 else 1001
						
						if (newScore < smallestScore) {
							smallestScore = newScore
						}
					}
					
					fun addTriple(triple : Triple<MazeNode, Int, Steps>) {
						if (triples.count { it.first == triple.first && it.third == triple.third } == 1) {
							val single = triples.single { it.first == triple.first && it.third == triple.third }
							if (triple.second < single.second) {
								triples.add(triple)
								triples.remove(single)
							}
						} else {
							triples.add(triple)
						}
					}
					
					if (neighbour in checkedNodes) {
						val currentOptions = triples.filter { it.first == neighbour && it.third == nextStep }
						
						if (currentOptions.isEmpty()) {
							addTriple(Triple(neighbour, smallestScore, nextStep))
							nodesToCheck.add(neighbour)
							checkedNodes.remove(neighbour)
						} else if (smallestScore < currentOptions.single().second) {
							triples.remove(triples.single { it.first == neighbour && it.third == nextStep })
							addTriple(Triple(neighbour, smallestScore, nextStep))
							nodesToCheck.add(neighbour)
							checkedNodes.remove(neighbour)
						}
					} else {
						addTriple(Triple(neighbour, smallestScore, nextStep))
						nodesToCheck.add(neighbour)
					}
				}
				
				nodesToCheck.remove(node)
			}
			
			println(triples.size)
			
			// No changes
			if (triples == triplesCopy) {
				break
			}
		}
		
		return triples
	}
}

fun main() {
	fun part1(input : List<String>) : Int {
		val mazeNodes = input.gridIndicesOf('.').map { MazeNode(it) } +
				MazeNode(input.gridIndicesOf('S')[0]) +
				MazeNode(input.gridIndicesOf('E')[0])
		val mazeEdges = mutableListOf<MazeEdge>()
		
		for (i in mazeNodes.indices) {
			for (j in mazeNodes.indices.toList().subList(i + 1, mazeNodes.size)) {
				if (Steps.isAdjacent(mazeNodes[i].pos, mazeNodes[j].pos)) {
					mazeEdges.add(MazeEdge(mazeNodes[i], mazeNodes[j], Steps.stepBetween(mazeNodes[i].pos, mazeNodes[j].pos)))
					mazeEdges.add(MazeEdge(mazeNodes[j], mazeNodes[i], Steps.stepBetween(mazeNodes[j].pos, mazeNodes[i].pos)))
				}
			}
		}
		
		val maze = Maze(mazeNodes, mazeEdges, MazeNode(input.gridIndicesOf('S')[0]), MazeNode(input.gridIndicesOf('E')[0]))
		
		val triples = maze.weirdDijkstra()
		
		return triples.filter { it.first == maze.target }.minOf { it.second }
	}
	
	fun part2(input : List<String>) : Int {
		val mazeNodes = input.gridIndicesOf('.').map { MazeNode(it) } +
				MazeNode(input.gridIndicesOf('S')[0]) +
				MazeNode(input.gridIndicesOf('E')[0])
		val mazeEdges = mutableListOf<MazeEdge>()
		
		for (i in mazeNodes.indices) {
			for (j in mazeNodes.indices.toList().subList(i + 1, mazeNodes.size)) {
				if (Steps.isAdjacent(mazeNodes[i].pos, mazeNodes[j].pos)) {
					mazeEdges.add(MazeEdge(mazeNodes[i], mazeNodes[j], Steps.stepBetween(mazeNodes[i].pos, mazeNodes[j].pos)))
					mazeEdges.add(MazeEdge(mazeNodes[j], mazeNodes[i], Steps.stepBetween(mazeNodes[j].pos, mazeNodes[i].pos)))
				}
			}
		}
		
		val maze = Maze(mazeNodes, mazeEdges, MazeNode(input.gridIndicesOf('S')[0]), MazeNode(input.gridIndicesOf('E')[0]))
		
		val triplesStartToEnd = maze.weirdDijkstra()
		val triplesEndToStart = maze.weirdDijkstra(start = maze.target).toMutableSet()
		val score = triplesStartToEnd.filter { it.first == maze.target }.minOf { it.second }
		
		val triplesToChange = triplesEndToStart.filter { it.first == maze.source && it.third != Steps.LEFT }
		
		for (triple in triplesToChange) {
			triplesEndToStart.remove(triple)
			if (triple.second + 1000 <= score) triplesEndToStart.add(Triple(triple.first, triple.second + 1000, Steps.LEFT))
		}
		
		val triplesToPrune = triplesEndToStart.filter { it.first == maze.source && it.third == Steps.LEFT }
		val tripleToKeep = triplesToPrune.minBy { it.second }
		
		for (triple in triplesToPrune) {
			if (triple != tripleToKeep) triplesEndToStart.remove(triple)
		}
		
		val distinctNodes = mutableSetOf(maze.source, maze.target)
		
		for (node in input.gridIndicesOf('.').map { MazeNode(it) }) {
			val firstTriples = triplesStartToEnd.filter { it.first == node }
			val steps = firstTriples.map { it.third }
			var total = Int.MAX_VALUE
			
			for (step in steps) {
				val opposite = triplesEndToStart.filter { it.first == node && it.third == step.opposite() }
				
				if (opposite.isEmpty()) {
					// Must turn here
					val minScore = triplesEndToStart.filter { it.first == node }.minOf { it.second } + 1000 + firstTriples.single { it.third == step }.second
					
					if (minScore < total) total = minScore
				} else {
					// Can go straight
					val oppScore = triplesEndToStart.single { it.first == node && it.third == step.opposite() }.second + firstTriples.single { it.third == step }.second
					val minOfOthers = triplesEndToStart.filter { it.first == node && it.third != step.opposite() }.minOf { it.second } + 1000 + firstTriples.single { it.third == step }.second
					
					if (min(oppScore, minOfOthers) < total) {
						total = min(oppScore, minOfOthers)
					}
				}
			}
			
			if (total <= score) {
				distinctNodes.add(node)
			}
		}
		
		return distinctNodes.size
	}
	
	val input = readInput("Day16")
	part1(input).println()
	part2(input).println()
}