import kotlin.math.*

fun main() {
	fun part1(input : List<String>) : String {
		var instructionPtr = 0
		val outputs = mutableListOf<Long>()
		
		var regA = input[0].split(": ")[1].toLong()
		var regB = input[1].split(": ")[1].toLong()
		var regC = input[2].split(": ")[1].toLong()
		
		println(regA / 2 xor 7)
		
		val program = input[4].split(": ")[1].split(",").map(String::toLong)
		
		fun comboOperand(operand : Long) : Long {
			return when (operand) {
				in 0..3 -> operand
				4L -> regA
				5L -> regB
				6L -> regC
				else -> - 1
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
	
	fun part2(input : List<String>) : Long {
		return input.size.toLong()
	}
	
	val input = readInput("Day17")
	part1(input).println()
//	part2(input).println()
}