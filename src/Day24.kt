import kotlin.math.*

val registries = mutableMapOf<String, Boolean>() // Map of id to value

class BinaryExpr(expr : String) {
	val leftId : String = expr.split(" ")[0]
	val rightId : String = expr.split(" ")[2]
	val op : String = expr.split(" ")[1]
	var outId : String = expr.split(" ").last()
	var compiled : Boolean = false
	
	fun compile() {
		assert(leftId in registries.keys)
		assert(rightId in registries.keys)
		
		registries[outId] = when (op) {
			"XOR" -> registries[leftId]!! xor registries[rightId]!!
			"OR" -> registries[leftId]!! || registries[rightId]!!
			"AND" -> registries[leftId]!! && registries[rightId]!!
			else -> throw Exception()
		}
		
		compiled = true
	}
}

fun main() {
	fun part1(input : List<String>) : Long {
		val splitIndex = input.withIndex().first { it.value.isEmpty() }.index
		for (i in 0..<splitIndex) {
			registries[input[i].split(": ")[0]] = input[i].split(": ")[1] == "1"
		}
		
		val expressions = input.subList(splitIndex + 1, input.size).map { BinaryExpr(it) }
		
		while (expressions.any { it.outId.startsWith("z") && !it.compiled }) {
			for (expression in expressions) {
				try {
					expression.compile()
				} catch (e : Exception) {
					// Do nothing, because we don't know one of the values
				}
			}
		}
		
		return registries
			.filter { it.key.startsWith("z") && it.value }
			.entries
			.sumOf { 2.0.pow(it.key.drop(1).toDouble()) }
			.toLong()
	}
	
	fun part2(input : List<String>) : String {
		val bitSize = 44
		val splitIndex = input.withIndex().first { it.value.isEmpty() }.index
		val expressions = input.subList(splitIndex + 1, input.size).map { BinaryExpr(it) }
		val swapped = mutableSetOf<String>()
		
		// The XORs should be used with the x and y bits, or to store into z
		expressions.filter {
			it.op == "XOR" && it.leftId.first() !in setOf('x', 'y', 'z') && it.rightId.first() !in setOf(
				'x',
				'y',
				'z'
			) && it.outId.first() !in setOf('x', 'y', 'z')
		}.map {
			println("Wrong on condition 1: ${it.outId}")
			swapped.add(it.outId)
		}
		
		for (i in 0..bitSize) {
			val xorExpr = expressions.single {
				it.op == "XOR" && setOf(it.leftId, it.rightId) == setOf(
					"x${
						i.toString().padStart(2, '0')
					}", "y${i.toString().padStart(2, '0')}"
				)
			}
			val outExpr = expressions.single {
				it.outId == "z${i.toString().padStart(2, '0')}"
			}
			
			// Shouldn't be using the output of x_i XOR y_i in an OR gate to get addition result, as it should also depend on the other input gate
			if (xorExpr.outId in expressions.filter { it.op == "OR" }.flatMap { setOf(it.leftId, it.rightId) }.toSet()) {
				println("Wrong on condition 2: ${xorExpr.outId}")
				swapped.add(xorExpr.outId)
			}
			
			// You need XOR to determine z_i
			if (outExpr.op != "XOR") {
				println("Wrong on condition 3: ${outExpr.outId}")
				swapped.add(outExpr.outId)
			}
			
			if (i > 0) {
				val prevAndExpr = expressions.single {
					it.op == "AND" && setOf(it.leftId, it.rightId) == setOf(
						"x${
							i.toString().padStart(2, '0')
						}", "y${
							i.toString().padStart(2, '0')
						}"
					)
				}
				
				// If the and expression's result is not used in anything other than an OR expression
				// Then it produces the wrong carry over: just play around with some binary numbers for a while
				if (expressions.any { prevAndExpr.outId in setOf(it.leftId, it.rightId) && it.op != "OR" }) {
					println("Wrong on condition 4: ${prevAndExpr.outId}")
					swapped.add(prevAndExpr.outId)
				}
			}
			
		}
		
		return swapped.sorted().joinToString(",")
	}
	
	val input = readInput("Day24")
	part1(input).println()
	part2(input).println()
}