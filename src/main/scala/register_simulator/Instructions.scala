package register_simulator

import register_simulator.nodes.{DecrementNode, IncrementNode, HaltNode, Instruction}
import register_simulator.utils.LinkedList

object Instructions {

  var instructions = new LinkedList[Instruction]

  private def decodeDoublePair(program: BigInt) = {
    decodePair(program)
  }

  private def decodeSinglePair(program: BigInt) = {
    decodePair(program + 1)
  }

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

  private def decodeList(program: BigInt): LinkedList[BigInt] = {
    lazy val result = new LinkedList[BigInt] // careful with that lazy
    if (program.equals(BigInt.apply(0)))
      return result
    val pair = decodeDoublePair(program)
    result += pair._1
    result ++= decodeList(pair._2)
    result
  }

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

  def decodeProgram(program: String) = {
    val result = new LinkedList[Instruction]
    lazy val list = decodeList(BigInt.apply(program))

    list.foreach(elem => result += decodeInstruction(elem))
    instructions = new LinkedList[Instruction](result)
    result
  }

  def encodeDoublePair(pair: (BigInt, BigInt)) = (BigInt.apply(2) ^ pair._1) * (pair._2 * 2 + 1)

  def encodeSinglePair(pair: (BigInt, BigInt)) = encodeDoublePair(pair) - 1

  def encodeList(list: LinkedList[BigInt]): BigInt = {
    if (list.isEmpty)
      return BigInt.apply(0)
    encodeDoublePair(list.head, encodeList(list.tail()))
  }

  def encodeInstruction(instruction: Instruction) = {
    instruction match {
      case instruction: HaltNode => BigInt.apply(0)
      case instruction: IncrementNode =>
        encodeDoublePair(BigInt.apply(instruction.register.index * 2), BigInt.apply(instruction.next.index))
      case instruction: DecrementNode =>
        encodeDoublePair(BigInt.apply(instruction.register.index * 2 + 1),
          encodeSinglePair(BigInt.apply(instruction.possible.index),
                           BigInt.apply(instruction.impossible.index)))
    }
  }

  def encodeProgram(instructions: LinkedList[Instruction]) = {
    val listNum = new LinkedList[BigInt]
    instructions.foreach(elem => listNum += encodeInstruction(elem))
    encodeList(listNum)

  }


}
