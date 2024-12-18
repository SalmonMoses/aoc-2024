package me.salmonmoses.days.twentyfour

import org.koin.core.annotation.Single
import me.salmonmoses.days.DayTask
import me.salmonmoses.days.TaskSpec
import me.salmonmoses.days.Day
import java.math.BigInteger

object Disassembler {
    fun getOperand(op: Long) = when (op) {
        in 0..3 -> op.toString()
        4L -> "A"
        5L -> "B"
        6L -> "C"
        else -> throw AssertionError()
    }

    fun disassembly(program: List<Long>) {
        var ip = 0
        while (ip < program.size) {
            val opcode = program[ip]
            when (opcode) {
                0L -> {
                    println("A <- A >> ${getOperand(program[ip + 1])}")
                    ip += 2
                }

                1L -> {
                    println("B <- B ^ ${program[ip + 1]}")
                    ip += 2
                }

                2L -> {
                    println("B <- ${getOperand(program[ip + 1])} mod 8")
                    ip += 2
                }

                3L -> {
                    println("if A != 0 jump ${program[ip + 1]}")
                    ip += 2
                }

                4L -> {
                    println("B <- B ^ C")
                    ip += 2
                }

                5L -> {
                    println("out ${getOperand(program[ip + 1])}")
                    ip += 2
                }

                6L -> {
                    println("B <- A >> ${getOperand(program[ip + 1])}")
                    ip += 2
                }

                7L -> {
                    println("C <- A >> ${getOperand(program[ip + 1])}")
                    ip += 2
                }
            }
        }
    }
}

@Single
@Day(17)
class Day17 : DayTask {
    override val spec1: TaskSpec
        get() = TaskSpec(
            "Register A: 729\n" +
                    "Register B: 0\n" +
                    "Register C: 0\n" +
                    "\n" +
                    "Program: 0,1,5,4,3,0", "4,6,3,5,6,3,5,2,1,0"
        )
    override val spec2: TaskSpec?
        get() = TaskSpec(
            "Register A: 2024\n" +
                    "Register B: 0\n" +
                    "Register C: 0\n" +
                    "\n" +
                    "Program: 0,3,5,4,3,0", "117440"
        )

    private data class Computer(
        var registerA: BigInteger,
        var registerB: BigInteger = BigInteger.ZERO,
        var registerC: BigInteger = BigInteger.ZERO
    ) {
        fun getOperand(op: BigInteger): BigInteger = when (op.toInt()) {
            0 -> BigInteger.ZERO
            1 -> BigInteger.ONE
            2 -> BigInteger.TWO
            3 -> BigInteger.valueOf(3)
            4 -> registerA
            5 -> registerB
            6 -> registerC
            else -> throw AssertionError()
        }

        fun compute(program: List<BigInteger>): List<BigInteger> {
            var ip = 0
            val output = mutableListOf<BigInteger>()
            while (ip < program.size) {
                val opcode = program[ip].toInt()
                when (opcode) {
                    0 -> {
                        registerA = registerA shr getOperand(program[ip + 1]).toInt()
                        ip += 2
                    }

                    1 -> {
                        registerB = registerB xor program[ip + 1]
                        ip += 2
                    }

                    2 -> {
                        registerB = getOperand(program[ip + 1]) % 8.toBigInteger()
                        ip += 2
                    }

                    3 -> {
                        if (registerA != BigInteger.valueOf(0L)) {
                            ip = program[ip + 1].toInt()
                        } else {
                            ip += 2
                        }
                    }

                    4 -> {
                        registerB = registerC xor registerB
                        ip += 2
                    }

                    5 -> {
                        output.add(getOperand(program[ip + 1]) % 8.toBigInteger())
                        ip += 2
                    }

                    6 -> {
                        registerB = registerA shr getOperand(program[ip + 1]).toInt()
                        ip += 2
                    }

                    7 -> {
                        registerC = registerA shr getOperand(program[ip + 1]).toInt()
                        ip += 2
                    }
                }
            }
            return output
        }
    }

    override fun task1(input: List<String>): String {
        val computer = Computer(registerA = input[0].split(":")[1].trim().toBigInteger())
        val program = input[4].split(":")[1].trim().split(",").map(String::toBigInteger)
        return computer.compute(program).joinToString(",")
    }

    override fun task2(input: List<String>): String {
        val program = input[4].split(":")[1].trim().split(",").map(String::toBigInteger)
        val aList = Array<BigInteger>(program.size) { BigInteger.ZERO }
        program.reversed().forEachIndexed { index, predictedOutput ->
            for (i in 0L..7L) {
                val maybeNewA = aList.toMutableList()
                maybeNewA.add(i.toBigInteger())
                val a = maybeNewA.reduce { acc, l -> (acc + l) shl 3 }
                val output = Computer(a).compute(program)
                if (output[0] == predictedOutput) {
                    aList[index] = i.toBigInteger()
                    break
                }
            }
        }
        assert(aList.size == program.size)
        return aList.reduce { acc, l -> (acc + l) shl 3 }.toString()
    }
}