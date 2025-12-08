package me.salmonmoses.days.nineteen

class BufferQueue(val size: Int) {
    private val buffer = Array(size) { 0 }
    private var writeIndex = 0
    private var readIndex = 0

    fun write(element: Int) {
        buffer[writeIndex] = element
        writeIndex = (writeIndex + 1) % size
    }

    fun read(): Int {
        val readElement = buffer[readIndex]
        readIndex = (readIndex + 1) % size
        return readElement
    }

    fun clear() {
        for (i in 0..<size) {
            buffer[i] = 0
        }
        readIndex = 0
        writeIndex = 0
    }
}

class Computer(memoryInit: List<Int>) {
    private enum class OperandMode {
        Position,
        Immediate
    }

    private var memory = memoryInit.toMutableList()
    private var ip = 0
    private var inputQueue = BufferQueue(5)
    var lastOutput = 0
        private set

    fun getAtPointer(pointer: Int): Int = memory[pointer]

    operator fun invoke(): Int {
        while (true) {
            val instruction = memory[ip]
            val opcode = instruction % 100
            val firstOpMode = getOperandMode(instruction % 1000 / 100)
            val secondOpMode = getOperandMode(instruction % 10000 / 1000)
            getOperandMode(instruction % 100000 / 10000)
            when (opcode) {
                1 -> add(firstOpMode, secondOpMode)
                2 -> mul(firstOpMode, secondOpMode)
                3 -> getInput()
                4 -> {
                    output(firstOpMode)
                    return lastOutput
                }

                5 -> jnz(firstOpMode, secondOpMode)
                6 -> jz(firstOpMode, secondOpMode)
                7 -> lessThen(firstOpMode, secondOpMode)
                8 -> eq(firstOpMode, secondOpMode)
                99 -> break
            }
        }
        return lastOutput
    }

    fun giveInput(input: Int) {
        inputQueue.write(input)
    }

    fun reset(memoryInit: List<Int>) {
        memory = memoryInit.toMutableList()
        ip = 0
        lastOutput = 0
        inputQueue.clear()
    }

    private fun getOperandMode(bit: Int): OperandMode = when (bit) {
        0 -> OperandMode.Position
        1 -> OperandMode.Immediate
        else -> OperandMode.Position
    }

    private fun getOperandValue(operand: Int, mode: OperandMode): Int = when (mode) {
        OperandMode.Position -> memory[operand]
        OperandMode.Immediate -> operand
    }

    private fun add(firstOpMode: OperandMode, secondOpMode: OperandMode) {
        val op1 = getOperandValue(memory[ip + 1], firstOpMode)
        val op2 = getOperandValue(memory[ip + 2], secondOpMode)
        val output = memory[ip + 3]
        memory[output] = op1 + op2
        ip += 4
    }

    private fun mul(firstOpMode: OperandMode, secondOpMode: OperandMode) {
        val op1 = getOperandValue(memory[ip + 1], firstOpMode)
        val op2 = getOperandValue(memory[ip + 2], secondOpMode)
        val output = memory[ip + 3]
        memory[output] = op1 * op2
        ip += 4
    }

    private fun getInput() {
        memory[memory[ip + 1]] = inputQueue.read()
        ip += 2
    }

    private fun output(opMode: OperandMode) {
        val operand = getOperandValue(memory[ip + 1], opMode)
        lastOutput = operand
        ip += 2
    }

    private fun jnz(firstOpMode: OperandMode, secondOpMode: OperandMode) {
        val condition = getOperandValue(memory[ip + 1], firstOpMode)
        val targetAddress = getOperandValue(memory[ip + 2], secondOpMode)
        if (condition != 0) {
            ip = targetAddress
        } else {
            ip += 3
        }
    }

    private fun jz(firstOpMode: OperandMode, secondOpMode: OperandMode) {
        val condition = getOperandValue(memory[ip + 1], firstOpMode)
        val targetAddress = getOperandValue(memory[ip + 2], secondOpMode)
        if (condition == 0) {
            ip = targetAddress
        } else {
            ip += 3
        }
    }

    private fun lessThen(firstOpMode: OperandMode, secondOpMode: OperandMode) {
        val op1 = getOperandValue(memory[ip + 1], firstOpMode)
        val op2 = getOperandValue(memory[ip + 2], secondOpMode)
        val output = memory[ip + 3]
        memory[output] = if (op1 < op2) 1 else 0
        ip += 4
    }

    private fun eq(firstOpMode: OperandMode, secondOpMode: OperandMode) {
        val op1 = getOperandValue(memory[ip + 1], firstOpMode)
        val op2 = getOperandValue(memory[ip + 2], secondOpMode)
        val output = memory[ip + 3]
        memory[output] = if (op1 == op2) 1 else 0
        ip += 4
    }
}