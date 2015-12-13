package register_simulator.utils

import register_simulator.{Instructions, Label, Register}
import register_simulator.nodes.{DecrementNode, IncrementNode, HaltNode, Instruction}

object Decoder {
  /**
    * Decodes 2^x(2*y+1) into <<x, y>>^
    * @param program the number to be decoded
    * @return a pair of BigInts representing <<x, y>>
    */
  private def decodeDoublePair(program: BigInt) = {
    decodePair(program)
  }

  /**
    * Decodes 2^x(2*y+1)-1 into <x, y>^
    * @param program the number to be decoded
    * @return a pair of BigInts representing <x, y>
    */
  private def decodeSinglePair(program: BigInt) = {
    // +1 as we're going in reverse
    decodePair(program + 1)
  }

  /**
    * Helper method to decode to a pair
    * @param program the number to be decoded
    * @return a pair of BigInts that can be used to represent <<x,y>> or <x,y>
    */
  private def decodePair(program: BigInt) = {
    var value = program
    var x = BigInt.apply(0)

    while (value % 2 == 0) {
      value >>= 1
      x += 1
    }
    value -= 1
    value /= 2

    (x, value)
  }

  /**
    * Decodes a number to a list in the format <<N1,<<N2,<<.....0>>>>
    * @param program the number to be decoded
    * @return a list of BigInts
    */
  private def decodeList(program: BigInt): LinkedList[BigInt] = {
    // Lazy evaluation + recursion = happiness
    lazy val result = new LinkedList[BigInt]
    // Terminate at 0
    if (program.equals(BigInt.apply(0)))
      return result
    val pair = decodeDoublePair(program)
    result += pair._1
    // Recurse!
    result ++= decodeList(pair._2)
    result
  }

  /**
    * Decodes a number to an instruction for this register machine
    * @param instruction the number to be decoded
    * @return an instruction node representing the number
    */
  private def decodeInstruction(instruction: BigInt): Instruction = {
    if (instruction.equals(BigInt.apply(0)))
      return new HaltNode
    val pair = decodeDoublePair(instruction)
    if (pair._1 % 2 == 0)
      return new IncrementNode(new Register((pair._1 / 2).toInt), new Label(pair._2.toInt))
    val pairLabels = decodeSinglePair(pair._2)

    new DecrementNode(new Register(((pair._1 - 1) / 2).toInt),
      new Label(pairLabels._1.toInt),
      new Label(pairLabels._2.toInt))
  }

  /**
    * Decodes a program from a string containing digits
    * @param program the string to be decoded
    * @return a list of instructions
    */
  def decodeProgram(program: String) = {
    val result = new LinkedList[Instruction]
    lazy val list = decodeList(BigInt.apply(program))

    list.foreach(elem => result += decodeInstruction(elem))
    Instructions.instructions = new LinkedList[Instruction](result)
    result
  }
}
