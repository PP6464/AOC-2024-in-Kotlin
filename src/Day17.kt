import kotlin.math.*

fun main() {
	fun part1(input : List<String>, radix : Int = 10) : String {
		var instructionPtr = 0
		val outputs = mutableListOf<Long>()
		
		var regA = input[0].split(": ")[1].toLong(radix)
		var regB = input[1].split(": ")[1].toLong()
		var regC = input[2].split(": ")[1].toLong()
		
		val program = input[4].split(": ")[1].split(",").map(String::toLong)
		
		fun comboOperand(operand : Long) : Long {
			return when (operand) {
				in 0..3 -> operand
				4L -> regA
				5L -> regB
				6L -> regC
				else -> -1
			}
		}
		
		while (instructionPtr in program.indices) {
			val instruction = program[instructionPtr]
			val operand = program[instructionPtr + 1]
			var incPtr = true
			
			when (instruction) {
				0L -> {
					regA = (regA / 2.0.pow(comboOperand(operand).toDouble())).toLong()
				}
				
				1L -> {
					regB = regB xor operand
				}
				
				2L -> {
					regB = comboOperand(operand) % 8
				}
				
				3L -> {
					if (regA != 0L) {
						instructionPtr = operand.toInt()
						incPtr = false
					}
				}
				
				4L -> {
					regB = regB xor regC
				}
				
				5L -> {
					outputs += comboOperand(operand) % 8
				}
				
				6L -> {
					regB = (regA / 2.0.pow(comboOperand(operand).toDouble())).toLong()
				}
				
				7L -> {
					regC = (regA / 2.0.pow(comboOperand(operand).toDouble())).toLong()
				}
			}
			
			if (incPtr) {
				instructionPtr += 2
			}
		}
		
		return outputs.joinToString(",")
	}
	
	fun part2(input : List<String>) : Long? {
		var aStr = ""
		val program = input.last().split(": ")[1]
		val size = program.split(",").size
		
		fun findStr(pos : Int) : String? {
			val possibilities = mutableListOf<Int>()
			
			if (pos >= size) {
				val altInput = listOf(
					"Register A: $aStr",
					"Register B: 0",
					"Register C: 0",
					"",
					"Program: $program",
				)
				
				val out = part1(altInput, 8)
				
				return if (out == program) {
					aStr
				} else {
					null
				}
			}
			
			for (i in 0..7) {
				val newStr = aStr + i
				
				val altInput = listOf(
					"Register A: $newStr",
					"Register B: 0",
					"Register C: 0",
					"",
					"Program: $program",
				)
				
				val out = part1(altInput, 8).split(",").joinToString("")
				
//				println("i: $i")
//				println("out: $out")
				
				if (out == program.split(",").joinToString("").substring(size - pos - 1, size)) {
					possibilities.add(i)
				}
			}
			
			if (possibilities.isEmpty()) return null
			
			for (option in possibilities) {
				aStr += option
				
				if (findStr(pos + 1) == null) {
					aStr = aStr.take(aStr.length - 1)
				} else {
					// There is an option
					return aStr
				}
			}
			
			return null
		}
		
		return findStr(0)?.toLong(8)
	}
	
	val input = readInput("Day17")
	part1(input, 10).println()
	part2(input).println()
}