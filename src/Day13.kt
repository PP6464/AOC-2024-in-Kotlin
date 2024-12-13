data class ClawMachine(
	val aX : Long,
	val aY : Long,
	val bX : Long,
	val bY : Long,
	val x : Long,
	val y : Long,
) {
	fun solve() : Long? {
		val matrix = listOf(
			listOf(aX, bX),
			listOf(aY, bY)
		)
		
		val det = (matrix[1][1] * matrix[0][0] - matrix[0][1] * matrix[1][0]).toDouble()
		
		val inverseWODet = listOf(
			listOf(bY, - bX),
			listOf(- aY, aX)
		)
		
		val solnWODet =
			listOf(inverseWODet[0][0] * x + inverseWODet[0][1] * y, inverseWODet[1][0] * x + inverseWODet[1][1] * y)
		
		return if (det != 0.0) {
			val nA = solnWODet[0] / det
			val nB = solnWODet[1] / det
			
			if (nA % 1 != 0.0 || nB % 1 != 0.0) {
				// Can't have non-integer solutions
				null
			} else {
				(nA * 3 + nB).toLong()
			}
		} else {
			null
		}
	}
}

fun main() {
	fun part1(input : List<String>) : Long {
		var totalPrice = 0L
		
		for (i in 0..<input.size / 4) {
			val aDx = input[4 * i].split(":")[1].split(",")[0].split("+")[1].toLong()
			val aDy = input[4 * i].split(":")[1].split(",")[1].split("+")[1].toLong()
			val bDx = input[4 * i + 1].split(":")[1].split(",")[0].split("+")[1].toLong()
			val bDy = input[4 * i + 1].split(":")[1].split(",")[1].split("+")[1].toLong()
			
			val prizePos =
				input[4 * i + 2].split(":")[1].split(",")[0].split("=")[1].toLong() to
						input[4 * i + 2].split(":")[1].split(",")[1].split("=")[1].toLong()
			
			val price = ClawMachine(aDx, aDy, bDx, bDy, prizePos.first, prizePos.second).solve()
			
			if (price != null) {
				totalPrice += price
			}
		}
		
		return totalPrice
	}
	
	fun part2(input : List<String>) : Long {
		var totalPrice = 0L
		
		for (i in 0..<input.size / 4) {
			val aDx = input[4 * i].split(":")[1].split(",")[0].split("+")[1].toLong()
			val aDy = input[4 * i].split(":")[1].split(",")[1].split("+")[1].toLong()
			val bDx = input[4 * i + 1].split(":")[1].split(",")[0].split("+")[1].toLong()
			val bDy = input[4 * i + 1].split(":")[1].split(",")[1].split("+")[1].toLong()
			
			val prizePos =
				input[4 * i + 2].split(":")[1].split(",")[0].split("=")[1].toInt() + 10000000000000 to
						input[4 * i + 2].split(":")[1].split(",")[1].split("=")[1].toInt() + 10000000000000
			
			val price = ClawMachine(aDx, aDy, bDx, bDy, prizePos.first, prizePos.second).solve()
			
			if (price != null) {
				totalPrice += price
			}
		}
		
		return totalPrice
	}
	
	val input = readInput("Day13")
	part1(input).println()
	part2(input).println()
}