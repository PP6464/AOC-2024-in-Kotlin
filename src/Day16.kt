// Pos is the "value" of the node in effect
data class MazeNode(val pos : Pair<Int, Int>)

data class MazeEdge(val start : MazeNode, val end : MazeNode, val step : Steps)

data class Maze(
	val nodes : List<MazeNode>,
	val edges : List<MazeEdge>,
	val source : MazeNode,
	val target : MazeNode
) {
	val triples = mutableSetOf(
		Triple(source, 1000, Steps.UP),
		Triple(source, 0, Steps.RIGHT),
	)
	val checkedNodes = mutableSetOf<MazeNode>()
	
	fun weirdDijkstra() : Int {
		while (true) {
			val triplesCopy = triples.toSet()
			
			val distinctNodes = triples.map { it.first }.toSet()
			
			for (node in distinctNodes) {
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
					
					triples.add(Triple(neighbour, smallestScore, nextStep))
				}
			}
			
			println(triples.size)
			
			// No changes
			if (triples == triplesCopy) {
				break
			}
		}
		
		return triples.filter { it.first == target }.minOf { it.second }
	}
	
	// Bear in mind path does not have the source node
	fun findDistOfPath(path : List<MazeNode>) : Int {
		var dist = 0
		var stepBefore = Steps.RIGHT
		
		for (i in 1..<path.size) {
			dist ++
			
			val newStep = Steps.stepBetween(path[i - 1].pos, path[i].pos)
			
			if (newStep != stepBefore) {
				dist += 1000
				stepBefore = newStep
			}
		}
		
		return dist
	}
	
	fun allPathsToTarget(start : MazeNode = source, pathSoFar : List<MazeNode> = emptyList()) : Set<List<MazeNode>> {
		val paths = mutableSetOf<List<MazeNode>>()
		
		for (neighbour in edges.filter { it.start == start }.map { it.end }) {
			if (neighbour == target) {
				return setOf(listOf(target))
			}
			
			if (neighbour in pathSoFar) continue
			
			println("Going to $neighbour")
			
			val pathsOfNeighbour = allPathsToTarget(neighbour, pathSoFar + neighbour)
			
			println("Found some paths")
			
			paths.addAll(pathsOfNeighbour.map { listOf(neighbour) + it })
		}
		
		return paths
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
//		return maze.allPathsToTarget().minOf { maze.findDistOfPath(it) } + 1
	
		val res = maze.weirdDijkstra()
		
		val grid = input.toMutableList()
		
//		for ((x, y) in maze.checkedNodes.map { it.pos }) {
//			grid[y] = grid[y].replaceRange(x..x, "O")
//		}
//
//		println(grid.joinToString("\n"))
		
		return res
	}
	
	fun part2(input : List<String>) : Int {
		return input.size
	}
	
	val input = readInput("Day16")
	part1(input).println()
//	part2(input).println()
}